package com.tools.xslxml;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import com.roots.CommonHelper;
import com.roots.Member;

public class XMLCreator {

	private Map<String, String> memberFileMap = null;
	private Map<String, Member> memberMasterMap = null;
	private static DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
	private String outfilePath = null;

	public XMLCreator(Map<String, String> memberFileMap,
			Map<String, Member> memberMasterMap, String outfilePath) {
		this.memberFileMap = memberFileMap;
		this.memberMasterMap = memberMasterMap;
		this.outfilePath = outfilePath;
	}

	public void generateXML(Member member, ArrayList<String> siblings,
			ArrayList<String> kids, ArrayList<String> pics) throws IOException {

		FileOperation fo = new FileOperation(outfilePath
				+ memberFileMap.get(member.getID()));
		String fNameID = member.getFNameID();
		String mNameID = member.getMNameID();
		String whNameID = member.getWhNameID();

		fo.writeToFile("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

		fo
				.writeToFile("<?xml-stylesheet type=\"text/xsl\" href=\"roots.xsl\"?>");

		fo.writeToFile("<member>");
		fo.writeToFile("	<id>" + member.getID() + "</id>");
		fo.writeToFile("	<name>" + member.getName() + "</name>");
		fo.writeToFile("	<age>" + member.getAge() + "</age>");
		fo.writeToFile("	<dateofbirth>" + checkDate(member.getDateOfBirth())
				+ "</dateofbirth>");
		fo.writeToFile("	<gender>" + member.getGender() + "</gender>");
		fo.writeToFile("	<fathername " + checkURL(fNameID) + ">"
				+ checkInMap(fNameID) + "</fathername>");
		fo.writeToFile("	<mothername " + checkURL(mNameID) + ">"
				+ checkInMap(mNameID) + "</mothername>");
		fo.writeToFile("	<betterhalfname " + checkURL(whNameID) + ">"
				+ checkInMap(whNameID) + "</betterhalfname>");
		fo.writeToFile("	");

		int idx = 1;
		if (!CommonHelper.isEmpty(siblings)) {
			for (String siblingID : siblings) {

				Member sibling = memberMasterMap.get(siblingID);

				fo.writeToFile("	<sibling>");
				fo.writeToFile("		<sno>" + (idx++) + "</sno>");
				fo.writeToFile("		<name " + checkURL(siblingID) + ">"
						+ sibling.getName() + "</name>");
				fo.writeToFile("		<tag>"
						+ ("Male".equals(sibling.getGender()) ? "brother"
								: "sister") + "</tag>");
				fo.writeToFile("	</sibling>");
			}
		}

		idx = 1;
		if (!CommonHelper.isEmpty(kids)) {
			for (String kidID : kids) {

				Member kid = memberMasterMap.get(kidID);

				fo.writeToFile("	<kid>");
				fo.writeToFile("		<sno>" + (idx++) + "</sno>");
				fo.writeToFile("		<name " + checkURL(kidID) + ">"
						+ kid.getName() + "</name>");
				fo.writeToFile("		<tag>"
						+ ("Male".equals(kid.getGender()) ? "son" : "daugther")
						+ "</tag>");
				fo.writeToFile("	</kid>");
			}
		}

		if (!CommonHelper.isEmpty(pics)) {
			for (String pic : pics) {
				fo.writeToFile("	<picture>");
				fo.writeToFile("	    <src>images\\" + pic + "</src>");
				fo.writeToFile("	    <url>images\\" + pic + "</url>");
				fo.writeToFile("	    <alt>" + pic + "</alt>");
				fo.writeToFile("	</picture>");
			}
		}

		fo.writeToFile("</member>");

		fo.close();
	}

	private String checkURL(String nameID) {
		if (CommonHelper.isEmpty(nameID)) {
			return "";
		}
		return "url=\"" + memberFileMap.get(nameID) + "\"";
	}

	private String checkDate(Calendar dateOfBirth) {
		if (dateOfBirth != null) {
			return "" + df.format(dateOfBirth.getTime());
		}
		return "";
	}

	private String checkInMap(String nameID) {
		if (CommonHelper.isEmpty(nameID)) {
			return "";
		}
		return checkValue(memberMasterMap.get(nameID).getName());
	}

	private String checkValue(String value) {
		return (CommonHelper.isEmpty(value)) ? "" : value;
	}

	public void generateLinks(String filename) throws IOException {

		FileOperation fo = new FileOperation(outfilePath + filename);

		fo.writeToFile("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");

		for (String memberID : memberMasterMap.keySet()) {
			fo.writeToFile("	<lmember>");
			fo.writeToFile("		<name " + checkURL(memberID) + ">"
					+ checkInMap(memberID) + "</name>");
			fo.writeToFile("	</lmember>");
		}

		fo.close();

	}
}
