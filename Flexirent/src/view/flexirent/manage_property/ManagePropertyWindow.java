package view.flexirent.manage_property;

import controller.PropertyManagementController;
import exceptions.NoPropertySelectedException;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.rental_property.RentalProperty;
import view.flexirent.AlertBox;

/**
 * This class enables the ManagePropertyView to be displayed as a popup window
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class ManagePropertyWindow {
	private static Stage window;
		public static void display(RentalProperty rentalProeprty) {
			window = new Stage();
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("Manage Property");
			
			ManagePropertyView managePropertyView;
			try {
				managePropertyView = new PropertyManagementController(rentalProeprty).getView();
			} catch (NoPropertySelectedException e) {
				AlertBox.display("No Property Selected", "Please select a property to manage");
				return;
			}
			Scene scene = new Scene(managePropertyView);
			window.setScene(scene);
			window.showAndWait();
		}
		
		public static void close() {
			window.close();
		}
}
