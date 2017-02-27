package au.com.innodev.wmboost.data;

import static au.com.innodev.wmboost.data.TestUtil.newIDataWithValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.wm.data.IData;

import au.com.innodev.wmboost.data.preset.DocumentFactories;

public class ItemEntryTest {
	private final DocumentFactory docFactory = DocumentFactories.getDefault();
	
	@Test
	public void testGetValOrNull_RegularVal() {
		Integer expected = 7;
		String entryValue = "7";
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertEquals(expected, document.intEntry("value1").getValOrNull());
	}
	
	@Test
	public void testGetValOrNull_NullVal() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertNull(document.intEntry("value1").getValOrNull());
	}
	
	@Test
	public void testGetValOrNull_NullVal_FailOnNull() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		try {
			document.intEntry("value1").getValOrNull(NullValHandling.FAIL);
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			assertTrue(e.getMessage().contains("value1"));
		}
	}
	
	@Test
	public void testGetValOrNull_NullVal_returnNull() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertNull(document.intEntry("value1").getValOrNull(NullValHandling.RETURN_NULL));
	}
	
	@Test
	public void testGetValOrNull_NullVal_returnDefault() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertNull(document.intEntry("value1").getValOrNull(NullValHandling.RETURN_DEFAULT));
	}
	
		
	@Test
	public void testGetValOrDefault_RegularVal() {
		Integer expected = 7;
		String entryValue = "7";
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertEquals(expected, document.intEntry("value1").getValOrDefault(9));
	}
	
	@Test
	public void testGetValOrDefault_NullVal() {
		Integer expected = 9;
		
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertEquals(expected, document.intEntry("value1").getValOrDefault(9));
	}
	
	@Test
	public void testGetValOrDefault_NullVal_FailOnNull() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		try {
			document.intEntry("value1").getValOrDefault(9, NullValHandling.FAIL);
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			assertTrue(e.getMessage().contains("value1"));
		}
	}
	
	@Test
	public void testGetValOrDefault_NullVal_returnNull() {
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertNull(document.intEntry("value1").getValOrDefault(9, NullValHandling.RETURN_NULL));
	}
	
	@Test
	public void testGetValOrDefault_NullVal_returnDefault() {
		Integer expected = 9;
		
		String entryValue = null;
		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		
		assertEquals(expected, document.intEntry("value1").getValOrDefault(9, NullValHandling.RETURN_DEFAULT));
	}

}
