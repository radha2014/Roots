/*
 * Created on Aug 20, 2010
 *
 * com.roots.Member.java
 * 
 * Copyright (c) 2005 NCR Corporation
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * NCR Corporation. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with NCR Corporation. 
 */
package com.roots;

import java.util.Calendar;

public class Member {

	private String ID;

	private int age;

	private Calendar dateOfBirth;

	private String fNameID;

	private String gender;

	private String kidCollectionID;

	private String mNameID;

	private String name;

	private String pics;

	private String siblingCollectionID;

	private String whNameID;

	private String description;

	public Member(String ID, String name, String gender) {
		this.ID = ID;
		this.name = name;
		this.gender = gender;
	}

	public Member(String ID, String name, int age, Calendar dateOfBirth,
			String gender, String fName, String mName, String whName,
			String pics, String description) {
		super();
		this.ID = ID;
		this.name = name;
		this.age = age;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.fNameID = fName;
		this.mNameID = mName;
		this.whNameID = whName;
		this.pics = pics;
		this.description = description;
	}

	public Member(String ID, String name, int age, Calendar dateOfBirth,
			String gender, String fName, String mName, String whName,
			String siblingCollectionID, String kidCollectionID, String pics,
			String description) {
		super();
		this.ID = ID;
		this.name = name;
		this.age = age;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.fNameID = fName;
		this.mNameID = mName;
		this.whNameID = whName;
		this.siblingCollectionID = siblingCollectionID;
		this.kidCollectionID = kidCollectionID;
		this.pics = pics;
		this.description = description;
	}

	public int getAge() {
		return age;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public String getFNameID() {
		return fNameID;
	}

	public String getGender() {
		return gender;
	}

	public String getKidCollectionID() {
		return kidCollectionID;
	}

	public String getMNameID() {
		return mNameID;
	}

	public String getName() {
		return name;
	}

	public String getPics() {
		return pics;
	}

	public String getSiblingCollectionID() {
		return siblingCollectionID;
	}

	public String getWhNameID() {
		return whNameID;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setFNameID(String name) {
		fNameID = name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setKidCollectionID(String kids) {
		this.kidCollectionID = kids;
	}

	public void setMNameID(String name) {
		mNameID = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public void setSiblingCollectionID(String siblings) {
		this.siblingCollectionID = siblings;
	}

	public void setWhNameID(String whName) {
		this.whNameID = whName;
	}

	public String toString() {
		StringBuilder strb = new StringBuilder();
		strb.append("ID = ").append(ID).append("|");
		strb.append("name = ").append(name).append("|");
		strb.append("age = ").append(age).append("|");

		if (dateOfBirth != null) {
			strb.append("dateOfBirth = ").append(
					dateOfBirth.get(Calendar.DAY_OF_MONTH)).append("//");
			strb.append(dateOfBirth.get(Calendar.MONTH)).append("//");
			strb.append(dateOfBirth.get(Calendar.YEAR)).append("|");
		} else {
			strb.append("dateOfBirth = ").append("|");
		}

		strb.append("gender = ").append(gender).append("|");
		strb.append("fName = ").append(fNameID).append("|");
		strb.append("mName = ").append(mNameID).append("|");
		strb.append("whName = ").append(whNameID).append("|");
		strb.append("siblings === ").append("|").append(siblingCollectionID);
		strb.append("kids === ").append("|").append(kidCollectionID);
		strb.append("pics = ").append(pics).append("\n");
		return strb.toString();
	}

	public String asRecord() {

		StringBuilder strb = new StringBuilder();
		strb.append(ID).append("|");
		strb.append(name).append("|");
		strb.append(age).append("|");

		if (dateOfBirth != null) {
			strb.append(dateOfBirth.get(Calendar.DAY_OF_MONTH)).append(":");
			strb.append(dateOfBirth.get(Calendar.MONTH)).append(":");
			strb.append(dateOfBirth.get(Calendar.YEAR));
		}

		strb.append("|");
		strb.append(isEmpty(gender) ? "" : gender).append("|");
		strb.append(fNameID).append("|");
		strb.append(mNameID).append("|");
		strb.append(whNameID).append("|");
		strb.append(siblingCollectionID).append("|");
		strb.append(kidCollectionID).append("|");
		strb.append(pics).append("|");
		// patch to fix blank being saved to file
		if (CommonHelper.isEmpty(description)) {
			strb.append("null");
		} else {
			strb.append(description);
		}
		return strb.toString();
	}

	private boolean isEmpty(String str) {
		return str == null || "null".equals(str) || str.trim().length() == 0;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
