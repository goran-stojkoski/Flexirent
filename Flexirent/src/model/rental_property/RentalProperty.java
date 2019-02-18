package model.rental_property;

import java.util.ArrayList;
import java.util.LinkedList;

import exceptions.InvalidDateException;
import exceptions.MissingInputException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.RentalRecord;
import model.constant.ApartmentSettings;
import model.constant.DatabaseSettings;
import model.constant.RentalPropertyFieldsEnum;
import model.constant.RentalPropertySettings;
import model.database.FlexiRentDB;
import model.interfaces.Maintainable;
import model.interfaces.Rentable;
import model.interfaces.Storable;
import utilities.DateTime;
import utilities.Helper;

/**
 * This abstract class describes the rental property object required for the
 * FlexiRent system. It is the superclass for the Apartment and Premium Suite
 * classes, which are the concrete classes the user can rent from
 * 
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 * 
 */

public abstract class RentalProperty implements Rentable, Maintainable, Storable {

	private String propertyID;
	private int streetNumber;
	private String streetName;
	private String suburbName;
	private String streetAddress;
	private int bedrooms;
	private String propertyStatus;
	private String propertyType;
	private String propertyDescription;
	private String imageFilename;
	
	private double dailyRate;
	private DateTime lastMaintenanceDate;

	private LinkedList<String> propertyFields = new LinkedList<String>();
	
	private ObservableList<RentalRecord> rentalRecords = FXCollections.observableArrayList();
	private ArrayList<String> rentalRecordsStringList;
	
	/**
	 * @param streetNumber
	 * @param streetName
	 * @param suburbName
	 * @param bedrooms
	 * @param dailyRate           - cost to rent the property per day
	 * @param propertyStatus      - property status can be Available, Rented or
	 *                            Under Maintenance
	 * @param propertyType        - premium suite or apartment
	 * @param propertyDescription - a description about the property with a minimum
	 *                            of 80 characters and a max of 100 characters
	 * @param imageFilename       - each property has the option of displaying an
	 *                            image. The image filename is a reference to the
	 *                            file stored in the images folder
	 * @throws MissingInputException - constructor throws exceptions when the user
	 *                               has not specified the required form fields
	 */
	public RentalProperty(int streetNumber, String streetName, String suburbName,int bedrooms, double dailyRate, String propertyStatus, String propertyType, String propertyDescription, String imageFilename) throws MissingInputException {
		if(streetNumber == 0) throw new MissingInputException(RentalPropertyFieldsEnum.STREET_NUMBER.getTableHeader());
		if(streetName.equals(null) || streetName.equals("") ) throw new MissingInputException(RentalPropertyFieldsEnum.STREET_NAME.getTableHeader());
		if(suburbName == null) throw new MissingInputException(RentalPropertyFieldsEnum.SUBURB_NAME.getTableHeader());
		if(bedrooms == 0) throw new MissingInputException(RentalPropertyFieldsEnum.BEDROOMS.getTableHeader());
		
		this.streetNumber = streetNumber;
		this.streetName = streetName;
		this.suburbName = suburbName;
		this.bedrooms = bedrooms;
		this.dailyRate = dailyRate;
		this.propertyStatus = propertyStatus;
		this.propertyType = propertyType;
		this.propertyDescription = propertyDescription;
		this.imageFilename = imageFilename;
		
		streetAddress = streetNumber + " " + streetName + " " + suburbName;
		propertyID = generatePropertyID();
		
		//This arraylist is required for the toString method which is used for database, importing and exporting functions
		propertyFields.add(propertyID);
		propertyFields.add(Integer.toString(streetNumber));
		propertyFields.add(streetName);
		propertyFields.add(suburbName);
		propertyFields.add(Integer.toString(bedrooms));
		propertyFields.add(Double.toString(dailyRate));
		propertyFields.add(propertyStatus);
		propertyFields.add(propertyType);
		propertyFields.add(propertyDescription);
		propertyFields.add(imageFilename);
		
		updateRentalRecords();
	}
	
	/**
	 * This method rents the rental property. It will throw exceptions if the conditions specified in the method are met
	 */
	public void rent(String customerID, DateTime rentDate, int numOfRentDay) throws MissingInputException, InvalidDateException {
		if(customerID.equals(null)) throw new MissingInputException("Customer ID");
		if(numOfRentDay == 0) throw new MissingInputException("To Date");
		if(rentDate == null) throw new MissingInputException("From Date");
		if(numOfRentDay<1) throw new InvalidDateException();
		new RentalRecord(propertyID, customerID, rentDate, numOfRentDay).addToDatabase();
		setPropertyStatus(RentalPropertySettings.STATUS_RENTED);
	}

	public boolean performMaintenance() {
		setPropertyStatus(RentalPropertySettings.STATUS_UNDER_MAINTENANCE);
		return true;
	}
	
	
	public boolean completeMaintenance(DateTime completionDate) {
		setPropertyStatus(RentalPropertySettings.STATUS_AVAILABLE);
		return false;
	}
	
