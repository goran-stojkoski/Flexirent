package model;

import model.constant.DatabaseSettings;
import model.constant.RentalRecordFieldsEnum;
import model.database.FlexiRentDB;
import utilities.DateTime;
import utilities.Helper;

public class RentalRecord {
	private String recordID;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private DateTime rentDate;
	private double rentalFee;
	private double lateFee;

	public RentalRecord(String recordID, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate, double rentalFee, double lateFee) {
		this.recordID = recordID;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
	}

	public RentalRecord(String propertyID, String customerID, DateTime rentDate, int rentedDays) {
		this.rentDate = rentDate;
		estimatedReturnDate = new DateTime(rentDate, rentedDays);
		recordID = propertyID + "_" + customerID + "_" + rentDate.getEightDigitDate();
	}
	
	
	public boolean addToDatabase() {
		String sqlRentDateString = Helper.dateTimeToSqlDateString(rentDate);
		String sqlEstimatedReturnDateString = Helper.dateTimeToSqlDateString(estimatedReturnDate);
		String sqlActualReturnDateString = Helper.dateTimeToSqlDateString(actualReturnDate);
			
		String query = "INSERT INTO " + DatabaseSettings.RENTAL_RECORDS_TABLE_NAME + " VALUES ("
				+ "'" + recordID +						"', "
				+ 		sqlRentDateString + 			", "
				+   	sqlEstimatedReturnDateString + 	", "
				+   	sqlActualReturnDateString + 	", "
				+		rentalFee +						", " 
				+ 		lateFee +						""
				+ ")";
			
			return FlexiRentDB.addTuple(query);
	}


	public void setActualReturnDate(DateTime date) {
		actualReturnDate = date;
	}
	
	public void setRentalFee(double rentalFee) {
		this.rentalFee =  rentalFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	
	public void updateSqlRecord() {
		String sqlUpdateString = 
				RentalRecordFieldsEnum.ACTUAL_RETURN_DATE.toString() + " = " +  Helper.dateTimeToSqlDateString(actualReturnDate) + ", " +
				RentalRecordFieldsEnum.RENTAL_FEE  + " = " + rentalFee + ", " +
				RentalRecordFieldsEnum.LATE_FEE + " = " + lateFee;
		FlexiRentDB.updateTuple(DatabaseSettings.RENTAL_RECORDS_TABLE_NAME, RentalRecordFieldsEnum.RECORD_ID.toString(), recordID, sqlUpdateString);
	}
	
	public String getRecordID() {
		return recordID;
	}
	
	public DateTime getRentDate() {
		return rentDate;
	}
	
	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}
	
	
	
}
