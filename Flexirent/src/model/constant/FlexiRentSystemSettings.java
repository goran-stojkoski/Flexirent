package model.constant;

/**
 * This class holds all of the constant values specifically related to the FlexiRent System
 * @author Graon Stojkoski
 * @date 20 Oct 2018
 */
public class FlexiRentSystemSettings {

	//Number Constants
	public static final int PROPERTY_CATALOGUE_LIMIT = 50;
	public static final int EXIT_INT = 0;
	
	//String Constants
	public static final String SELECT_OPTION_STRING = "Select Option: ";
	public static final String DEFAULT_SUBURB_NAME = "Melbourne";
	public static final String AVAILABLE_PROPERTIES_TITLE = "RENT PROPERTY - AVAILABLE PROPERTIES";
	public static final String DURATION_OF_STAY_TITLE = "RENT PROPERTY > DURATION OF STAY";
	public static final String DURATION_OF_STAY_INPUT = "Please enter the duratin of the booking in days";
	public static final String DURATION_OF_STAY_MIN_DAYS_LABEL = "Minimum days allowed for this booking:";
	public static final String DURATION_OF_STAY_MAX_DAYS_LABEL = "MAximum days allowed for this booking:";
	public static final String RENTED_PROPERTIES_TITLE = "RETURN PROPERTY > RENTED PROPERTIES";
	public static final String CUSTOMER_HAS_RETURNED_PROPERTY_STRING = "HAS SUCCESSFULY RETURNED BY";
	public static final String MAINTAINABLE_PROPERTIES_TITLE = "PROPERTIES ELIGABLE FOR MAINTENANCE";
	public static final String PROPERTIES_UNDER_MAINTENANCE_TITLE = "PROPERTIES UNDER MAINTENANCE";
	public static final String PROPERTIES_CATALOGUE_TITLE = "FLEXIRENT PROPERTIES CATALOGUE";
	public static final String PROPERTY_CATALOGUE_EMPTY = "PROPERTY CATALOGUE EMPTY: THERE ARE NO RENTAL PROPERTIES IN TEH RENTAL PROPERTY CATALOGUE";
	public static final String RENT_START_DATE = "RENT PROPERTY > CHECK IN DATE";
	public static final String MIN_CHECK_IN_DATE = "Earliest Check In date available:";
	public static final String MAX_CHECK_IN_DATE = "Latest Check In date available:";
	public static final String APP_CLOSE_MESSAGE ="Flexirent Application Closed" + System.lineSeparator() + System.lineSeparator() + "Have a nice day";
	public static final String EXPORT_FILENAME = "export_data.txt";
	
	public static final String[] Suburbs = new String[]{
			"Melbourne",
			"South Bank",
			"Carlton",
			"Docklands",
			"North Melbourne",
			"East Melbourne",
			"Port Melbourne",
			"Parkville",
			"South Yarra"
	};
	

	

}
