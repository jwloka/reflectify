/*******************************************************************************
 * Copyright (c) 2009 Jan Wloka.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jan Wloka - initial API and implementation
 *******************************************************************************/

package org.wloka.reflectify.internal.assist;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.ASTRewriteCorrectionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * Creates a reflectify proposal for field accesses, e.g.:
 * 		[v.bar]		v.getClass().getField("bar").get(v);
 * 		[Foo.bar]	Class.forName("p1.Foo").getField("bar").getBoolean(null);
 * 
 * 
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class FieldAccessProposalCreator extends AbstractProposalCreator {

	@SuppressWarnings({ "unchecked" })
	public IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context) {
		if (node == null) {
			throw new IllegalArgumentException(ReflectifyMessages.NoOrIllegalNodeError);
		}
		
		IVariableBinding field = null;
		if (node.getNodeType() == ASTNode.FIELD_ACCESS) {
			field = ((FieldAccess)node).resolveFieldBinding();
			if (field == null) {
				throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
			}
		} else if (node.getNodeType() == ASTNode.QUALIFIED_NAME) {
			IBinding bind = ((QualifiedName)node).resolveBinding();
			if (bind == null 
					|| bind.getKind() != IBinding.VARIABLE
					|| !((IVariableBinding)bind).isField()) {
				throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
			}
			field = (IVariableBinding)bind;
		} else if (node.getNodeType() == ASTNode.SIMPLE_NAME) {
			IBinding bind = ((SimpleName)node).resolveBinding();
			if (bind == null 
					|| bind.getKind() != IBinding.VARIABLE
					|| !((IVariableBinding)bind).isField()) {
				throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
			}
			field = (IVariableBinding)bind;
			
		} else {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		
		AST        ast     = node.getAST();
		ASTRewrite rewrite = ASTRewrite.create(ast);

		MethodInvocation methGetClass = createGetClassInvocation(node, field, ast);
		MethodInvocation methGetField = createGetFieldInvocation(field, methGetClass, ast);
		MethodInvocation methGetValue = createGetFieldValueInvocation(field, ast);
		methGetValue.setExpression(methGetField);
		
		if (Modifier.isStatic(field.getModifiers())) {
			methGetValue.arguments().add(ast.newNullLiteral());
		} else {
			Expression exprVar = createFieldAccessVariableExpression(node, ast);
			methGetValue.arguments().add(ASTNode.copySubtree(ast, exprVar));
		}
		
		rewrite.replace(node, methGetValue, null); // no edit group
		return new ASTRewriteCorrectionProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
				context.getCompilationUnit(),
				rewrite, 
				getRelevance(), 
				JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_LOCAL));
	}

	private Expression createFieldAccessVariableExpression(ASTNode fieldAccess, AST ast) {
		Expression result = null;
		switch (fieldAccess.getNodeType()) {
		case ASTNode.FIELD_ACCESS:
			result = ((FieldAccess)fieldAccess).getExpression();
			break;
		case ASTNode.QUALIFIED_NAME:
			result = ((QualifiedName)fieldAccess).getQualifier();
			break;
		default:
			result = ast.newThisExpression();
			break;
		}
		return result;
	}
	
	private MethodInvocation createGetClassInvocation(ASTNode fieldAccess, IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = null;
		if (Modifier.isStatic(fieldBind.getModifiers())) {
			result = createClassForName(fieldBind.getDeclaringClass(), ast);
		} else {
			result = ast.newMethodInvocation();
			result.setName(ast.newSimpleName("getClass")); //$NON-NLS-1$
			Expression exprVar = createFieldAccessVariableExpression(fieldAccess, ast);
			result.setExpression((Expression)ASTNode.copySubtree(ast, exprVar));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private MethodInvocation createGetFieldInvocation(IVariableBinding field, MethodInvocation accClass, AST ast) {
		MethodInvocation result = ast.newMethodInvocation();
		result.setName(ast.newSimpleName("getField")); //$NON-NLS-1$
		result.setExpression(accClass);
		StringLiteral fieldName = ast.newStringLiteral();
		fieldName.setLiteralValue(field.getName());
		result.arguments().add(fieldName);
		return result;
	}
	
	private MethodInvocation createGetFieldValueInvocation(IVariableBinding field,  AST ast) {
		MethodInvocation result    = ast.newMethodInvocation();
		ITypeBinding     fieldType = field.getType();
		if (fieldType.isPrimitive()) {
			Code code = PrimitiveType.toCode(fieldType.getName());
			if (PrimitiveType.BOOLEAN.equals(code)) {
				result.setName(ast.newSimpleName("getBoolean")); //$NON-NLS-1$
			} else if (PrimitiveType.BYTE.equals(code)) {
				result.setName(ast.newSimpleName("getByte")); //$NON-NLS-1$
			} else if (PrimitiveType.CHAR.equals(code)) {
				result.setName(ast.newSimpleName("getChar")); //$NON-NLS-1$
			} else if (PrimitiveType.DOUBLE.equals(code)) {
				result.setName(ast.newSimpleName("getDouble")); //$NON-NLS-1$
			} else if (PrimitiveType.FLOAT.equals(code)) {
				result.setName(ast.newSimpleName("getFloat")); //$NON-NLS-1$
			} else if (PrimitiveType.INT.equals(code)) {
				result.setName(ast.newSimpleName("getInt")); //$NON-NLS-1$
			} else if (PrimitiveType.LONG.equals(code)) {
				result.setName(ast.newSimpleName("getLong")); //$NON-NLS-1$
			} else if (PrimitiveType.SHORT.equals(code)) {
				result.setName(ast.newSimpleName("getShort")); //$NON-NLS-1$
			}
		} else {
			result.setName(ast.newSimpleName("get")); //$NON-NLS-1$
		}
		return result;
	}
}
