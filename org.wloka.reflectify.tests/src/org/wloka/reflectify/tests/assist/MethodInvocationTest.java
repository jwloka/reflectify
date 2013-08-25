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

import junit.framework.Test;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * 
 * 
 * @author Jan Wloka
 */
public class MethodInvocationTest extends TestBase {

	public MethodInvocationTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new Suite(MethodInvocationTest.class);
	}

	@Override
	public void setUpSuite() throws Exception {
		setTestProjectName("TestData");
		super.setUpSuite();
		getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, PROGRESS_MONITOR);
	}
	
	public void testGetAssistsWithMethodInvocationOnLocalVariableReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target1", new String[0]);
		
		int[] targetSelection = getMethodInvocationSelection(1, target);
		
		IJavaCompletionProposal actual = getProposal(
											ReflectifyMessages.ReflectifyAssistProcessor_method, 
											target, 
											targetSelection);
		
		assertProposal("d.getClass().getMethod(\"getFoo\").invoke(d)", actual);
	}

	public void testGetAssistsWithStaticMethodInvocationOnLocalVariableReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target2", new String[0]);
		
		int[] targetSelection = getMethodInvocationSelection(1, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.MethodInvocationData.Data\")" 
						+ ".getMethod(\"staticFoo\")" 
						+ ".invoke(null)", actual);
	}
	
	public void testGetAssistsWithStaticMethodInvocationOnClassAccessReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target3", new String[0]);
		
		int[] targetSelection = getMethodInvocationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.MethodInvocationData.Data\")" 
						+ ".getMethod(\"staticFoo\")" 
						+ ".invoke(null)", actual);
	}
	
	public void testGetAssistsWithMethodInvocationOnLocalVariableWithParametersReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target4", new String[0]);
		
		int[] targetSelection = getMethodInvocationSelection(1, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.MethodInvocationData.Data\")" 
						+ ".getMethod(\"staticBar\", String[].class)" 
						+ ".invoke(null, new String[] { \"dummy\" })", actual);
	}

	public void testGetAssistsWithStaticMethodInvocationOnClassAccessWithParametersReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target5", new String[0]);
		
		int[] targetSelection = getMethodInvocationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.MethodInvocationData.Data\")" 
				+ ".getMethod(\"staticBar\", String[].class)" 
				+ ".invoke(null, new String[] { \"dummy\" })", actual);
	}
	
	public void testGetAssistsWithMethodInvocationOnThisExpressionReflectifiesMethodInvocation() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "MethodInvocationData");
		IMethod target = type.getMethod("target6", new String[0]);

		int[] targetSelection = getMethodInvocationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_method, 
				target, 
				targetSelection);

		assertProposal("getClass().getMethod(\"target5\").invoke(this);", actual);
	}
	
	/** 
	 * @return selection (offset,length) of first method invocation node within statement given by <code>statementIndex</code>
	 */
	private int[] getMethodInvocationSelection(int statementIndex, IMethod target) {
		MethodDeclaration targetNode = compileTargetMethod(target);
		Statement stmt = (Statement) targetNode.getBody().statements().get(statementIndex );
		final int[] selection = new int[2];
		stmt.accept( new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				if (selection[1] == 0) {
					selection[0] = node.getStartPosition();
					selection[1] = node.getLength();
				}
				return false;
			}
		});
		return selection;
	}
}
