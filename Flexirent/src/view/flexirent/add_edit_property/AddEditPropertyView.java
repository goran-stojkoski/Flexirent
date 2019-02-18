package view.flexirent.add_edit_property;

import controller.AddPropertyController;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import view.flexirent.ImageUploadNode;
import view.flexirent.PropertyInfoInput;

/**
 * This class is the parent view to add/edit properties
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class AddEditPropertyView extends VBox {
	
	private AddPropertyController controller;
	private PropertyInfoInput propertyInfoInput;
	private AddEditPropertyButtonBarView bottomNode;
	private ImageUploadNode imageUploadNode;
	private Region leftSpacer;
	private Region rightSpacer;
	
	private Text heading;
	
	public AddEditPropertyView(AddPropertyController controller) {
		this.controller = controller;
		
		propertyInfoInput = new PropertyInfoInput();
		imageUploadNode = new ImageUploadNode();
		heading = new Text("Add Property");
		leftSpacer = new Region();
		rightSpacer = new Region();
		
		bottomNode = new AddEditPropertyButtonBarView();
		
		super.getChildren().addAll(heading,imageUploadNode,propertyInfoInput,bottomNode);
		super.setPadding(new Insets(10));
		setLayout();
	}
		
	public PropertyInfoInput getPropertyInfoInput() {
		return propertyInfoInput;
	}
	
	public AddEditPropertyButtonBarView getBottomNode() {
		return bottomNode;
	}
	
	public ImageUploadNode getImageUploadNode() {
		return imageUploadNode;
	}
	
	public AddPropertyController getController() {
		return controller;
	}
	
	public void resetForm() {
		propertyInfoInput.clearFields();
		imageUploadNode.resetImage();
	}
	
	private void setLayout() {
		HBox.setMargin(propertyInfoInput, new Insets(0,10,0,0));
		heading.setFont(Font.font(30));
		HBox.setHgrow(leftSpacer, Priority.SOMETIMES);
		HBox.setHgrow(rightSpacer, Priority.SOMETIMES);
		imageUploadNode.getPropertyImage().setFitWidth(500);
	}
	
}
