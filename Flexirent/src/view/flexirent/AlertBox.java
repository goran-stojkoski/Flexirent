package view.flexirent;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is window that van be called form anywhere in the application to display success, error and alert messages
 * @author GSTOJKOS
 *
 */
public class AlertBox {

	private static Stage window;
	private static Scene scene;
	private static Text message;
	private static Button okButton;
	private static GridPane layout;
	
	public static void display(String alertTitle, String alertMessage) {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(alertTitle);
		
		message = new Text(alertMessage);
		okButton = new Button("Ok");
		okButton.setPrefWidth(100);
		
		layout = new GridPane();
		layout.add(message, 0, 0);
		layout.add(okButton, 0, 1);
		GridPane.setMargin(message, new Insets(10,20,10,20));
		GridPane.setMargin(okButton, new Insets(10,20,20,20));
		GridPane.setHalignment(okButton, HPos.CENTER);
		
		okButton.setOnAction(e -> window.close());

		scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