	/**
	 * This method updates the rental records of a property. This is done by taking
	 * the latest list of records for this property from the database and importing
	 * them into the Rental Property object
	 */
	public void updateRentalRecords() {
		ObservableList<RentalRecord> newRentalRecords = FXCollections.observableArrayList();
		String[] recordStringArray;
		rentalRecordsStringList = FlexiRentDB.toStringArray(DatabaseSettings.RENTAL_RECORDS_TABLE_NAME);
		for (String rentalRecord : rentalRecordsStringList) {
			if (rentalRecord.contains(propertyID)) {
				recordStringArray = rentalRecord.split(":");
				String recordID = recordStringArray[0];
				DateTime rentDate = Helper.dbStringDateToDateTime(recordStringArray[1]);
				DateTime estimatedReturnDate = Helper.dbStringDateToDateTime(recordStringArray[2]);
				DateTime actualReturnDate = Helper.dbStringDateToDateTime(recordStringArray[3]);
				double rentalFee = Double.parseDouble(recordStringArray[4]);
				double lateFee = Double.parseDouble(recordStringArray[5]);
				newRentalRecords.add(new RentalRecord(recordID, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee));
			}
		}
		rentalRecords.removeAll(rentalRecords);
		rentalRecords.addAll(newRentalRecords);
	}
	
	//Secondary Constructor to be used when lastMaintenanceDate has been specified. Requirement for Premium Suite but can also be used for Apartment
	public RentalProperty(int streetNumber, String streetName, String suburbName,int bedrooms, double dailyRate, DateTime lastMaintenanceDate , String propertyStatus, String propertyType, String propertyDescription, String imageFilename) throws MissingInputException {
		this(streetNumber, streetName, suburbName,bedrooms, dailyRate, propertyStatus, propertyType, propertyDescription, imageFilename);
		this.lastMaintenanceDate = lastMaintenanceDate;
	}	
	
	/**
	 * This method unique propertyIDs. It uses A_ and S_ suffixes to distinguish between Apartments and Premium Suites respectively
	 */
	private String generatePropertyID() {
		String UniqueString = streetAddress.replaceAll(" ", "");
		if (propertyType == ApartmentSettings.PROPERTY_TYPE_LABEL)
			return "A_" + UniqueString;
		else
			return "S_" + UniqueString;
	}
	
	/**
	 * This method generates the SQL query to add a property to the database
	 */
	public boolean addToDatabase() {
			String sqlDateString = "NULL";	//Initial condition in case a property is an apartment and a lastMaintencanceDate value has not been specified
			if (lastMaintenanceDate != null)
				sqlDateString = "TO_DATE('" +		lastMaintenanceDate.toString() + 	"', 'DD/MM/YYYY')"; //sets the new value to provide the database with a valid date entry
				String query = "INSERT INTO " + DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME + " VALUES ("
						+ "'" + propertyID + 			"', "
						+ 		streetNumber + 			", "
						+ "'" + streetName + 				"', "
						+ "'" + suburbName + 				"', "
						+ "'" + propertyType + 			"', "
						+ 		bedrooms + 				", " 
						+ 		sqlDateString +			", "
						
						+ "'" +	propertyStatus +		"', "
						+ "'" +	propertyDescription + 	"', "
						+		dailyRate +				", " 
						+ "'" +	imageFilename +			"'"
						+ ")";
				return FlexiRentDB.addTuple(query);
	}

	public void setPropertyStatus(String status) {
		propertyStatus = status;
		FlexiRentDB.updateTuple(DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME, RentalPropertyFieldsEnum.PROPERTY_ID.toString(), propertyID, RentalPropertyFieldsEnum.PROPERTY_STATUS.toString(), propertyStatus);
		updateRentalRecords();
	}
	
	public RentalRecord getRentalRecord(int index) {
		return rentalRecords.get(index-1);
	}

	public void setLastMaintenanceDate(DateTime lastMaintenanceDate) {
		this.lastMaintenanceDate = lastMaintenanceDate;
	}
	
	public String toString() {
		return String.join(":", propertyFields);
	}
	
	public String getPropertyID() {
		return propertyID;
	}
	public int getStreetNumber() {
		return streetNumber;
	}
	public String getStreetName() {
		return streetName;
	}
	public String getSuburbName() {
		return suburbName;
	}
	public int getBedrooms() {
		return bedrooms;
	}
	public String getPropertyStatus() {
		return propertyStatus;
	}	
	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}
	
	public ObservableList<RentalRecord> getRentalRecords() {
		return rentalRecords;
	}
		
	public String getPropertyType() {
		return propertyType;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public double getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}

	public String getPropertyDescription() {
		return propertyDescription;
	}

	public String getImageFilename() {
		return imageFilename;
	}
	
}
