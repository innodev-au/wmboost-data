package au.com.innodev.wmboost.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;

import com.google.common.collect.Lists;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.data.MBoolean;
import com.wm.data.MInteger;
import com.wm.data.MLong;

import au.com.innodev.wmboost.data.CollectionDocEntry;
import au.com.innodev.wmboost.data.DocEntry;
import au.com.innodev.wmboost.data.DocEntryIterableResource;
import au.com.innodev.wmboost.data.Document;
import au.com.innodev.wmboost.data.DocumentFactory;
import au.com.innodev.wmboost.data.InexistentEntryException;
import au.com.innodev.wmboost.data.KeyValue;
import au.com.innodev.wmboost.data.UnexpectedEntryValueException;
import au.com.innodev.wmboost.data.support.DocumentFactories;

public class DocumentTest {

	private final DocumentFactory docFactory = DocumentFactories.getDefault();

	@Test
	public void testInsertionDeletion() {
		Document document = docFactory.create();

		assertTrue(document.isEmpty());
		assertEquals(document.getNumRawEntries(), 0);
		assertTrue(CollectionUtils.isEqualCollection(Lists.newArrayList(), document.getKeys()));

		document.entry("val1").put("MyVal1");
		assertFalse(document.isEmpty());
		assertEquals(1, document.getNumRawEntries());
		assertTrue(CollectionUtils.isEqualCollection(Lists.newArrayList("val1"), document.getKeys()));

		document.entry("val2").put("MyVal2");
		assertFalse(document.isEmpty());
		assertEquals(2, document.getNumRawEntries());
		assertTrue(CollectionUtils.isEqualCollection(Lists.newArrayList("val1", "val2"), document.getKeys()));

		document.entry("val1").remove();
		assertFalse(document.isEmpty());
		assertEquals(1, document.getNumRawEntries());
		assertTrue(CollectionUtils.isEqualCollection(Lists.newArrayList("val2"), document.getKeys()));

		document.entry("val2").remove();
		assertTrue(document.isEmpty());
		assertEquals(0, document.getNumRawEntries());
		assertTrue(CollectionUtils.isEqualCollection(Lists.newArrayList(), document.getKeys()));
	}

	@Test
	public void testGetValue() {
		Integer originalValue = 5;
		Integer expected = 5;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1").getValOrNull());
	}

	@Test
	public void testIntegerFromString() {
		String originalValue = "3";
		Integer expected = 3;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}

	@Test
	public void testIntegerFromLong() {
		Long originalValue = Long.valueOf(3);
		Integer expected = 3;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}

	@Test
	public void testIntegerFromInteger() {
		Integer originalValue = Integer.valueOf(3);
		Integer expected = 3;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}

	@Test
	public void testIntegerFromNull() {
		Integer originalValue = null;
		Integer expected = null;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}

