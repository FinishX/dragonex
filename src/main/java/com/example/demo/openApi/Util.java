package com.example.demo.openApi;

public class Util {

	public static boolean TextIsEmpty(String str) {
		if(str == null || str.trim() == "") {
			return true;
		}
		return false;
	}
}
