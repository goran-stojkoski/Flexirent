package application;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;


import javafx.stage.Stage;
import model.constant.DatabaseSettings;
import model.constant.RentalPropertyFieldsEnum;
import model.constant.RentalRecordFieldsEnum;
import model.database.FlexiRentDB;
import view.flexirent.FlexirentView;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *	Starter class for Flexirent application
 *  @author Goran Stojkoski
 *  @date 20 Oct 2018
 *  @version v1.0
 */
public class Main extends Application {
	public static final Logger logger = Logger.getLogger(Main.class.getName());
	private static FileHandler finerHandler = null;
	private static FileHandler warningHandler = null;
	private static ConsoleHandler consoleHandler = null;
	
	
	@Override
	public void start(Stage primaryStage) {
		//Initialize logger
		logIt();
		
		//Check to see if the database tables exist. If the tables do not exist, they are added automatically
		logger.fine("The program has been started");
		logger.warning("This is a warning");
		try {
			FlexiRentDB.connectionTest();
			if(FlexiRentDB.tableTest(DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME) != 1) {
				FlexiRentDB.createTable(RentalPropertyFieldsEnum.getCreateTableStatement());
				logger.log(Level.WARNING,DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME +" table was created");
			}
			if(FlexiRentDB.tableTest(DatabaseSettings.RENTAL_RECORDS_TABLE_NAME) != 1) {
				FlexiRentDB.createTable(RentalRecordFieldsEnum.getCreateTableStatement());
				logger.log(Level.WARNING,DatabaseSettings.RENTAL_RECORDS_TABLE_NAME + " table was created");
			}
			Parent parent = new FlexirentView();
			primaryStage.setTitle("FlexiRent System");
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void logIt() {
		try {
			logger.setUseParentHandlers(false);
			finerHandler = new FileHandler("logs/fine.log",false);
			warningHandler = new FileHandler("logs/warning.log",false);
			consoleHandler = new ConsoleHandler();

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		logger.setLevel(Level.FINER);
		warningHandler.setLevel(Level.WARNING);
		finerHandler.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.FINER);
		
		finerHandler.setFormatter(new SimpleFormatter());

		logger.addHandler(finerHandler);
		logger.addHandler(warningHandler);
		logger.addHandler(consoleHandler);
	}

}
