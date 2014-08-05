package com.entercard.coopmedlem.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

// TODO: Auto-generated Javadoc
/**
 * The Class StringUtils.
 */
@SuppressLint("DefaultLocale") public class StringUtils {

	/** The Constant EMPTY. */
	public final static String EMPTY = "";
	
	/** The Constant BLANK. */
	public static final String BLANK = " ";

	/**
	 * Checks if is empty.
	 *
	 * @param str the str
	 * @return true, if is empty
	 */
	public static boolean isEmpty(String str) {
		return (str == null || EMPTY.equals(str));
	}

	/**
	 * Checks if is not empty.
	 *
	 * @param str the str
	 * @return true, if is not empty
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * Md5.
	 *
	 * @param s the s
	 * @return the string
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException shouldNotHappen) {
			shouldNotHappen.printStackTrace();
		}
		return "";
	}

	/**
	 * Join.
	 *
	 * @param s the s
	 * @param delimiter the delimiter
	 * @return the string
	 */
	public static String join(Collection<?> s, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		Iterator<?> i = s.iterator();
		while (i.hasNext()) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

	/**
	 * Removes the blanks.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String removeBlanks(String string) {
		return string.replace(BLANK, EMPTY);
	}

	/**
	 * Trim string.
	 *
	 * @param string the string
	 * @return the string
	 */
	public static String trimStringAndDigits(String string) {
		return string.replaceAll("(\\r|\\n|\\t)", "").replaceAll("[0-9]","").trim();
	}
	
	public static String trimStringOnly(String string) {
		return string.replaceAll("(\\r|\\n|\\t)", "").trim();
	}
	
	/**
	 * Safe.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String safe(String str) {
		return isEmpty(str) ? EMPTY : str;
	}

//	/**
//	 * Format to locale.
//	 *
//	 * @param text the text
//	 * @return the string
//	 */
//	public static String formatToLocale(String text) {
//
//		double currency = Double.parseDouble(text);
//		// Format to US locale
//		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
//		return format.format(currency);
//	}

	/**
	 * validate your email address format. Ex-akhi@mani.com
	 *
	 * @param email the email
	 * @return true, if is valid email
	 */
	public static boolean isValidEmail(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Gets the random uuid.
	 *
	 * @return the random uuid
	 */
	public static String getRandomUUID() {
		String uniqueID = UUID.randomUUID().toString();
		return uniqueID;
	}

	/**
	 * Gets the spanned text.
	 *
	 * @param toBeSpannedText the to be spanned text
	 * @param completeText the complete text
	 * @param flag the flag
	 * @return the spanned text
	 */
	public final static Spannable getSpannedText(String toBeSpannedText,
			String completeText, int flag) {
		Spannable wordtoSpan = new SpannableString(completeText);
		if (flag == 0)
			wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0,
					toBeSpannedText.length(), Spannable.SPAN_POINT_POINT);
		else
			wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD),
					toBeSpannedText.length(), completeText.length() - 1,
					Spannable.SPAN_POINT_POINT);
		return wordtoSpan;
	}

	/**
	 * Replace newlines with breaks.
	 *
	 * @param source the source
	 * @return the string
	 */
	public static String replaceNewlinesWithBreaks(String source) {
		return source != null ? source.replaceAll("(?:\n|\r\n)", "<br>") : "";
	}

	/**
	 * Gets the styled text from html.
	 *
	 * @param source the source
	 * @return the styled text from html
	 */
	// remove HTML tags but preserve supported HTML text styling (if there is
	// any)
	public static CharSequence getStyledTextFromHtml(String source) {
		return android.text.Html.fromHtml(replaceNewlinesWithBreaks(source));
	}

	/**
	 * Format currency locally.
	 *
	 * @param amount the amount
	 * @return the string
	 */
	public static String roundAndFormatCurrency(String amount) {
		double doubleAmount = Double.parseDouble(amount);
		double finalValue = (double) Math.round(doubleAmount * 100) / 100;
		String amountTxt = (String.format("%,.2f", finalValue));
		return amountTxt;
		
	}
	
	/**
	 * 
	 * @param currency
	 * @return
	 */
	public static String formatStringCurrencyAddNorwegianCode(String currency) {
		BigInteger number = new BigInteger(currency);
		String amountTxt = (String.format("%,d", number));// .replace(',',' ')
		// Add code
		StringBuffer buffer = new StringBuffer();
		buffer.append("NOK ");
		buffer.append(amountTxt);
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param amount
	 * @return
	 */
	public static String removeCurrencyFormat(String amount) {
		String finalTxt = amount.replace(",", "").replace("NOK", "").trim();
		return finalTxt;
	}
	/**
	 * Gets the aplhabets input filter.
	 *
	 * @return the aplhabets input filter
	 */
	public static InputFilter getAplhabetsInputFilter() {
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (source.charAt(i) != ' '
							&& !Character.isLetter(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};
		return filter;
	}

	/**
	 * Gets the aplhabets and numbers input filter.
	 *
	 * @return the aplhabets and numbers input filter
	 */
	public static InputFilter getAplhabetsAndNumbersInputFilter() {
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (source.charAt(i) != ' '
							&& !Character.isLetterOrDigit(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};
		return filter;
	}

	/**
	 * Gets the numbers input filter.
	 *
	 * @return the numbers input filter
	 */
	public static InputFilter getNumbersInputFilter() {
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isDigit(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};
		return filter;
	}
}
