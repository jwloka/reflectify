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
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.ASTRewriteCorrectionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * Creates reflectify proposal for field assignments, e.g.:
 * 		[v.bar = new Object()]	v.getClass().getField("bar").set(v, new Object());
 * 		[Foo.bar = true]		Class.forName("p1.Foo").getField("bar").setBoolean(null, true);
 * 
 * 
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class FieldAssignmentProposalCreator extends AbstractProposalCreator {

	@SuppressWarnings("unchecked")
	public IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context) {
		if (node == null || node.getNodeType() != ASTNode.ASSIGNMENT) {
			throw new IllegalArgumentException(ReflectifyMessages.NoOrIllegalNodeError);
		}
		Assignment		 assNode = (Assignment)node;
		AST         	 ast     = assNode.getAST();
		ASTRewrite  	 rewrite = ASTRewrite.create(ast);

		Expression leftExpr  = assNode.getLeftHandSide();
		Expression rightExpr = assNode.getRightHandSide();
		
		MethodInvocation methGetClass = null;
		IVariableBinding fieldBind    = null;
		if (leftExpr.getNodeType() == ASTNode.FIELD_ACCESS) {
			FieldAccess fa = (FieldAccess)leftExpr;
			fieldBind = resolveFieldBinding(fa);
			methGetClass = createGetClassMethodInvocation(fa, fieldBind, ast);
		} else if (leftExpr.getNodeType() == ASTNode.QUALIFIED_NAME) {
			QualifiedName qn = (QualifiedName)leftExpr;
			fieldBind = resolvedFieldBinding(qn);
			methGetClass = createGetClassMethodInvocation(qn, fieldBind, ast);
		} else if (leftExpr.getNodeType() == ASTNode.SIMPLE_NAME) { // possible ?
			SimpleName sn = (SimpleName)leftExpr;
			fieldBind = resolveFieldBinding(sn);
			methGetClass = createGetClassMethodInvocation(fieldBind, ast);
		} else {
			throw new IllegalArgumentException(ReflectifyMessages.UnexpectedAssignmentNodeError);
		}
		MethodInvocation methGetField = createGetFieldMethodInvocation(fieldBind, ast);
		methGetField.setExpression(methGetClass);
		
		MethodInvocation methSetValue = createSetValueMethodInvocation(fieldBind, ast);
		methSetValue.setExpression(methGetField);
		if (Modifier.isStatic(fieldBind.getModifiers())) {
			methSetValue.arguments().add(ast.newNullLiteral());
		} else {
			Expression exprVar = null;
			if (leftExpr.getNodeType() == ASTNode.FIELD_ACCESS) {
				exprVar = ((FieldAccess)leftExpr).getExpression();
			} else if (leftExpr.getNodeType() == ASTNode.QUALIFIED_NAME) {
				exprVar = ((QualifiedName)leftExpr).getQualifier();
			}
			if (exprVar != null) {
				methSetValue.arguments().add(ASTNode.copySubtree(ast, exprVar));
			} else { // also simple name
				methSetValue.arguments().add(ast.newThisExpression());
			}
		}
		methSetValue.arguments().add(ASTNode.copySubtree(ast, rightExpr));
		
		
		rewrite.replace(assNode, methSetValue, null); // no edit group
		return new ASTRewriteCorrectionProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
				context.getCompilationUnit(),
				rewrite, 
				getRelevance(), 
				JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_LOCAL));
	}

	private MethodInvocation createGetClassMethodInvocation(FieldAccess fa, IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = null;
		if (Modifier.isStatic(fieldBind.getModifiers())) {
			result = createClassForName(fieldBind.getDeclaringClass(), ast);
		} else {
			result = ast.newMethodInvocation();
			result.setName(ast.newSimpleName("getClass")); //$NON-NLS-1$
			Expression exprVar = fa.getExpression();
			if (exprVar == null) {
				exprVar = ast.newThisExpression();
			}
			result.setExpression((Expression)ASTNode.copySubtree(ast, exprVar));
		}
		return result;
	}

	private MethodInvocation createGetClassMethodInvocation(QualifiedName qn, IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = null;
		if (Modifier.isStatic(fieldBind.getModifiers())) {
			result = createClassForName(fieldBind.getDeclaringClass(), ast);
		} else {
			result = ast.newMethodInvocation();
			result.setName(ast.newSimpleName("getClass")); //$NON-NLS-1$
			Expression exprVar = qn.getQualifier();
			if (exprVar == null) {
				exprVar = ast.newThisExpression();
			}
			result.setExpression((Expression)ASTNode.copySubtree(ast, exprVar));
		}
		return result;
	}
	
	private MethodInvocation createGetClassMethodInvocation(IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = null;
		if (Modifier.isStatic(fieldBind.getModifiers())) {
			result = createClassForName(fieldBind.getDeclaringClass(), ast);
		} else {
			result = ast.newMethodInvocation();
			result.setName(ast.newSimpleName("getClass")); //$NON-NLS-1$
			result.setExpression(ast.newThisExpression());
		}
		return result;
	}

	private IVariableBinding resolveFieldBinding(FieldAccess fa) {
		IVariableBinding result = fa.resolveFieldBinding();
		if (result == null) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		return result;
	}

	private IVariableBinding resolvedFieldBinding(QualifiedName qn) {
		IBinding result = qn.resolveBinding();
		if (result == null 
				|| result.getKind() != IBinding.VARIABLE
				|| !((IVariableBinding)result).isField()) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		return (IVariableBinding)result;
	}

	private IVariableBinding resolveFieldBinding(SimpleName sn) {
		IBinding result = sn.resolveBinding();
		if (result == null 
				|| result.getKind() != IBinding.VARIABLE
				|| !((IVariableBinding)result).isField()) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		return (IVariableBinding)result;
	}

	@SuppressWarnings("unchecked")
	private MethodInvocation createGetFieldMethodInvocation(IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = ast.newMethodInvocation();
		result.setName(ast.newSimpleName("getField")); //$NON-NLS-1$
		StringLiteral fieldName = ast.newStringLiteral();
		fieldName.setLiteralValue(fieldBind.getName());
		result.arguments().add(fieldName);
		return result;
	}

	private MethodInvocation createSetValueMethodInvocation(IVariableBinding fieldBind, AST ast) {
		MethodInvocation result = ast.newMethodInvocation();
		ITypeBinding     fieldType = fieldBind.getType();
		if (fieldType.isPrimitive()) {
			Code code = PrimitiveType.toCode(fieldType.getName());
			if (PrimitiveType.BOOLEAN.equals(code)) {
				result.setName(ast.newSimpleName("setBoolean")); //$NON-NLS-1$
			} else if (PrimitiveType.BYTE.equals(code)) {
				result.setName(ast.newSimpleName("setByte")); //$NON-NLS-1$
			} else if (PrimitiveType.CHAR.equals(code)) {
				result.setName(ast.newSimpleName("setChar")); //$NON-NLS-1$
			} else if (PrimitiveType.DOUBLE.equals(code)) {
				result.setName(ast.newSimpleName("setDouble")); //$NON-NLS-1$
			} else if (PrimitiveType.FLOAT.equals(code)) {
				result.setName(ast.newSimpleName("setFloat")); //$NON-NLS-1$
			} else if (PrimitiveType.INT.equals(code)) {
				result.setName(ast.newSimpleName("setInt")); //$NON-NLS-1$
			} else if (PrimitiveType.LONG.equals(code)) {
				result.setName(ast.newSimpleName("setLong")); //$NON-NLS-1$
			} else if (PrimitiveType.SHORT.equals(code)) {
				result.setName(ast.newSimpleName("setShort")); //$NON-NLS-1$
			}
		} else {
			result.setName(ast.newSimpleName("set")); //$NON-NLS-1$
		}
		return result;
	}
}
