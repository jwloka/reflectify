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


package org.wloka.reflectify.assist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.wloka.reflectify.internal.assist.ClassAccessProposalCreator;
import org.wloka.reflectify.internal.assist.ClassInstanceCreationProposalCreator;
import org.wloka.reflectify.internal.assist.FieldAccessProposalCreator;
import org.wloka.reflectify.internal.assist.FieldAssignmentProposalCreator;
import org.wloka.reflectify.internal.assist.MethodInvocationProposalCreator;



/**
 *
 * 
 * @author Jan Wloka
 */
public class ReflectifyAssistProcessor implements IQuickAssistProcessor {

	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
		ASTNode coveringNode= context.getCoveringNode();
		if (coveringNode != null) {
			List<IJavaCompletionProposal> result = new ArrayList<IJavaCompletionProposal>();
			
			// we don't care for errors/warnings and show the quick assists
			getReflectifyProposals(coveringNode, context, result);
			return result.toArray(new IJavaCompletionProposal[result.size()]);
		}
		return null;
	}

	public boolean hasAssists(IInvocationContext context) throws CoreException {
		ASTNode coveringNode = context.getCoveringNode();
		if (coveringNode != null) {
			return getReflectifyProposals(coveringNode, context, null);
		}
		return false;
	}

	private boolean getReflectifyProposals(ASTNode node, IInvocationContext context, List<IJavaCompletionProposal> result) {
		try {
			switch (node.getNodeType()) {
			case ASTNode.EXPRESSION_STATEMENT:
				return getReflectifyProposals(((ExpressionStatement)node).getExpression(), context, result);
			case ASTNode.RETURN_STATEMENT:
				return getReflectifyProposals(((ReturnStatement)node).getExpression(), context, result);
			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
					VariableDeclarationStatement varDecl  = (VariableDeclarationStatement)node;
					VariableDeclarationFragment  varFrgmt = (VariableDeclarationFragment)varDecl.fragments().iterator().next();
					return getReflectifyProposals(varFrgmt.getInitializer(), context, result);
			case ASTNode.TYPE_LITERAL:
				IJavaCompletionProposal caProp = new ClassAccessProposalCreator().createProposal(node, context);
				result.add(caProp);
				break;
			case ASTNode.CLASS_INSTANCE_CREATION:
				IJavaCompletionProposal cicProp = new ClassInstanceCreationProposalCreator().createProposal(node, context);
				result.add(cicProp);
				break;
			case ASTNode.METHOD_INVOCATION:
				IJavaCompletionProposal miProp = new MethodInvocationProposalCreator().createProposal(node, context);
				result.add(miProp);
				break;
			case ASTNode.SIMPLE_NAME:
			case ASTNode.FIELD_ACCESS:
			case ASTNode.QUALIFIED_NAME:
				IJavaCompletionProposal faProp = new FieldAccessProposalCreator().createProposal(node, context);
				result.add(faProp);
				break;
			case ASTNode.ASSIGNMENT:
				IJavaCompletionProposal fasProp = new FieldAssignmentProposalCreator().createProposal(node, context);
				result.add(fasProp);
				break;
			default:
				return false;
			}
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}