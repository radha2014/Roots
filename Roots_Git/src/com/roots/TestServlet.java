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

public class TestServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Just call the Post handler
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nextAction = request.getParameter("nextAction");
		
		System.out.println(request.getParameter("membername"));
		request.setAttribute("membername", "rkkkk");

		System.out.println(request.getParameter("R1"));

		System.out.println(request.getParameter("nextAction"));

		String arr[] = request.getParameterValues("kid");
		for (String str : arr) {
			System.out.println(str);
		}

		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/test.jsp");
		dispatcher.forward(request, response);
	}

}
