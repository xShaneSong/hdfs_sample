package com.simple.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hadoop.fs.FileStatus;

import com.simple.model.HdfsDAO;
import com.simple.model.UserDAO;

public class RegisterServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		UserDAO user = new UserDAO();
		try {
			user.insert(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		HdfsDAO hdfs = new HdfsDAO();
		FileStatus[] documentList = hdfs.getDirectoryFromHdfs();
		request.setAttribute("documentList", documentList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}