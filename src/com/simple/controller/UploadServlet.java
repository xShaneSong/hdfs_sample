package com.simple.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.hadoop.fs.FileStatus;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.simple.model.HdfsDAO;

@MultipartConfig(location="/tmp", maxFileSize=1048576)
public class UploadServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		File file;
		int maxFileSize = 50 * 1024 * 1024;
		int maxMemSize = 50 * 1024 * 1024;

//		Part part = request.getPart("file");
//        String name = this.getFileName(part);
//        part.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/" + name);
        
		ServletContext context = getServletContext();
		String filePath = context.getInitParameter("file-upload");
		System.out.println("source file path:" + filePath + "");
		
		String contentType = request.getContentType();
		if ((contentType.indexOf("multipart/form-data") >= 0))
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			factory.setRepository(new File("/root/workspace/hadoop/sample/clouddisk/upload"));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			
			try {
				List fileItems = upload.parseRequest(request);
				Iterator i = fileItems.iterator();
				System.out.println("begin to upload file to tomcat server</p>");
				while (i.hasNext())
				{
					FileItem fi = (FileItem)i.next();
					if (!fi.isFormField())
					{
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						String fn = fileName.substring(fileName.lastIndexOf("/") + 1);
						System.out.println("<br>" + fn + "<br>");
						boolean isInMemory = fi.isInMemory();
						long sizeInBytes = fi.getSize();
						if (fileName.lastIndexOf("/") >= 0)
						{
							file = new File(filePath, fileName.substring(fileName.lastIndexOf("/")));
						} else {
							file = new File(filePath, fileName.substring(fileName.lastIndexOf("/") + 1));
						}
						fi.write(file);
						System.out.println("upload file to tomcat server success!");
						System.out.println("begin to upload file to hadoop hdfs</p>");
						String name = filePath + "/" + fileName;
						HdfsDAO hdfs = new HdfsDAO();
						hdfs.copyFile(name);
						System.out.println("upload file to hadoop hdfs success!");
						FileStatus[] documentList = hdfs.getDirectoryFromHdfs();
						request.setAttribute("documentList", documentList);
						System.out.println("get list data" + documentList);
						request.getRequestDispatcher("index.jsp").forward(request, response);
					}
				}
			} catch (Exception ex)
			{
				System.out.println(ex);
			}
		} else {
			System.out.println("<p>No file uploaded</p>");
		}
		
	}
	
	private String getFileName(Part part) {
        String name = null;
        for (String dispotion : part.getHeader("Content-Disposition").split(";")) {
            if (dispotion.trim().startsWith("filename")) {
                name = dispotion.substring(dispotion.indexOf("=") + 1).replace("\"", "").trim();
                name = name.substring(name.lastIndexOf("\\") + 1);
                break;
            }
        }
        return name;
    }
}
