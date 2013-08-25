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
 * Creates reflectify proposal for class access via <code>TypeLiteral</code>, e.g.:
 * 		[String.class]	Class.forName("java.lang.String")
 *
 * 
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class ClassAccessProposalCreator extends AbstractProposalCreator {
	
	public IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context) {
		if (node == null || node.getNodeType() != ASTNode.TYPE_LITERAL) {
			throw new IllegalArgumentException(ReflectifyMessages.NoOrIllegalNodeError);
		}
		
		TypeLiteral  litNode  = (TypeLiteral)node;
		ITypeBinding typeBind = litNode.getType().resolveBinding();
		if (typeBind == null) {
			throw new IllegalArgumentException(ReflectifyMessages.BindingResolutionError);
		}
		
		AST              ast     = litNode.getAST();
		ASTRewrite       rewrite = ASTRewrite.create(ast);
		MethodInvocation methInv = createClassForName(typeBind, ast);

		rewrite.replace(litNode, methInv, null); // no edit group
		return new ASTRewriteCorrectionProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_class, 
				context.getCompilationUnit(),
				rewrite, 
				getRelevance(), 
				JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_LOCAL));
	}
}
