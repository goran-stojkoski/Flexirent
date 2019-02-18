package view.flexirent.catalgoue_management;

import java.io.File;

import controller.PropertyManagementController;
import exceptions.NoPropertySelectedException;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.rental_property.RentalProperty;

/**
 * This view holds all of the items necessary to display the property and all of its information
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class PropertyInfoView extends VBox {

	private ImageView imageView;
	private Image propertyImage;
	private Text propertyAddress;
	private Text propertyType;
	private Text bedrooms;
	private Text propertyStatus;
	private Text propertyDescription;
	private Button rentButton;
	private RentalProperty displayedRentalProperty;

	/**
	 * This constructor is called when there is no rental property available to show
	 */
	public PropertyInfoView() {
		setImage("NoImageAvailable.jpg");
		propertyAddress = new Text("Property Address Not Available");
		propertyType = new Text("Property Type Not Available");
		propertyDescription = new Text("This is the description of the property. Everything about the property in detail should go here.");
		bedrooms = new Text("Bedrooms: 0");
		propertyStatus = new Text("Property Status: Available");
		rentButton = new Button("Rent");
		setLayout();
	}
	
	/**
	 * This constructor receives a RentalProperty and displays its information and image
	 * @param rentalProperty
	 */
	public void showRentalProperty(RentalProperty rentalProperty) {
		propertyAddress.setText(rentalProperty.getStreetAddress());
		propertyType.setText(rentalProperty.getPropertyType());
		bedrooms.setText(Integer.toString(rentalProperty.getBedrooms()));
		propertyStatus.setText(rentalProperty.getPropertyStatus());
		propertyDescription.setText(rentalProperty.getPropertyDescription());
		imageView.setImage(new Image(new File("images/" + rentalProperty.getImageFilename()).toURI().toString()));
		displayedRentalProperty = rentalProperty;
	}

	private void setImage(String imageName) {
		propertyImage = new Image(new File("images/" + imageName).toURI().toString());
		imageView  = new ImageView(propertyImage);
	}
	
	public ImageView getPropertyImage() {
		return imageView;
	}

	public RentalProperty getDisplayedRentalProperty() {
		return displayedRentalProperty;
	}

	public Button getRentButton() {
		return rentButton;
	}
	
	private void setLayout() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		HBox imageCaption = new HBox(propertyAddress,spacer,propertyType);
		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		HBox BottomHBox = new HBox(propertyStatus,spacer2,rentButton);
		Region spacer3 = new Region();
		Separator hSeparator = new Separator(Orientation.HORIZONTAL);
		Separator hSeparator2 = new Separator(Orientation.HORIZONTAL);
		VBox.setVgrow(spacer3, Priority.ALWAYS);
		super.getChildren().addAll(imageView, imageCaption,hSeparator,propertyDescription,spacer3,hSeparator2,BottomHBox);
		super.setSpacing(5);
		super.setMargin(propertyDescription, new Insets(10,0,0,0));
		super.setVgrow(propertyDescription, Priority.ALWAYS);
		imageView.setFitWidth(600);
		imageView.setPreserveRatio(true);
	}
}
