package view.flexirent;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utilities.FileImporterExporter;
import view.flexirent.catalgoue_management.CatalogueManagementView;

import controller.CatalogueManagementController;

/**
 * This class is the main pane for which the flexiretn system application is displayed on. It has three main components
 * 1. The CatalogueManagementView object which holds the TableView and PropertyInfoView objects
 * 2. The CatalogueManagementController which controls all of the functions within the views
 * 3. The MenuBar which allows the user access import, export and close functions
 * @author Goran Stojkoski
 * @version v1.0
 * @date 20 Oct 2018
 */
public class FlexirentView extends VBox {

	private CatalogueManagementController catalogueManagementController;
	private CatalogueManagementView catalogueManagementControllerView;

	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem importButton;
	private MenuItem exportButton;
	private MenuItem closeButton;
	private Pane pageView;

	
	public FlexirentView() {
		// Initialize Panes
		catalogueManagementController = new CatalogueManagementController();
		catalogueManagementControllerView = catalogueManagementController.getView();

		// Create MenuBar
		importButton = new MenuItem("Import");
		exportButton = new MenuItem("Export");
		closeButton = new MenuItem("Close");
		fileMenu = new Menu("File");
		fileMenu.getItems().addAll(importButton, exportButton, closeButton);
		menuBar = new MenuBar(fileMenu);


		super.getChildren().addAll(menuBar, catalogueManagementControllerView);
		setLayout();
		setActionEvents();
	}

	public Pane getPageView() {
		return pageView;
	}

	public void setPageView(Pane pageView) {
		super.getChildren().remove(2);
		super.getChildren().add(pageView);
		super.setMargin(pageView, new Insets(10));
	}
	
	/**
	 * This class sets the simple action events for the file menu items that point to static methods within the FileImporterExport Class and Platform Class
	 */
	private void setActionEvents() {
		importButton.setOnAction(e -> {FileImporterExporter.importRentalProperties(); catalogueManagementController.updatePropertyList();});
		exportButton.setOnAction(e -> FileImporterExporter.exportRentalProperties());
		closeButton.setOnAction(e -> Platform.exit());
	}

	private void setLayout() {
		super.setMargin(catalogueManagementControllerView, new Insets(10));
	}
}
