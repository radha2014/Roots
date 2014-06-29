/*
 * Created on Oct 14, 2010
 *
 * com.tools.XSLCreator.java
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
package com.tools.xslxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sun.nio.ch.SocketOpts.IP;

import com.roots.CommonHelper;
import com.roots.MainDAO;
import com.roots.Member;

public class GenerateRootApplication {

	private static String xslfile = "roots.xsl";
	private static String xmllinksfile = "links.xml";
	private static String inputpath = "C:\\eclipse3.3.2\\";
	private static String outputpath = "C:\\MyDocs\\Documents\\xfiles\\";

	public static void main(String[] args) throws IOException {

		GenerateRootApplication creator = new GenerateRootApplication();

		CommonHelper.log("Trying to load input files from ... " + inputpath);

		Map<String, Member> memberMasterMap = new HashMap<String, Member>();
		Map<String, ArrayList<String>> collectionMasterMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> imgCollectionMap = new HashMap<String, ArrayList<String>>();

		boolean filesLoaded = creator.loadfiles(memberMasterMap,
				collectionMasterMap, imgCollectionMap);

		if (filesLoaded) {
			creator.generateXSL();
			creator.generateXMLFiles(memberMasterMap, collectionMasterMap,
					imgCollectionMap);
		}

		CommonHelper.log("Copy the images to images folder within "
				+ outputpath);
	}

	private MainDAO _mainDAO = new MainDAO();

	private Map<String, String> generateMemberXMLMap(
			Map<String, Member> memberMasterMap) {

		Map<String, String> memberFileMap = new HashMap<String, String>();

		for (Member member : memberMasterMap.values()) {
			String id = member.getID();
			String name = member.getName();

			// xml files names are in the format <id>_<name>.xml
			String xmlfilename = id
					+ (CommonHelper.isEmpty(name) ? "" : "_" + name) + ".xml";

			memberFileMap.put(member.getID(), xmlfilename.replaceAll(" ", "_"));
		}

		return memberFileMap;
	}

	private void generateXMLFiles(Map<String, Member> memberMasterMap,
			Map<String, ArrayList<String>> collectionMasterMap,
			Map<String, ArrayList<String>> imgCollectionMap) throws IOException {

		Map<String, String> memberFileMap = generateMemberXMLMap(memberMasterMap);
		XMLCreator xmlCreator = new XMLCreator(memberFileMap, memberMasterMap,
				outputpath);

		for (Member member : memberMasterMap.values()) {

			String siblingsCol = member.getSiblingCollectionID();
			String kidsCol = member.getKidCollectionID();
			String imgCol = member.getPics();

			xmlCreator.generateXML(member,
					collectionMasterMap.get(siblingsCol), collectionMasterMap
							.get(kidsCol), imgCollectionMap.get(imgCol));
		}

		// xmlCreator.generateLinks(xmllinksfile);

		CommonHelper.log("xml files generated...");
	}

	private void generateXSL() throws IOException {
		XSLCreator xslCreator = new XSLCreator(outputpath + xslfile);
		xslCreator.generateXSL();
		CommonHelper.log("xsl file generated..." + outputpath + xslfile);
	}

	private boolean loadfiles(Map<String, Member> memberMasterMap,
			Map<String, ArrayList<String>> collectionMasterMap,
			Map<String, ArrayList<String>> imgCollectionMap) {

		try {
			_mainDAO.loadMembers(memberMasterMap, inputpath + "members.txt");
			CommonHelper.log("Members loaded successfully...");

			_mainDAO.loadMemCollections(collectionMasterMap, inputpath
					+ "collections.txt");
			CommonHelper.log("Member Collections loaded successfully...");

			_mainDAO.loadImgCollections(imgCollectionMap, inputpath
					+ "images.txt");
			CommonHelper.log("Images loaded successfully...");

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
