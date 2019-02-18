package model.constant;

import java.util.ArrayList;

import utilities.Helper;
/**
 * This class holds all of the constant values specifically related to the Rental Record fields. It also contains methods that generate SQL queries using the enum's map collection
 * @author Graon Stojkoski
 * @date 20 Oct 2018
 */
public enum RentalRecordFieldsEnum {
	
	RECORD_ID("Record ID", "recordID", "VARCHAR",255,false, true),
	RENT_DATE("DATE", false, false),
	ESTIMATED_RETURN_DATE("DATE", false, false),
	ACTUAL_RETURN_DATE("DATE", true, false),
	RENTAL_FEE("DOUBLE", true, false),
	LATE_FEE("DOUBLE", true, false);

	private String tableHeader;
	private String fieldValue;
	private String dataType;
	private Integer maxLength;
	private boolean nullable;
	private boolean primaryKey;
	
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
	private RentalRecordFieldsEnum(String tableHeader, String fieldValue, String dataType, int maxLength, boolean nullable, boolean primaryKey) {
		this.tableHeader = tableHeader;
		this.fieldValue = fieldValue;
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
	}

	/**
	 * This constructor is used when the user does not need to specify a specific value for the java field name
	 */
	private RentalRecordFieldsEnum(String dataType, int maxLength, boolean nullable, boolean primaryKey) {
		//cannot access constructor therefore need to repeat method
		tableHeader = Helper.toProperCase(this.toString());
		this.dataType = dataType;
		this.maxLength = maxLength;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		fieldValue = Helper.toCamelCase(this.toString());
	}

	/**
	 * This constructor is used for number values
	 */
	private RentalRecordFieldsEnum(String dataType, boolean nullable, boolean primaryKey) {
		tableHeader =  Helper.toProperCase(this.toString());
		this.dataType = dataType;
		this.nullable = nullable;
		this.primaryKey = primaryKey;
		fieldValue = Helper.toCamelCase(this.toString());
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
		StringBuilder sqlCreateTableStatement = new StringBuilder("CREATE TABLE "+ DatabaseSettings.RENTAL_RECORDS_TABLE_NAME +" (");
		ArrayList<String> primaryKey = new ArrayList<String>();
		for (RentalRecordFieldsEnum sqlTableHeader : RentalRecordFieldsEnum.values()) {
			sqlCreateTableStatement.append(sqlTableHeader.getSqlCreateTableLine());
			sqlCreateTableStatement.append(",");
			if (sqlTableHeader.primaryKey == true)
				primaryKey.add(sqlTableHeader.toString());
		}
		sqlCreateTableStatement.append("PRIMARY KEY (" + String.join(",", primaryKey) + "))");
		return sqlCreateTableStatement.toString();
	}

	public String getTableHeader() {
		return tableHeader;
	}
	
	public String getFieldValue() {
		return fieldValue;
	}

	public String getDataType() {
		return dataType;
	}

	public int getMaxLength() {
		return maxLength;
	}
	
}
