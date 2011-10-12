package rdrei.net.android.wakimail.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.rdrei.android.wakimail.wak.WAKDateParser;

import org.junit.Assert;
import org.junit.Test;

public class WAKDateParserTest {
	
	@Test
	public void testParser() {
		String dateString = "11.10.2011, 09:08";
		Calendar date = WAKDateParser.parse(dateString);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		CharSequence formatted = format.format(date.getTime());
		Assert.assertEquals("2011-10-11 09:08", formatted);
	}
}
