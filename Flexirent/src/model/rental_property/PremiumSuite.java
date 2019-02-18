package model.rental_property;

import exceptions.MissingInputException;
import model.RentalRecord;
import model.constant.PremiumSuiteSettings;
import model.constant.RentalPropertySettings;
import utilities.DateTime;
import utilities.Helper;
/**
 * This class implements the specific methods requied for Premium Suites
 * @author Goran Stojkoski
 * @date 20 Oct 2018
 * @version v1.0
 */

public class PremiumSuite extends RentalProperty {
	
	public PremiumSuite(int streetNumber, String streetName, String suburbName, DateTime lastMaintenanceDate , String propertyStatus, String propertyDescription, String imageFilename) throws MissingInputException {
		super(streetNumber, streetName, suburbName, PremiumSuiteSettings.DEFAULT_BEDROOMS, PremiumSuiteSettings.RENTAL_RATE, lastMaintenanceDate , propertyStatus, PremiumSuiteSettings.PREMIUM_SUITE_LABEL, propertyDescription, imageFilename);
	}

	@Override
	public void returnProperty(DateTime returnDate) {
		// Set property status to available
		super.setPropertyStatus(RentalPropertySettings.STATUS_AVAILABLE);

		// update rental record
		RentalRecord rentalRecord = null;
		for (RentalRecord rr : super.getRentalRecords())
			if(rr.getActualReturnDate() == null)
				rentalRecord = rr;

		// update actual return date
		rentalRecord.setActualReturnDate(returnDate);

		DateTime startDate = rentalRecord.getRentDate();
		DateTime estimatedReturnDate = rentalRecord.getEstimatedReturnDate();

		// update rental fee and late fee
		int lateDays = DateTime.diffDays(returnDate, estimatedReturnDate);
		if (lateDays > 0) {
			rentalRecord.setRentalFee(Helper.round(DateTime.diffDays(estimatedReturnDate, startDate) * PremiumSuiteSettings.RENTAL_RATE, 2));
			rentalRecord.setLateFee(Helper.round(lateDays * PremiumSuiteSettings.LATE_FEE_RATE, 2));
		} else {
			rentalRecord.setRentalFee(Helper.round(DateTime.diffDays(returnDate, startDate) * PremiumSuiteSettings.RENTAL_RATE, 2));
			rentalRecord.setLateFee(0);
		}
	}

	@Override
	public boolean completeMaintenance(DateTime completionDate) {
		super.completeMaintenance(completionDate);
		return false;
	}

	public DateTime getMinCheckInDate() {
		DateTime lastMaintenanceDate = super.getLastMaintenanceDate();
		DateTime currentDate = Helper.getCurrentDate();
		if (DateTime.diffDays(lastMaintenanceDate, currentDate) > 0)
			return lastMaintenanceDate;
		else
			return currentDate;
	}

	public DateTime getMaxCheckInDate() {
		return new DateTime(super.getLastMaintenanceDate(), PremiumSuiteSettings.MAINTENANCE_INTERVAL_DAYS - 1);
	}
}
