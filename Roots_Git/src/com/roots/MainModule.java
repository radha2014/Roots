/*
 * Created on Aug 20, 2010
 *
 * com.roots.MainModule.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class MainModule {

	private static Map<String, Member> memberMasterMap = new HashMap<String, Member>();

	private static Map<String, ArrayList<String>> collectionMasterMap = new HashMap<String, ArrayList<String>>();

	// [ img.jpg, <memID_1, memID_2..memID_n> ]
	private static Map<String, ArrayList<String>> img2MemberMap = new HashMap<String, ArrayList<String>>();

	// [ imgColID, <img.jpg_1, img_2.jpg..img.jpg_n> ]
	private static Map<String, ArrayList<String>> imgCollectionMap = new HashMap<String, ArrayList<String>>();

	private static MainDAO _mainDAO = null;

	private static ResourceBundle rb = null;

	// store all images
	private static ArrayList<String> availableImgList = null;

	private static Member CURRENT_MEMBER = null;

	private DataSetter dataSetter = null;

	public MainModule() {
		rb = ResourceBundle.getBundle("Resources");
		System.out.println(rb.getString("DATA_FILE_LOCATION"));

		_mainDAO = new MainDAO();
		dataSetter = new DataSetter(memberMasterMap, collectionMasterMap,
				imgCollectionMap, img2MemberMap);
		try {
			_mainDAO.loadMembers(memberMasterMap);
			_mainDAO.loadMemCollections(collectionMasterMap, "collections.txt");
			_mainDAO.loadImgCollections(imgCollectionMap, "images.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addToMasterMap(String str, Member mem) {
		System.out.println("adding..." + str + "..." + mem.getName());
		memberMasterMap.put(str, mem);
	}

	private static String getNewMemberID() {
		return MainDAO.getNewID("A");
	}

	private static String getNewMemberCollectionID() {
		return MainDAO.getNewID("C");
	}

	private static String getNewImgCollectionID() {
		return MainDAO.getNewID("P");
	}

	/**
	 * 1. Create a new member<br>
	 * 2. establish sibling collection<br>
	 * 3. establish kids collection set<br>
	 * 4. update father record with kid collection, wife <br>
	 * 5. set mother record with kid collection, husband<br>
	 * 6. set wife/husband record with kid collection, husband/wife<br>
	 * 7. update each sibling record with father, mother, sibling collection<br>
	 * 8. update each kid record with father, mother, sibling collction<br>
	 * 
	 * @param request
	 */
	public void captureData(HttpServletRequest request) {

		// check if create a new member or update existing member
		Member member = null;
		if (request.getParameter("memberID") != null
				&& request.getParameter("requestedID") != null
				&& request.getParameter("memberID").equals(
						request.getParameter("requestedID"))) {

			// update existing member
			member = updateMember(request, request.getParameter("requestedID"));

		} else { // create a new member and details of collection records.

			member = newMember(request);
			addToMasterMap(member.getID(), member);

		}
		CURRENT_MEMBER = member;

		// update relations of father (wife, kids)
		Member f = memberMasterMap.get(member.getFNameID());
		f.setKidCollectionID(CURRENT_MEMBER.getSiblingCollectionID());
		if (f.getWhNameID() == null) {
			f.setWhNameID(member.getMNameID());
		}

		// update relations of mother (husband, kids)
		Member m = memberMasterMap.get(member.getMNameID());
		m.setKidCollectionID(CURRENT_MEMBER.getSiblingCollectionID());
		if (m.getWhNameID() == null) {
			m.setWhNameID(member.getFNameID());
		}

		// update relations of wife/husband (wife/husband, kids)
		Member wh = memberMasterMap.get(member.getWhNameID());
		wh.setKidCollectionID(CURRENT_MEMBER.getKidCollectionID());
		if (wh.getWhNameID() == null) {
			wh.setWhNameID(member.getID());
		}

		// relations of siblings (father, mother, siblings)
		if (member.getSiblingCollectionID() != null) {
			ArrayList<String> siblingIDList = collectionMasterMap.get(member
					.getSiblingCollectionID());
			if (!CommonHelper.isEmpty(siblingIDList)) {
				for (String siblingID : siblingIDList) {
					Member sibling = memberMasterMap.get(siblingID);
					sibling.setFNameID(member.getFNameID());
					sibling.setMNameID(member.getMNameID());
					sibling.setSiblingCollectionID(member
							.getSiblingCollectionID());
				}
			}
		}

		// update relations of kids (father, mother, siblings)
		if (member.getKidCollectionID() != null) {
			List<String> kidIDList = (List<String>) collectionMasterMap
					.get(member.getKidCollectionID());
			for (String kidID : kidIDList) {
				Member kid = memberMasterMap.get(kidID);
				kid.setFNameID(member.getID());
				kid.setMNameID(member.getWhNameID());
				kid.setSiblingCollectionID(member.getKidCollectionID());
			}
		}
		// ImgUploader.getFiless(request);
	}

	private String establishSiblings(HttpServletRequest request, Member member) {

		ArrayList<String> siblingArr = new ArrayList<String>();
		String newSiblingCollectionID = getNewMemberCollectionID();

		collectionMasterMap.put(newSiblingCollectionID, siblingArr);

		String[] siblingsName = request.getParameterValues("siblings");
		String[] siblingsGender = request.getParameterValues("siblingsGender");
		int idx = 0;
		for (String siblingName : siblingsName) {
			String siblingGender = siblingsGender[idx++];
			if (!CommonHelper.isEmpty(siblingName)
					&& !CommonHelper.isEmpty(siblingGender)) {
				String id = getNewMemberID();
				Member bd = new Member(id, siblingName, siblingGender);
				addToMasterMap(id, bd);
				siblingArr.add(id);
			}
		}
		siblingArr.add(member.getID());
		return newSiblingCollectionID;
	}

	/**
	 * Check if ID exists, update the name and gender
	 * 
	 * @param request
	 * @param member
	 * @return
	 */
	private String updateSiblings(HttpServletRequest request, Member member) {

		if (CommonHelper.NEW_ENTRY.equals(request.getParameter("D26"))) {
			return establishSiblings(request, member);
		}

		String newSiblingCollectionID = member.getSiblingCollectionID();
		ArrayList<String> siblingArr = null;

		if (CommonHelper.isEmpty(newSiblingCollectionID)) {
			siblingArr = new ArrayList<String>();
			newSiblingCollectionID = getNewMemberCollectionID();
			collectionMasterMap.put(newSiblingCollectionID, siblingArr);
		} else {
			siblingArr = collectionMasterMap.get(newSiblingCollectionID);
			siblingArr.clear();
		}

		String[] siblingsName = request.getParameterValues("siblings");
		String[] siblingsID = request.getParameterValues("siblingsID");
		String[] siblingsGender = request.getParameterValues("siblingsGender");
		for (int idx = 0; idx < siblingsID.length; idx++) {
			String siblingName = siblingsName[idx];
			String siblingID = siblingsID[idx];
			String siblingGender = siblingsGender[idx];
			if (!CommonHelper.isEmpty(siblingID)
					&& !CommonHelper.isNewEntry(siblingID)) {
				if (!siblingID.startsWith(member.getID())) {
					Member sib = memberMasterMap.get(siblingID);
					siblingArr.add(sib.getID());
				}
			} else {
				if (!CommonHelper.isEmpty(siblingName)) {
					String id = getNewMemberID();
					Member sib = new Member(id, siblingName, siblingGender);
					addToMasterMap(id, sib);
					siblingArr.add(id);
				}
			}
		}
		if (!siblingArr.contains(member.getID())) {
			siblingArr.add(member.getID());
		}
		return newSiblingCollectionID;
	}

	/**
	 * Check if ID exists, update the name and gender
	 * 
	 * @param request
	 * @param member
	 * @return
	 */
	private String updateKids(HttpServletRequest request, Member member) {

		if (CommonHelper.NEW_ENTRY.equals(request.getParameter("D27"))) {
			return establishkids(request, member);
		}

		String[] kidsName = request.getParameterValues("kids");
		String[] kidsID = request.getParameterValues("kidsID");
		String[] kidsGender = request.getParameterValues("kidsGender");

		if (CommonHelper.isEmpty(kidsName)) {
			return null;
		}

		String newKidCollectionID = member.getKidCollectionID();
		ArrayList<String> kidArr = null;
		if (CommonHelper.isEmpty(newKidCollectionID)) {
			kidArr = new ArrayList<String>();
			newKidCollectionID = getNewMemberCollectionID();
			collectionMasterMap.put(newKidCollectionID, kidArr);
		} else {
			kidArr = collectionMasterMap.get(newKidCollectionID);
			kidArr.clear();
		}

		for (int idx = 0; idx < kidsID.length; idx++) {
			String kidName = kidsName[idx];
			String kidID = kidsID[idx];
			String kidGender = kidsGender[idx];
			if (!CommonHelper.isEmpty(kidID) && !CommonHelper.isNewEntry(kidID)) {
				Member kid = memberMasterMap.get(kidID);
				kidArr.add(kid.getID());
			} else {
				if (!CommonHelper.isEmpty(kidName)) {
					String id = getNewMemberID();
					Member sib = new Member(id, kidName, kidGender);
					addToMasterMap(id, sib);
					kidArr.add(id);
				}
			}
		}
		return newKidCollectionID;
	}

	private String establishkids(HttpServletRequest request, Member member) {

		String[] kidsName = request.getParameterValues("kids");
		String[] kidsGender = request.getParameterValues("kidsGender");

		if (CommonHelper.isEmpty(kidsName)) {
			return null;
		}

		System.out.println("establishkids called..");
		String newKidCollectionID = getNewMemberCollectionID();
		ArrayList<String> kidArr = new ArrayList<String>();
		collectionMasterMap.put(newKidCollectionID, kidArr);

		int idx = 0;
		for (String kidName : kidsName) {
			String kidGender = kidsGender[idx++];
			if (!CommonHelper.isEmpty(kidName)
					&& !CommonHelper.isEmpty(kidGender)) {
				String id = getNewMemberID();
				Member k = new Member(id, kidName, kidGender);
				addToMasterMap(id, k);
				kidArr.add(id);
			}
		}
		return newKidCollectionID;
	}

	private Member updateMember(HttpServletRequest request, String requestedID) {

		System.out.println("Update member called..");
		String name = request.getParameter("T1");

		int age = 0;
		if (!CommonHelper.isEmpty(request.getParameter("T14"))) {
			age = Integer.parseInt(request.getParameter("T14"));
		}

		Calendar dateOfBirth = null;

		if (request.getParameter("D2") != null
				&& request.getParameter("D3") != null
				&& request.getParameter("D4") != null) {
			int day = Integer.parseInt(request.getParameter("D2"));
			int month = Integer.parseInt(request.getParameter("D3"));
			int year = Integer.parseInt(request.getParameter("D4"));
			dateOfBirth = Calendar.getInstance();
			dateOfBirth.set(Calendar.DAY_OF_MONTH, day);
			dateOfBirth.set(Calendar.MONTH, month);
			dateOfBirth.set(Calendar.YEAR, year);
		}

		String gender = request.getParameter("D5");

		String fNameID = null;
		if (CommonHelper.isEmpty(request.getParameter("D23"))
				|| CommonHelper.NEW_ENTRY.equals(request.getParameter("D23"))) {
			fNameID = getNewMemberID();
			Member f = new Member(fNameID, request.getParameter("T3"), "MALE");
			addToMasterMap(fNameID, f);
		} else {
			fNameID = request.getParameter("D23");
			memberMasterMap.get(fNameID).setName(request.getParameter("T3"));
		}

		String mNameID = null;
		if (CommonHelper.isEmpty(request.getParameter("D24"))
				|| CommonHelper.NEW_ENTRY.equals(request.getParameter("D24"))) {
			mNameID = getNewMemberID();
			addToMasterMap(mNameID, new Member(mNameID, request
					.getParameter("T4"), "FEMALE"));
		} else {
			mNameID = request.getParameter("D24");
			memberMasterMap.get(mNameID).setName(request.getParameter("T4"));
		}

		String whNameID = null;
		if (CommonHelper.isEmpty(request.getParameter("D25"))
				|| CommonHelper.NEW_ENTRY.equals(request.getParameter("D25"))) {
			whNameID = getNewMemberID();
			addToMasterMap(whNameID, new Member(whNameID, request
					.getParameter("T5"), ("FEMALE".equals(gender)) ? "MALE"
					: "FEMALE"));
		} else {
			whNameID = request.getParameter("D25");
			memberMasterMap.get(whNameID).setName(request.getParameter("T5"));
		}

		String description = request.getParameter("description");
		description = description.replaceAll("\r\n", "<br>");

		Member member = memberMasterMap.get(requestedID);
		member.setName(name);
		member.setAge(age);
		member.setDateOfBirth(dateOfBirth);
		member.setGender(gender);
		member.setFNameID(fNameID);
		member.setMNameID(mNameID);
		member.setWhNameID(whNameID);
		member.setDescription(description);

		String siblingCollection = updateSiblings(request, member);
		member.setSiblingCollectionID(siblingCollection);

		String kidCollection = updateKids(request, member);
		member.setKidCollectionID(kidCollection);

		return member;
	}

	/**
	 * Called to create a new member
	 * 
	 * @param request
	 * @return
	 */
	public Member newMember(HttpServletRequest request) {

		String name = request.getParameter("T1");
		System.out.println("New member creation called.." + name);

		int age = 0;
		if (!CommonHelper.isEmpty(request.getParameter("T14"))) {
			age = Integer.parseInt(request.getParameter("T14"));
		}
		Calendar dateOfBirth = null;

		if (request.getParameter("D2") != null
				&& request.getParameter("D3") != null
				&& request.getParameter("D4") != null) {
			int day = Integer.parseInt(request.getParameter("D2"));
			int month = Integer.parseInt(request.getParameter("D3"));
			int year = Integer.parseInt(request.getParameter("D4"));
			dateOfBirth = Calendar.getInstance();
			dateOfBirth.set(Calendar.DAY_OF_MONTH, day);
			dateOfBirth.set(Calendar.MONTH, month);
			dateOfBirth.set(Calendar.YEAR, year);
		}

		String gender = request.getParameter("D5");

		String fNameID = null;
		if (CommonHelper.NEW_ENTRY.equals(request.getParameter("D23"))) {
			fNameID = getNewMemberID();
			Member f = new Member(fNameID, request.getParameter("T3"), "MALE");
			addToMasterMap(fNameID, f);
		} else {
			fNameID = request.getParameter("D23");
			Member f = memberMasterMap.get(fNameID);
			f.setName(request.getParameter("T3"));
		}

		String mNameID = null;
		if (CommonHelper.NEW_ENTRY.equals(request.getParameter("D24"))) {
			mNameID = getNewMemberID();
			addToMasterMap(mNameID, new Member(mNameID, request
					.getParameter("T4"), "FEMALE"));
		} else {
			mNameID = request.getParameter("D24");
			Member m = memberMasterMap.get(mNameID);
			m.setName(request.getParameter("T4"));
		}

		String whNameID = null;
		if (CommonHelper.NEW_ENTRY.equals(request.getParameter("D25"))) {
			whNameID = getNewMemberID();
			addToMasterMap(whNameID, new Member(whNameID, request
					.getParameter("T5"), ("FEMALE".equals(gender)) ? "MALE"
					: "FEMALE"));
		} else {
			whNameID = request.getParameter("D25");
			Member wh = memberMasterMap.get(whNameID);
			wh.setName(request.getParameter("T5"));
		}

		String description = request.getParameter("description");
		description = description.replaceAll("\r\n", "<br>");

		String id = getNewMemberID();
		Member member = new Member(id, name, age, dateOfBirth, gender, fNameID,
				mNameID, whNameID, null, description);

		String siblingCollection = establishSiblings(request, member);
		member.setSiblingCollectionID(siblingCollection);

		String kidCollection = establishkids(request, member);
		member.setKidCollectionID(kidCollection);

		return member;
	}

	public void write2File() throws IOException {
		CommonHelper.display(img2MemberMap, "img2MemberMap");
		_mainDAO.write2File(memberMasterMap, collectionMasterMap,
				imgCollectionMap);
	}

	public void reload(HttpServletRequest request) throws IOException {
		CURRENT_MEMBER = null;
		memberMasterMap.clear();
		collectionMasterMap.clear();
		imgCollectionMap.clear();
		dataSetter.clearLinks();
		_mainDAO.loadMembers(memberMasterMap);
		_mainDAO.loadCollections(collectionMasterMap, "collections.txt");
		_mainDAO.loadCollections(imgCollectionMap, "images.txt");
		refreshImg2MemberMap();
	}

	/**
	 * initializes img2MemberMap hashmap [ img.jpg, <memID_1, memID_2..memID_n> ]
	 * img2MemberMap maintains all memberids linked to an image
	 * 
	 */
	private void refreshImg2MemberMap() {

		for (String memID : memberMasterMap.keySet()) {
			String picColID = memberMasterMap.get(memID).getPics();
			if (!CommonHelper.isEmpty(picColID)) {
				ArrayList<String> pics = imgCollectionMap.get(picColID);
				for (String img : pics) {
					ArrayList<String> linkedMembers = img2MemberMap.get(img);
					if (CommonHelper.isEmpty(linkedMembers)) {
						linkedMembers = new ArrayList<String>();
						img2MemberMap.put(img, linkedMembers);
					}
					if (!linkedMembers.contains(memID)) {
						linkedMembers.add(memID);
					}
				}
			}

		}
		CommonHelper.display(img2MemberMap, "img2MemberMap being initialized");
	}

	public void setData(HttpServletRequest request) {

		if ("OpenMem".equals(request.getParameter("nextAction"))) {
			String requestedID = request.getParameter("memberID");
			CURRENT_MEMBER = memberMasterMap.get(requestedID);
		}
		dataSetter.setData(request, CURRENT_MEMBER != null ? CURRENT_MEMBER
				.getID() : null);

	}

	/**
	 * load all images from a defined location and return them in a list
	 */
	public void getImgList(HttpServletRequest request) {
		ArrayList<String> arrList = _mainDAO.getImgList();
		String[] arr = CommonHelper.getSortedList(arrList);
		availableImgList = new ArrayList<String>(Arrays.asList(arr));
	}

	/**
	 * set the requested current image and linked members in the request
	 * 
	 * @param request
	 */
	public void setImgList(HttpServletRequest request) {

		if (CommonHelper.isEmpty(availableImgList)) {
			getImgList(request);
		}

		int currentImage = 0;
		if (!CommonHelper.isEmpty(request.getParameter("current"))) {
			currentImage = Integer.parseInt(request.getParameter("current"));
		}

		if (!CommonHelper.isEmpty(availableImgList)
				&& availableImgList.get(currentImage) != null) {
			request.setAttribute("images", availableImgList.get(currentImage));
			request.setAttribute("linkedMemberList", img2MemberMap
					.get(availableImgList.get(currentImage)));
		}
		request.setAttribute("current", currentImage);
		request.setAttribute("availableImgList", availableImgList);
		request.setAttribute("imageCount", availableImgList.size());
		request.setAttribute("memberlist", dataSetter.getMemberList());

		HashMap<String, Integer> hmap = new PaginationModule().getPages(
				currentImage, availableImgList.size());
		Set<String> set = hmap.keySet();
		for (String key : set) {
			// set pagination links to request....in the format of pgN
			request.setAttribute(key, hmap.get(key));
		}
	}

	/**
	 * get image, member list update img2Mmeber, member2Img update each member
	 * (add or remove image from it's pic list)
	 * 
	 * @param request
	 */
	public void saveImgList(HttpServletRequest request) {

		String img = request.getParameter("linkImage");
		String[] memberList = request.getParameterValues("linkedMemberIDs");

		if (!CommonHelper.isEmpty(img)) {

			// remove de-selected members
			if (img2MemberMap.containsKey(img)) {

				ArrayList<String> oldMembers = img2MemberMap.get(img);
				if (!CommonHelper.isEmpty(oldMembers)) {
					if (!CommonHelper.isEmpty(memberList)) {
						ArrayList<String> newMembers = CommonHelper
								.asArray(memberList);
						oldMembers.removeAll(newMembers);
					}
					ArrayList<String> tmpOldMembers = new ArrayList<String>();
					tmpOldMembers.addAll(oldMembers);
					for (String memID : tmpOldMembers) {
						removeImageLinkage(img, memID);
					}
				}
			}

			// add/update new members
			if (!CommonHelper.isEmpty(memberList)) {
				for (String memID : memberList) {
					if (!CommonHelper.isEmpty(memID)) {
						// save image to member, update member2Img if required
						boolean updateSuccess = updateMemberImage(memID, img);
						if (updateSuccess) {
							updateImg2Member(memID, img);
						}
					}
				}
			}
		}
	}

	/**
	 * 1. remove img from imgCol (imgCollectionMap) on member <br>
	 * 2. remove member from img2memberMap
	 * 
	 * @param img
	 * @param memID
	 */
	private void removeImageLinkage(String img, String memID) {

		if (memberMasterMap.containsKey(memID)) {

			Member mem = memberMasterMap.get(memID);
			String imgColID = mem.getPics();

			if (!CommonHelper.isEmpty(imgColID)
					&& imgCollectionMap.containsKey(imgColID)) {
				ArrayList<String> linkedImages = imgCollectionMap.get(imgColID);
				linkedImages.remove(img);
			}

			if (img2MemberMap.containsKey(img)) {
				ArrayList<String> linkedmembers = img2MemberMap.get(img);
				linkedmembers.remove(memID);
			}

		}
	}

	private void updateImg2Member(String memID, String img) {
		ArrayList<String> linkedMembers = null;
		if (img2MemberMap.containsKey(img)) {
			linkedMembers = img2MemberMap.get(img);
		} else {
			linkedMembers = new ArrayList<String>();
			img2MemberMap.put(img, linkedMembers);
		}
		if (!linkedMembers.contains(memID)) {
			linkedMembers.add(memID);
		}
	}

	/**
	 * update image on the member check pic col on member, if available, update
	 * it else create a new pic col and add pic to it
	 * 
	 * @param memID
	 * @param img
	 */
	private boolean updateMemberImage(String memID, String img) {

		if (memberMasterMap.containsKey(memID)) {

			Member mem = memberMasterMap.get(memID);

			String imgColID = mem.getPics();
			if (CommonHelper.isEmpty(imgColID)) {
				imgColID = getNewImgCollectionID();
			}

			ArrayList<String> linkedImages = null;
			if (imgCollectionMap.containsKey(imgColID)) {
				linkedImages = imgCollectionMap.get(imgColID);
			} else {
				linkedImages = new ArrayList<String>();
			}

			mem.setPics(imgColID);
			if (!linkedImages.contains(img)) {
				linkedImages.add(img);
			}
			imgCollectionMap.put(imgColID, linkedImages);

			return true;
		}
		return false;
	}
}
