package model.rental_property;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.MissingInputException;

public class ApartmentTest {

	private Apartment apartment;
	
			
			
	@Before
	public void setUp() throws Exception {
		apartment = new Apartment(12, "John Street", "Melbourne", "Available", 3, "oadskjdpoaoskdposakdpokasdkjfsipgvsdjnfbejfbipjefibjjfdbiojdfbiojdfiobjjfbiojfdbijdfbiojdfiobjdjfb", "NoImageSelected.jpg");
	}

	@Test
	public void testGetStreetNumber() throws MissingInputException{
		assertEquals(12, apartment.getStreetNumber());
	}

	@Test(expected = MissingInputException.class)
	public void testAdd() throws MissingInputException{
		apartment = new Apartment(0, "John Street", "Melbourne", "Available", 3, "oadskjdpoaoskdposakdpokasdkjfsipgvsdjnfbejfbipjefibjjfdbiojdfbiojdfiobjjfbiojfdbijdfbiojdfiobjdjfb", "NoImageSelected.jpg");
	}
	

}
