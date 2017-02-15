package au.com.innodev.wmboost.data.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.Document;
import au.com.innodev.wmboost.data.support.Documents;

public class DocumentsTest {

	@Test
	public void testWrap() {
		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "value1", "Hello!");
		cursor.destroy();
		
		Document document = Documents.wrap(idata);
		assertTrue(document.containsKey("value1"));
		assertFalse(document.containsKey("somethingElse"));
	}
	
	@Test
	public void testCreate() {
		Document document = Documents.create();
		document.entry("value1").put("Hello!");
		
		IDataCursor cursor = document.getIData().getCursor();
		cursor.destroy();
		Object val  = IDataUtil.get(cursor, "value1");
		assertEquals("Hello!", val);
		
		
	}

}
