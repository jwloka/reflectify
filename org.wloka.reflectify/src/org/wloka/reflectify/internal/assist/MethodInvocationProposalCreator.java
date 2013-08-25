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

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.ASTRewriteCorrectionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * Create reflectify proposal for method invocations, e.g.:
 * 		[v.getFoo()]	  	v.getClass().getMethod("getFoo").invoke(v);
 * 		[Bar.staticFoo()]	Class.forName("p1.Bar").getMethod("staticFoo").invoke(null);
 * 
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class MethodInvocationProposalCreator extends AbstractProposalCreator {

	public IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context) {
		if (node == null || node.getNodeType() != ASTNode.METHOD_INVOCATION) {
			throw new IllegalArgumentException(ReflectifyMessages.NoOrIllegalNodeError);
		}

		MethodInvocation invNode = (MethodInvocation)node;
		IMethodBinding   method  = invNode.resolveMethodBinding();
		if (method == null) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		
		AST              ast       = invNode.getAST();
		ASTRewrite       rewrite   = ASTRewrite.create(ast);
		ITypeBinding[]   paramTyps = method.getParameterTypes();
		MethodInvocation accClass  = createGetClassMethodInvocation(invNode, method, ast);
		
		MethodInvocation methGetMeth = createGetClassMethodInvocation(invNode, paramTyps, ast);
		methGetMeth.setExpression(accClass);

		MethodInvocation methInv = createInvokeMethodInvocation(invNode, method, ast);
		methInv.setExpression(methGetMeth);

		rewrite.replace(invNode, methInv, null); // no edit group

		return new ASTRewriteCorrectionProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				context.getCompilationUnit(),
				rewrite, 
				getRelevance(), 
				JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_LOCAL));
	}

	@SuppressWarnings("unchecked")
	private MethodInvocation createGetClassMethodInvocation(MethodInvocation invNode, ITypeBinding[] paramTyps, AST ast) {
		MethodInvocation result = ast.newMethodInvocation();
		result.setName(ast.newSimpleName("getMethod")); //$NON-NLS-1$
		StringLiteral methName = ast.newStringLiteral();
		methName.setLiteralValue(invNode.getName().getFullyQualifiedName());
		result.arguments().add(methName);
		if (paramTyps != null && paramTyps.length > 0) {
			for (ITypeBinding paramTyp : paramTyps) {
				TypeLiteral curTyp = ast.newTypeLiteral();
				curTyp.setType(createType(paramTyp, ast));
				result.arguments().add(curTyp);
			}
		}
		return result;
	}

	private MethodInvocation createGetClassMethodInvocation(MethodInvocation invNode, IMethodBinding method, AST ast) {
		MethodInvocation accClass = null; 
		if (Modifier.isStatic(method.getModifiers())) {
			accClass = createClassForName(method.getDeclaringClass(), ast);
		} else {
			MethodInvocation methGetClass = ast.newMethodInvocation();
			methGetClass.setName(ast.newSimpleName("getClass")); //$NON-NLS-1$
			Expression exprVar = invNode.getExpression();
			if (exprVar != null) {
				Expression varAcc = (Expression)ASTNode.copySubtree(ast, exprVar);
				methGetClass.setExpression(varAcc);
			}
			accClass = methGetClass;
		}
		return accClass;
	}

	@SuppressWarnings("unchecked")
	private MethodInvocation createInvokeMethodInvocation(MethodInvocation node, IMethodBinding method, AST ast) {
		MethodInvocation result = ast.newMethodInvocation();
		result.setName(ast.newSimpleName("invoke")); //$NON-NLS-1$
		if (Modifier.isStatic(method.getModifiers())) {
			result.arguments().add(ast.newNullLiteral());
		} else {
			Expression exprVar  = node.getExpression();
			Expression receiver = null;
			if (exprVar != null) {
				receiver = (Expression)ASTNode.copySubtree(ast, exprVar); 
			} else {
				receiver = ast.newThisExpression();
			}
			result.arguments().add(receiver);
		}
		
		List<Expression> paramVars = node.arguments();
		for (Expression curParam : paramVars) {
			result.arguments().add(ASTNode.copySubtree(ast, curParam));
		}

		return result;
	}
}
