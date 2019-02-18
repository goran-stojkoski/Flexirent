package utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import exceptions.MissingInputException;
import model.RentalRecord;
import model.constant.ApartmentSettings;
import model.rental_property.Apartment;
import model.rental_property.PremiumSuite;
import model.rental_property.RentalProperty;

/**
 * This class contains a collection of static methods that are general in nature
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class Helper {

	//Checks whether the input for the CustomerID format is valid. Only allows CUS### format i.e. CUS123
	public static boolean validCustID(String custID) {
		if (custID.matches("(?i:^CUS\\d{3}$)"))
			return true;
		else
			return false;
	}

	//Checks whether a valid start date has been entered
	public static boolean validStartDate(DateTime checkInDate) {
		String nowString = DateTime.getCurrentTime();
		int day = Integer.parseInt(nowString.split("-")[2]);
		int month = Integer.parseInt(nowString.split("-")[1]);
		int year = Integer.parseInt(nowString.split("-")[0]);
		
		if (DateTime.diffDays(checkInDate, new DateTime(day, month, year)) >= 0)
			return true;
		else
			return false ;
	}
	
	//More accurate rounding
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	//Returns the current date at midnight to avoid confusion when using the DiffDays() method in DateTime
	public static DateTime getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String dateString = sdf.format(date);
		int day = Integer.parseInt(dateString.split("/")[0]);
		int month = Integer.parseInt(dateString.split("/")[1]);
		int year = Integer.parseInt(dateString.split("/")[2]);
		return new DateTime(day, month, year);
	}
	
	/**
	 * Converts any string that is delimited by "_" to Proper Case. for example: hello_WORLD -> Hello World. This class is mainly used for creating table headers from enum values
	 * @param text - the string that is being converted
	 */
	public static String toProperCase(String text) {
		String[] UpperCaseStringArray = text.toString().toUpperCase().split("_");
		String[] LowerCaseStringArray = text.toString().toLowerCase().split("_");
		int ArrayLength = UpperCaseStringArray.length;
		String[] ProperCaseStringArray = new String[ArrayLength];

		for (int i = 0; i<ArrayLength;i++) {
			ProperCaseStringArray[i] = UpperCaseStringArray[i].charAt(0) + LowerCaseStringArray[i].substring(1);
		}

		return String.join(" ", ProperCaseStringArray);
	}

	/**
	 * Converts any string that is delimited by "_" to Proper Case. for example: hello_WORLD -> helloWorld. This class is mainly used for referencing field names.
	 * @param text - the string that is being converted
	 */
	public static String toCamelCase(String text) {
		String[] UpperCaseStringArray = text.toString().toUpperCase().split("_");
		String[] LowerCaseStringArray = text.toString().toLowerCase().split("_");
		int ArrayLength = UpperCaseStringArray.length;
		String[] camelCaseStringArray = new String[ArrayLength];
		
		camelCaseStringArray[0] = LowerCaseStringArray[0];
		for (int i = 1; i < ArrayLength; i++) {
			camelCaseStringArray[i] = UpperCaseStringArray[i].charAt(0) + LowerCaseStringArray[i].substring(1);
		}
		return String.join("", camelCaseStringArray);
	}
	
	/**
	 * This method converts LocalDate format to DateTime format for use between datePicker and the flexirent format for storing date respectively
	 * @param DatePickerValue - value that is being converted
	 */
	public static DateTime localDateToDateTime(LocalDate DatePickerValue) {
		String[] date = DatePickerValue.toString().split("-");
		return new DateTime(Integer.parseInt(date[2]), Integer.parseInt(date[1]),Integer.parseInt(date[0]));
	}
	
	/**
	 * This method converts DateTime format to LocalDate format for use between flexirent and the datePicker format for storing date respectively
	 * @param DatePickerValue - value that is being converted
	 */
	public static LocalDate dateTimeToLocalDate(DateTime dateTime) {
		return LocalDate.parse(dateTime.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	//converts the String date obtained from the datebate to DateTime format that is used by the flexirent system
	public static DateTime dbStringDateToDateTime(String dbDateString) {
		if(dbDateString.equals("null"))
			return null;
		String[] dateString = dbDateString.split("-");
		int day = Integer.parseInt(dateString[2]);
		int month = Integer.parseInt(dateString[1]);
		int year = Integer.parseInt(dateString[0]);
		return new DateTime(day, month, year);
	}	
	
	//converts DateTime format which is used by the flexirent system into the string type required for storing a date value in the SQL database
	public static String dateTimeToSqlDateString(DateTime date) {
		if (date != null)
			return "TO_DATE('" + date.toString() + "', 'DD/MM/YYYY')";
		else
			return "NULL";
	}
	
	/**
	 * This method converts an ArrayList of Strings that contain rental property
	 * information into an ArrayList of RentalProperties. This method is used for
	 * importing rental properties from file and also obtaining the latest state of
	 * the database within the flexirent system
	 * 
	 * @param propertyStringList - An ArrayList of Strings with all of the fields required to create Rental Property objects
	 * @return
	 */
	public static ArrayList<RentalProperty> propertyStringListToPropetyObjectList(ArrayList<String> propertyStringList){
		ArrayList<RentalProperty> rentalProperties = new ArrayList<RentalProperty>();
		String[] propertyStringArray;
		String dateString;
		DateTime lastMaintenanceDate = null;
		for(String property : propertyStringList) {
			propertyStringArray = property.split(":");
			
			int streetNumber = Integer.parseInt(propertyStringArray[1]);
			String street = propertyStringArray[2];
			String suburb = propertyStringArray[3];
			String propertyType = propertyStringArray[4];
			int bedrooms = Integer.parseInt(propertyStringArray[5]);
			String propertyDescription = propertyStringArray[8];
			String imageFilename = propertyStringArray[10];
			dateString = propertyStringArray[6];
			if (!dateString.equals("null")) {
				String dateStringArray[] = dateString.split("-");
				lastMaintenanceDate = new DateTime(Integer.parseInt(dateStringArray[2]), Integer.parseInt(dateStringArray[1]), Integer.parseInt(dateStringArray[0]));
			}
			String propertyStatus = propertyStringArray[7];
			if (propertyType.equals(ApartmentSettings.PROPERTY_TYPE_LABEL))
			try {
				rentalProperties.add(new Apartment(streetNumber, street, suburb, propertyStatus, bedrooms, propertyDescription, imageFilename));
			} catch (MissingInputException e) {
			}
			else
				try {
					rentalProperties.add(new PremiumSuite(streetNumber, street, suburb, lastMaintenanceDate, propertyStatus,propertyDescription,imageFilename));
				} catch (Exception e) {
				}
		}
		return rentalProperties;
	}
	
	/**
	 * This method converts an ArrayList of String that contains rental record
	 * information into an Arraylist of RentalRecords for a specific property. This
	 * method is used when updating information in the database for a specific
	 * Rental Record, for example: after a property has been returned or had
	 * maintenance complete
	 * 
	 * @param rentalRecordStringListToRentalRecordObjectList - An ArrayList of Strings with all of the information required to create Rental Record Objects
	 */	
	public static ArrayList<RentalRecord> rentalRecordStringListToRentalRecordObjectList(ArrayList<String> rentalRecordsStringList, String propertyID) {
		ArrayList<RentalRecord> rentalRecords = new ArrayList<RentalRecord>();
		String[] recordStringArray;
		for (String rentalRecord : rentalRecordsStringList) {
			if (rentalRecord.contains(propertyID)) {
				recordStringArray = rentalRecord.split(":");
				String recordID = recordStringArray[0];
				DateTime rentDate = Helper.dbStringDateToDateTime(recordStringArray[1]);
				DateTime estimatedReturnDate = Helper.dbStringDateToDateTime(recordStringArray[2]);
				DateTime actualReturnDate = Helper.dbStringDateToDateTime(recordStringArray[3]);
				double rentalFee = Double.parseDouble(recordStringArray[4]);
				double lateFee = Double.parseDouble(recordStringArray[5]);
				rentalRecords.add(new RentalRecord(recordID, rentDate, estimatedReturnDate, actualReturnDate, rentalFee,
						lateFee));
			}
		}
		return rentalRecords;
	}
	
	/**
	 * This method converts an ArrayList of String that contains rental record
	 * information into an Arraylist of RentalRecords. This method is used when
	 * importing rental properties from a file
	 * 
	 * @param rentalRecordStringListToRentalRecordObjectList - An ArrayList of Strings with all of the information required to create Rental Record Objects
	 */	
	public static ArrayList<RentalRecord> rentalRecordStringListToRentalRecordObjectList(ArrayList<String> rentalRecordsStringList) {
		return rentalRecordStringListToRentalRecordObjectList(rentalRecordsStringList,"");
	}
	
}
