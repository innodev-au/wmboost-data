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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

import com.wm.data.IData;
import com.wm.data.IDataCursor;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * {@link Document} implementation.
 */
final class DocumentImpl implements Document {

	private final DocumentConfig config;
	private final IData iData;

	DocumentImpl(IData document, DocumentConfig config) {
		this.iData = Preconditions.checkNotNull(document);
		this.config = Preconditions.checkNotNull(config);
	}

	IDataCursorResource newCursorResource() {
		return new IDataCursorResource(iData);
	}

	public boolean containsKey(String key) {
		IDataCursorResource cursorRes = newCursorResource();
		try {
			Assert.hasLength(key, "Invalid key was provided (null or empty string)");
			return cursorRes.getCursor().first(key);
		}
		finally {
			cursorRes.close();
		}
	}

	public Collection<String> getKeys() {

		Set<String> keys = new HashSet<String>();
		
		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataCursor cursor = cursorRes.getCursor();
			boolean hasMore = cursor.first();
			while (hasMore) {
				keys.add(cursor.getKey());

				hasMore = cursor.next();
			}
			return Collections.unmodifiableSet(keys);
		}
		finally {
			cursorRes.close();
		}
	}

	public IData getIData() {
		return iData;
	}

	ConversionService getInternalConversionService() {
		return config.getInternalConversionService();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
			int count = getNumRawEntries();

			sb.append("Document with ");
			sb.append(count);
			if (count == 1) {
				sb.append(" entry");
			} else {
				sb.append(" entries");
			}
			sb.append(", wrapping IData of type ");
			sb.append(iData.getClass().getCanonicalName());
			sb.append("");

		return sb.toString();
	}

	@Override
	public boolean isEmpty() {
		IDataCursorResource cursorRes = newCursorResource();
		try {
			// True if first entry not found		
			return ! cursorRes.getCursor().first();
		}
		finally {
			cursorRes.close();
		}
	}

	@Override
	public int getNumRawEntries() {
		int count = 0;
		IDataCursorResource cursorRes = newCursorResource();
		try {
			boolean existsAtCurrentPos = cursorRes.getCursor().first();
			
			while(existsAtCurrentPos) {
				count++;
				existsAtCurrentPos = cursorRes.getCursor().next();
			}
			return count;
		}
		finally {
			cursorRes.close();
		}
			
	}

	@Override
	public void clear() {
		boolean hasMore;
		
		do {
			IDataCursorResource cursorRes = newCursorResource();
			try {
				hasMore = cursorRes.getCursor().first();
				if (hasMore) {
					cursorRes.getCursor().delete();
				}				
			}
			finally {
				cursorRes.close();
			}
			
		} while (hasMore);
		
	}
	
	
	@Override
	public DocEntryIterableResource getRawEntries() {
		return new IDataEntryIterable(iData);
	}
	

	/* **************** Entries section *********************/
	// TODO add null checks
	public DocEntry<Object> entry(String key) {

		return entry(key, Object.class);
	}

	public <T> DocEntry<T> entry(String key, Class<T> type) {

		return new DocEntryImpl<T>(this, key, TypeDescriptor.valueOf(type));
	}

	public DocEntry<Document> entryOfDocument(String key) {

		return new DocEntryImpl<Document>(this, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class));
	}
	
	public DocEntry<String> entryOfString(String key) {
		return entry(key, String.class);
	}

	@Override
	public DocEntry<Boolean> entryOfBoolean(String key) {
		return entry(key, Boolean.class);
	}
	
	@Override
	public DocEntry<Integer> entryOfInteger(String key) {	
		return entry(key, Integer.class);
	}
	
	@Override
	public DocEntry<Long> entryOfLong(String key) {	
		return entry(key, Long.class);
	}
	
	@Override
	public DocEntry<Short> entryOfShort(String key) {	
		return entry(key, Short.class);
	}
	
	@Override
	public DocEntry<Double> entryOfDouble(String key) {
		return entry(key, Double.class);
	}
	
	@Override
	public DocEntry<BigDecimal> entryOfBigDecimal(String key) {
		return entry(key, BigDecimal.class);
	}
	
	@Override
	public DocEntry<Float> entryOfFloat(String key) {
		return entry(key, Float.class);
	}

	public <E> CollectionDocEntry<E> entryOfCollection(String key, Class<E> memberType) {

		return new CollectionDocEntryImpl<E>(this, key, memberType);
	}

	@Override
	public CollectionDocEntry<Object> entryOfCollection(String key) {	
		return new CollectionDocEntryImpl<Object>(this, key, Object.class);
	}
	
	public CollectionDocEntry<String> entryOfStrings(String key) {
		return entryOfCollection(key, String.class);
	}

	public CollectionDocEntry<Document> entryOfDocuments(String key) {

		return new CollectionDocEntryImpl<Document>(this, key, Document.class, IData.class);
	}
	
	@Override
	public CollectionDocEntry<Boolean> entryOfBooleans(String key) {
		return entryOfCollection(key, Boolean.class);		
	}
	
	@Override
	public CollectionDocEntry<Integer> entryOfIntegers(String key) {
		return entryOfCollection(key, Integer.class);		
	}
	
	@Override
	public CollectionDocEntry<Long> entryOfLongs(String key) {
		return entryOfCollection(key, Long.class);		
	}
	
	@Override
	public CollectionDocEntry<Short> entryOfShorts(String key) {
		return entryOfCollection(key, Short.class);		
	}
	
	@Override
	public CollectionDocEntry<Double> entryOfDoubles(String key) {
		return entryOfCollection(key, Double.class);		
	}
	
	@Override
	public CollectionDocEntry<BigDecimal> entryOfBigDecimals(String key) {
		return entryOfCollection(key, BigDecimal.class);		
	}
	
	@Override
	public CollectionDocEntry<Float> entryOfFloats(String key) {
		return entryOfCollection(key, Float.class);		
	}
	
	@Override
	public CollectionDocEntry<Character> entryOfCharacters(String key) {
		return entryOfCollection(key, Character.class);		
	}
	
	@Override
	public ScatteredEntry<Object> scatteredEntry(String key) {
		return scatteredEntry(key, Object.class);		
	}
	
	@Override
	public <T> ScatteredEntry<T> scatteredEntry(String key, Class<T> type) {		
		return new ScatteredEntryImpl<T>(this, key, TypeDescriptor.valueOf(type));
	}
	
	@Override
	public ScatteredEntry<String> scatteredEntryOfString(String key) {	
		return scatteredEntry(key, String.class);
	}
	
	@Override
	public ScatteredEntry<Document> scatteredEntryOfDocument(String key) {
		return new ScatteredEntryImpl<Document>(this, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class));
	}
	
}
