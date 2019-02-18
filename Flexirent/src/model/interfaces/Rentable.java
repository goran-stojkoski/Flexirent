package model.interfaces;

import exceptions.InvalidDateException;
import exceptions.MissingInputException;
import utilities.DateTime;

/**
 * This method enforces the implementation of the following methods required for renting the property
 */
public interface Rentable {
	void rent(String customerID, DateTime rentDate, int numOfRentDay) throws MissingInputException, InvalidDateException;
	void returnProperty(DateTime returnDate);
}
