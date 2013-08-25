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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.tests.model.AbstractJavaModelTests;

/**
 *
 *
 * @author Jan Wloka
 */
public class FileBasedTest extends AbstractJavaModelTests {

    private static final String SEGMENT_DELIMITER = "/";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String WORKSPACE_DIR = "workspace";

	private List<String> testProjects;

	public FileBasedTest(String testName, String testProject) {
		this(testName);
		if (testProject == null) {
			throw new IllegalArgumentException("Test project cannot be to null!");
		}
		setTestProjectName(testProject);
	}

	public FileBasedTest(String testName) {
		super(testName);
	}

	@Override
	public void setUpSuite() throws Exception {
		// ensure auto-building is turned off
        IWorkspaceDescription descr = getWorkspace().getDescription();
        if (descr.isAutoBuilding()) {
            descr.setAutoBuilding(false);
            getWorkspace().setDescription(descr);
        }
        try {
        	if (testProjects != null) {
        		for (String curPrj : testProjects) {
        			setUpJavaProject(curPrj);
				}
        	}
		}
        catch (NullPointerException ex) {
			throw new IllegalStateException("Test project has not been set properly!");
		}

		super.setUpSuite();
	}

    @Override
	public void tearDownSuite() throws Exception {
    	if (testProjects != null) {
    		for (String curPrj : testProjects) {
    			this.deleteProject(curPrj);
			}
    	}
        super.tearDownSuite();
    }

    @Override
	public String getSourceWorkspacePath() {
        return getPluginDirectoryPath()
                + java.io.File.separator
                + WORKSPACE_DIR;
    }

    protected String getResource(String packageName, String resourceName) {
        IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

        IResource resource = wsRoot.findMember(new Path(SEGMENT_DELIMITER
                + testProjects
                + SEGMENT_DELIMITER
                + packageName
                + SEGMENT_DELIMITER
                + resourceName));
        assertNotNull("No resource found", resource);
        return resource.getLocation().toOSString();
    }


    protected String getSource(ASTNode astNode, char[] source) {
        String result = new String(
        		CharOperation.subarray(
        					source,
        					astNode.getStartPosition() + 1,
        					astNode.getStartPosition()
        						+ astNode.getLength()
        						- 1));
        if (result.endsWith("\\n")) {
            return result.substring(0, result.length() - 2) + LINE_SEPARATOR;
        }
        return result;
    }

	protected IJavaProject getTestProject(String prj) {
		return this.testProjects != null ? getJavaProject(prj) : null;
	}

	protected List<String> getTestProjectNames() {
		return testProjects;
	}

	protected void setTestProjectName(String prjName) {
		this.testProjects = null;
		addTestProjectName(prjName);
	}

	protected void addTestProjectName(String prjName) {
		if (this.testProjects == null) {
			this.testProjects = new ArrayList<String>();
		}
		if (prjName != null && !this.testProjects.contains(prjName)) {
			this.testProjects.add(prjName);
		}
	}

	public IType getType(String projectName, String srcFolder, String pkg, String typeName) throws JavaModelException {
	    ICompilationUnit declaringUnit =
	        getCompilationUnit(projectName, srcFolder, pkg, typeName + ".java");

	    IType type = declaringUnit != null ? declaringUnit.getType(typeName) : null;
	    if (type == null) {
		    type = getJavaProject(projectName).findType(pkg + "." + typeName);
	    }
	    assertNotNull(type);
	    assertTrue(type.exists());

	    return type;
	}

	/**
	 * Returns the OS path to the directory that contains this plug-in.
	 */
	@Override
	protected String getPluginDirectoryPath() {
		try {
			URL    platformURL = Platform.getBundle(ReflectifyTestPlugin.PLUGIN_ID).getEntry(SEGMENT_DELIMITER);
			String pluginFile  = FileLocator.toFileURL(platformURL).getFile();
			return new File(pluginFile).getAbsolutePath();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	protected String[] getProjectClasspath() {
		List<String> result = new ArrayList<String>();

		for (IClasspathEntry curEntry : projectClasspath) {
			String curPath = curEntry.getPath().toOSString();
			result.add(curPath);
		}

		return result.toArray( new String[result.size()] );
	}
}