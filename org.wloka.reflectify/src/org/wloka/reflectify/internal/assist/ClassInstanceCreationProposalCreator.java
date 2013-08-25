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
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.ASTRewriteCorrectionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * Creates reflectify proposal for class instance creations, e.g.:
 * 		[new FooClass()]	Class.forName("p1.FooClass").newInstance();	
 * 		[new FooClass(2)]	Class.forName("p1.FooClass").getConstructor(int.class).newInstance(2);
 * 
 * 
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class ClassInstanceCreationProposalCreator extends AbstractProposalCreator {
	
	@SuppressWarnings("unchecked")
	public IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context) {
		if (node == null || node.getNodeType() != ASTNode.CLASS_INSTANCE_CREATION) {
			throw new IllegalArgumentException(ReflectifyMessages.NoOrIllegalNodeError);
		}
		
		ClassInstanceCreation cicNode = (ClassInstanceCreation)node;
		
		AST            ast     = cicNode.getAST();
		ASTRewrite     rewrite = ASTRewrite.create(ast);
		IMethodBinding method  = cicNode.resolveConstructorBinding();
		
		if (method == null) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		ITypeBinding[] paramTyps = method.getParameterTypes();
		
		MethodInvocation methFor = createClassForName(cicNode.getType().resolveBinding(), ast);
		MethodInvocation methNew = ast.newMethodInvocation();
		methNew.setName(ast.newSimpleName("newInstance")); //$NON-NLS-1$
		if (paramTyps == null || paramTyps.length == 0) {
			methNew.setExpression(methFor);
		} else {
			MethodInvocation methGet = createGetConstructorMethod(paramTyps, ast);
			methGet.setExpression(methFor);
			
			List<Expression> paramVars = cicNode.arguments();
			for (Expression curParam : paramVars) {
				methNew.arguments().add(rewrite.createMoveTarget(curParam));
			}
			methNew.setExpression(methGet);
		}
		
		rewrite.replace(node, methNew, null); // no edit group

		return new ASTRewriteCorrectionProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_instance, 
				context.getCompilationUnit(),
				rewrite, 
				getRelevance(), 
				JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_LOCAL));
	}

	@SuppressWarnings("unchecked")
	private MethodInvocation createGetConstructorMethod(ITypeBinding[] paramTyps,  AST ast) {
		MethodInvocation methGet = ast.newMethodInvocation();
		methGet.setName(ast.newSimpleName("getConstructor")); //$NON-NLS-1$
		for (ITypeBinding paramTyp : paramTyps) {
			TypeLiteral curTyp = ast.newTypeLiteral();
			curTyp.setType(createType(paramTyp, ast));
			methGet.arguments().add(curTyp);
		}
		return methGet;
	}
}
