package in.co.raystech.maven.project4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import in.co.raystech.maven.project4.exception.ImageSaveException;
import in.co.raystech.maven.project4.util.S3Handler;

/**
 * Welcome functionality Controller. Performs operation for Show Welcome page
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */
@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
@MultipartConfig
public class WelcomeCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(WelcomeCtl.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("WelcomeCtl Method doGet Started");
		// Create path components to save the file
		final Part filePart = request.getPart("file");

		try {
			S3Handler.uploadProductImage(filePart.getInputStream(), null);

		} catch (ImageSaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;

	}

	@Override
	protected String getView() {
		return ORSView.WELCOME_VIEW;
	}

}