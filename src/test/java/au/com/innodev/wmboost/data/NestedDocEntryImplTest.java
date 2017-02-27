package au.com.innodev.wmboost.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.preset.Documents;

public class NestedDocEntryImplTest {

	@Test
	public void testPutNew() {
		Document document = Documents.create();
		Document nestedDoc = document.docEntry("nested1").putNew();
		nestedDoc.entry("number").put(5);
		
		IData topIData = document.getIData();
		Object nestedIDataObj = IDataUtil.get(topIData.getCursor(), "nested1");
		assertTrue(nestedIDataObj instanceof IData);
		
		IData nestedIData = ((IData)nestedIDataObj);
		assertEquals(5, IDataUtil.get(nestedIData.getCursor(), "number"));
	}

}
