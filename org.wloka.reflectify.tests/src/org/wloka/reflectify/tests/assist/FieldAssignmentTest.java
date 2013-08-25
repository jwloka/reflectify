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
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * 
 * 
 * @author Jan Wloka
 */
public class FieldAssignmentTest extends TestBase {

	public FieldAssignmentTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new Suite(FieldAssignmentTest.class);
	}

	@Override
	public void setUpSuite() throws Exception {
		setTestProjectName("TestData");
		super.setUpSuite();
		getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, PROGRESS_MONITOR);
	}
	
	public void testGetAssistsWithFieldAssignmentReflectifiesFieldAssignment() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
		IMethod target = type.getMethod("target1", new String[0]);
		
		int[] targetSelection = getLastAssignmentExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
				target, 
				targetSelection);
		
		assertProposal("d.getClass().getField(\"_object\").set(d, new Object())", actual);
	}

	public void testGetAssistsWithStaticFieldAssignmentReflectifiesFieldAssignment() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
		IMethod target = type.getMethod("target2", new String[0]);
		
		int[] targetSelection = getLastAssignmentExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")" 
						+ ".getField(\"_staticObject\")" 
						+ ".set(null, new Object())", actual);
	}
	
	public void testGetAssistsWithIntFieldAssignmentReflectifiesFieldAssignment() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
		IMethod target = type.getMethod("target3", new String[0]);
		
		int[] targetSelection = getLastAssignmentExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
				target, 
				targetSelection);
		
		assertProposal("d.getClass().getField(\"_int\").setInt(d, 3)", actual);
	}

 	public void testGetAssistsWithStaticIntFieldAssignmentReflectifiesFieldAssignment() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
		IMethod target = type.getMethod("target4", new String[0]);
		
		int[] targetSelection = getLastAssignmentExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
						+ ".getField(\"_staticInt\")" 
						+ ".setInt(null, 10)", actual);
	}
 	
 	public void testGetAssistsWithByteFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target5", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_byte\").setByte(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticByteFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target6", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticByte\")" 
 				+ ".setByte(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithCharFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target7", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_char\").setChar(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticCharFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target8", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticChar\")" 
 				+ ".setChar(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithLongFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target9", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_long\").setLong(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticLongFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target10", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticLong\")" 
 				+ ".setLong(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithFloatFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target11", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_float\").setFloat(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticFloatFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target12", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticFloat\")" 
 				+ ".setFloat(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithDoubleFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target13", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_double\").setDouble(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticDoubleFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target14", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticDouble\")" 
 				+ ".setDouble(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithShortFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target15", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_short\").setShort(d, 3)", actual);
 	}
 	
 	public void testGetAssistsWithStaticShortFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target16", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticShort\")" 
 				+ ".setShort(null, 10)", actual);
 	}
 	
 	public void testGetAssistsWithBooleanFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target17", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_boolean\").setBoolean(d, true)", actual);
 	}
 	
 	public void testGetAssistsWithStaticBooleanFieldAssignmentReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target18", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAssignmentData.Data\")"
 				+ ".getField(\"_staticBoolean\")" 
 				+ ".setBoolean(null, true)", actual);
 	}
 	
 	public void testGetAssistsWithFieldAssignmentOnFieldAccessReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target19", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("this.getClass().getField(\"field\").set(this, new Data())", actual);
 	}
 	
 	public void testGetAssistsWithFieldAssignmentOnFieldAccessAfterMethodCallReflectifiesFieldAssignment() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAssignmentData");
 		IMethod target = type.getMethod("target20", new String[0]);
 		
 		int[] targetSelection = getLastAssignmentExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldwrite, 
 				target, 
 				targetSelection);
 		
 		assertProposal("getField().getClass().getField(\"_object\").set(getField(), new Data());", actual);
 	}
 	
	/** @return selection (offset,length) of expression node of LAST assignment */
	private int[] getLastAssignmentExpressionSelection(IMethod target) {
		MethodDeclaration targetNode = compileTargetMethod(target);
		final int[] selection = new int[2];
		targetNode.getBody().accept( new ASTVisitor() {
			@Override
			public boolean visit(Assignment node) {
				selection[0] = node.getStartPosition();
				selection[1] = node.getLength();
				return false;
			}
		});
		return selection;
	}
}
