package view.flexirent.manage_property;


import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * This view holds all all of the button available to the user to perform actions that manage the property
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class ManagePropertyButtonBar extends VBox {

	private Button rentProperty;
	private Button returnProperty;
	private Button performMaintenance;
	private Button completeMaintenance;
	private Button editProperty;
	
	
	public ManagePropertyButtonBar() {
		rentProperty = new Button("Rent Proeprty");
		returnProperty = new Button("Return Proeprty");
		performMaintenance = new Button("Perform Maintenance");
		completeMaintenance = new Button("Complete Maintenance");
		editProperty = new Button("Edit Property");
		
		super.getChildren().addAll(rentProperty, returnProperty,performMaintenance,completeMaintenance,editProperty);
		
		for (Node button : super.getChildren()) {
			((Button) button).wrapTextProperty().setValue(true);
			((Button) button).setTextAlignment(TextAlignment.CENTER);
		}
	}

	public Button getRentPropertyButton() {
		return rentProperty;
	}

	public Button getReturnPropertyButton() {
		return returnProperty;
	}

	public Button getPerformMaintenanceButton() {
		return performMaintenance;
	}

	public Button getCompleteMaintenanceButton() {
		return completeMaintenance;
	}

	public Button getEditPropertyButton() {
		return editProperty;
	}
	
}
