package au.com.innodev.wmboost.data;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

import static au.com.innodev.wmboost.data.TestUtil.newIDataWithValue;
import au.com.innodev.wmboost.data.support.DocumentFactories;

public class ScatteredEntryTest {

	private final DocumentFactory docFactory = DocumentFactories.getDefault();
	
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
		Collection<String> actual = res.scatteredEntryOfStrings("item").getValOrEmpty();
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
		Collection<Document> actual = res.scatteredEntryOfDocuments("item").getValOrEmpty();
		List<Document> actualList = Lists.newArrayList(actual);

		assertEquals(3, actual.size());
		assertEquals("a", actualList.get(0).entry("value1").getVal());
		assertEquals("b", actualList.get(1).entry("value1").getVal());
		assertEquals("c", actualList.get(2).entry("value1").getVal());
	}
	
	@Test
	public void testPut() {
		List<String> list = Lists.newArrayList("Hello", "World");
		
		Document doc = docFactory.create();
		doc.scatteredEntryOfStrings("words").put(list);
		IData iData = doc.getIData();
		
		IDataCursor cursor = iData.getCursor();
		
		assertEquals(2, IDataUtil.size(cursor));
		cursor.first();
		assertEquals("words", cursor.getKey());
		assertEquals("Hello", cursor.getValue());
		
		cursor.next();
		assertEquals("words", cursor.getKey());
		assertEquals("World", cursor.getValue());
		
	}
	
	/*
	 * Note: currently, put always places values at the end 
	 */
	@Test
	public void testPutInOrder() {
		List<String> list = Lists.newArrayList("Keep", "Me", "In", "Order");
		
		IData iData = IDataFactory.create();
		{
		IDataCursor cursor = iData.getCursor();
		cursor.insertAfter("first", "A");
		cursor.insertAfter("words", "Word to replace");
		cursor.insertAfter("middle", "M");
		cursor.insertAfter("words", "Another word to replace");
		cursor.insertAfter("last", "Z");
		}
		Document doc = docFactory.wrap(iData);
		doc.scatteredEntryOfStrings("words").put(list);
		
		IDataCursor cursor2 = doc.getIData().getCursor();
		
		assertEquals(3+4, IDataUtil.size(cursor2));
		cursor2.first();
		assertEquals("first", cursor2.getKey());
		assertEquals("A", cursor2.getValue());
		
		cursor2.next();
		assertEquals("middle", cursor2.getKey());
		assertEquals("M", cursor2.getValue());
		
		cursor2.next();
		assertEquals("last", cursor2.getKey());
		assertEquals("Z", cursor2.getValue());
		
		cursor2.next();
		assertEquals("words", cursor2.getKey());
		assertEquals("Keep", cursor2.getValue());
		
		cursor2.next();
		assertEquals("words", cursor2.getKey());
		assertEquals("Me", cursor2.getValue());
		
		cursor2.next();
		assertEquals("words", cursor2.getKey());
		assertEquals("In", cursor2.getValue());
		
		cursor2.next();
		assertEquals("words", cursor2.getKey());
		assertEquals("Order", cursor2.getValue());
		
	}

	/*
	 * Note: currently, put always places values at the end 
	 */
	@Test
	public void testPutConvertedInOrder() {
		List<Integer> list = Lists.newArrayList(4, 3, 2, 1);
		
		IData iData = IDataFactory.create();
		{
		IDataCursor cursor = iData.getCursor();
		cursor.insertAfter("first", "A");
		cursor.insertAfter("numbers", "8");
		cursor.insertAfter("middle", "M");
		cursor.insertAfter("numbers", "9");
		cursor.insertAfter("last", "Z");
		}
		Document doc = docFactory.wrap(iData);
		doc.scatteredEntryOfStrings("numbers").putConverted(list);
		
		IDataCursor cursor2 = doc.getIData().getCursor();
		
		assertEquals(3+4, IDataUtil.size(cursor2));
		cursor2.first();
		assertEquals("first", cursor2.getKey());
		assertEquals("A", cursor2.getValue());
		
		cursor2.next();
		assertEquals("middle", cursor2.getKey());
		assertEquals("M", cursor2.getValue());
		
		cursor2.next();
		assertEquals("last", cursor2.getKey());
		assertEquals("Z", cursor2.getValue());
		
		cursor2.next();
		assertEquals("numbers", cursor2.getKey());
		assertEquals("4", cursor2.getValue());
		
		cursor2.next();
		assertEquals("numbers", cursor2.getKey());
		assertEquals("3", cursor2.getValue());
		
		cursor2.next();
		assertEquals("numbers", cursor2.getKey());
		assertEquals("2", cursor2.getValue());
		
		cursor2.next();
		assertEquals("numbers", cursor2.getKey());
		assertEquals("1", cursor2.getValue());
		
	}
	
	@Test
	public void testRemove() {
		IData iData = IDataFactory.create();
		{
		IDataCursor cursor = iData.getCursor();
		cursor.insertAfter("first", "A");
		cursor.insertAfter("words", "Word to replace");
		cursor.insertAfter("middle", "M");
		cursor.insertAfter("words", "Another word to replace");
		cursor.insertAfter("last", "Z");
		}
		Document doc = docFactory.wrap(iData);
		doc.scatteredEntryOfStrings("words").remove();
		
		IDataCursor cursor2 = doc.getIData().getCursor();
		
		assertEquals(3, IDataUtil.size(cursor2));
		cursor2.first();
		assertEquals("first", cursor2.getKey());
		assertEquals("A", cursor2.getValue());
		
		cursor2.next();
		assertEquals("middle", cursor2.getKey());
		assertEquals("M", cursor2.getValue());
		
		cursor2.next();
		assertEquals("last", cursor2.getKey());
		assertEquals("Z", cursor2.getValue());
		
	}

}
