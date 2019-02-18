package view.flexirent.catalgoue_management;

import controller.CatalogueFilterController;
import controller.CatalogueManagementController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.rental_property.RentalProperty;

/**
 * This view is the parent for that holds all of the views necessary for the manage the properties. It contains both the catalogue and property views
 * @author Goran Stojosku
 * @version v1.0
 * @date 20 Oct 2018
 */
public class CatalogueManagementView extends GridPane {

	private PropertyCatalogueView propertyCatalogueView;
	private PropertyInfoView propertyInfo;
	private CatalogueFilterView filter;
	private Text heading;
	
	public CatalogueManagementView(CatalogueManagementController controller, ObservableList<RentalProperty> rentalProperties) {
		propertyCatalogueView = new PropertyCatalogueView(rentalProperties);
		propertyInfo = new PropertyInfoView();
		filter = new CatalogueFilterController(propertyCatalogueView.getPropertyTable(),rentalProperties).getView();
		heading = new Text("Property Catalogue");
		GridPane.setConstraints(heading, 0, 0);
		GridPane.setConstraints(filter, 0,1);
		GridPane.setConstraints(propertyCatalogueView, 0, 2);
		GridPane.setConstraints(propertyInfo, 1, 2);
		super.getChildren().addAll(heading,filter,propertyCatalogueView,propertyInfo);
		setLayout();				
	}

	public PropertyCatalogueView getPropertyCatalogueView() {
		return propertyCatalogueView;
	}

	public PropertyInfoView getPropertyInfoView() {
		return propertyInfo;
	}

	public CatalogueFilterView getPropertyFilterView() {
		return filter;
	}

	private void setLayout() {
		heading.setFont(Font.font(30));
		super.setPadding(new Insets(10));
		super.setHgap(10);
		super.setVgap(5);
		super.setMargin(filter, new Insets(0, 0, 10, 0));
	}

}
