package au.com.innodev.wmboost.data;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.wm.data.IData;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.preset.Documents;

public class DocumentImplTest {

	@Test
	public void testToString()
	{
		IData iData = IDataFactory.create();
		IDataUtil.put(iData.getCursor(), "x", 1);
		IDataUtil.put(iData.getCursor(), "y", 2);
		
		// Assumes one possible implementation class
		DocumentImpl document = (DocumentImpl)Documents.wrap(iData );
		
		String str = document.toString();
		assertTrue(str.contains("2 entries"));
	}
}
