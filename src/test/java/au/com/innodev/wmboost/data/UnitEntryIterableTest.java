package au.com.innodev.wmboost.data;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;

import au.com.innodev.wmboost.data.preset.Documents;

public class UnitEntryIterableTest {

	@Test
	public void testDocumentAndListConversion() {
		IData nested1 = TestUtil.newIDataWithValue("nested1");
		IData nested2 = TestUtil.newIDataWithValue("nested2");
		
		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		cursor.insertAfter("string", "x");
		cursor.insertAfter("intArray", new Integer[] {3,5});
		
		cursor.insertAfter("iDataArray", new IData[] {nested1, nested2});
		
		Document document = Documents.wrap(idata);
		UnitEntryIterable iterable = new UnitEntryIterable(document);
		Iterator<KeyValue> it = iterable.iterator();
		
		assertTrue(it.hasNext());
		KeyValue keyValue0 = it.next();
		assertEquals("string", keyValue0.getKey());
		assertEquals("x", keyValue0.getValue());
		
		assertTrue(it.hasNext());
		KeyValue keyValue1 = it.next();
		assertEquals(keyValue1.getKey(), "intArray");
		Object value1 = keyValue1.getValue();
		assertTrue(List.class.isInstance(value1));
		assertEquals(3, ((List<?>)value1).get(0));
		assertEquals(5, ((List<?>)value1).get(1));
		
		assertTrue(it.hasNext());
		KeyValue keyValue2 = it.next();
		assertEquals(keyValue2.getKey(), "iDataArray");
		Object value2 = keyValue2.getValue();
		assertTrue(List.class.isInstance(value2));
		
		Object nestedDoc1 = ((List<?>)value2).get(0);
		assertTrue(Document.class.isInstance(nestedDoc1));
		assertEquals("nested1", ((Document)nestedDoc1).entry("value1").getVal());
		
		Object nestedDoc2 = ((List<?>)value2).get(1);
		assertTrue(Document.class.isInstance(nestedDoc2));
		assertEquals("nested2", ((Document)nestedDoc2).entry("value1").getVal());
		
		assertFalse(it.hasNext());
		
	}

}
