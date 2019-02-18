package model.interfaces;

import utilities.DateTime;

/**
 * This method enforces the implementation of the following methods required for carrying out maintenanc on a property
 */
public interface Maintainable {
	boolean performMaintenance();
	boolean completeMaintenance(DateTime completionDate);
}
