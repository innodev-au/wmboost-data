package au.com.innodev.wmboost.data;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class TestUtil {

	public static IData newIDataWithValue(Object value) {
		IData idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "value1", value);
		cursor.destroy();

		return idata;
	}
}
