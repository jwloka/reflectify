package org.wloka.reflectify.testdata;

public class MethodInvocationData {
	/** d.getClass().getMethod("getFoo").invoke(d); */
	public void target1() {
		Data d = new Data();
		d.getFoo();
	}

	/** Class.forName("org.wloka.reflectify.testdata.MethodInvocationData.Data").getMethod("staticFoo").invoke(null); */
	@SuppressWarnings("static-access")
	public void target2() {
		Data d = new Data();
		d.staticFoo();
	}
	
	/** Class.forName("org.wloka.reflectify.testdata.MethodInvocationData.Data").getMethod("staticFoo").invoke(null); */
	public void target3() {
		Data.staticFoo();
	}
	
	/** Class.forName("org.wloka.reflectify.testdata.MethodInvocationData.Data").getMethod("staticBar", String[].class).invoke(null,new String[] { "dummy" } ); */
	@SuppressWarnings("static-access")
	public void target4() {
		Data d = new Data();
		d.staticBar(new String[] {"dummy"});
	}
	
	/** Class.forName("org.wloka.reflectify.testdata.MethodInvocationData.Data").getMethod("staticBar", String[].class).invoke(null,new String[] { "dummy" } ); */
	public void target5() {
		Data.staticBar(new String[] {"dummy"});
	}
	
	/** getClass().getMethod(\"target5\").invoke(this); */
	public void target6() {
		target5();
	}
	
	static class Data {
		public void getFoo() {}
		public static void staticFoo() {}
		public static void staticBar(String[] str) {}
		public void getThis() { getFoo(); }
	}
}
