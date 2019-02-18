package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.constant.DatabaseSettings;

/**
 * This Class is a static so that it can provide generic uses of its methods anywhere in the application
 * @author Goran Stojkoski
 * @date 20 Oct 2018
 * @version v1.0
 */
public class FlexiRentDB {

	private static String dbName = DatabaseSettings.DB_NAME;
	private static Connection con = null;

	private FlexiRentDB() {
	}

	private static Connection getConnection(String databaseName) {
		dbName = databaseName;
		try {
			if (con == null || con.isClosed()) {
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
				con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + dbName, "SA", "");
			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return con;
	}

	private static Connection getConnection() {
		return getConnection(dbName);
	}

	public static int connectionTest() throws SQLException  {
		if (getConnection() == null)
			return 0;
		else
			return 1;
	}

	public static int tableTest(String tableName) {
		try {
			getConnection().createStatement().executeQuery("SELECT * FROM " + tableName);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return e.getErrorCode();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return 1;
	}
	
	/**
	 * This method returns an ArrayList of string type to the user based on the provided table name. The fields are seperated by the ":" delimeter
	 * @param tableName - requested table to get the data from
	 */
	public static ArrayList<String> toStringArray(String tableName) {
		ArrayList<String> sqlList = new ArrayList<String>();
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			String query = "SELECT * FROM " + tableName;
			boolean tableExists = false;
			while (!tableExists) {
				try (ResultSet resultSet = stmt.executeQuery(query)) {
					tableExists = true;
					int colCount = resultSet.getMetaData().getColumnCount();
					while (resultSet.next()) {
						ArrayList<String> dbRow = new ArrayList<String>();
						for (int i = 1; i <= colCount; i++)
							dbRow.add(resultSet.getString(i));
						sqlList.add(String.join(":", dbRow));
					}
				} catch (SQLException e) {
					System.out.println("Table " + tableName + " Not Found. Attempting to create table");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		return sqlList;
	}
	
	public static boolean addTuple(String sqlAddTubleString) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate(sqlAddTubleString);
			con.commit();
			System.out.println("Insert into table executed successfully");
			System.out.println(result + " row(s) affected");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public static boolean deleteTuple(String primaryKeyHeader, String primaryKey, String tableName) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			String query = "DELETE FROM " + tableName + " WHERE "+primaryKeyHeader+" = '" + primaryKey + "'";
			int result = stmt.executeUpdate(query);
			
			System.out.println("Delete from table " + tableName + " executed successfully");
			System.out.println(result + " row(s) affected");
			toStringArray(tableName);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static void createTable(String createTableSqlStatement) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate(createTableSqlStatement);
			if(result == 0)
				System.out.println("The table has been created successfully");
			else
				System.out.println("The table has not been created");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteTable(String tableName) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
			int result = stmt.executeUpdate("DROP TABLE " + tableName);
			
			if(result == 0)
				System.out.println("Table " + tableName + " has been deleted successfully");
			else
				System.out.println("Table " + tableName + " was not deleted");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void createDatabase() {
		try (Connection con = getConnection()) {
			System.out.println("Connection to database " + dbName + " created successfully");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//updates the database if only a specific value is needed to be updated
	public static void updateTuple(String tableName, String primaryKeyField, String primaryKey, String field, String value) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
			int result = stmt.executeUpdate("UPDATE " + tableName + " SET " + field +" = '" + value +"' WHERE " + primaryKeyField + " = '" + primaryKey + "';");
			if(result == 0)
				System.out.println("Table " + tableName + " was not updated");
			else
				System.out.println("Table " + tableName + " has been updated successfully");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//updates the database based on a custom SQL string provided by the user. This method is useful when wanting to change more than one tuple at a time
	public static void updateTuple(String tableName, String primaryKeyField, String primaryKey, String updateString) {
		try (Connection con = getConnection(); Statement stmt = con.createStatement()) {
			int result = stmt.executeUpdate("UPDATE " + tableName + " SET " + updateString + " WHERE " + primaryKeyField + " = '" + primaryKey + "';");
			if(result == 0)
				System.out.println("Table " + tableName + " was not updated");
			else
				System.out.println("Table " + tableName + " has been updated successfully");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
}
