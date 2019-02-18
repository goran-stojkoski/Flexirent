package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import exceptions.InvalidDateException;
import exceptions.MissingInputException;
import exceptions.NoPropertySelectedException;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.RentalRecord;
import model.constant.PremiumSuiteSettings;
import model.constant.RentalRecordFieldsEnum;
import model.rental_property.Apartment;
import model.rental_property.RentalProperty;
import utilities.DateTime;
import utilities.Helper;
import view.flexirent.AlertBox;
import view.flexirent.DatesPickerWindow;
import view.flexirent.SingleDatePicker;
import view.flexirent.manage_property.ManagePropertyView;

/**
 * This class manages all of the user interactions with the Property Management
 * window. The purpose of the Property Management view is to provide the user
 * with a dedicated window to carry out specific property related tasks such as
 * rent property, perform maintenance etc.
 * @author Goran Stojkoski
 * @v1.0
 * @date 20 Oct 2018
 */
public class PropertyManagementController {

	private ManagePropertyView view;
	private RentalProperty rentalProperty;
	private ArrayList<TableColumn<RentalRecord, String>> tableHeaderArray;
	private TableView<RentalRecord> rentalRecordsTable;
	private ObservableList<RentalRecord> rentalRecords;

	/**
	 * @param rentalProperty - The class holds a reference to the Rental Property being managed by the use
	 * @throws NoPropertySelectedException - an exception that's thrown when the class is called but no property has been selected
	 */
	public PropertyManagementController(RentalProperty rentalProperty) throws NoPropertySelectedException {
		view = new ManagePropertyView(rentalProperty);
		this.rentalProperty = rentalProperty;
		tableHeaderArray = new ArrayList<TableColumn<RentalRecord, String>>();
		rentalRecordsTable = view.getRentalRecordsTable();
		rentalRecords = rentalProperty.getRentalRecords();
		buildTable();
		setButtonActions();
	}
	
	/**
	 * This method directs the view's buttons to the appropriate action methods
	 */
	public void setButtonActions() {
		view.getButtonBar().getRentPropertyButton().setOnAction(e -> rentProperty());
		view.getButtonBar().getReturnPropertyButton().setOnAction(e -> returnProperty());
		view.getButtonBar().getPerformMaintenanceButton().setOnAction(e -> performMaintenance());
		view.getButtonBar().getCompleteMaintenanceButton().setOnAction(e -> completeMaintenance());
	}

	/**
	 * Displays a window for the user to select from and to dates. If the user
	 * deliberately closes the window without choosing any dates the boolean
	 * deliberateExit value is set to true and the method is aborted
	 */
	public void rentProperty() {
		DatesPickerWindow datesPickerWindow;
		if(rentalProperty instanceof Apartment)
			datesPickerWindow = new DatesPickerWindow(); // this call is for apartments that do not have a strict maintenance schedule
		else
			datesPickerWindow = new DatesPickerWindow(								// this call is for premium suites
							new DateTime(rentalProperty.getLastMaintenanceDate(),	// it sets a maxDate limit according
							PremiumSuiteSettings.MAINTENANCE_INTERVAL_DAYS));		// to the property's last maintenance date
		datesPickerWindow.display();
		if(datesPickerWindow.isDeliberateExit())
			return;
		LocalDate fromDate;
		LocalDate toDate;
		int diffDays = 0;
		String custID;
		fromDate = datesPickerWindow.getFromDate().getValue();
		toDate = datesPickerWindow.getToDate().getValue();
		custID = datesPickerWindow.getCustomerID().getText();
		boolean validProperty = true;

		if (toDate != null) {
			diffDays = DateTime.diffDays(Helper.localDateToDateTime(toDate), Helper.localDateToDateTime(fromDate));
		}

		if (validProperty) {
			try {
				rentalProperty.rent(custID, Helper.localDateToDateTime(fromDate), diffDays);
			} catch (InvalidDateException e) {
				AlertBox.display("Invalid Date", "Please enter a valid date range. To date cannot be before From date");
				return;
			} catch (MissingInputException e) {
				AlertBox.display("Date Missing", "Please ensure the following field has been filled: " + e.getInvalidField());
				return;
			} catch (NullPointerException e) {
				AlertBox.display("Date Missing", "Please ensure both From and To dates have been selected");
				return;
			}
		}
		//successful rent property
		changeViewPropertyStatus(rentalProperty.getPropertyStatus());
	}
	
