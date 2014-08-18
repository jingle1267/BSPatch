package com.bspatch.test;

import android.util.Log;

/**
 * jni
 * @author Administrator
 *
 */
public class BSPatchDemo {
	private static boolean LOGD = true;
	private static String tag = "BSPatchDemo";
	
	static {
		try {
			System.loadLibrary("bspatch");
		} catch (UnsatisfiedLinkError e) {
			if (LOGD) {
				System.err.println("Cannot load hello library:\n " + e.toString());
				Log.e(tag, "Cannot load hello library:\n " + e.toString());
			}
		}
	}

	public native int combine(String oldFile, String newFile, String patch);
	
}
