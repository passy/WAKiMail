package net.rdrei.android.wakimail.wak;

import java.util.Calendar;

/**
 * Parser for the date format used on the WAK homepage, e.g.
 * "11.10.2011, 09:08".
 * @author pascal
 *
 */
public class WAKDateParser {
	public static Calendar parse(String date) {
		Calendar result = Calendar.getInstance();
		
		String[] parts = date.split(", ");
		String[] datePart = parts[0].split("\\.");
		String[] timePart = parts[1].split(":");
		
		// Holy freaking shit. I always thought Python's datetime library
		// got some serious issues, but this is just pure insanity. Who
		// would design such a fucking horrible API? You have to remember
		// that this is even the second try of designing a usable date API as
		// they failed with util.Date already.
		result.set(Calendar.DAY_OF_MONTH, Integer.valueOf(datePart[0]));
		// Wow, seriously? Months range from 0 to 11. Of course, everything
		// else is just as usual.
		result.set(Calendar.MONTH, Integer.valueOf(datePart[1]) - 1);
		result.set(Calendar.YEAR, Integer.valueOf(datePart[2]));
		result.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timePart[0]));
		result.set(Calendar.MINUTE, Integer.valueOf(timePart[1]));
		
		return result;
	}
}
