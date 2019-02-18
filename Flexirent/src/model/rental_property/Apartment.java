package model.rental_property;

import exceptions.MissingInputException;
import model.RentalRecord;
import model.constant.ApartmentEnum;
import model.constant.ApartmentSettings;
import model.constant.RentalPropertySettings;
import utilities.DateTime;
import utilities.Helper;

/**
 * This class implements the specific methods requied for Apartments
 * @author Goran Stojkoski
 * @date 20 Oct 2018
 * @version v1.0
 */
public class Apartment extends RentalProperty {

	public Apartment(int streetNumber, String streetName, String suburbName, String propertyStatus, int bedrooms,  String propertyDescription, String imageFilename) throws MissingInputException {
		super(streetNumber, streetName, suburbName, bedrooms, ApartmentEnum.findByKey(bedrooms).getDailyRate(), propertyStatus, ApartmentSettings.PROPERTY_TYPE_LABEL, propertyDescription, imageFilename);
	}
	

	@Override
	public void returnProperty(DateTime returnDate) {
		double dailyRate = super.getDailyRate();
		// Set property status to available
		super.setPropertyStatus(RentalPropertySettings.STATUS_AVAILABLE);

		// update rental record
		RentalRecord rentalRecord = null;
		for (RentalRecord rr : super.getRentalRecords())
			if(rr.getActualReturnDate() == null)
				rentalRecord = rr;

		//// update actual return date
		rentalRecord.setActualReturnDate(returnDate);

		//// update rental fee and late fee values
		DateTime startDate = rentalRecord.getRentDate();
		DateTime estimatedReturnDate = rentalRecord.getEstimatedReturnDate();
		int lateDays = DateTime.diffDays(returnDate, estimatedReturnDate);
		if (lateDays > 0) {
			rentalRecord.setRentalFee(Helper.round(DateTime.diffDays(estimatedReturnDate, startDate) * dailyRate, 2));
			rentalRecord.setLateFee(Helper.round(lateDays * dailyRate * ApartmentSettings.LATE_FEE_MULTIIPLIER, 2));
		} else {
			rentalRecord.setRentalFee(Helper.round(DateTime.diffDays(returnDate, startDate) * dailyRate, 2));
			rentalRecord.setLateFee(0);
		}
		rentalRecord.updateSqlRecord();
	}
}
