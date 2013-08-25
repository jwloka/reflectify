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

package org.wloka.reflectify;

import org.eclipse.osgi.util.NLS;

/**
 *
 * 
 * @author Jan Wloka
 */
public class ReflectifyMessages extends NLS {
	private static final String BUNDLE_NAME = "org.wloka.reflectify.messages"; //$NON-NLS-1$
	
	public static String ReflectifyAssistProcessor_class;
	public static String ReflectifyAssistProcessor_instance;
	public static String ReflectifyAssistProcessor_method;
	public static String ReflectifyAssistProcessor_fieldread;
	public static String ReflectifyAssistProcessor_fieldwrite;
	
	public static String NoOrIllegalNodeError;
	public static String BindingResolutionError;
	public static String UnexpectedAssignmentNodeError;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ReflectifyMessages.class);
	}
}
