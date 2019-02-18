package view.flexirent.manage_property;

import exceptions.NoPropertySelectedException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.RentalRecord;
import model.constant.RentalPropertySettings;
import model.rental_property.RentalProperty;
import view.flexirent.ImageUploadNode;
import view.flexirent.PropertyInfoInput;

/**
 * This is the parent view that holds all of the sub views to manage a chosen
 * property. This class cannot be called unless a valid property is passed to
 * the constructor
 * 
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class ManagePropertyView extends BorderPane {

	private Text managePropertyText;
	private ImageUploadNode imageUploadNode;
	private ManagePropertyButtonBar buttonBar;
	private PropertyInfoInput propertyInfoInput;
	private TableView<RentalRecord> rentalRecordsTable;
	private Button rentPropertyButton, returnPropertyButton, performMaintenanceButton, completeMaintenaceButton, editPropertyButton;
	private VBox bottomVBoxNode;
	private Button closeButton;
	
	/**
	 * This constructor populates all of the fields inside the propertyInfoInput
	 * view. This view has been chosen so that later on functionality to edit some
	 * of the properties fields can be added. If an invalid property has been passed to this constructor it will throw a {@link NoPropertySelectedException}
	 * 
	 * @param rentalProperty
	 * @throws NoPropertySelectedException
	 */
	public ManagePropertyView(RentalProperty rentalProperty) throws NoPropertySelectedException {
		if (rentalProperty == null) throw new NoPropertySelectedException();
		managePropertyText = new Text("Manage Property");
		imageUploadNode = new ImageUploadNode(rentalProperty.getImageFilename());
		buttonBar = new ManagePropertyButtonBar();
		propertyInfoInput = new PropertyInfoInput(rentalProperty);
		rentalRecordsTable = new TableView<RentalRecord>();
		closeButton = new Button("Close");
		bottomVBoxNode = new VBox(rentalRecordsTable,closeButton);
		
		rentPropertyButton = buttonBar.getRentPropertyButton();
		returnPropertyButton = buttonBar.getReturnPropertyButton();
		performMaintenanceButton = buttonBar.getPerformMaintenanceButton();
		completeMaintenaceButton = buttonBar.getCompleteMaintenanceButton();
		editPropertyButton = buttonBar.getEditPropertyButton();
		
		super.setTop(managePropertyText);
		super.setLeft(propertyInfoInput);
		super.setCenter(imageUploadNode);
		super.setRight(buttonBar);
		super.setBottom(bottomVBoxNode);
		setButtonStates(rentalProperty.getPropertyStatus());
		editPropertyButton.setDisable(true);
		setLayout();
		closeButton.setOnAction(e -> ManagePropertyWindow.close());
	}
	
	/**
	 * This method enables and disables the buttons based on the status of the Rental Property
	 * @param propertyStatus
	 */
	public void setButtonStates(String propertyStatus) {
		for (Node button : buttonBar.getChildren())
			((Button) button).setDisable(true);
		if (propertyStatus.equals(RentalPropertySettings.STATUS_RENTED)) {
			for (Node button : buttonBar.getChildren())
				if (button == returnPropertyButton)
					((Button) button).setDisable(false);
		} else if (propertyStatus.equals(RentalPropertySettings.STATUS_UNDER_MAINTENANCE)) {
			for (Node button : buttonBar.getChildren())
				if (button == completeMaintenaceButton)
					((Button) button).setDisable(false);
		} else if (propertyStatus.equals(RentalPropertySettings.STATUS_AVAILABLE)) {
			for (Node button : buttonBar.getChildren())
				if (button == rentPropertyButton || button == performMaintenanceButton)
					((Button) button).setDisable(false);
		}
	}
	
	public ManagePropertyButtonBar getButtonBar() {
		return buttonBar;
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public PropertyInfoInput getPropertyInfoInput() {
		return propertyInfoInput;
	}
	

	public TableView<RentalRecord> getRentalRecordsTable() {
		return rentalRecordsTable;
	}

	private void setLayout() {
		managePropertyText.setFont(Font.font(30));
		imageUploadNode.getPropertyImage().setFitHeight(300);
		int buttonCount = buttonBar.getChildren().size();
		rentalRecordsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		for (Node button : buttonBar.getChildren()) {
			((Button) button).minHeightProperty().bind(imageUploadNode.heightProperty().divide(buttonCount));
			((Button) button).setPrefWidth(100);
		}
		bottomVBoxNode.setAlignment(Pos.BOTTOM_RIGHT);
		VBox.setMargin(closeButton, new Insets(10, 0, 0, 0));
		super.setPadding(new Insets(10));
		super.setMargin(bottomVBoxNode, new Insets(10, 0, 0, 0));
		super.setMargin(propertyInfoInput, new Insets(0, 10, 0, 0));
		super.setMargin(managePropertyText, new Insets(0, 0, 10, 0));
	}
}
