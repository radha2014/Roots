/*
 * Created on Aug 18, 2010
 *
 * com.roots.AddMember.java
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
	private static MainModule _mainModule = null;
	static {
		_mainModule = new MainModule();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * "nextPage" and "nextAction" decide the next view and action (non-Javadoc)
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pageRequested = request.getParameter("nextPage");
		String nextAction = request.getParameter("nextAction");
		System.out.println("doPost() page = " + pageRequested + ", acion = "
				+ nextAction);

		String nextPage = null;

		if ("load_application".equals(pageRequested)) {

			nextPage = "/index.jsp";

			if ("Save".equals(nextAction)) {
				// capture all the data sent from UI
				_mainModule.captureData(request);
			}
			if ("Save_Session".equals(nextAction)) {
				// write all the data to file
				_mainModule.write2File();
			}
			if ("OpenMem".equals(nextAction)) {
				// opens the details of requested member
			}
			if ("Reload_Session".equals(nextAction)) {
				// discard and reload all data from files
				_mainModule.reload(request);
			}

			_mainModule.setData(request);
		}

		if ("Link_Images".equals(pageRequested)) {

			nextPage = "/linkimages.jsp";

			if ("reload_images".equals(nextAction)) {

				_mainModule.getImgList(request);

			} 
			if ("saveLinkage".equals(nextAction)) {

				_mainModule.saveImgList(request);
				_mainModule.getImgList(request);

			}
			if ("Save_Session".equals(nextAction)) {
				// write all the data to file
				_mainModule.write2File();
			}

			_mainModule.setImgList(request);
		}

		if ("Load_Viewer".equals(pageRequested)) {

			nextPage = "/viewer.jsp";
			_mainModule.setData(request);
		}

		System.out.println("doPost() requesting page..." + nextPage);

		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}
}
