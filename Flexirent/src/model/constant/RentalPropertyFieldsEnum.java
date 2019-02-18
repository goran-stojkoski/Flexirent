package model.constant;

import java.util.ArrayList;
/**
 * This class holds all of the constant values specifically related to the Rental Property fields. It also contains methods that generate SQL queries using the enum's map collection
 * @author Graon Stojkoski
 * @date 20 Oct 2018
 */
public enum RentalPropertyFieldsEnum {

	PROPERTY_ID("Property ID", "PropertyID", "VARCHAR",255,false, true, true),
	STREET_NUMBER("INT", false, false,false),
	STREET_NAME("VARCHAR",255, false, false,true),
	SUBURB_NAME("VARCHAR",255,false, false,true),
	PROPERTY_TYPE("VARCHAR",255,false, false,true),
	BEDROOMS("INT",false, false,true),
	LAST_MAINTENACE_DATE("DATE",true,false,false),
	PROPERTY_STATUS("VARCHAR",255,false, false,true),
	PROPERTY_DESCRIPTION("VARCHAR",255,true, false,false),
	DAILY_RATE("DOUBLE",false, false,true),
	IMAGE_FILENAME("VARCHAR",255,true, false,false);
	
	private String dataType;
	private Integer maxLength;
	private boolean nullable;
	private String sqlNullEntry;
	private String tableHeader;
	private boolean primaryKey;
	private boolean visiableInTableView;
	private String fieldValue;

	/**
	 * This method is used when the user wants to specify a specific value for the Java field name
	 * @param tableHeader - TableView header that will be used in the view
	 * @param fieldValue - The Java field value
	 * @param dataType
	 * @param maxLength - the max length used for the SQL query
	 * @param nullable
	 * @param primaryKey
	 * @param visiableInTableView - user can choose weather or not the field is displayed in the TableView
	 */
	private RentalPropertyFieldsEnum(String tableHeader, String fieldValue, String dataType, int maxLength, boolean nullable, boolean primaryKey, boolean visiableInTableView) {
		this.tableHeader = tableHeader;
		this.fieldValue = fieldValue;
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.visiableInTableView = visiableInTableView;
	}

	/**
	 * This constructor is used when the user does not need to specify a specific value for the java field name
	 */
	private RentalPropertyFieldsEnum(String dataType, int maxLength, boolean nullable, boolean primaryKey,boolean visiableInTableView) {
		// cannot access constructor therefore need to repeat method
		tableHeader = toProperCase();
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.visiableInTableView = visiableInTableView;
		fieldValue = toCamelCase();
	}

	/**
	 * This constructor is used for number values
	 */
	private RentalPropertyFieldsEnum(String dataType, boolean nullable, boolean primaryKey,boolean visiableInTableView) {
		tableHeader = toProperCase();
		this.dataType = dataType;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		this.visiableInTableView = visiableInTableView;
		fieldValue = toCamelCase();
	}



	private String toCamelCase() {
		String[] UpperCaseStringArray = this.toString().toUpperCase().split("_");
		String[] LowerCaseStringArray = this.toString().toLowerCase().split("_");
		int ArrayLength = UpperCaseStringArray.length;
		String[] camelCaseStringArray = new String[ArrayLength];
		camelCaseStringArray[0] = LowerCaseStringArray[0];
		for (int i = 1; i < ArrayLength; i++) {
			camelCaseStringArray[i] = UpperCaseStringArray[i].charAt(0) + LowerCaseStringArray[i].substring(1);
		}
		return String.join("", camelCaseStringArray);
	}

	private String toProperCase() {
		String[] UpperCaseStringArray = this.toString().toUpperCase().split("_");
		String[] LowerCaseStringArray = this.toString().toLowerCase().split("_");
		int ArrayLength = UpperCaseStringArray.length;
		String[] ProperCaseStringArray = new String[ArrayLength];

		for (int i = 0; i<ArrayLength;i++) {
			ProperCaseStringArray[i] = UpperCaseStringArray[i].charAt(0) + LowerCaseStringArray[i].substring(1);
		}

		return String.join(" ", ProperCaseStringArray);
	}

	/**
	 * this method generates individual SQL lines for adding column headers to the database
	 */
	private String getSqlCreateTableLine() {
		String sqlString = this.toString() + " " + dataType;

		if (maxLength != null)
			sqlString = sqlString + "(" + maxLength + ")";
		if (nullable == false)
			sqlString = sqlString + " NOT NULL";

		return sqlString;
	}
	
	/**
	 * this method generates a SQL query based on the fields in the this enum. It also uses the getSqlCreateTableLine() method to create individual lines of the code for each field
	 */
	public static String getCreateTableStatement() {
		StringBuilder sqlCreateTableStatement = new StringBuilder("CREATE TABLE "+ DatabaseSettings.RENTAL_PROPERTY_TABLE_NAME +" (");
		ArrayList<String> primaryKey = new ArrayList<String>();
		for (RentalPropertyFieldsEnum sqlTableHeader : RentalPropertyFieldsEnum.values()) {
			sqlCreateTableStatement.append(sqlTableHeader.getSqlCreateTableLine());
			sqlCreateTableStatement.append(",");
			if (sqlTableHeader.primaryKey == true)
				primaryKey.add(sqlTableHeader.toString());
		}
		sqlCreateTableStatement.append("PRIMARY KEY (" + String.join(",", primaryKey) + "))");
		return sqlCreateTableStatement.toString();
	}
	public String getDataType() {
		return dataType;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	public String getSqlNullEntry() {
		return sqlNullEntry;
	}
	
	public String getTableHeader() {
		return tableHeader;
	}
	
	public boolean isVisiableInTableView() {
		return visiableInTableView;
	}
	
	public String getFieldValue() {
		return fieldValue;
	}
}