	@Test
	public void testIntegerFromDecimal() {
		String originalValue = "3.14";

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		try {
			document.entry("value1", Integer.class).getValOrNull();
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().toLowerCase().contains("integer"));
			assertTrue(e.getMessage().contains("3.14"));
			assertTrue(e.getMessage().contains("value1"));
		}
	}

	@Test
	public void testBooleanFromTrue() {
		String originalValue = "true";
		Boolean expected = true;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Boolean.class).getValOrNull());
	}

	@Test
	public void testBooleanFromFalse() {
		String originalValue = "false";
		Boolean expected = false;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Boolean.class).getValOrNull());
	}

	@Test
	public void testBooleanFromYes() {
		String originalValue = "yes";
		Boolean expected = true;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Boolean.class).getValOrNull());
	}

	@Test
	public void testBooleanFromInvalidString() {
		String originalValue = "notABool";

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		try {
			document.entry("value1", Boolean.class).getValOrNull();
			fail();
		} catch (Exception e) {
			assertTrue(e.getMessage().toLowerCase().contains("boolean"));
			assertTrue(e.getMessage().contains("notABool"));
			assertTrue(e.getMessage().contains("value1"));
		}
	}

	@Test
	public void testBooleanFromEmptyString() {
		String originalValue = "";
		Boolean expectedValue = null;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expectedValue, document.entry("value1", Boolean.class).getValOrNull());
	}

	@Test
	public void testMBooleanToBoolean() {
		MBoolean originalValue = new MBoolean(true);
		Boolean expected = Boolean.TRUE;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Boolean.class).getValOrNull());
	}

	@Test
	public void testEmptyString() {
		String originalValue = "";
		String expectedValue = "";
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expectedValue, document.entryOfString("value1").getValOrNull());
	}

	@Test
	public void testAtomicLongToInteger() {
		AtomicLong originalValue = new AtomicLong(3);
		Integer expected = 3;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}
	
	@Test
	public void testBigIntegerToInteger() {
		Integer originalValue = 345;
		BigInteger expected = new BigInteger("345");
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", BigInteger.class).getValOrNull());
	}

	@Test
	public void testMLongToIntger() {
		MLong originalValue = new MLong(345);
		Integer expected = 345;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Integer.class).getValOrNull());
	}

	@Test
	public void testLongToMInteger() {
		Long originalValue = 67L;
		int expected = 67;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		MInteger value = document.entry("value1", MInteger.class).getValOrNull();
		assertEquals(expected, value.intValue());
	}

	@Test
	public void testMLongToString() {
		Long originalValue = 678L;
		String expected = "678";

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfString("value1").getValOrNull());
	}

	@Test
	public void testStringToMLong() {
		String originalValue = "91";
		long expected = 91;

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", MLong.class).getValOrNull().longValue());
	}

	private static class Test1 {
		
	}
	
	@Test
	public void testStringFromCustomObject() {
		Test1 originalValue = new Test1();
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		try {
			document.entryOfString("value1").getValOrNull();
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().toLowerCase().contains("string"));
			assertTrue(e.getMessage().contains("value1"));
		}
	}

	@Test
	public void testStringFromInteger() {
		Integer originalValue = 3;
		String expected = "3";
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfString("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfIBoolean() {
		String originalValue = "true";
		Boolean expected = true;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfBoolean("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfInteger() {
		String originalValue = "3";
		Integer expected = 3;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfInteger("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfLong() {
		String originalValue = "3";
		Long expected = 3L;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfLong("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfShort() {
		String originalValue = "3";
		Short expected = 3;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfShort("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfFloat() {
		String originalValue = "3.14";
		Float expected = 3.14F;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfFloat("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfDouble() {
		String originalValue = "3.14";
		Double expected = 3.14;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfDouble("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfBigDecimal() {
		String originalValue = "3.14";
		BigDecimal expected = BigDecimal.valueOf(3.14);
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfBigDecimal("value1").getValOrNull());
	}
	
	@Test
	public void testEntryOfCharacter() {
		String originalValue = "W";
		Character expected = 'W';
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Character.class).getValOrNull());
	}	

	@Test
	public void testEntryOfEnum() {
		String originalValue = "CEILING";
		RoundingMode expected = RoundingMode.CEILING;
		
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", RoundingMode.class).getVal());
		
	}
	
	@Test
	public void testEntryOfCollection() {
		Object[] originalValues = {"Hello", 5};
		List<Object> expected = Lists.<Object>newArrayList("Hello",5);;
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfCollection("value1").getVal());
	}
	
	@Test
	public void testEntryOfBooleans() {
		String[] originalValues = {"true", "false", "true"};
		List<Boolean> expected = Lists.newArrayList(true, false, true);;
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfBooleans("value1").getVal());
	}
	
	@Test
	public void testEntryOfIntegers() {
		String[] originalValues = {"3", "5"};
		List<Integer> expected = Lists.newArrayList(3,5);;
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfIntegers("value1").getVal());
	}
	
	@Test
	public void testEntryOfLongs() {
		String[] originalValues = {"3", "5"};
		List<Long> expected = Lists.newArrayList(3L,5L);;
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfLongs("value1").getVal());
	}
	
	@Test
	public void testEntryOfShorts() {
		String[] originalValues = {"3", "5"};
		List<Short> expected = Lists.newArrayList(Short.valueOf("3"),Short.valueOf("5"));
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfShorts("value1").getVal());
	}
	
	@Test
	public void testEntryOfFloats() {
		String[] originalValues = {"3.14", "5.8"};
		List<Float> expected = Lists.newArrayList(3.14F, 5.8F);
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfFloats("value1").getVal());
	}
	
	@Test
	public void testEntryOfDoubles() {
		String[] originalValues = {"3.14", "5.8"};
		List<Double> expected = Lists.newArrayList(3.14, 5.8);
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfDoubles("value1").getVal());
	}
	
	@Test
	public void testEntryOfBigDecimals() {
		String[] originalValues = {"3.14", "5.8"};
		List<BigDecimal> expected = Lists.newArrayList(BigDecimal.valueOf(3.14), BigDecimal.valueOf(5.8));
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfBigDecimals("value1").getVal());
	}
	
	@Test
	public void testEntryOfCharacters() {
		String[] originalValues = {"A", "C", "M", "E"};
		List<Character> expected = Lists.newArrayList('A', 'C', 'M', 'E');
		IData idata = newIDataWithValue(originalValues);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfCharacters("value1").getVal());
	}
	

	
	@Test
	public void testDateToString() {
		String strValue = "2017-01-01T00:00:00.000Z";
		String expected = strValue;
		
		Date instant = DateTimeUtils.toDate(Instant.parse(strValue));
		
		IData idata = newIDataWithValue(instant);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfString("value1").getValOrNull());
	}
	
	@Test
	public void testStringToDate() {
		String strValue = "2017-01-01T00:00:00.000Z";
		Date expectedDate = DateTimeUtils.toDate(Instant.parse(strValue));
		
		IData idata = newIDataWithValue(strValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expectedDate, document.entry("value1", Date.class).getValOrNull());
	}

	

	@Test
	public void testMandatoryStringFromInteger() {
		Integer originalValue = 3;
		String expected = "3";
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfString("value1").getNonNullVal());
	}

	@Test
	public void testMandatoryStringNotPresent() {
		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		try {
			document.entryOfString("someValue").getNonNullVal();
			fail();
		} catch (InexistentEntryException e) {
			assertTrue(e.getMessage().toLowerCase().contains("entry doesn't exist"));
			assertTrue(e.getMessage().contains("someValue"));
		}
	}

	@Test
	public void testMandatoryValue() {
		Integer originalValue = 5;
		Long expected = 5L;
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entry("value1", Long.class).getNonNullVal());
	}

	@Test
	public void testMandatoryValueNotPresent() {
		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		try {
			document.entry("someValue", Integer.class).getNonNullVal();
			fail();
		} catch (InexistentEntryException e) {
			assertTrue(e.getMessage().toLowerCase().contains("entry doesn't exist"));
			assertTrue(e.getMessage().contains("someValue"));
		}
	}

	@Test
	public void testMandatoryValueNull() {
		IData idata = newIDataWithValue(null);

		Document document = docFactory.wrap(idata);
		try {
			document.entry("value1", Integer.class).getNonNullVal();
			fail();
		} catch (UnexpectedEntryValueException e) {
			assertTrue(e.getMessage().toLowerCase().contains("null value was found"));
			assertTrue(e.getMessage().contains("value1"));
		}
	}

	@Test
	public void testStringListFromStringArray() {
		Integer[] originalValue = new Integer[] { 1, 2 };
		List<String> expected = Lists.newArrayList("1", "2");

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfStrings("value1").getVal());
	}

	@Test
	public void testIntegersFromStringArray() {
		String[] originalValue = new String[] { "1", "2" };
		List<Integer> expected = Lists.newArrayList(1, 2);

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfCollection("value1", Integer.class).getVal());
	}
	
	@Test
	public void testIntegersFromStringArrayWithGetValOrEmpty() {
		String[] originalValue = new String[] { "1", "2" };
		List<Integer> expected = Lists.newArrayList(1, 2);

		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfIntegers("value1").getValOrEmpty());
	}
	
	@Test
	public void testEmpty_GetValOrEmpty() {
		List<Integer> expected = Lists.newArrayList();

		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfIntegers("inexistentEntry").getValOrEmpty());
	}
	
	@Test
	public void testGetNonEmptyVal_withItems() {
		String[] originalValue = new String[] { "1", "2" };
		List<Integer> expected = Lists.newArrayList(1, 2);

		IData idata = newIDataWithValue(originalValue);
		
		Document document = docFactory.wrap(idata);
		assertEquals(expected, document.entryOfIntegers("value1").getNonEmptyVal());
	}
	
	@Test
	public void testGetNonEmptyVal_withNonExistent() {
		IData idata = IDataFactory.create();

		Document document = docFactory.wrap(idata);
		try {
			document.entryOfIntegers("inexistentEntry").getNonEmptyVal();
			fail();
		}
		catch (InexistentEntryException e) {
			// test succeeded
		}		
	}
	
	@Test
	public void testGetNonEmptyVal_withEmpty() {
		String[] originalValue = new String[] {};
		IData idata = newIDataWithValue(originalValue);

		Document document = docFactory.wrap(idata);
		try {
			document.entryOfIntegers("value1").getNonEmptyVal();
			fail();
		}
		catch (UnexpectedEntryValueException e) {
			// test succeeded
		}		
	}
	

	@Test
	public void testgetValueWithUnconvertableType() {
		IData idata = newIDataWithValue("hello");
		Document document = docFactory.wrap(idata);
		try {
			document.entry("value1", BufferedReader.class).getVal();
			fail();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().toLowerCase().contains("bufferedreader"));
			assertTrue(e.getMessage().contains("value1"));
		}
	}

	@Test
	public void testNestedResource() {
		IData nestedidata = newIDataWithValue("inside!");

		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "subDocument", nestedidata);
		cursor.destroy();

		Document pipelineRes = docFactory.wrap(idata);
		Document nestedRes = pipelineRes.entryOfDocument("subDocument").getVal();
		{
			assertEquals("inside!", nestedRes.entryOfString("value1").getValOrNull());
		}
	}

	@Test
	public void testOptionalNestedResource() {
		IData idata = newIDataWithValue("someValue");

		Document res = docFactory.wrap(idata);
		assertNull(res.entryOfDocument("inexsitentDoc").getValOrNull());
	}

	@Test
	public void testGetAllValuesForKey() {
		List<String> expected = Lists.newArrayList("a", "b", "c");

		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		cursor.insertAfter("something", "x");
		cursor.insertAfter("item", "a");
		cursor.insertAfter("item", "b");
		cursor.insertAfter("somethingElse", "y");
		cursor.insertAfter("item", "c");
		cursor.insertAfter("somethingDifferent", "z");

		Document res = docFactory.wrap(idata);
		Collection<String> actual = res.scatteredEntryOfString("item").getValues();
		assertTrue(CollectionUtils.isEqualCollection(expected, actual));
	}

	@Test
	public void testGetAllDocValuesForKey() {
		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		cursor.insertAfter("something", newIDataWithValue("x"));
		cursor.insertAfter("item", newIDataWithValue("a"));
		cursor.insertAfter("item", newIDataWithValue("b"));
		cursor.insertAfter("somethingElse", newIDataWithValue("y"));
		cursor.insertAfter("item", newIDataWithValue("c"));
		cursor.insertAfter("somethingDifferent", newIDataWithValue("z"));

		Document res = docFactory.wrap(idata);
		Collection<Document> actual = res.scatteredEntryOfDocument("item").getValues();
		List<Document> actualList = Lists.newArrayList(actual);

		assertEquals(3, actual.size());
		assertEquals("a", actualList.get(0).entry("value1").getVal());
		assertEquals("b", actualList.get(1).entry("value1").getVal());
		assertEquals("c", actualList.get(2).entry("value1").getVal());
	}

	@Test
	public void testPutAsString() {
		IData idata = IDataFactory.create();
		Document document = docFactory.wrap(idata);
		document.entryOfString("value1").putConverted(3);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		assertEquals("3", IDataUtil.get(cursor, "value1"));
		assertEquals(1, IDataUtil.size(cursor));
	}

	@Test
	public void testPut() {
		IData idata = IDataFactory.create();
		Document document = docFactory.wrap(idata);
		document.entry("value1").put(3);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		assertEquals(3, IDataUtil.get(cursor, "value1"));
		assertEquals(1, IDataUtil.size(cursor));
	}

	@Test
	public void testPutIntegerList() {
		Integer[] expected = {3, 1, 4};
		List<Integer> input = Lists.newArrayList(3, 1, 4);
		
		Document document = docFactory.create();
		document.entryOfIntegers("value1").put(input);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		Integer[] returnedValue = (Integer[]) IDataUtil.get(cursor, "value1");
		assertArrayEquals(expected, returnedValue);
	}
	
	
	@Test
	public void testPutCollectionListFromIntegerList() {
		Object[] expected = {3, 1, 4};
		List<Integer> input = Lists.newArrayList(3, 1, 4);
		
		Document document = docFactory.create();
		
		// This compiles because covariants re OK as input, e.g., store Integer elements in an Object collection entry
		document.entryOfCollection("value1").put(input);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		Object[] returnedValue = (Object[]) IDataUtil.get(cursor, "value1");
		assertArrayEquals(expected, returnedValue);
	}
	
	@Test
	public void testGetValOrDefault_Covariant() {
		List<Object> expected = Lists.<Object>newArrayList(3, 1, 4);
		List<Integer> input = Lists.newArrayList(3, 1, 4);
		
		Document document = docFactory.create();
		
		// This compiles because we accept a covariant, e.g., List<Integer> on an Object collection default value
		List<Object> returnedValue = document.entryOfCollection("value1").getValOrDefault(input);
		
		assertEquals(expected, returnedValue);
		
		// modify returning value
		returnedValue.add("A");
		
		// but ensure original list hasn't been altered - an Integer List shouldn't contain a String
		assertEquals(3, input.size()); 
		
	}
	
	/*
	 * Ensures that a an IData instance is retrieved as a Document, even through entry(Object)  
	 */
	@Test
	public void testGetDocumentFromObjectEntry() {
		IData nestedIData = newIDataWithValue("myNestedVal");
		
		IData topIData = newIDataWithValue(nestedIData);
		Document topDoc = docFactory.wrap(topIData);		
		
		Object retrievedNestedIData = topDoc.entry("value1").getVal();
		
		assertTrue(retrievedNestedIData instanceof Document);
		assertEquals("myNestedVal", ((Document) retrievedNestedIData).entry("value1").getVal());
	}
	
	/*
	 * Ensures that a an IData instance is retrieved when explicitly asking for that type  
	 */
	@Test
	public void testGetIDataFromObjectEntry() {
		IData nestedIData = newIDataWithValue("field1");
		
		IData topIData = newIDataWithValue(nestedIData);
		Document topDoc = docFactory.wrap(topIData);		
		
		Object retrievedNestedIData = topDoc.entry("value1", IData.class).getVal();
		
		assertTrue(retrievedNestedIData instanceof IData);
	}
	
	/*
	 * Ensures that a an IData[] instance is retrieved as a List of documents, even through entry(Object)  
	 */
	@Test
	public void testGetDocumentsFromObjectEntry() {
		IData nestedIData1 = newIDataWithValue("A");
		IData nestedIData2 = newIDataWithValue("B");
		IData[] nestedArray = {nestedIData1, nestedIData2};
		
		IData topIData = newIDataWithValue(nestedArray);
		Document topDoc = docFactory.wrap(topIData);		
		
		Object retrievedDocs = topDoc.entry("value1").getVal();
		
		assertTrue(retrievedDocs instanceof List);
		List<?> retrievedList = (List<?>) retrievedDocs;
				
		Object elem1 = retrievedList.get(0);		
		assertTrue(elem1 instanceof Document);
		assertEquals("A", ((Document)elem1).entry("value1").getVal());
		
		Object elem2 = retrievedList.get(1);		
		assertTrue(elem2 instanceof Document);
		assertEquals("B", ((Document)elem2).entry("value1").getVal());
	}
	
	
	
	
	@Test
	public void testPutIntegerArray() {
		Integer[] expected = {3, 1, 4};
		Integer[] input = {3, 1, 4};
		
		Document document = docFactory.create();
		document.entryOfIntegers("value1").put(input);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		Integer[] returnedValue = (Integer[]) IDataUtil.get(cursor, "value1");
		assertArrayEquals(expected, returnedValue);
	}
	
	@Test
	public void testPutConvertedIntegers() {
		String[] expected = {"3", "1", "4"};
		List<Integer> input = Lists.newArrayList(3, 1, 4);
		
		Document document = docFactory.create();
		document.entryOfStrings("value1").putConverted(input);

		// confirm value has actually been added
		IDataCursor cursor = document.getIData().getCursor();
		String[] returnedValue = (String[]) IDataUtil.get(cursor, "value1");
		assertArrayEquals(expected, returnedValue);
	}
	
	/*
	 * Ensures that a Document instance is put as an IData, even when invoking the untyped entry() method.  
	 */
	@Test
	public void testPutDocumentToObjectEntry() {
		IData idata = newIDataWithValue("field1");
		Document nested = docFactory.wrap(idata);		
		
		Document document = docFactory.create();
		document.entry("nestedDoc").put(nested);
		
		Object storedNestedDoc = IDataUtil.get(document.getIData().getCursor(), "nestedDoc");
		
		assertTrue(storedNestedDoc instanceof IData);
	}
	
	/*
	 * Ensures that list of Document instances is put as IData[], even when invoking the untyped entry() method.  
	 */
	@Test
	public void testPutDocumentsToObjectEntry() {
		IData idata1 = newIDataWithValue("A");
		Document nested1 = docFactory.wrap(idata1);		
		
		IData idata2 = newIDataWithValue("B");
		Document nested2 = docFactory.wrap(idata2);
		
		List<Object> docs = Lists.<Object>newArrayList(nested1, nested2);
		
		Document document = docFactory.create();
		document.entry("nestedDoc").put(docs);
		
		Object storedNestedDocs = IDataUtil.get(document.getIData().getCursor(), "nestedDoc");
		assertTrue(storedNestedDocs instanceof IData[]);
		
		IData[] retrievedArray = (IData[]) storedNestedDocs;
				
		assertEquals("A", IDataUtil.get(retrievedArray[0].getCursor(), "value1"));
		
		assertEquals("B", IDataUtil.get(retrievedArray[1].getCursor(), "value1"));
		
	}
	
	@Test
	public void testRemove() {
		IData idata = newIDataWithValue("anyValue");
		Document document = docFactory.wrap(idata);
		assertEquals(1, IDataUtil.size(document.getIData().getCursor()));

		// action
		document.entry("value1").remove();

		// confirm value has been removed
		assertEquals(0, IDataUtil.size(document.getIData().getCursor()));
		assertNull(IDataUtil.get(document.getIData().getCursor(), "value1"));
	}

	@Test
	public void testGetTopLevelKeys() {
		List<String> expected = Lists.newArrayList("value1", "value2");

		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "value1", "something");
		IDataUtil.put(cursor, "value2", "another one");
		cursor.destroy();

		Document document = docFactory.wrap(idata);
		CollectionUtils.isEqualCollection(expected, document.getKeys());
	}

	@Test
	public void testPresence_PresentNonNull() {
		IData idata = newIDataWithValue("Hello");
		Document document = docFactory.wrap(idata);
		DocEntry<String> value1Presence = document.entryOfString("value1");
		assertTrue(value1Presence.isAssigned());
		assertEquals("Hello", value1Presence.getVal());
	}

	@Test
	public void testPresence_PresentButNull() {
		IData idata = newIDataWithValue(null);
		Document document = docFactory.wrap(idata);
		DocEntry<String> value1Presence = document.entryOfString("value1");
		assertTrue(value1Presence.isAssigned());
		assertNull(value1Presence.getVal());
	}

	@Test
	public void testPresence_Absent() {
		IData idata = IDataFactory.create();
		Document document = docFactory.wrap(idata);
		DocEntry<String> value1Presence = document.entryOfString("inexistentKey");
		assertFalse(value1Presence.isAssigned());
	}

	@Test
	public void testNestedDoc() {

		// setup
		Document nested = docFactory.create();
		nested.entryOfString("a1").put("val-a1");
		nested.entryOfString("a2").put("val-a2");

		Document top = docFactory.create();
		top.entryOfDocument("nested").put(nested);

		// verify
		IData topIDataObj = top.getIData();
		IData topIData = (IData) topIDataObj;

		Object nestedObj = IDataUtil.get(topIData.getCursor(), "nested");
		assertTrue("Actual type was" + nestedObj.getClass(), nestedObj instanceof IData);

		IData nestedIData = (IData) nestedObj;

		assertEquals("val-a1", IDataUtil.get(nestedIData.getCursor(), "a1"));
		assertEquals("val-a2", IDataUtil.get(nestedIData.getCursor(), "a2"));
	}

	@Test
	public void testInsertNullDocument() {
		Document document = docFactory.create();
		document.entryOfDocument("nullDoc").put(null);
		assertNull(IDataUtil.get(document.getIData().getCursor(), "nullDoc"));
	}

	@Test
	public void testNestedDocs() {
		Document nestedA = docFactory.create();
		nestedA.entryOfString("a1").put("val-a1");
		nestedA.entryOfString("a2").put("val-a2");

		Document nestedB = docFactory.create();
		nestedB.entryOfString("b1").put("val-b1");
		nestedB.entryOfString("b2").put("val-b2");

		List<Document> documents = Lists.newArrayList(nestedA, nestedB);
		// Document[] docArray = documents.toArray(new Document[0]);

		Document top = docFactory.create();
		top.entryOfDocuments("table").put(documents);

		Object tableIDataObj = IDataUtil.get(top.getIData().getCursor(), "table");

		assertTrue(IData[].class.isInstance(tableIDataObj));

		IData[] iDataArray = (IData[]) tableIDataObj;
		assertEquals(2, iDataArray.length);

		assertEquals("val-a1", IDataUtil.get(iDataArray[0].getCursor(), "a1"));
		assertEquals("val-a2", IDataUtil.get(iDataArray[0].getCursor(), "a2"));

		assertEquals("val-b1", IDataUtil.get(iDataArray[1].getCursor(), "b1"));
		assertEquals("val-b2", IDataUtil.get(iDataArray[1].getCursor(), "b2"));
	}

	@Test
	public void testPresence_NestedDocs() {

		IData[] nestedIDatas = new IData[] { newIDataWithValue("nestedValue1"), newIDataWithValue("nestedValue2") };

		IData idata = newIDataWithValue(nestedIDatas);

		Document topDoc = docFactory.wrap(idata);
		CollectionDocEntry<Document> p = topDoc.entryOfDocuments("value1");
		
		assertTrue(p.isAssigned());
		
		Iterator<Document> it = p.getVal().iterator();
		Document doc1 = it.next();
		assertEquals("nestedValue1", doc1.entry("value1").getValOrNull());
		
		Document doc2 = it.next();
		assertEquals("nestedValue2", doc2.entry("value1").getValOrNull());
	}
	
	@Test
	public void testGetEntries() {
		Document document = docFactory.create();
		document.entry("z").put("1");
		document.entry("y").put("2");
		document.entry("x").put("3");
		
		DocEntryIterableResource docEntriesResource = document.getRawEntries();
	    try {
			Iterator<KeyValue> it = docEntriesResource.iterator();
			
			assertTrue(it.hasNext());
			KeyValue keyValue1 = it.next();
			assertEquals(keyValue1.getKey(), "z");
			assertEquals(keyValue1.getValue(), "1");
			
			assertTrue(it.hasNext());
			KeyValue keyValue2 = it.next();
			assertEquals(keyValue2.getKey(), "y");
			assertEquals(keyValue2.getValue(), "2");
			
			assertTrue(it.hasNext());
			KeyValue keyValue3 = it.next();
			assertEquals(keyValue3.getKey(), "x");
			assertEquals(keyValue3.getValue(), "3");
			
			assertFalse(it.hasNext());
		}
		finally {
			docEntriesResource.close();
		}
	}
	
	@Test
	public void testClear() {
		Document document = docFactory.create();
		document.entry("z").put("1");
		document.entry("y").put("2");
		document.entry("x").put("3");
		
		assertEquals(3, document.getNumRawEntries());
		assertTrue(document.getRawEntries().iterator().hasNext());
		
		document.clear();
		
		assertEquals(0, document.getNumRawEntries());
		assertFalse(document.getRawEntries().iterator().hasNext());
	}
	
	private IData newIDataWithValue(Object value) {
		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "value1", value);
		cursor.destroy();

		return idata;
	}
}