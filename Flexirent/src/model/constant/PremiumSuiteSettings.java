package model.constant;

/**
 * This class holds all of the constant values specifically related to Premium Suites
 * @author Graon Stojkoski
 * @date 20 Oct 2018
 */
public class PremiumSuiteSettings {
	//Number Constants
	public static final double RENTAL_RATE = 554;
	public static final double LATE_FEE_RATE = 662;
	public static final int MAINTENANCE_INTERVAL_DAYS = 10;
	public static final int DEFAULT_BEDROOMS = 1;
	public static final int MIN_RENT_DAYS = 1;
	
	//String Constants
	public static final String LAST_MAINTENANCE_DATE_LABEL = "LAST MAINTENANCE DATE";
	public static final String WARNING_PROPERTY_MAINTENANCE_IS_REQUIRED = "WARNING!: THE LAST MAINTENANCE DATE OF THE PROPERTY IS GREATER THAN 10 DAYS"
			+ System.lineSeparator() + "THE PROPERTY HAS BEEN AUTOMATICALLY PLACED UNDER MAINTENANCE" + System.lineSeparator() + "THE PROPERTY WILL REMAIN UNAVAILABLE UNTIL MAINTENANCE IS COMPLETE" + System.lineSeparator();

	public static final String PREMIUM_SUITE_LABEL = "Premium Suite";
	public static final int PREMIUM_SUITE_CODENUMBER = 2;
}
