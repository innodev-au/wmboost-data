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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.internal.Preconditions;

class ScatteredEntryImpl<T> extends BaseEntry<T, T> implements ScatteredEntry<T> {

	private final TypeDescriptor accessorType;
	private final TypeDescriptor mutatorType;

	public ScatteredEntryImpl(DocumentImpl document, String key, TypeDescriptor accessorType, TypeDescriptor mutatorType, NormaliseOption normaliseOption) {
		super(document, key, normaliseOption);

		this.accessorType = Preconditions.checkNotNull(accessorType);
		this.mutatorType = mutatorType;
	}

	public ScatteredEntryImpl(DocumentImpl document, String key, TypeDescriptor typeSpec, NormaliseOption normaliseOption) {
		this(document, key, typeSpec, null, normaliseOption);
	}

	@Override
	public Collection<T> getValues() {	
		List<T> list = new ArrayList<T>();

		IDataCursorResource cursorRes = newCursorResource();
		try {
			boolean hasMore = cursorRes.getCursor().first(getKey());
			while (hasMore) {
				Object value = cursorRes.getCursor().getValue();
				T converted = convertAndNormaliseValForGet(value, accessorType);

				list.add(converted);
				hasMore = cursorRes.getCursor().next(getKey());
			}
		}
		finally {
			cursorRes.close();
		}
		return list;
	}
	
		
	@Override
	public void put(Iterable<T> values) {
		for (T value : values) {
			doPut(value);
		}
	}
	
	@Override
	public void putConverted(Iterable<?> values) {
		// FIXME: remove others - but only if conversion doesn't fail. Keep a temporary list
		for (Object value : values) {
			Object convertedValue = getConvertedValue(value, accessorType);

			doPut(convertedValue);
			
		}		
	}
	
	private void doPut(Object value) {
		Object valueToPut = convertAndNormaliseValForPut(value, mutatorType);

		// FIXME
		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataUtil.put(cursorRes.getCursor(), getKey(), valueToPut);
		}
		finally {
			cursorRes.close();
		}
	}

	@Override
	public void remove() {
		boolean hasMore;
		
		do {
			IDataCursorResource cursorRes = newCursorResource();
			try {
				hasMore = cursorRes.getCursor().first(getKey());
				if (hasMore) {
					cursorRes.getCursor().delete();
				}				
			}
			finally {
				cursorRes.close();
			}
			
		} while (hasMore);
	}
	
}
