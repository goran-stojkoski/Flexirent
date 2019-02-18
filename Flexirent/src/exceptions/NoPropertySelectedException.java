package exceptions;

/**
 * This exception is thrown when the user has not selected a property to manage
 * @author Goran Stojkoski
 * @version v.10
 * @date 20 Oct 2018
 */
public class NoPropertySelectedException extends Exception {
	public NoPropertySelectedException() {
		System.err.println("No Property Selected");
	}
}
