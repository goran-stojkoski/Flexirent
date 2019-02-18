package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import exceptions.InsufficientCharactersException;
import exceptions.MissingInputException;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.constant.ApartmentSettings;
import model.constant.PremiumSuiteSettings;
import model.constant.RentalPropertySettings;
import model.rental_property.Apartment;
import model.rental_property.PremiumSuite;
import model.rental_property.RentalProperty;
import utilities.DateTime;
import utilities.Helper;
import view.flexirent.AlertBox;
import view.flexirent.PropertyInfoInput;
import view.flexirent.add_edit_property.AddEditPropertyView;

/**
 * This class holds the main AddEditPropertyView class. The class handles the functionality of the Flexirent application
 *	@author Goran Stojkoski
 * 	@date  20 Oct 2018
 * 	@version v1.0
 */

public class AddPropertyController {

	AddEditPropertyView view;;
	PropertyInfoInput propertyInfoInput;
	int streetNumber;
	int bedrooms;
	String street;
	String suburb;
	String description;
	String propertyType;
	DateTime lastMaintenanceDate;
	String imageFileName;
	String propertyStatus;

	/**
	 * The controller instantiates the view and creates the action events for the
	 * buttons on the form
	 */
	public AddPropertyController() {
		view = new AddEditPropertyView(this);
		setActionEvetnts();
	}

	/**
	 * This method handles the userform inputs and prompts the user with alert boxes
	 * to correct Errors they have made. Once all the inputs have been validated,
	 * they passed to the model to create objects of the either the Apartment or
	 * Premium Suite classes
	 */
	private void addProperty() {
		propertyInfoInput = view.getPropertyInfoInput(); //the form the user is requested to fill out
		RentalProperty rentalProperty;
		try {
			streetNumber = Integer.parseInt(propertyInfoInput.getStreetNumberTextField().getText());
			street = propertyInfoInput.getStreetTextField().getText().replace("'", "''");	// the repalceAll function allows for Street Names with apostrophes, 
																								//for e.g. O'Brien Street without disrupting SQL query
			suburb = propertyInfoInput.getsuburbComboBox().getSelectionModel().getSelectedItem().replace("'", "''"); //see above comment
			description = getValidPropertyDescription().replace ("'", "''"); //see above comment
			propertyStatus = RentalPropertySettings.STATUS_AVAILABLE;
			bedrooms = propertyInfoInput.getBedrooms();

			if (view.getPropertyInfoInput().getApartmentTypeRadioButton().isSelected()) {	// ADD PROPERTY STRATEGY adds an apartment if this radiobutton is selected (message 1/3)
				if(bedrooms == 0) {AlertBox.display("Form Incomplete", "Please enter the number of bedrooms"); return;}
				propertyType = ApartmentSettings.PROPERTY_TYPE_LABEL;
				rentalProperty = new Apartment(streetNumber, street, suburb, propertyStatus, bedrooms, description,
						imageFileName);
			} else if (view.getPropertyInfoInput().getPremiumSuitTypeRadioButton().isSelected()) { // ADD PROPERTY STRATEGY adds a premium suite if this radiobutton is selected (message 2/3)
				try {
					lastMaintenanceDate = Helper.localDateToDateTime(propertyInfoInput.getLastMaintenanceDateDatePicker().getValue());
				} catch (Exception e) {
					AlertBox.display("Form Incomplete", "Please enter the last maintenance date");
					return;
				}
				lastMaintenanceDate = Helper.localDateToDateTime(propertyInfoInput.getLastMaintenanceDateDatePicker().getValue());
				propertyType = PremiumSuiteSettings.PREMIUM_SUITE_LABEL;
				rentalProperty = new PremiumSuite(streetNumber, street, suburb, lastMaintenanceDate, propertyStatus,
						description, imageFileName);
			} else {																			
				AlertBox.display("Incomplete Form", "Please enter the type of property being added"); // ADD PROPERTY STRATEGY an alert box is shown if the use has not selected a proeprty 
				return;
			}
			if (rentalProperty.addToDatabase()) {
				view.resetForm();
			}
		} catch (NumberFormatException e) {
			AlertBox.display("Missing Input", "Please enter a street number");
			return;
		} catch (InsufficientCharactersException e) {
			AlertBox.display("Missing Input", "Please enter a street number");
			return;
		} catch (MissingInputException e) {
			AlertBox.display("Missing Input", "Please ensure the following field has been filled: " + e.getInvalidField());
			return;
		} catch (NullPointerException e) {
			AlertBox.display("Missing Input", "Please ensure the form has been filled correctly");
			return;
		}
		
		//Property add successful
		AlertBox.display("Add Property Success", "The property has been sucesfully added to the system");
		
	}

	private String getValidPropertyDescription() throws InsufficientCharactersException {
		String propertyDescription = view.getPropertyInfoInput().getPropertyDescription().getText();
		if (view.getPropertyInfoInput().getPropertyDescription().getText().length() < 80)
			AlertBox.display("Insufficient Characters", "The property description must be between 80 and 100 characters");
		return propertyDescription;
	}

	private void uploadImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
		File selectedImage = fileChooser.showOpenDialog(null);
		if (selectedImage != null) {
			File savedImage = new File("images/" + selectedImage.getName());
			try {
				Files.copy(selectedImage.toPath(), savedImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
				view.getImageUploadNode().getPropertyImage().setImage(new Image(savedImage.toURI().toString()));
				imageFileName = selectedImage.getName();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method sets the action events that occur when the buttons are pressed
	 */
	public void setActionEvetnts() {
		view.getBottomNode().getAddPropertyButton().setOnAction(e -> addProperty());
		view.getImageUploadNode().getUploadImageButton().setOnAction(e -> uploadImage());
	}

	public AddEditPropertyView getView() {
		return view;
	}

}
