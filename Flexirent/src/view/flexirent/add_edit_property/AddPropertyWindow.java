package view.flexirent.add_edit_property;

import controller.AddPropertyController;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class enables the add/edit property view to pop out as a separate window
 * @author GSTOJKOS
 *
 */
public class AddPropertyWindow {

	private Stage window;
	private AddEditPropertyView addEditPropertyView;
	
	public AddPropertyWindow() {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Add Property");
		
		addEditPropertyView = new AddPropertyController().getView();
		addEditPropertyView.getBottomNode().getCloseButoon().setOnAction(e -> window.close());
		Scene scene = new Scene(addEditPropertyView);
		window.setScene(scene);
	}

	public void display() {
		window.showAndWait();
	}
}