	/**
	 * this method displays a date picker window to the user to select the check-out
	 * date for the property. The check-in date from the rental record is passed to
	 * the constructor of the date picker window in order to stop the user from
	 * attempting to return the property on a date before the check-in date
	 */
	private void returnProperty() {
		DateTime rentDate = null;
		for(RentalRecord rentalRecord : rentalProperty.getRentalRecords())
			if(rentalRecord.getActualReturnDate() == null)
				rentDate = rentalRecord.getRentDate();
		SingleDatePicker datePicker = new SingleDatePicker("Return Property", rentDate);
		datePicker.display();
		if(datePicker.isDeliberateExit())
			return;
		try {
			rentalProperty.returnProperty(Helper.localDateToDateTime(datePicker.getDatePicker().getValue()));
		} catch (Exception e) {
			AlertBox.display("No Date", "Please choose a check-out date");
		}
		//Successful return property
		changeViewPropertyStatus(rentalProperty.getPropertyStatus());
	}

	/**
	 * This method puts the property into maintenance
	 */
	private void performMaintenance() {
		rentalProperty.performMaintenance();
		changeViewPropertyStatus(rentalProperty.getPropertyStatus());
	}

	/**
	 * This method notifies the program that the maintenance is complete. It
	 * displays a date picker to the user for them to enter the maintenance
	 * completion date. All dates have purposely been allowed in order to allow the
	 * user to set dates in the past. If the completeMaintenance method is
	 * successful the Last Maintenance Date datePicker in the view is updated with
	 * the new value
	 */
	private void completeMaintenance() {
		LocalDate lastMaintenanceDate = null;
		SingleDatePicker datePicker = new SingleDatePicker("Complete Maintenance");
		datePicker.display();
		if(datePicker.isDeliberateExit())
			return;
		try {
			lastMaintenanceDate = datePicker.getDatePicker().getValue();
			rentalProperty.completeMaintenance(Helper.localDateToDateTime(lastMaintenanceDate));
		} catch (NullPointerException e) {
			AlertBox.display("No Date", "Please choose a maintenance completion date");
			return;
		}
		//complete maintenance successful
		view.getPropertyInfoInput().getLastMaintenanceDateDatePicker().setValue(lastMaintenanceDate);
		changeViewPropertyStatus(rentalProperty.getPropertyStatus()); //update last maintenance date datepicker;
	}

	/**
	 * Displays an alert box to the user confirming that their requested action has been complete and the property's status has changed accordingly
	 * @param status
	 */
	private void changeViewPropertyStatus(String status) {
		view.setButtonStates(status);
		AlertBox.display("Property Status Change", "The property's status is now set to: " + status);
	}

	
	
	/**
	 * This method builds the rental records table using .values() collection in the RentalRecordFieldsEnum Class
	 */
	private void buildTable() {
		for (RentalRecordFieldsEnum tableHeader : RentalRecordFieldsEnum.values()) {
			tableHeaderArray.add(new TableColumn<RentalRecord, String>(tableHeader.getTableHeader()));
			tableHeaderArray.get(tableHeaderArray.size() - 1)
					.setCellValueFactory(new PropertyValueFactory<>(tableHeader.getFieldValue()));
		}
		rentalRecordsTable.setItems(rentalRecords);
		rentalRecordsTable.getColumns().addAll(tableHeaderArray);

	}

	public ManagePropertyView getView() {
		return view;
	}

}
