package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.RentalRecord;
import model.constant.ApartmentSettings;
import model.constant.DatabaseSettings;
import model.constant.FlexiRentSystemSettings;
import model.constant.PremiumSuiteSettings;
import model.database.FlexiRentDB;
import model.rental_property.RentalProperty;
import view.flexirent.AlertBox;

/**
 * This class imports and exports flexirent Rental Property files.
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class FileImporterExporter {
	
	/**
	 * This method downloads the latest state of the database using the toString method. The method then writes the content from the 
	 */
	public static void exportRentalProperties() {
		PrintWriter printWriter = null;
		File exportFile = new File(FlexiRentSystemSettings.EXPORT_FILENAME); //file to be written to
		DirectoryChooser directoryChoser = new DirectoryChooser();
		File savedFile = new File(directoryChoser.showDialog(new Stage()) + "/" + exportFile.getName()); //user chooses the directory to save file to
		if (exportFile != null && !savedFile.getPath().contains("null")) {
			try {
				printWriter = new PrintWriter(exportFile);
			} catch (java.lang.NullPointerException e) {
				// user aborted file chooser
				return;
			} catch (FileNotFoundException e2) {
				System.err.println("The file cannont be created or opened");
				return;
			}

			ArrayList<String> propertyListArry = FlexiRentDB.toStringArray(DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME);
			for (String property : propertyListArry)
				printWriter.println(property);
			ArrayList<String> rentalRecordsListArray = FlexiRentDB
					.toStringArray(DatabaseSettings.RENTAL_RECORDS_TABLE_NAME);
			for (String record : rentalRecordsListArray)
				printWriter.println(record);
			printWriter.close();

			try {
				Files.copy(exportFile.toPath(), savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			AlertBox.display("Export Successful","The Flexirent Database was successfully exported to:" + exportFile.getName());
		}
	}

	
	/**
	 * This method imports flexirent rental properties that are formatted in the appropriate format 
	 */
	public static void importRentalProperties() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		File importFile = null;
		Scanner scanner = null;
		String line = null;
		ArrayList<String> propertyStringList = new ArrayList<String>(); 
		ArrayList<String> rentalRecordsStringList = new ArrayList<String>();
		ArrayList<RentalProperty> rentalProperties;
		ArrayList<RentalRecord> rentalRecords;
		
		try {
			importFile = fileChooser.showOpenDialog(null);
			scanner = new Scanner(importFile);
		} catch (java.lang.NullPointerException e) {
			//user aborted file chooser
			return;
		} catch (IOException e) {
			System.err.println("No Such File");
			return;
		}

		//since the file contains both rental properties and rental records two lists need to be created
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			//1. The rental properties list
			if(line.contains(ApartmentSettings.PROPERTY_TYPE_LABEL) || line.contains(PremiumSuiteSettings.PREMIUM_SUITE_LABEL))
				propertyStringList.add(line);
			//2. The rental records list
			else if(line.contains("_CUS"))
				rentalRecordsStringList.add(line);
		}
		//each of the lists are now imported to the database using the Storable interface
		rentalProperties = Helper.propertyStringListToPropetyObjectList(propertyStringList);
		rentalRecords = Helper.rentalRecordStringListToRentalRecordObjectList(rentalRecordsStringList);
		for(RentalProperty rentalProperty : rentalProperties)
			rentalProperty.addToDatabase();
		for(RentalRecord rentalRecord : rentalRecords)
			rentalRecord.addToDatabase();
		
		scanner.close();
	}

}
