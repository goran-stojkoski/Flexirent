package view.flexirent;

import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.DateTime;
import utilities.Helper;

/**
 * This class is used to display a window with aDatePicker for the user to chose a value from. It used when a user is returning or completing maintenance of a property
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class SingleDatePicker {

	private Stage window;
	private Scene scene;
	private Text message;
	private Button okButton;
	private Button cancelButton;
	private DatePicker datePicker;
	private HBox buttonsHBox;
	private VBox layout;
	private DateTime minDate;
	private boolean deliberateExit;

	/**
	 * This constructor create a window with that allows the user to pick any date
	 * in the past, present and future. Useful for when a maintenance date is set
	 * 
	 * @param title
	 */
	public SingleDatePicker(String title) {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);

		message = new Text(title);
		okButton = new Button("Ok");
		cancelButton = new Button("Cancel");
		datePicker = new DatePicker();
		buttonsHBox = new HBox(okButton, cancelButton);
		layout = new VBox();

		setActionEvents();
		setLayout();
		
		scene = new Scene(layout);
		window.setScene(scene);
	}

	/**
	 * This constructor create a window with that allows the user to only pick from
	 * date past the minDate value. Used when returning a property. A property
	 * cannot be returned before the check-in date
	 * 
	 * @param title
	 */
	public SingleDatePicker(String title, DateTime minDate) {
		this(title);
		this.minDate = minDate;
		datePicker.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate localMinDate = Helper.dateTimeToLocalDate(minDate);
				setDisable(empty || date.compareTo(localMinDate) < 0);
			}
		});
	}

	/**
	 * This method provides simple input validation. More complex input validation is performed in the PropertyManagementController
	 */
	public void setActionEvents() {
		okButton.setOnAction(e -> {
			if(datePicker.getValue() == null) {
				AlertBox.display("Form Incomplete", "Please select a date");
				return;
			}
			else
				window.close();
		});
		cancelButton.setOnAction(e ->{
			deliberateExit = true;
			window.close();
		});
		
		window.setOnCloseRequest(e -> {
			deliberateExit = true;
			window.close();
		});
		
	}
	
	private boolean fieldsEmpty() {
		if(datePicker == null)
			return true;
		return false;
	}

	public void display() {
		window.showAndWait();
	}

	public DatePicker getDatePicker() {
		return datePicker;
	}
	
	public boolean isDeliberateExit() {
		return deliberateExit;
	}
	
	/**
	 * this method controls the layout and styling of the window
	 */
	private void setLayout() {
		message.setFont(Font.font(20));
		datePicker.setPrefWidth(150);
		layout.getChildren().addAll(message, new Label("Choose Date"), datePicker, buttonsHBox);
		layout.setPadding(new Insets(10));
		VBox.setMargin(message, new Insets(0, 0, 10, 0));
		VBox.setMargin(datePicker, new Insets(0, 0, 10, 0));
		VBox.setMargin(buttonsHBox, new Insets(20, 0, 0, 0));
		buttonsHBox.setSpacing(5);
		buttonsHBox.setAlignment(Pos.BOTTOM_RIGHT);
	}
	
}
