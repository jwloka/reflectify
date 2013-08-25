package org.wloka.reflectify.testdata;

import java.util.List;


public class ClassAccessData {
	
	public void target1() {
		String.class.toString();
	}
	
	public Class<String[]> target2() {
		return String[].class;
	}
	
	@SuppressWarnings("rawtypes")
	public Class<List> target3() {
		return List.class;
	}
}
