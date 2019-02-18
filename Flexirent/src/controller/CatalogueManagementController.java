package controller;

import java.util.ArrayList;

import exceptions.NoPropertySelectedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.constant.DatabaseSettings;
import model.constant.RentalPropertyFieldsEnum;
import model.constant.RentalPropertySettings;
import model.database.FlexiRentDB;
import model.rental_property.RentalProperty;
import utilities.Helper;
import view.flexirent.AlertBox;
import view.flexirent.add_edit_property.AddPropertyWindow;
import view.flexirent.catalgoue_management.CatalogueManagementView;
import view.flexirent.catalgoue_management.PropertyInfoView;
import view.flexirent.manage_property.ManagePropertyWindow;

/**
 * This class controls all of the interactions between the user and the form
 * when the catalog management window is shown. The purpose of the catalog
 * manager is to be able to go through the list of properties from a high level.
 * Dedicated rental property management is handled by the
 * ProeprtManagementControler
 * 
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class CatalogueManagementController {

	ObservableList<RentalProperty> rentalProperties = FXCollections.observableArrayList();
	ArrayList<String> propertyStringList;
	CatalogueManagementView view;
	TableView<RentalProperty> propertyTable;
	PropertyInfoView propertyInfo;
	private ArrayList<TableColumn<RentalProperty, String>> tableHeaderArray;

	public CatalogueManagementController() {
		updatePropertyList(); // this method intially updates and imports the Rental Properties
		
		view = new CatalogueManagementView(this,rentalProperties);
		propertyInfo = view.getPropertyInfoView();
		propertyTable = view.getPropertyCatalogueView().getPropertyTable();
		tableHeaderArray = new ArrayList<TableColumn<RentalProperty, String>>();
		buildTable();
		
		//call to activate all of the action events for buttons, tableView selections etc.
		setButtonActions();
		if(rentalProperties.size()>0) {
			propertyTable.getSelectionModel().select(0);
			propertyInfo.showRentalProperty(rentalProperties.get(0));
		}

	}	

	/*
	 * This method updates the property list in the TableView and is usually called when a change has been made
	 */
	public void updatePropertyList() {
		rentalProperties.removeAll(rentalProperties);
		ArrayList<String> rentalPropertiesStringLisst = FlexiRentDB.toStringArray(DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME);
		if(rentalPropertiesStringLisst.size() != 0)
			rentalProperties.addAll(Helper.propertyStringListToPropetyObjectList(rentalPropertiesStringLisst));
	}
	
	/**
	 * When a user selects a property in the tableview list this method tells the propertyInfoView to display the property
	 */
	private void tableViewSelectionAction() {
		propertyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldObject, newObject) -> {
			if(newObject != null) propertyInfo.showRentalProperty(newObject);
			try {
				if(newObject.getPropertyStatus().equals(RentalPropertySettings.STATUS_RENTED))
					propertyInfo.getRentButton().setDisable(true);
				else  propertyInfo.getRentButton().setDisable(false);
			} catch (Exception e) {
			}
		});
	}
	
	/**
	 * This method enables the renting of a property from the CatalogueManagemnetView 
	 */
	private void rentPropertyFromCatalogue() {
		propertyInfo.getRentButton().setOnAction(e -> {
			RentalProperty rentalProperty = propertyInfo.getDisplayedRentalProperty();
			try {
				new PropertyManagementController(rentalProperty).rentProperty();
			} catch (NoPropertySelectedException e2) {
				AlertBox.display("No Property Selected", "Please select a property to rent");
				return;
			}
			updatePropertyList();
		});
		
	}
	
	private void setButtonActions() {
		manageProperty();
		deleteProperty();
		addProperty();
		tableViewSelectionAction();
		rentPropertyFromCatalogue();
	}	
	
	/**
	 * Opens an instance of the Manage Property View window
	 */
	private void manageProperty() {
		view.getPropertyCatalogueView().getManagePropertyButton().setOnAction(e -> {ManagePropertyWindow.display(propertyTable.getSelectionModel().getSelectedItem()); updatePropertyList();});
	}
	
	/**
	 * removes a property from the flexirent database
	 */
	private void deleteProperty() {
		view.getPropertyCatalogueView().getRemovePropertyButton().setOnAction(e -> {
			String removePropertyID = propertyTable.getSelectionModel().getSelectedItem().getPropertyID().replace("'", "''"); // .replace method required to handle apostrophes in SQL statement
			if (FlexiRentDB.deleteTuple(RentalPropertyFieldsEnum.PROPERTY_ID.toString(), removePropertyID,DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME) == true)
				AlertBox.display("Success Deletion", "The property has been successfully deleted from the system");
			else
				AlertBox.display("Unsuccessful Deletion","There was an error in deleting the property from the system");
			updatePropertyList();
		});
	}
	
	/**
	 * adds a property from the flexirent database
	 */
	private void addProperty() {
		view.getPropertyCatalogueView().getAddPropertyButton().setOnAction(e -> {new AddPropertyWindow().display(); updatePropertyList();});
	}

	/**
	 * This method builds the TableView automatically from the RentalPropertyFieldsEnum Class
	 */
	public void buildTable() {
			for (RentalPropertyFieldsEnum tableHeader : RentalPropertyFieldsEnum.values()) {
				if (tableHeader.isVisiableInTableView()) {
					tableHeaderArray.add(new TableColumn<RentalProperty, String>(tableHeader.getTableHeader()));
					tableHeaderArray.get(tableHeaderArray.size() - 1).setCellValueFactory(new PropertyValueFactory<>(tableHeader.getFieldValue()));
				}
			}
			propertyTable.setItems(rentalProperties);
			propertyTable.getColumns().addAll(tableHeaderArray);
		}

	public RentalProperty getSelectedRentalProperty() {
		return propertyTable.getSelectionModel().getSelectedItem();
	}
	
	public CatalogueManagementView getView() {
		return view;
	}

	public TableView<RentalProperty> getPropertyTable() {
		return propertyTable;
	}

	public ObservableList<RentalProperty> getRentalProperties() {
		return rentalProperties;
	}
	
	
	
}
