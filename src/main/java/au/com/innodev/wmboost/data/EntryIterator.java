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
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.springframework.core.convert.ConversionService;

import com.wm.data.IDataCursor;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * An entry iterator
 */
class EntryIterator implements Iterator<KeyValue>, Closeable {

	private final ConversionService conversionService;
	private IDataCursor cursor;
	
	EntryIterator(DocumentImpl document) {
		Preconditions.checkNotNull(document, "document cannot be null");
		this.cursor = document.getIData().getCursor();
		this.conversionService =  document.getInternalConversionService();
	}

	@Override
	public boolean hasNext() {
		ensureNotClosed();
		return cursor.hasMoreData();
	}
	
	@Override
	public KeyValue next() {
		ensureNotClosed();
		boolean lastNotAlreadyVisited = cursor.next();
		
		if (! lastNotAlreadyVisited) {
			throw new NoSuchElementException("Iterator had already reached last element");
		}
		
		String key = cursor.getKey();
		Object value = EntryUtil.normaliseValueForGet(cursor.getValue(), conversionService);
		return new ImmutableKeyValue(key, value);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove operation is not supported");
	}
	
	@Override
	public void close() {
		if (cursor != null) {
			cursor.destroy();
			cursor = null;
		}
		
	}
	
	private void ensureNotClosed() {
		if (cursor == null) {
			throw new IllegalStateException("Unable to iterate over IData. Cursor has already been closed");
		}
	}
}
