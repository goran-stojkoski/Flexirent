package view.flexirent.catalgoue_management;

import java.util.Arrays;
import java.util.LinkedList;

import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import model.constant.ApartmentEnum;
import model.constant.FlexiRentSystemSettings;

/**
 * This class contains the view for the filtering function used in the catalogue management window
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class CatalogueFilterView extends HBox {

	MenuButton propertyType;
	CheckMenuItem allTypes, apartment, premiumSuite;

	MenuButton bedrooms;
	CheckMenuItem allBedrooms, oneBedroom, twoBedrooms, threeBedrooms;

	MenuButton location;
	CheckMenuItem[] locations;
	
	MenuButton propertyStatus;
	CheckMenuItem allStatuses, avaialble, rented, underMaintenance;

	Region spacer;

	Button resetFilter;

	LinkedList<CheckMenuItem> filterItems;
	LinkedList<MenuButton> filterGroups = new LinkedList<MenuButton>();

	public CatalogueFilterView() {
		propertyType = new MenuButton("Property Type");
		allTypes = new CheckMenuItem("All");
		apartment = new CheckMenuItem("Apartment");
		premiumSuite = new CheckMenuItem("Premium Suite");
		bedrooms = new MenuButton("Bedrooms");
		allBedrooms = new CheckMenuItem("All");
		oneBedroom = new CheckMenuItem(Integer.toString(ApartmentEnum.ONE_BEDROOM.getIntValue()));
		twoBedrooms = new CheckMenuItem(Integer.toString(ApartmentEnum.TW0_BEDROOM.getIntValue()));
		threeBedrooms = new CheckMenuItem(Integer.toString(ApartmentEnum.THREE_BEDROOM.getIntValue()));
		location = new MenuButton("Location");
		propertyStatus = new MenuButton("Property Status");
		allStatuses = new CheckMenuItem("All");
		avaialble = new CheckMenuItem("Available");
		rented = new CheckMenuItem("Rented");
		underMaintenance = new CheckMenuItem("Under Maintenance");

		int suburbCount = FlexiRentSystemSettings.Suburbs.length;
		locations = new CheckMenuItem[suburbCount+1];
		locations[0] = new CheckMenuItem("All");
		filterItems = new LinkedList<>(Arrays.asList(new CheckMenuItem[]{apartment,premiumSuite,oneBedroom,twoBedrooms,threeBedrooms,avaialble,rented,underMaintenance}));
		for(int i = 0;i<suburbCount;i++) {
			locations[i+1] = new CheckMenuItem(FlexiRentSystemSettings.Suburbs[i]);
			filterItems.add(locations[i+1]);
		}
		spacer = new Region();
		
		resetFilter = new Button("Reset");

		setLayout();
	}

	public Button getResetButton() {
		return resetFilter;
	}
	
	public LinkedList<CheckMenuItem> getFilterItems() {
		return filterItems;
	}

	public LinkedList<MenuButton> getFilterGroups(){
		return filterGroups;
	}
	
	public MenuButton getPropertyType() {
		return propertyType;
	}

	public CheckMenuItem getAllTypes() {
		return allTypes;
	}

	public MenuButton getBedrooms() {
		return bedrooms;
	}

	public CheckMenuItem getAllBedrooms() {
		return allBedrooms;
	}

	public MenuButton getLocation() {
		return location;
	}

	public CheckMenuItem[] getLocations() {
		return locations;
	}

	public MenuButton getPropertyStatus() {
		return propertyStatus;
	}

	public CheckMenuItem getAllStatuses() {
		return allStatuses;
	}

	private void setLayout() {
		propertyType.getItems().addAll(allTypes, apartment, premiumSuite);
		bedrooms.getItems().addAll(allBedrooms, oneBedroom, twoBedrooms, threeBedrooms);
		location.getItems().addAll(locations);
		propertyStatus.getItems().addAll(allStatuses, avaialble, rented, underMaintenance);
		filterGroups.add(propertyType);
		filterGroups.add(bedrooms);
		filterGroups.add(location);
		filterGroups.add(propertyStatus);
		
		super.getChildren().addAll(propertyType, bedrooms, location, propertyStatus, spacer,
				resetFilter);
		spacer.setPrefWidth(20);
	}
	
}