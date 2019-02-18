package view.flexirent.add_edit_property;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * This class provides a simple view that contains the add and close buttons to add a property to the system
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class AddEditPropertyButtonBarView extends HBox {
	
	private Button addPropertyButton;
	private Button closeButton;
	
	public AddEditPropertyButtonBarView(){
		addPropertyButton = new Button("Add to Catalogue");
		closeButton = new Button("Close");
		super.getChildren().addAll(addPropertyButton,closeButton);
		super.setSpacing(5);
		super.setAlignment(Pos.BOTTOM_RIGHT);
		super.setMargin(addPropertyButton,new Insets(10,0,0,0));
	}
	
	public Button getAddPropertyButton() {
		return addPropertyButton;
	}
	
	public Button getCloseButoon() {
		return closeButton;
	}

}
