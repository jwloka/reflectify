package org.wloka.reflectify.testdata;

public class FieldAssignmentData {
	private Data field;
	
	/** d.getClass().getField("_object").set(d, new Object()); */
	public void target1() {
		Data d = new Data();
		d._object = new Object(); 
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticObject").set(null, new Object()); */
	public void target2() {
	 	Data._staticObject = new Object();
	}

	/** d.getClass().getField("_int").setInt(d, 3); */
	public void target3() {
		Data d = new Data();
		d._int = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticInt").setInt(null, true); */
	public void target4() {
		Data._staticInt = 10;
	}
	
	/** d.getClass().getField("_byte").setByte(d, 3); */
	public void target5() {
		Data d = new Data();
		d._byte = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticByte").setByte(null, true); */
	public void target6() {
		Data._staticByte = 10;
	}
	
	/** d.getClass().getField("_char").setChar(d, 3); */
	public void target7() {
		Data d = new Data();
		d._char = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticChar").setChar(null, true); */
	public void target8() {
		Data._staticChar = 10;
	}
	
	/** d.getClass().getField("_long").setLong(d, 3); */
	public void target9() {
		Data d = new Data();
		d._long = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticLong").setLong(null, true); */
	public void target10() {
		Data._staticLong = 10;
	}
	
	/** d.getClass().getField("_float").setFloat(d, 3); */
	public void target11() {
		Data d = new Data();
		d._float = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticFloat").setFloat(null, true); */
	public void target12() {
		Data._staticFloat = 10;
	}
	
	/** d.getClass().getField("_double").setDouble(d, 3); */
	public void target13() {
		Data d = new Data();
		d._double = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticDouble").setDouble(null, true); */
	public void target14() {
		Data._staticDouble = 10;
	}
	
	/** d.getClass().getField("_short").setShort(d, 3); */
	public void target15() {
		Data d = new Data();
		d._short = 3;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticShort").setShort(null, true); */
	public void target16() {
		Data._staticShort = 10;
	}
	
	/** d.getClass().getField("_boolean").setBoolean(d, 3); */
	public void target17() {
		Data d = new Data();
		d._boolean = true;
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAssignmentData.Data").getField("_staticBoolean").setBoolean(null, true); */
	public void target18() {
		Data._staticBoolean = true;
	}
	
	/** this.getClass().getField("field").set(this, new Data()) */
	public void target19() {
		this.field = new Data();
	}
	
	/** getField().getClass().getField("_object").set(getField(), new Data()); */
	public void target20() {
		getField()._object = new Data();
	}
	
	private Data getField() {
		return field;
	}

	@SuppressWarnings("unused")
	static class Data {
		private Data 	_field = new Data();
		private Object 	_object = new Object();
		private int 	_int = 1;
		private byte 	_byte = 2;
		private char 	_char = 3;
		private long 	_long = 4l;
		private float 	_float = 5f;
		private double 	_double = 6d;
		private short 	_short = 7;
		private boolean	_boolean = true;
		private static Object 	_staticObject = new Object();
		private static int 		_staticInt = 1;
		private static byte 	_staticByte = 2;
		private static char 	_staticChar = 3;
		private static long 	_staticLong = 4l;
		private static float 	_staticFloat = 5f;
		private static double 	_staticDouble = 6d;
		private static short 	_staticShort = 7;
		private static boolean 	_staticBoolean = true;
	}
}
