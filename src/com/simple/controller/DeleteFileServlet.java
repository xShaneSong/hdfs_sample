package com.simple.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FileStatus;

import com.simple.model.HdfsDAO;

public class DeleteFileServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		String filePath = new String(request.getParameter("filePath").getBytes("ISO-8859-1"), "GB2312");
		HdfsDAO hdfs = new HdfsDAO();
		hdfs.deleteFromHdfs(filePath);
		System.out.println("====" + filePath + "====");
		
		FileStatus[] documentList = hdfs.getDirectoryFromHdfs();
		request.setAttribute("documentList", documentList);
		System.out.println("get List data" + documentList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doGet(request, response);
	}
}
