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
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;

/**
 * Holds methods shared among all reflectify proposal creators.
 * 
 * 
 * @author Jan Wloka
 */
public abstract class AbstractProposalCreator {
	
	public abstract IJavaCompletionProposal createProposal(ASTNode node, IInvocationContext context);
	
	@SuppressWarnings("unchecked")
	protected MethodInvocation createClassForName(ITypeBinding type, AST ast) {
		MethodInvocation methFor = ast.newMethodInvocation();
		methFor.setName(ast.newSimpleName("forName")); //$NON-NLS-1$
		methFor.setExpression(ast.newSimpleName("Class")); //$NON-NLS-1$
		String qualClassName = type.getQualifiedName();
		if (qualClassName.indexOf('<') > - 1) {
			qualClassName = qualClassName.substring(0, qualClassName.indexOf('<'));
		}
		StringLiteral methForParam = ast.newStringLiteral();
		methForParam.setLiteralValue(qualClassName);
		methFor.arguments().add(methForParam);
		return methFor;
	}

	protected Type createType(ITypeBinding typeBind, AST ast) {
		ITypeBinding realType = typeBind;
		Type         result   = null;
		if (typeBind.isArray()) {
			realType = typeBind.getElementType();
		}
		if (realType.isPrimitive()) {
			result = ast.newPrimitiveType(PrimitiveType.toCode(realType.getName()));
		} else if (realType.isParameterizedType()) {
			String typeName = realType.getName();
			result = ast.newSimpleType(ast.newSimpleName(typeName.substring(0, typeName.indexOf('<'))));
		} else {
			result = ast.newSimpleType(ast.newSimpleName(realType.getName()));
		}
		if (typeBind.isArray()) {
			return ast.newArrayType(result, typeBind.getDimensions());
		}
		return result;
	}

	protected int getRelevance() {
		return 1;
	}
}
