package com.entercard.coop.utils;

import java.text.DecimalFormat;

public class StringUtils {
	/**
	 * @param string
	 * @return
	 */
	public static String formatCurrency(String string) {
		if (string.length() > 4) {
			DecimalFormat formatter = new DecimalFormat("00,000.00");
			return formatter.format(Double.valueOf(string));
		} else {
			DecimalFormat formatter = new DecimalFormat("0.00");
			return formatter.format(Double.valueOf(string));
		}
	}
}
