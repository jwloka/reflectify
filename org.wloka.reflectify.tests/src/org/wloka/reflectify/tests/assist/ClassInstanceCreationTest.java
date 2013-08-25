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
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * 
 * 
 * @author Jan Wloka
 */
public class ClassInstanceCreationTest extends TestBase {

	public ClassInstanceCreationTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new Suite(ClassInstanceCreationTest.class);
	}

	@Override
	public void setUpSuite() throws Exception {
		setTestProjectName("TestData");
		super.setUpSuite();
		getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, PROGRESS_MONITOR);
	}
	
	public void testGetAssistsWithClassInstanceOnClassAccessReflectifiesClassInstance() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "ClassInstanceCreationData");
		IMethod target = type.getMethod("target1", new String[0]);
		
		int[] targetSelection = getClassInstanceCreationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
											ReflectifyMessages.ReflectifyAssistProcessor_instance, 
											target, 
											targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.ClassInstanceCreationData.Data\")" 
							+ ".newInstance()", actual);
	}

	public void testGetAssistsWithClassInstanceOnConstructorCallWithParameterReflectifiesClassInstance() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "ClassInstanceCreationData");
		IMethod target = type.getMethod("target2", new String[0]);
		
		int[] targetSelection = getClassInstanceCreationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_instance, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.ClassInstanceCreationData.Data\")" 
						+ ".getConstructor(String.class)" 
						+ ".newInstance(\"bar\")", actual);
	}
	
	public void testGetAssistsWithClassInstanceOnConstructorCallWithPrimitiveParameterReflectiviesClassInstance() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "ClassInstanceCreationData");
		IMethod target = type.getMethod("target3", new String[0]);
		
		int[] targetSelection = getClassInstanceCreationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_instance, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.ClassInstanceCreationData.Data\")" 
				+ ".getConstructor(int.class)" 
				+ ".newInstance(2)", actual);
	}
	
	public void testGetAssistsWithClassInstanceOnConstructorCallWithComplexParameterReflectifiesClassInstance() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "ClassInstanceCreationData");
		IMethod target = type.getMethod("target4", new String[0]);
		
		int[] targetSelection = getClassInstanceCreationSelection(0, target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_instance, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.ClassInstanceCreationData.Data\")" 
						+ ".getConstructor(File.class)" 
						+ ".newInstance(new File(\"bar\"))", actual);
	}

	/** 
	 * @return selection (offset,length) of first class instance creation node within statement given by <code>statementIndex</code>
	 */
	private int[] getClassInstanceCreationSelection(int statementIndex, IMethod target) {
		MethodDeclaration targetNode = compileTargetMethod(target);
		Statement stmt = (Statement) targetNode.getBody().statements().get(statementIndex );
		final int[] selection = new int[2];
		stmt.accept( new ASTVisitor() {
			@Override
			public boolean visit(ClassInstanceCreation node) {
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
