package com.roots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Tester {
	public static void main(String[] args) {
		String val = "abc";
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 25; i++)
			set.add(val + i);

		ArrayList<String> arrlist = new ArrayList<String>();
		String[] arr = new String[set.size()];
		Arrays.sort(set.toArray(arr));

		for (String s : arr) {
			System.out.println("" + s);
		}
	}
}
