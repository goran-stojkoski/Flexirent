package controller;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import model.rental_property.RentalProperty;
import view.flexirent.catalgoue_management.CatalogueFilterView;

/**
 * This class controls all of the filtering functionality of the proprerties table
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class CatalogueFilterController {
	private CatalogueFilterView view;
	MenuButton propertyType;
	CheckMenuItem allTypes;

	MenuButton bedrooms;
	CheckMenuItem allBedrooms;

	MenuButton location;
	CheckMenuItem[] locations;
	
	MenuButton propertyStatus;
	CheckMenuItem allStatuses;

	LinkedList<MenuButton> filterGroups;
	CatalogueManagementController propertyManagementController;
	TableView<RentalProperty> propertyTable;
	ObservableList<RentalProperty> rentalProperties;
	
	public CatalogueFilterController(TableView<RentalProperty> propertyTable, ObservableList<RentalProperty> rentalProperties) {
		view = new CatalogueFilterView();
		this.propertyTable = propertyTable;
		this.rentalProperties = rentalProperties;
		filterGroups = view.getFilterGroups();
		setActionEvents();
		selectAll();
	}
	
	// sets simple action events
	public void setActionEvents() {
		filterButtonActions();
		
		propertyType = view.getPropertyType();
		allTypes = view.getAllTypes();
		bedrooms = view.getBedrooms();
		allBedrooms = view.getAllBedrooms();
		location = view.getLocation();
		locations = view.getLocations();
		propertyStatus = view.getPropertyStatus();
		allStatuses = view.getAllStatuses();
				
		selectOposites(propertyType,allTypes);
		selectOposites(bedrooms, allBedrooms);
		selectOposites(location, locations[0]);
		selectOposites(propertyStatus, allStatuses);
	}
	
	//When the "All" button is selected in the filter menu, this action selects all of the sub menu items
	public void selectAll() {
		for (MenuButton filterItem : view.getFilterGroups()) {
			for(MenuItem cMenuitem : filterItem.getItems())
				((CheckMenuItem) cMenuitem).setSelected(true);
		}
	}

	// this method selects the opposite of the check item. i.e. if the item is checked it unchecks it, if it's unchecked it checks it.
	private void selectOposites(MenuButton buttonGroup, CheckMenuItem allButton) {
		allButton.setOnAction(e -> {
			for (MenuItem filterItem : buttonGroup.getItems()) {
			if(allButton.isSelected())
				((CheckMenuItem) filterItem).setSelected(false);
			else
				((CheckMenuItem) filterItem).setSelected(true);
			}
			allButton.setSelected(!allButton.isSelected());
			getFilteredList();
		});
	}
	
	/**
	 * This method controls the filtering of the tableview. Since the filter items
	 * are in groups i.e. apartments and premium suits in Property Type. One, two or
	 * three in number of bedrooms group, the filter first need to check each group
	 * to see if either of conditions are met. for example one OR two bedroom. After
	 * each group is checked the filter then checks if it can find properties that
	 * meet meet all the selected criteria
	 */
	public void getFilteredList() {
		ObservableList<RentalProperty> filteredList = FXCollections.observableArrayList();
		ObservableList<RentalProperty> remainingProperties = FXCollections.observableArrayList();

		filteredList.addAll(rentalProperties);	//add all the properties in the database to the filter list
		ArrayList<String> checkItems = new ArrayList<>(); //this is a list of items that the filter needs to check

		for (MenuButton filterGroup : filterGroups) { //STEP 2. then checks if all the selected conditions are met
			for (MenuItem filterItem : filterGroup.getItems())	//STEP 1. first checks within groups
				if (((CheckMenuItem) filterItem).isSelected())
					checkItems.add(filterItem.getText());
			if(checkItems.size()>0) {
			for (RentalProperty rentalProperty : filteredList) {
				for (String checkedItem : checkItems) {
					if (rentalProperty.toString().contains(":" + checkedItem + ":")) { //the filter uses the rental property toString() method to run the check
						remainingProperties.add(rentalProperty);
					}
				}
			}
				filteredList.removeAll(filteredList);
				filteredList.addAll(remainingProperties);
				remainingProperties.removeAll(remainingProperties);
				checkItems.removeAll(checkItems);
			}
		}
		propertyTable.getItems().removeAll();
		propertyTable.setItems(filteredList);
	}

	/**
	 * This method activates all of the filter buttons to resond to user clicks
	 */
	public void filterButtonActions() {
		LinkedList<CheckMenuItem> filterItems = view.getFilterItems();
		for (CheckMenuItem filterItem : filterItems) {
			filterItem.setOnAction(e -> getFilteredList());
		}
		view.getResetButton().setOnAction(e -> {
			propertyTable.getItems().removeAll();
			propertyTable.setItems(rentalProperties);
			selectAll();
		});
	}
	
	public CatalogueFilterView getView() {
		return view;
	}
	
}
