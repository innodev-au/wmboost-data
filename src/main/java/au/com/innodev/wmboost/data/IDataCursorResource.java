/**
 * Copyright 2017 Innodev
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package au.com.innodev.wmboost.data;

import java.io.Closeable;

import com.wm.data.IData;
import com.wm.data.IDataCursor;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>A cursor resource
 */
class IDataCursorResource implements Closeable {
	private IDataCursor cursor;
	
	public IDataCursorResource(IData iData) {
		Preconditions.checkNotNull(iData, "iData cannot be null");
		cursor = iData.getCursor();
	}
	
	public IDataCursor getCursor() {			
		return cursor;
	}

	@Override
	public void close() {
		cursor.destroy();
	}

}
