package view.flexirent.catalgoue_management;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.rental_property.RentalProperty;

/**
 * This view is the holds property catalog and the buttons to manage the Rental Property catalog
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class PropertyCatalogueView extends VBox {

	private TableView<RentalProperty> propertyTable;
	private Button managePropertyButton, removePropertyButton, addPropertyButton;
	private HBox buttonGroup;
	
	public PropertyCatalogueView(ObservableList<RentalProperty> rentalProperties) {
		propertyTable = new TableView<>();
		managePropertyButton = new Button("Manage Property");
		removePropertyButton = new Button("Remove Property");
		addPropertyButton = new Button("Add Property");
		buttonGroup = new HBox(managePropertyButton, removePropertyButton, addPropertyButton);
		
		super.getChildren().addAll(propertyTable, buttonGroup);
		setLayout();
	}

	public TableView<RentalProperty> getPropertyTable() {
		return propertyTable;
	}

	public Button getManagePropertyButton() {
		return managePropertyButton;
	}

	public Button getRemovePropertyButton() {
		return removePropertyButton;
	}

	public Button getAddPropertyButton() {
		return addPropertyButton;
	}
	
	private void setLayout() {
		propertyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		propertyTable.setPrefHeight(425);
		super.setVgrow(propertyTable, Priority.ALWAYS);
		super.setSpacing(5);
		buttonGroup.setSpacing(5);
		buttonGroup.setAlignment(Pos.BOTTOM_RIGHT);
	}
}
