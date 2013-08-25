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

package org.wloka.reflectify.tests.assist;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.ui.text.correction.AssistContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.wloka.reflectify.assist.ReflectifyAssistProcessor;
import org.wloka.reflectify.tests.FileBasedTest;


/**
 * 
 *
 * @author Jan Wloka
 */
@SuppressWarnings("restriction")
public class TestBase extends FileBasedTest {
	protected final static NullProgressMonitor PROGRESS_MONITOR = new NullProgressMonitor();;
	
	public TestBase(String testName) {
		super(testName);
	}

	protected IJavaCompletionProposal getProposal(String proposalName, IMethod target, final int[] selection) throws CoreException {
		ReflectifyAssistProcessor testObj = new ReflectifyAssistProcessor();
		IJavaCompletionProposal[] result = testObj.getAssists(new AssistContext(target.getCompilationUnit(), selection[0], selection[1]), new IProblemLocation[0]);
		
		for (IJavaCompletionProposal cur : result) {
			if (proposalName.equals(cur.getDisplayString())) {
				return cur;
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	protected MethodDeclaration compileTargetMethod(final IMethod target) {
		ASTParser p = ASTParser.newParser(AST.JLS3);
		p.setSource(target.getCompilationUnit());
		p.setResolveBindings(true);
		CompilationUnit unit = (CompilationUnit) p.createAST(PROGRESS_MONITOR);
		final MethodDeclaration[] result = new MethodDeclaration[1];
		unit.accept( new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration node) {
				if (target.getElementName().equals(node.getName().toString())) {
					result[0] = node;
				}
				return false;
			}
		});
		return result[0];
	}
	
	protected static void assertProposal(String expectedContent, IJavaCompletionProposal proposal) {
		assertNotNull("No reflectify proposal found.", proposal);
		String actualContent = proposal.getAdditionalProposalInfo();
		assertNotNull("No reflectify proposal info found.", actualContent);
		
		// remove html tags, e.g. <br>, </b>
		actualContent = actualContent.replaceAll("\\<[\\w\\/]*\\>[\\s]*", ""); 
		assertTrue("Expected content not found in reflectify proposal.", actualContent.indexOf(expectedContent) > -1);
	}
}