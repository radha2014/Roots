/*
 * Created on 27-Aug-10
 *
 * com.roots.MainDAO.java
 * 
 */
package com.roots;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class MainDAO {

	private static int MEM_ID_SEQ = 1000;

	private static int COLL_ID_SEQ = 1000;

	private static int PIC_COLL_ID_SEQ = 1000;

	public static String getNewID(String string) {

		if (string.charAt(0) == 'A') {

			return string + MEM_ID_SEQ++;

		} else if (string.charAt(0) == 'P') {

			return string + PIC_COLL_ID_SEQ++;
		}
		return string + COLL_ID_SEQ++;
	}

	/**
	 * Depending on the parameter, decided if the data is to written to each
	 * folder, a single list
	 * 
	 * @throws IOException
	 */
	public void writeMembers2File(Map<String, Member> hmap) throws IOException {

		File f = new File("members.txt");
		FileOutputStream outFile = new FileOutputStream(f);

		String[] keys = CommonHelper.getSortedList(hmap.keySet());
		for (String memberID : keys) {
			Member member = hmap.get(memberID);
			writeToFile(memberID + "-" + member.asRecord(), outFile);
			System.out
					.println("writing.." + memberID + "-" + member.asRecord());
		}

		if (outFile != null) {
			outFile.close();
		}
	}

	public void writeCollections2File(
			Map<String, ArrayList<String>> siblingColl, String fileName)
			throws IOException {

		File f = new File(fileName);
		FileOutputStream outFile = new FileOutputStream(f);

		StringBuilder strb = new StringBuilder();

		String[] keys = CommonHelper.getSortedList(siblingColl.keySet());
		for (String collectionID : keys) {
			strb.delete(0, strb.length());
			strb.append(collectionID).append("-");
			for (String siblingID : (ArrayList<String>) siblingColl
					.get(collectionID)) {
				strb.append(siblingID).append("|");
			}
			writeToFile(strb.substring(0, strb.length() - 1), outFile);
		}

		if (outFile != null) {
			outFile.close();
		}
	}

	private void writeToFile(String str, FileOutputStream outFile)
			throws IOException {
		outFile.write(str.getBytes());
		outFile.write("\n".getBytes());
	}

	public void loadMembers(Map<String, Member> memberMasterMap)
			throws IOException {
		loadMembers(memberMasterMap, "members.txt");
	}

	/**
	 * Reads the input file to traverse member records, create member objects
	 * and puts them in a map
	 * 
	 * @param memberMasterMap
	 * @throws IOException
	 */
	public void loadMembers(Map<String, Member> memberMasterMap, String filename)
			throws IOException {

		File f = new File(filename);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String str = br.readLine();
		while (str != null) {
			String ID = str.substring(0, str.indexOf("-"));
			str = str.substring(str.indexOf("-") + 1);
			String[] memberDetails = str.split("\\|");
			String name = memberDetails[1];
			int age = Integer.parseInt(memberDetails[2]);

			Calendar dob = null;
			if (!CommonHelper.isEmpty(memberDetails[3])) {
				String[] dobArr = memberDetails[3].split(":");
				dob = Calendar.getInstance();
				dob.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dobArr[0]));
				dob.set(Calendar.MONTH, Integer.parseInt(dobArr[1]));
				dob.set(Calendar.YEAR, Integer.parseInt(dobArr[2]));
			}

			String gender = memberDetails[4];
			String fNameID = memberDetails[5];
			String mNameID = memberDetails[6];
			String whNameID = memberDetails[7];
			String siblingCollectionID = memberDetails[8];
			String kidCollectionID = memberDetails[9];
			String pics = memberDetails[10];
			String description = memberDetails[11];

			Member member = new Member(ID, name, age, dob, gender, fNameID,
					mNameID, whNameID, siblingCollectionID, kidCollectionID,
					pics, description);
			memberMasterMap.put(ID, member);

			str = br.readLine();
		}

		resetMemberSequence(memberMasterMap.keySet());
	}

	public void loadMemCollections(
			Map<String, ArrayList<String>> collectionMasterMap,
			String inFileName) throws IOException {
		loadCollections(collectionMasterMap, inFileName);
		resetMemCollectionSequence(collectionMasterMap.keySet());
	}

	public void loadImgCollections(
			Map<String, ArrayList<String>> collectionMasterMap,
			String inFileName) throws IOException {
		loadCollections(collectionMasterMap, inFileName);
		resetImgCollectionSequence(collectionMasterMap.keySet());
	}

	/**
	 * Reads the input file to traverse collection records, create collection
	 * objects and puts them in two separate maps, one for siblings, one for
	 * kids
	 * 
	 * @param collectionMasterMap
	 * @param kidsMasterMap
	 * @throws IOException
	 */
	public void loadCollections(
			Map<String, ArrayList<String>> collectionMasterMap,
			String inFileName) throws IOException {

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(inFileName);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			return;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String str = br.readLine();
		while (str != null) {
			String collID = str.substring(0, str.indexOf("-"));
			str = str.substring(str.indexOf("-") + 1);
			ArrayList<String> arrList = new ArrayList<String>();
			arrList.addAll(Arrays.asList(str.split("\\|")));
			collectionMasterMap.put(collID, arrList);
			str = br.readLine();
		}
	}

	private void resetMemberSequence(Set<String> keys) {
		if (keys.size() != 0) {
			MEM_ID_SEQ = resetSequence(keys);
		}
	}

	private void resetImgCollectionSequence(Set<String> keys) {
		if (keys.size() != 0) {
			PIC_COLL_ID_SEQ = resetSequence(keys);
		}
	}

	private void resetMemCollectionSequence(Set<String> keys) {
		if (keys.size() != 0) {
			COLL_ID_SEQ = resetSequence(keys);
		}
	}

	private int resetSequence(Set<String> keys) {
		int val = 0;
		for (String str : keys) {
			int count = Integer.parseInt(str.substring(1));
			if (count > val) {
				val = count;
			}
		}
		return val + 1;
	}

	public void write2File(Map<String, Member> memberMasterMap,
			Map<String, ArrayList<String>> siblingsMasterMap,
			Map<String, ArrayList<String>> imgCollectionMap) throws IOException {
		Set<String> memIDs = memberMasterMap.keySet();
		writeMembers2File(memberMasterMap);
		writeCollections2File(siblingsMasterMap, "collections.txt");
		writeCollections2File(imgCollectionMap, "images.txt");
	}

	/**
	 * load all images from a defined location and return them in a list
	 */
	public ArrayList<String> getImgList(String imagePath) {
		ArrayList<String> arrList = new ArrayList<String>();
		try {
			File path = new File(imagePath);
			File[] files = path.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					if (!f.getName().toUpperCase().endsWith("JPG")) {
						continue;
					}
					arrList.add(f.getName());
				}
			}
		} catch (Exception e) {
		}
		return arrList;
	}

	/**
	 * load all images from a defined location and return them in a list
	 */
	public ArrayList<String> getImgList() {
		String imagePath = "C:\\javapg\\workspace33\\Roots\\WebContent\\images";
		return getImgList(imagePath);
	}
}
