package exceptions;

/**
 * This exception is thrown when the user has not typed enough characters for the property description
 * @author Goran Stojkoski
 * @version v.10
 * @date 20 Oct 2018
 */
public class MissingInputException extends Exception {

	String emptyField;
	
	public MissingInputException(String emptyField) {
		this.emptyField = emptyField;
		System.err.println("A value was not provided for: " + emptyField);
	}
	
	public String getInvalidField() {
		return emptyField;
	}
}
