package org.wloka.reflectify.testdata;

import java.io.File;

public class ClassInstanceCreationData {
	// Class.forName("org.wloka.reflectify.testdata.ClassInstanceCreationData.Data").getConstructor().newInstance();
	public void target1() {
		new Data();
	}
	// Class.forName("org.wloka.reflectify.testdata.ClassInstanceCreationData.Data").getConstructor(String.class).newInstance("bar");
	public void target2() {
		new Data("bar");
	}
	// Class.forName("org.wloka.reflectify.testdata.ClassInstanceCreationData.Data").getConstructor(int.class).newInstance(2);
	public void target3() {
	 	new Data(2);
	}
	// Class.forName("org.wloka.reflectify.testdata.ClassInstanceCreationData.Data").getConstructor(File.class).newInstance(new File("bar"));
	public void target4() {
		new Data(new File("bar"));
	}
	class Data {
		public Data() {}
		public Data(String str) {}
		public Data(int num) {}
		public Data(File file) {}
	}
}
