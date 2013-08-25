package org.wloka.reflectify.testdata;

public class FieldAccessData {
	/** d.getClass().getField("_object").get(d); */
	public Object target1() {
		Data d = new Data();
		return d._object;		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticObject").get(null); */
	public Object target2() {
		return Data._staticObject;	
	}

	/** d.getClass().getField("_int").getInt(d); */
	public int target3() {
		Data d = new Data();
	 	return d._int; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticInt").getInt(null); */
	public int target4() {
		return Data._staticInt;	
	}
	
	/** d.getClass().getField("_byte").getByte(d); */
	public int target5() {
		Data d = new Data();
		return d._byte; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticByte").getByte(null); */
	public int target6() {
		return Data._staticByte;	
	}
	
	/** d.getClass().getField("_char").getChar(d); */
	public char target7() {
		Data d = new Data();
		return d._char; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticChar").getChar(null); */
	public char target8() {
		return Data._staticChar;	
	}
	
	/** d.getClass().getField("_long").getLong(d); */
	public long target9() {
		Data d = new Data();
		return d._long; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticLong").getLong(null); */
	public long target10() {
		return Data._staticLong;	
	}
	
	/** d.getClass().getField("_float").getFloat(d); */
	public float target11() {
		Data d = new Data();
		return d._float; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticFloat").getFloat(null); */
	public float target12() {
		return Data._staticFloat;	
	}
	
	/** d.getClass().getField("_double").getDouble(d); */
	public double target13() {
		Data d = new Data();
		return d._double; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticDouble").getDouble(null); */
	public double target14() {
		return Data._staticDouble;	
	}
	
	/** d.getClass().getField("_short").getShort(d); */
	public short target15() {
		Data d = new Data();
		return d._short; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticShort").getShort(null); */
	public short target16() {
		return Data._staticShort;	
	}
	
	/** d.getClass().getField("_boolean").getBoolean(d); */
	public boolean target17() {
		Data d = new Data();
		return d._boolean; 		
	}
	/** Class.forName("org.wloka.reflectify.testdata.FieldAccessData.Data").getField("_staticBoolean").getBoolean(null); */
	public boolean target18() {
		return Data._staticBoolean;	
	}
	
	static class Data {
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
