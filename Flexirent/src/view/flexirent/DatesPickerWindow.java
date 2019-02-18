package view.flexirent;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.DateTime;
import utilities.Helper;
/**
 * This class provides a window for the user to select from and to dates to book a proeprty. The user must also ensure they are booking the proeprty against a valid CustomerID
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class DatesPickerWindow {
	
	private Stage window;
	private Scene scene;
	private Text message;
	private Button rentButton;
	private Button cancelButton;
	private DatePicker fromDate;
	private DatePicker toDate;
	private HBox buttonsHBox;
	private HBox datesHBox;
	private VBox layout;
	private LocalDate maxDate;
	private TextField customerID;
	private boolean deliberateExit = false;

	/**
	 * Creates a DatePicker window
	 */
	public DatesPickerWindow() {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		message = new Text("Select Dates");
		rentButton = new Button("Rent");
		cancelButton = new Button("Cancel");
		fromDate = new DatePicker();
		toDate = new DatePicker();
		buttonsHBox = new HBox(rentButton,cancelButton);
		layout = new VBox();
		datesHBox = new HBox(new VBox(new Label("Check-In Date"), fromDate), new VBox(new Label("Check-Out Date"), toDate));
		customerID = new TextField("CUS###");
		layout.getChildren().addAll(message, new Label("Customer ID. Must be in format CUS###. for e.g. CUS123"),
				customerID, datesHBox, buttonsHBox);

		
		toDate.setDisable(true);
		setMinMaxDates();
		setLayout();
		scene = new Scene(layout);
		window.setScene(scene);
	}
	/**
	 * Creates a DatePicker window and recieves a maxDate value to places a limit of either the From and To DatePickers
	 */
	public DatesPickerWindow(DateTime maxDate) {
		this();
		this.maxDate = Helper.dateTimeToLocalDate(maxDate);
	}

	/**
	 * This method limits the user from picking dates in the past and if a maxDate
	 * value has been provided it also stops the user from selecting dates past the
	 * maxDate value
	 */
	private void setMinMaxDates() {
		fromDate.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				toDate.setDisable(false);
				super.updateItem(date, empty);
				if (date.isBefore(LocalDate.now())) {
					setDisable(true);
				}
				if (maxDate != null)
					if (date.isAfter(maxDate))
						setDisable(true);
			}
		});
		
		fromDate.valueProperty().addListener((observable, oldValue, newValue) -> toDate.setDayCellFactory(picker -> new DateCell() {
					@Override
					public void updateItem(LocalDate date, boolean empty) {
						super.updateItem(date, empty);
						if (date.isBefore(fromDate.getValue())) {
							setDisable(true);
						}
						if (maxDate != null) {
							if (date.isAfter(maxDate))
								setDisable(true);
						} else if (date.isAfter(newValue.plusDays(28)))
							setDisable(true);
					}
				}));
	}
	
	/**
	 * Create the action events for the buttons on the forms
	 */
	public void setActionEvents() {
		rentButton.setOnAction(e -> {
			if (!fieldsEmpty()) {
				if (Helper.validCustID(customerID.getText()))
					window.close();
				else
					AlertBox.display("Invalid UserID", "Please enter a valid userID");
			} else
				AlertBox.display("Form Incomplete", "Please ensure all the fields are filled");
		});
		
		cancelButton.setOnAction(e -> { // user has requested to deliberately exit the window without selecting dates
			deliberateExit = true;
			window.close();
		});

		window.setOnCloseRequest(e -> {
			deliberateExit = true;
			window.close();
		});
	}

	/**
	 * Checks if any of the fields are empty for form simple form validation within
	 * the this window. More complex form validation is handled in the
	 * ManagePropertiesController. This method will return a true value if ANY one
	 * field is empty
	 */
	private boolean fieldsEmpty() {
		if(message.getText().equals(null))
			return true;
		if(fromDate == null)
			return true;
		if(toDate == null)
			return true;
		return false;
	}
	
	/**
	 * Clears all the fields within the datepicker window
	 */
	private void clearFields() {
		fromDate.setValue(null);
		toDate.setValue(null);
		customerID.setPromptText("CUS###");
		deliberateExit = false;
	}
	
	public void display() {
		window.showAndWait();
	}

	public DatePicker getFromDate() {
		return fromDate;
	}

	public DatePicker getToDate() {
		return toDate;
	}
	
	public TextField getCustomerID() {
		return customerID;
	}
	
	public boolean isDeliberateExit() {
		return deliberateExit;
	}
	
	/**
	 * This method controls the form styling and layout
	 */
	private void setLayout() {
		datesHBox.setSpacing(15);
		message.setFont(Font.font(20));
		fromDate.setPrefWidth(150);
		toDate.setPrefWidth(150);
		layout.setPadding(new Insets(10));
		VBox.setMargin(customerID, new Insets(0, 0, 10, 0));
		VBox.setMargin(message, new Insets(0, 0, 10, 0));
		VBox.setMargin(fromDate, new Insets(0, 0, 10, 0));
		VBox.setMargin(buttonsHBox, new Insets(20, 0, 0, 0));
		buttonsHBox.setSpacing(5);
		buttonsHBox.setAlignment(Pos.BOTTOM_RIGHT);
		customerID.setPromptText("CUSXXX");
		customerID.maxWidthProperty().bind(fromDate.widthProperty());
		setActionEvents();
	}

}
