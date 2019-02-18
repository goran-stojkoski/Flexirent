package view.flexirent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.constant.ApartmentSettings;
import model.constant.FlexiRentSystemSettings;
import model.constant.PremiumSuiteSettings;
import model.rental_property.Apartment;
import model.rental_property.RentalProperty;

/**
 * This class is the main view that the user sees in order to input or edit property information
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class PropertyInfoInput extends VBox {

	private TextField streetNumberTextField;
	private TextField streetTextField;
	private ComboBox<String> suburbComboBox;
	private Label propertyDescriptionLabel;
	private TextArea propertyDescription;
	private RadioButton apartmentTypeRadioButton;
	private RadioButton premiumSuitTypeRadioButton;
	private DatePicker lastMaintenanceDateDatePicker;
	private RadioButton oneBedroomRadioButton;
	private RadioButton twoBedroomRadioButton;
	private RadioButton threeBedroomRadioButton;

	private VBox streetNumberVBox ;

	private VBox streetNameVBox;
	private VBox suburbVBox;

	private HBox propertyAddress;
	private HBox additionalInfo;
	
	private VBox lastMaintenanceDateVBox;

	private VBox propertyTypeVBox;
	
	private VBox bedroomsVBox;
	private VBox propDescInfo;
	
	private Region[] spacers = new Region[4]; //An array of spacers used to help with the layout of the view
	
	private ToggleGroup propertyTypeGroup;
	private ToggleGroup bedroomsGroup;
	
	private int bedrooms = 0;

	/**
	 * The constructor of this class receives a rental property and it's fields are initialised by the fileds of the rental property
	 * @param rentalProperty
	 */
	public PropertyInfoInput(RentalProperty rentalProperty) {
		streetNumberTextField = new TextField(Integer.toString(rentalProperty.getStreetNumber()));
		streetTextField = new TextField(rentalProperty.getStreetName());
		suburbComboBox = new ComboBox<String>();
		propertyDescription = new TextArea(rentalProperty.getPropertyDescription());
		apartmentTypeRadioButton = new RadioButton(ApartmentSettings.PROPERTY_TYPE_LABEL);
		premiumSuitTypeRadioButton = new RadioButton(PremiumSuiteSettings.PREMIUM_SUITE_LABEL);
		lastMaintenanceDateDatePicker = new DatePicker();
		oneBedroomRadioButton = new RadioButton(ApartmentSettings.ONE_BEDROOM_FIELD);
		twoBedroomRadioButton = new RadioButton(ApartmentSettings.TWO_BEDROOM_FIELD);
		threeBedroomRadioButton = new RadioButton(ApartmentSettings.THREE_BEDROOM_FIELD);

		suburbComboBox.getItems().addAll(FlexiRentSystemSettings.Suburbs);
		suburbComboBox.getSelectionModel().select(rentalProperty.getSuburbName());
		if(rentalProperty instanceof Apartment)
			apartmentTypeRadioButton.setSelected(true);
		else
			premiumSuitTypeRadioButton.setSelected(true);
		if(rentalProperty.getBedrooms() == 1)
			oneBedroomRadioButton.setSelected(true);
		else if(rentalProperty.getBedrooms() ==2)
			twoBedroomRadioButton.setSelected(true);
		else
			threeBedroomRadioButton.setSelected(true);
		if(rentalProperty.getLastMaintenanceDate() != null)
			lastMaintenanceDateDatePicker.setValue(LocalDate.parse(rentalProperty.getLastMaintenanceDate().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		setupTheView();
		
	}
	
	/**
	 * This constructor is used when no proeprty is selected from the TableView
	 */
	public PropertyInfoInput() {
		//Initiase Objects
		streetNumberTextField = new TextField();
		streetTextField = new TextField();
		suburbComboBox = new ComboBox<String>();
		propertyDescription = new TextArea();
		apartmentTypeRadioButton = new RadioButton(ApartmentSettings.PROPERTY_TYPE_LABEL);
		premiumSuitTypeRadioButton = new RadioButton(PremiumSuiteSettings.PREMIUM_SUITE_LABEL);
		lastMaintenanceDateDatePicker = new DatePicker();
		oneBedroomRadioButton = new RadioButton(ApartmentSettings.ONE_BEDROOM_FIELD);
		twoBedroomRadioButton = new RadioButton(ApartmentSettings.TWO_BEDROOM_FIELD);
		threeBedroomRadioButton = new RadioButton(ApartmentSettings.THREE_BEDROOM_FIELD);
		setupTheView();
	}

	private void setupTheView() {
		for(int i=0;i<4;i++)
			spacers[i] = new Region();
		//Address Info
		streetNumberVBox = new VBox(new Label("Street Number"), streetNumberTextField);
		streetNameVBox = new VBox(new Label("Street Name"), streetTextField);
		suburbVBox = new VBox(new Label("Suburb"), suburbComboBox);
		
		//Property Info
		propertyTypeVBox = new VBox(new Label("Property Type"), apartmentTypeRadioButton,premiumSuitTypeRadioButton);
		lastMaintenanceDateVBox = new VBox(new Label("Last Maintenance Date"), lastMaintenanceDateDatePicker);
		bedroomsVBox = new VBox(new Label("Number of Bedrooms"), oneBedroomRadioButton, twoBedroomRadioButton,threeBedroomRadioButton);

		String propertyDescriptionLabelString = "Property Description (between 80 and 100 characters.";
		propertyDescriptionLabel = new Label(propertyDescriptionLabelString);
		propDescInfo = new VBox(propertyDescriptionLabel, propertyDescription);
		
		propertyTypeGroup = new ToggleGroup();
		bedroomsGroup = new ToggleGroup();

		propertyAddress = new HBox(streetNumberVBox, spacers[0] , streetNameVBox,spacers[1], suburbVBox);
		additionalInfo = new HBox(propertyTypeVBox, spacers[2],bedroomsVBox, spacers[3], lastMaintenanceDateVBox);
		
		//Setup property description input rules
		propertyDescription.setTextFormatter(new TextFormatter<String>(change -> change.getControlNewText().length() <= 100 ? change : null));
		propertyDescription.textProperty().addListener((obsVal, oldVal, newVal)-> {propertyDescriptionLabel.setText(propertyDescriptionLabelString + " Characters so far: " + obsVal.getValue().length());});
		
		suburbComboBox.getItems().addAll(FlexiRentSystemSettings.Suburbs);
		super.getChildren().addAll(propertyAddress,additionalInfo, propDescInfo);

		
		// Setup ToggleGroups
		apartmentTypeRadioButton.setToggleGroup(propertyTypeGroup);
		premiumSuitTypeRadioButton.setToggleGroup(propertyTypeGroup);
		oneBedroomRadioButton.setToggleGroup(bedroomsGroup);
		twoBedroomRadioButton.setToggleGroup(bedroomsGroup);
		threeBedroomRadioButton.setToggleGroup(bedroomsGroup);
		
		setActionItems();
		setLayout();
	}

	/**
	 * This method points to the various simple actions item methods
	 */
	private void setActionItems() {
		setRadioButtonControl();
		setMinValuetDatePicker();
		allowOnlyNumbersInTextField();
	}
	
	/**
	 * This method forces the selection/deselction of the radio buttons based on whether the user's selection of property type
	 */
	private void setRadioButtonControl() {
		apartmentTypeRadioButton.setOnAction(e -> {
			oneBedroomRadioButton.setSelected(false);
			twoBedroomRadioButton.setSelected(false);
			twoBedroomRadioButton.setDisable(false);
			threeBedroomRadioButton.setSelected(false);
			threeBedroomRadioButton.setDisable(false);
		});

		premiumSuitTypeRadioButton.setOnAction(e -> {
			oneBedroomRadioButton.setSelected(true);
			twoBedroomRadioButton.setSelected(false);
			twoBedroomRadioButton.setDisable(true);
			threeBedroomRadioButton.setSelected(false);
			threeBedroomRadioButton.setDisable(true);
		});

		oneBedroomRadioButton.setOnAction(e -> bedrooms = 1);
		twoBedroomRadioButton.setOnAction(e -> bedrooms = 2);
		threeBedroomRadioButton.setOnAction(e -> bedrooms = 3);
	}

	/**
	 * This method prevents all dates in the past from being picked by the user
	 */
	private void setMinValuetDatePicker() {
		lastMaintenanceDateDatePicker.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (date.isAfter(LocalDate.now()))
					setDisable(true);
			}
		});
	}

	/**
	 * This method blocks text from being typed in the street number section
	 */
	private void allowOnlyNumbersInTextField() {
		streetNumberTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					streetNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	// Used when a user has complete the form and has submitted the values to the
	// controller. The fields will clear in order to allow the user to add another
	// property whilst the window is still open
	public void clearFields() {
		streetNumberTextField.clear();
		streetTextField.clear();
		suburbComboBox.getSelectionModel().clearSelection();
		apartmentTypeRadioButton.setSelected(false);
		premiumSuitTypeRadioButton.setSelected(false);
		oneBedroomRadioButton.setSelected(false);
		twoBedroomRadioButton.setSelected(false);
		threeBedroomRadioButton.setSelected(false);
		propertyDescription.clear();
		lastMaintenanceDateDatePicker.getEditor().clear();
	}
	
	// Disables all the fields in the this class
	public void disableFields() {
		streetNumberTextField.setDisable(true);
		streetTextField.setDisable(true);
		suburbComboBox.setDisable(true);
		apartmentTypeRadioButton.setDisable(true);
		premiumSuitTypeRadioButton.setDisable(true);
		oneBedroomRadioButton.setDisable(true);
		twoBedroomRadioButton.setDisable(true);
		threeBedroomRadioButton.setDisable(true);
		propertyDescription.setDisable(true);
		lastMaintenanceDateDatePicker.setDisable(true);
	}
	
	
	public TextField getStreetNumberTextField() {
		return streetNumberTextField;
	}

	public TextField getStreetTextField() {
		return streetTextField;
	}

	public ComboBox<String> getsuburbComboBox() {
		return suburbComboBox;
	}

	public RadioButton getApartmentTypeRadioButton() {
		return apartmentTypeRadioButton;
	}

	public RadioButton getPremiumSuitTypeRadioButton() {
		return premiumSuitTypeRadioButton;
	}

	public DatePicker getLastMaintenanceDateDatePicker() {
		return lastMaintenanceDateDatePicker;
	}

	public RadioButton getOneBedroomRadioButton() {
		return oneBedroomRadioButton;
	}

	public RadioButton getTwoBedroomRadioButton() {
		return twoBedroomRadioButton;
	}

	public RadioButton getThreeBedroomRadioButton() {
		return threeBedroomRadioButton;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public TextArea getPropertyDescription() {
		return propertyDescription;
	}

	/**
	 * This method sets the layout and the style of this view
	 */
	private void setLayout() {
		propertyDescription.setPrefHeight(100);
		streetNumberTextField.setPrefWidth(90);
		streetTextField.setPrefWidth(100);
		lastMaintenanceDateDatePicker.setPrefWidth(125);
		Insets TopInset10 = new Insets(10, 0, 0, 0);
		super.setMargin(bedroomsVBox, TopInset10);
		super.setMargin(lastMaintenanceDateVBox, TopInset10);
		propertyAddress.setSpacing(10);
		propertyTypeVBox.setSpacing(5);
		bedroomsVBox.setSpacing(5);
		propertyDescription.setWrapText(true);
		super.setSpacing(10);
		propertyDescription.setPrefWidth(270);
		for(Region spacer : spacers)
		HBox.setHgrow(spacer, Priority.ALWAYS);
	}

}
