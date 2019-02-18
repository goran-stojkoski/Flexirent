package model.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * The apartments enum defines all the fields necessary for the the different bedroom configurations
 * @author Goran Stojkoski
 *
 */
public enum ApartmentEnum {
	
	ONE_BEDROOM(1, "One Bedroom", 143),
	TW0_BEDROOM(2, "Two Bedrooms", 210),
	THREE_BEDROOM(3, "Three Bedrooms", 319);
	
	private final int intValue;
	private final String stringValue;
	private final double dailyRate;
	
	
	private ApartmentEnum(int intValue, String stringValue, double dailyRate) {
		this.intValue = intValue;
		this.stringValue = stringValue;
		this.dailyRate = dailyRate;
	}

	private static final Map<Integer, ApartmentEnum> map;
	
	static {
		map = new HashMap<Integer, ApartmentEnum>();
		for (ApartmentEnum v : ApartmentEnum.values()) {
			map.put(v.intValue, v);
		}
	}

	public static ApartmentEnum findByKey(int i) {
		return map.get(i);
	}
	
	public int getIntValue() {
		return intValue;
	}


	public String getStringValue() {
		return stringValue;
	}


	public double getDailyRate() {
		return dailyRate;
	}
	
	
}