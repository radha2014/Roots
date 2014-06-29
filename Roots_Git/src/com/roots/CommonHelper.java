/*
 * Created on 29-Aug-10
 *
 * com.roots.CommonHelper.java
 * 
 */
package com.roots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonHelper {

	public static String NEW_ENTRY = "NEW ENTRY";

	public static String BLANK_STR = "";

	public static String DELIMITER_STR = "|";

	public static boolean isEmpty(String str) {
		return (str == null || str.equals("null") || str.trim().length() == 0);
	}

	public static boolean isEmpty(ArrayList<String> list) {
		return (list == null || list.size() == 0);
	}

	public static boolean isNewEntry(String str) {
		return (!isEmpty(str) && NEW_ENTRY.equals(str));
	}

	public static boolean isEmpty(String[] strArr) {

		if (strArr != null) {
			for (String str : strArr) {
				if (!isEmpty(str)) {
					return false;
				}
			}
		}

		return true;
	}

	public static String convert2PipedelimitedString(String[] strArr) {
		StringBuilder strb = new StringBuilder();
		for (String str : strArr) {
			if (!isEmpty(str)) {
				strb.append(str).append("|");
			}
		}
		return strb.substring(0, strb.length() - 1);
	}

	public static ArrayList<String> asArray(String[] memberList) {
		ArrayList<String> arrList = new ArrayList<String>();
		for (String mem : memberList) {
			if (!isEmpty(mem)) {
				arrList.add(mem);
			}
		}
		return arrList;
	}

	public static void display(Map<String, ArrayList<String>> hmap, String msg) {
		Set<String> keys = hmap.keySet();
		System.out.println("displaying..." + msg);
		for (String key : keys) {
			System.out.println(key + "===[" + hmap.get(key) + "]");
		}
	}

	@SuppressWarnings( { "unchecked" })
	public static String[] getSortedList(Object obj) {

		if (obj instanceof List) {
			List<String> list = (List<String>) obj;
			String[] arr = new String[list.size()];
			Arrays.sort(list.toArray(arr));

			return arr;

		}
		if (obj instanceof Set) {
			Set<String> set = (Set<String>) obj;
			String[] arr = new String[set.size()];
			Arrays.sort(set.toArray(arr));

			return arr;
		}
		return null;
	}

	public static void log(String string) {
		System.out.println(string);
	}
}
