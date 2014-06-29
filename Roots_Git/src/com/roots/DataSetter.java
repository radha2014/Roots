/*
 * Created on 29-Aug-10
 *
 * com.roots.DataSetter.java
 * 
 */
package com.roots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class DataSetter {

	private Map<String, Member> memberMasterMap;

	private Map<String, ArrayList<String>> collectionMasterMap;

	private Map<String, ArrayList<String>> imgCollectionMap;

	private Map<String, ArrayList<String>> img2MemberMap;

	private String memberlinks;

	public DataSetter(Map<String, Member> memberMasterMap,
			Map<String, ArrayList<String>> collectionMasterMap,
			Map<String, ArrayList<String>> imgCollectionMap,
			Map<String, ArrayList<String>> img2MemberMap) {
		this.memberMasterMap = memberMasterMap;
		this.collectionMasterMap = collectionMasterMap;
		this.imgCollectionMap = imgCollectionMap;
		this.img2MemberMap = img2MemberMap;
	}

	/**
	 * Set common list of members, siblings & kids collections in the request
	 * object along with details of requested MemberID.
	 * 
	 * @param request
	 * @param requestedMemberID
	 */
	public void setData(HttpServletRequest request, String requestedMemberID) {
		ArrayList<String> memberlist = getMemberList();

		request.setAttribute("memberlist", getMemberList());
		request.setAttribute("collectionlist", getCollectionList());
		request.setAttribute("idgenderlist", getIDGenderList());
		request.setAttribute("memberlinks", getLinks(memberlist));

		Member requestedMember = memberMasterMap.get(requestedMemberID);
		if (requestedMember == null) {
			return;
		}

		request.setAttribute("requestedid", requestedMember.getID());
		request.setAttribute("name", requestedMember.getName());
		request.setAttribute("age", requestedMember.getAge());
		request.setAttribute("description", requestedMember.getDescription());

		if (requestedMember.getDateOfBirth() != null) {
			request.setAttribute("day", requestedMember.getDateOfBirth().get(
					Calendar.DAY_OF_MONTH));
			request.setAttribute("month", requestedMember.getDateOfBirth().get(
					Calendar.MONTH));
			request.setAttribute("year", requestedMember.getDateOfBirth().get(
					Calendar.YEAR));
		}
		request.setAttribute("gender", requestedMember.getGender());

		Member f = memberMasterMap.get(requestedMember.getFNameID());
		request.setAttribute("fName", (f != null) ? f.getName() : null);
		request.setAttribute("fNameID", requestedMember.getFNameID());

		Member m = memberMasterMap.get(requestedMember.getMNameID());
		request.setAttribute("mName", (m != null) ? m.getName() : null);
		request.setAttribute("mNameID", requestedMember.getMNameID());

		Member wh = memberMasterMap.get(requestedMember.getWhNameID());
		request.setAttribute("whName", (wh != null) ? wh.getName() : null);
		request.setAttribute("whNameID", requestedMember.getWhNameID());

		request.setAttribute("siblingCollID", requestedMember
				.getSiblingCollectionID());

		request.setAttribute("kidCollID", requestedMember.getKidCollectionID());

		request.setAttribute("linkedPics", imgCollectionMap.get(requestedMember
				.getPics()));
	}

	private String getLinks(ArrayList<String> list) {

		if (CommonHelper.isEmpty(memberlinks)) {
			prepareLinks(list);
		}

		return memberlinks;
	}

	public void clearLinks() {

		memberlinks = null;
	}

	private void prepareLinks(ArrayList<String> list) {

		String listItemID = null;
		String listItemName = null;
		StringBuilder strb = new StringBuilder();

		if (list != null && !list.isEmpty()) {
			for (String listItem : list) {
				if (listItem != null) {
					listItemID = listItem.substring(0, listItem.indexOf("-"));
					listItemName = listItem
							.substring(listItem.indexOf("-") + 1);
					strb.append("<a href=\"javascript:setMemberID('"
							+ listItemID + "')\" >");
					strb
							.append((CommonHelper.isEmpty(listItemName)) ? listItemID
									: listItemName);
					strb.append("</a> | ");
				}
			}
		}
		memberlinks = strb.toString();
	}

	public ArrayList<String> getMemberList() {
		String[] memberArr = new String[memberMasterMap.keySet().size()];
		int i = 0;
		for (String memberID : memberMasterMap.keySet()) {
			memberArr[i++] = memberID + "-"
					+ memberMasterMap.get(memberID).getName();
		}

		Arrays.sort(memberArr, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				String str0 = (String) arg0;
				String str1 = (String) arg1;
				str0 = str0.substring(str0.indexOf("-") + 1);
				str1 = str1.substring(str1.indexOf("-") + 1);
				return str0.compareToIgnoreCase(str1);
			}
		});

		ArrayList<String> memberlist = new ArrayList<String>(Arrays
				.asList(memberArr));
		return memberlist;

	}

	public List<String> getCollectionList() {

		// setting sibling collections <br>
		// sibling_collection_ID - sibling1_ID:sibling1_Name,
		// sibling2_ID:sibling2_Name, sibling3_ID:sibling3_Name...<br>
		List<String> collectionlist = new ArrayList<String>(collectionMasterMap
				.keySet().size());
		for (String sibCollID : collectionMasterMap.keySet()) {
			StringBuffer strb = new StringBuffer(sibCollID);
			strb.append("- ");
			List<String> siblist = collectionMasterMap.get(sibCollID);
			for (String sibID : siblist) {
				strb.append(sibID);
				strb.append(":");
				strb.append(memberMasterMap.get(sibID).getName()).append(",");
			}
			collectionlist.add(strb.substring(0, strb.length() - 1));
		}

		return collectionlist;
	}

	public List<String> getIDGenderList() {
		// sibling collection ID- sibling1_ID:Gender, sibling2_ID:Gender,
		// sibling3_ID:Gender...
		List<String> iDGenderList = new ArrayList<String>(collectionMasterMap
				.keySet().size());
		for (String sibCollID : collectionMasterMap.keySet()) {
			StringBuffer strb = new StringBuffer(sibCollID);
			strb.append("- ");
			List<String> siblist = collectionMasterMap.get(sibCollID);
			for (String sibID : siblist) {
				strb.append(sibID);
				strb.append(":");
				strb.append(memberMasterMap.get(sibID).getGender()).append(",");
			}
			iDGenderList.add(strb.substring(0, strb.length() - 1));
		}

		return iDGenderList;
	}

}
