package in.co.raystech.maven.project4.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Part;

import in.co.raystech.maven.project4.exception.ImageSaveException;

/**
 * Data Utility class to format data from one format to another
 * 
 * @author Front Controller
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class DataUtility {

	/**
	 * Application Date Format
	 */
	public static final String APP_DATE_FORMAT = "MM/dd/yyyy";

	public static final String APP_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";

	/**
	 * Date formatter
	 */
	private static final SimpleDateFormat formatter = new SimpleDateFormat(APP_DATE_FORMAT);

	private static final SimpleDateFormat timeFormatter = new SimpleDateFormat(APP_TIME_FORMAT);

	/**
	 * Trims trailing and leading spaces of a String
	 * 
	 * @param val
	 * @return
	 */
	public static String getString(String val) {
		if (DataValidator.isNotNull(val)) {
			return val.trim();
		} else {
			return val;
		}
	}

	/**
	 * Converts and Object to String
	 * 
	 * @param val
	 * @return
	 */
	public static String getStringData(Object val) {
		if (val != null) {
			return val.toString();
		} else {
			return "";
		}
	}

	/**
	 * Converts String into Integer
	 * 
	 * @param val
	 * @return
	 */
	public static int getInt(String val) {
		if (DataValidator.isInteger(val)) {
			return Integer.parseInt(val);
		} else {
			return 0;
		}
	}

	/**
	 * Converts String into Long
	 * 
	 * @param val
	 * @return
	 */
	public static long getLong(String val) {
		if (DataValidator.isLong(val)) {
			return Long.parseLong(val);
		} else {
			return 0;
		}
	}

	/**
	 * Converts String into Date
	 * 
	 * @param val
	 * @return
	 */
	public static Date getDate(String val) {
		Date date = null;
		try {
			date = formatter.parse(val);
		} catch (Exception e) {

		}
		return date;
	}

	/**
	 * Converts Date into sql date
	 * 
	 * @param val
	 * @return
	 */
	public static java.sql.Date getSQLDate(Date val) {

		java.sql.Date d = null;

		try {
			d = new java.sql.Date(val.getYear(), val.getMonth(), val.getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * Converts String into Date
	 * 
	 * @param val
	 * @return
	 */
	public static String getDateString(Date date) {

		try {
			return formatter.format(date);
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * Gets date 
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date) {
		return null;
	}

	/**
	 * Converts String into Time
	 * 
	 * @param val
	 * @return
	 */
	public static Timestamp getTimestamp(String val) {

		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp((timeFormatter.parse(val)).getTime());
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	public static Timestamp getTimestamp(long l) {

		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(l);
		} catch (Exception e) {
			return null;
		}
		return timeStamp;
	}

	public static Timestamp getCurrentTimestamp() {
		Timestamp timeStamp = null;
		try {
			timeStamp = new Timestamp(new Date().getTime());
		} catch (Exception e) {
		}
		return timeStamp;

	}

	public static long getTimestamp(Timestamp tm) {
		try {
			return tm.getTime();
		} catch (Exception e) {
			return 0;
		}
	}

	public static void main(String[] args) {
		System.out.println((getTimestamp("12/28/2016 23:11:35")));

	}
	
	
	public static String saveImage(String location,Part filePart,String fileName ) throws ImageSaveException{
		
		OutputStream out = null;
		InputStream filecontent = null;

		try {
			out = new FileOutputStream(new File(location + File.separator + fileName));
			filecontent = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = filecontent.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			
		} catch (FileNotFoundException fne) {
			throw new ImageSaveException("You either did not specify a file to upload or are "
					+ "trying to upload a file to a protected or nonexistent " + "location."+ "<br/> ERROR: " + fne.getMessage()) ;
		} catch (IOException e) {
			throw new ImageSaveException(e) ;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new ImageSaveException(e) ;
				}
			}
			if (filecontent != null) {
				try {
					filecontent.close();
				} catch (IOException e) {
					throw new ImageSaveException(e) ;
				}
			}
		}
		return "New file " + fileName + " created at " + location;
		
		
	}

}
