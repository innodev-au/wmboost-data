import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.wm.data.IData;

import au.com.innodev.wmboost.data.Document;
import au.com.innodev.wmboost.data.TestUtil;
import au.com.innodev.wmboost.data.UnexpectedEntryValueException;
import au.com.innodev.wmboost.data.preset.Documents;

public class StringEntryImplTest {

	@Test
	public void testGetNonBlank_withText() {
		String expected = "hello!";
		IData iData= TestUtil.newIDataWithValue("hello!");
		Document document = Documents.wrap(iData);
		
		assertEquals(expected, document.entryOfString("value1").getNonBlankVal());
	}
	
	@Test
	public void testGetNonBlank_empty() {
		IData iData= TestUtil.newIDataWithValue("");
		Document document = Documents.wrap(iData);
		
		try {
			document.entryOfString("value1").getNonBlankVal();
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			// success
		}
	}
	
	@Test
	public void testGetNonBlank_whitespace() {
		IData iData= TestUtil.newIDataWithValue("   ");
		Document document = Documents.wrap(iData);
		
		try {
			document.entryOfString("value1").getNonBlankVal();
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			// success
		}
	}

	@Test
	public void testGetNonEmpty_withText() {
		String expected = "hello!";
		IData iData= TestUtil.newIDataWithValue("hello!");
		Document document = Documents.wrap(iData);
		
		assertEquals(expected, document.entryOfString("value1").getNonEmptyVal());
	}
	
	@Test
	public void testGetNonEmpty_empty() {
		IData iData= TestUtil.newIDataWithValue("");
		Document document = Documents.wrap(iData);
		
		try {
			document.entryOfString("value1").getNonEmptyVal();
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			// success
		}
	}
}
