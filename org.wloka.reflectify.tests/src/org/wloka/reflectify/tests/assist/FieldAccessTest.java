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
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.wloka.reflectify.ReflectifyMessages;


/**
 * 
 * 
 * @author Jan Wloka
 */
public class FieldAccessTest extends TestBase {

	public FieldAccessTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new Suite(FieldAccessTest.class);
	}

	@Override
	public void setUpSuite() throws Exception {
		setTestProjectName("TestData");
		super.setUpSuite();
		getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, PROGRESS_MONITOR);
	}
	
	public void testGetAssistsWithFieldAccessReflectifiesFieldAccess() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
		IMethod target = type.getMethod("target1", new String[0]);
		
		int[] targetSelection = getReturnStatementExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
											ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
											target, 
											targetSelection);
		
		assertProposal("d.getClass().getField(\"_object\").get(d)", actual);
	}

	public void testGetAssistsWithStaticFieldAccessReflectifiesFieldAccess() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
		IMethod target = type.getMethod("target2", new String[0]);
		
		int[] targetSelection = getReturnStatementExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
							+ ".getField(\"_staticObject\")" 
							+ ".get(null)", actual);
	}
	
	public void testGetAssistsWithIntFieldAccessReflectifiesFieldAccess() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
		IMethod target = type.getMethod("target3", new String[0]);
		
		int[] targetSelection = getReturnStatementExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
				target, 
				targetSelection);
		
		assertProposal("d.getClass().getField(\"_int\").getInt(d)", actual);
	}

 	public void testGetAssistsWithStaticIntFieldAccessReflectifiesFieldAccess() throws Exception {
		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
		IMethod target = type.getMethod("target4", new String[0]);
		
		int[] targetSelection = getReturnStatementExpressionSelection(target);
		
		IJavaCompletionProposal actual = getProposal(
				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
				target, 
				targetSelection);
		
		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
							+ ".getField(\"_staticInt\")" 
							+ ".getInt(null)", actual);
	}
	
 	public void testGetAssistsWithByteFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target5", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_byte\").getByte(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticByteFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target6", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticByte\")" 
 				+ ".getByte(null)", actual);
 	}
 	
 	public void testGetAssistsWithCharFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target7", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_char\").getChar(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticCharFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target8", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticChar\")" 
 				+ ".getChar(null)", actual);
 	}
 	
 	public void testGetAssistsWithLongFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target9", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_long\").getLong(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticLongFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target10", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticLong\")" 
 				+ ".getLong(null)", actual);
 	}
 	
 	public void testGetAssistsWithFloatFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target11", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_float\").getFloat(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticFloatFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target12", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticFloat\")" 
 				+ ".getFloat(null)", actual);
 	}
 	
 	public void testGetAssistsWithDoubleFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target13", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_double\").getDouble(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticDoubleFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target14", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticDouble\")" 
 				+ ".getDouble(null)", actual);
 	}
 	
 	public void testGetAssistsWithShortFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target15", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_short\").getShort(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticShortFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target16", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticShort\")" 
 				+ ".getShort(null)", actual);
 	}
 	
 	public void testGetAssistsWithBooleanFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target17", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("d.getClass().getField(\"_boolean\").getBoolean(d)", actual);
 	}
 	
 	public void testGetAssistsWithStaticBooleanFieldAccessReflectifiesFieldAccess() throws Exception {
 		IType   type   = getType("TestData", "src", "org.wloka.reflectify.testdata", "FieldAccessData");
 		IMethod target = type.getMethod("target18", new String[0]);
 		
 		int[] targetSelection = getReturnStatementExpressionSelection(target);
 		
 		IJavaCompletionProposal actual = getProposal(
 				ReflectifyMessages.ReflectifyAssistProcessor_fieldread, 
 				target, 
 				targetSelection);
 		
 		assertProposal("Class.forName(\"org.wloka.reflectify.testdata.FieldAccessData.Data\")" 
 				+ ".getField(\"_staticBoolean\")" 
 				+ ".getBoolean(null)", actual);
 	}
 	
	/** @return selection (offset,length) of expression node of first return statement **/
	private int[] getReturnStatementExpressionSelection(IMethod target) {
		MethodDeclaration targetNode = compileTargetMethod(target);
		final int[] selection = new int[2];
		targetNode.getBody().accept( new ASTVisitor() {
			@Override
			public boolean visit(ReturnStatement node) {
				if (selection[1] == 0) {
					Expression sel = node.getExpression();
					selection[0] = sel.getStartPosition();
					selection[1] = sel.getLength();
				}
				return false;
			}
		});
		return selection;
	}
}
