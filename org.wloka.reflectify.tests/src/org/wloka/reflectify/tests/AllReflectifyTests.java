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

package org.wloka.reflectify.tests;

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Jan Wloka
 */
public class AllReflectifyTests extends TestCase {

	public AllReflectifyTests(String name) {
		super(name);
	}

	public static Class<?>[] getAllTestClasses() {
		return new Class[] {
			org.wloka.reflectify.tests.assist.AllTests.class
		};
	}

	public static Test suite() {
		TestSuite ts = new TestSuite("All Reflectify Tests");

		Class<?>[] testClasses = getAllTestClasses();

		for (Class<?> testClass : testClasses) {
			try{
				Method suiteMethod = testClass.getDeclaredMethod(
						"suite", new Class[0]); //$NON-NLS-1$
				Test suite = (Test)suiteMethod.invoke(null, new Object[0]);
				ts.addTest(suite);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ts;
	}
}
