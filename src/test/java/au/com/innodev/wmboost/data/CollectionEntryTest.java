package au.com.innodev.wmboost.data;

import static au.com.innodev.wmboost.data.TestUtil.newIDataWithValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.wm.data.IData;
import com.wm.data.IDataFactory;

import au.com.innodev.wmboost.data.preset.DocumentFactories;

public class CollectionEntryTest {
	private final DocumentFactory docFactory = DocumentFactories.getDefault();
	
	@Test
	public void testIntegersFromStringArrayWithGetValOrEmpty() {
		String[] originalValue = new String[] { "1", "2" };
		List<Integer> expected = Lists.newArrayList(1, 2);

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.intsEntry("value1").getValOrEmpty());
	}
	
	@Test
	public void testIntegersFromStringArrayWithGetValOrEmpty_ReturnDefault() {
		String[] entryValue = null;

		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		assertEquals(0, document.intsEntry("value1").getValOrEmpty(NullValHandling.RETURN_DEFAULT).size());
	}
	
	@Test
	public void testIntegersFromStringArrayWithGetValOrEmpty_ReturnNull() {
		String[] entryValue = null;

		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);
		assertNull(document.intsEntry("value1").getValOrEmpty(NullValHandling.RETURN_NULL));
	}
	
	@Test
	public void testIntegersFromStringArrayWithGetValOrEmpty_FailONNull() {
		String[] entryValue = null;

		IData idata = newIDataWithValue(entryValue);

		Document document = docFactory.wrap(idata);

		try {
			document.intsEntry("value1").getValOrEmpty(NullValHandling.FAIL);
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			assertTrue(e.getMessage().contains("value1"));
		}
	}
	
	
	@Test
	public void testEmpty_GetValOrEmpty() {
		List<Integer> expected = Lists.newArrayList();

		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.intsEntry("inexistentEntry").getValOrEmpty());
	}
	
	@Test
	public void testGetNonEmptyVal_withItems() {
		String[] originalValue = new String[] { "1", "2" };
		List<Integer> expected = Lists.newArrayList(1, 2);

		IData idata = newIDataWithValue(originalValue);
		
		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.intsEntry("value1").getNonEmptyVal());
	}
	
	@Test
	public void testGetNonEmptyVal_withNonExistent() {
		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		try {
			document.intsEntry("inexistentEntry").getNonEmptyVal();
			fail();
		}
		catch (InexistentEntryException e) {
			// test succeeded
		}		
	}
	
	@Test
	public void testGetNonEmptyVal_withEmpty() {
		String[] entryValue = new String[] {};
		IData idata = newIDataWithValue(entryValue);
	
		Document document = docFactory.wrap(idata);
		try {
			document.intsEntry("value1").getNonEmptyVal();
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			// test succeeded
		}		
	}

	@Test
	public void testGetValOrDefault_returnDefault() {		
		List<Integer> expected = Lists.newArrayList(9);

		String[] entryValue = null;
		
		IData idata = newIDataWithValue(entryValue);
		Document document = docFactory.wrap(idata);

		assertEquals(expected, document.intsEntry("inexistentEntry").getValOrDefault(Lists.newArrayList(9)));
	}
}
