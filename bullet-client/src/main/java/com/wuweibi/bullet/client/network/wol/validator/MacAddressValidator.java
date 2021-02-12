package com.wuweibi.bullet.client.network.wol.validator;

import java.util.regex.Pattern;

/**
 * The Class MacAddressValidator.
 */
public class MacAddressValidator {

	/** The Constant PATTERN_1. */
	private static final String PATTERN_1 = "^([0-9A-F]{12})$";

	/** The Constant PATTERN_2. */
	private static final String PATTERN_2 = "^([0-9A-F]{2}[:-])"
			+ "{5}([0-9A-F]{2})$";

	/** The Constant PATTERN_3. */
	private static final String PATTERN_3 = "^([0-9A-F]{4}[.])"
			+ "{2}([0-9A-F]{4})$";

	/**
	 * Validate.
	 * 
	 * @param macAddress
	 *            the mac address
	 * @return true, if successful
	 */
	public static boolean validate(String macAddress) {
		if (Pattern.matches(PATTERN_1, macAddress.toUpperCase())) {
			return true;
		}
		if (Pattern.matches(PATTERN_2, macAddress.toUpperCase())) {
			return true;
		}

		return Pattern.matches(PATTERN_3, macAddress.toUpperCase());

	}

}
