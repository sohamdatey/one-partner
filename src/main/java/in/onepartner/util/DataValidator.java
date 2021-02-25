package in.onepartner.util;

import java.util.Date;

/**
 * This class validates input data
 * 
 * @author FrontController
 * @version 1.0
 * @Copyright (c) SunilOS
 */

public class DataValidator {

	/**
	 * Checks if value is Null
	 * 
	 * this is unicode in case of reference ^\\pL+[\\pL\\]pZ\\pP]{0,}$ add \\ in
	 * case regex do not work
	 * 
	 * @param val
	 * @return
	 */

	public static boolean isAlpha(String name) {

		return name.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$");

	}

	public static boolean checkPasswordLength(String name) {

		if (name.length() >= 6 && name.length() <= 10) {
			return true;

		} else
			return false;

	}

	/**
	 * Checks if value is AlphaNmeric
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isAlphaNmericWithNoSpace(String val) {
		try {
			int number = Integer.parseInt(val);
			if (number > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			String str = "([_A-Za-z0-9 ]+)";
			if (isNotNull(val)) {
				try {
					return val.matches(str);
				} catch (Exception e1) {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public static boolean isCorrectPhoneNumber(String number) {

		return number.matches("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$");

	}

	public static boolean isNull(String[] strings) {
		if (strings == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNull(String val) {
		if (val == null || val.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if value is NOT Null
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotNull(String val) {
		return !isNull(val);
	}

	/**
	 * Checks if value is an Integer
	 * 
	 * @param val
	 * @return
	 */

	public static boolean isInteger(String val) {

		if (isNotNull(val)) {
			try {
				int i = Integer.parseInt(val);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * Checks if marks are greater than 100
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isGreaterMarks(String val) {
		if (isNotNull(val)) {

			int marks = DataUtility.getInt(val);

			if (marks > 100) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if marks are smaller than 0
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isSmallerMarks(String val) {
		if (isNotNull(DataUtility.getString(val))) {
			int marks = DataUtility.getInt(val);

			if (marks < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if value is Long
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isLong(String val) {
		if (isNotNull(val)) {
			try {
				long i = Long.parseLong(val);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * Checks if value is valid Email ID
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmail(String pattern) {

		String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (isNotNull(pattern)) {
			try {
				return pattern.matches(emailreg);
			} catch (NumberFormatException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	/**
	 * Checks if value is Date
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isDate(String val) {

		Date d = null;
		if (isNotNull(val)) {
			d = DataUtility.getDate(val);
		}
		return d != null;
	}

	public static boolean checkAge(Date d) {

		if (d != null) {

			Date enteredDate = d;
			Date now = DataUtility.getSQLDate(new Date());

			long timeBetween = now.getTime() - enteredDate.getTime();
			double yearsBetween = timeBetween / 3.156e+10;
			int age = (int) Math.floor(yearsBetween);

			if (age <= 16) {

				return false;
			}
		} else {

			return true;

		}
		return true;
	}

	/**
	 * Test above methods
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
