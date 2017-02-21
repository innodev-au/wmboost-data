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

import static au.com.innodev.wmboost.data.NormaliseOption.DONT_NORMALISE;
import static au.com.innodev.wmboost.data.NormaliseOption.MAY_NORMALISE;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
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

		// Using LinkedHashSet to preserve insertion order
		Set<String> keys = new LinkedHashSet<String>();
		
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
	public ItemEntry<Object> entry(String key) {
		return new ItemEntryImpl<Object>(this, key, TypeDescriptor.valueOf(Object.class), MAY_NORMALISE);
	}

	public <T> ItemEntry<T> entry(String key, Class<T> type) {
		Preconditions.checkNotNull(type);
		
		
		if (Document.class.isAssignableFrom(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) entryOfDocument(key);
			return entry;
		}
		else if (Object.class.equals(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) entry(key);
			return entry;
		}
		else if (Collection.class.isAssignableFrom(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) entryOfCollection(key);
			return entry;
		}		
		else {
			return specificTypeEntry(key, type);
		}		
		
	}
	
	public ItemEntry<Document> entryOfDocument(String key) {
		return new ItemEntryImpl<Document>(this, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class), DONT_NORMALISE);
	}
	
	private <T> ItemEntry<T> specificTypeEntry(String key, Class<T> type) {
		return new ItemEntryImpl<T>(this, key, TypeDescriptor.valueOf(type), DONT_NORMALISE);
	}
	
	public ItemEntry<String> entryOfString(String key) {
		return specificTypeEntry(key, String.class);
	}

	@Override
	public ItemEntry<Boolean> entryOfBoolean(String key) {
		return specificTypeEntry(key, Boolean.class);
	}
	
	@Override
	public ItemEntry<Integer> entryOfInteger(String key) {	
		return specificTypeEntry(key, Integer.class);
	}
	
	@Override
	public ItemEntry<Long> entryOfLong(String key) {	
		return specificTypeEntry(key, Long.class);
	}
	
	@Override
	public ItemEntry<Short> entryOfShort(String key) {	
		return specificTypeEntry(key, Short.class);
	}
	
	@Override
	public ItemEntry<Double> entryOfDouble(String key) {
		return specificTypeEntry(key, Double.class);
	}
	
	@Override
	public ItemEntry<BigDecimal> entryOfBigDecimal(String key) {
		return specificTypeEntry(key, BigDecimal.class);
	}
	
	@Override
	public ItemEntry<Float> entryOfFloat(String key) {
		return specificTypeEntry(key, Float.class);
	}

	public <E> CollectionEntry<E> entryOfCollection(String key, Class<E> memberType) {
		
		if (Document.class.isAssignableFrom(memberType)) {
			@SuppressWarnings("unchecked")
			CollectionEntry<E> entry = (CollectionEntry<E>) entryOfDocuments(key);
			return entry;
		}
		else if (Object.class.equals(memberType)) {
			@SuppressWarnings("unchecked")
			CollectionEntry<E> entry = (CollectionEntry<E>) entryOfCollection(key);
			return entry;
		}		
		else {
			return entryOfTypedCollection(key, memberType);
		}		
		
	}
	
	@Override
	public CollectionEntry<Object> entryOfCollection(String key) {	
		return new CollectionEntryImpl<Object>(this, key, Object.class, MAY_NORMALISE);
	}
	

	private <E> CollectionEntry<E> entryOfTypedCollection(String key, Class<E> memberType) {
		return new CollectionEntryImpl<E>(this, key, memberType, DONT_NORMALISE);
	}
	
	@Override
	public CollectionEntry<Document> entryOfDocuments(String key) {
		return new CollectionEntryImpl<Document>(this, key, Document.class, IData.class, DONT_NORMALISE);
	}

	@Override
	public CollectionEntry<String> entryOfStrings(String key) {
		return entryOfTypedCollection(key, String.class);
	}
	
	@Override
	public CollectionEntry<Boolean> entryOfBooleans(String key) {
		return entryOfTypedCollection(key, Boolean.class);		
	}
	
	@Override
	public CollectionEntry<Integer> entryOfIntegers(String key) {
		return entryOfTypedCollection(key, Integer.class);		
	}
	
	@Override
	public CollectionEntry<Long> entryOfLongs(String key) {
		return entryOfTypedCollection(key, Long.class);		
	}
	
	@Override
	public CollectionEntry<Short> entryOfShorts(String key) {
		return entryOfTypedCollection(key, Short.class);		
	}
	
	@Override
	public CollectionEntry<Double> entryOfDoubles(String key) {
		return entryOfTypedCollection(key, Double.class);		
	}
	
	@Override
	public CollectionEntry<BigDecimal> entryOfBigDecimals(String key) {
		return entryOfCollection(key, BigDecimal.class);		
	}
	
	@Override
	public CollectionEntry<Float> entryOfFloats(String key) {
		return entryOfTypedCollection(key, Float.class);		
	}	
	
	@Override
	public ScatteredEntry<Object> scatteredEntry(String key) {
		return scatteredEntry(key, Object.class);		
	}
	
	@Override
	public <T> ScatteredEntry<T> scatteredEntry(String key, Class<T> memberType) {		
		return new ScatteredEntryImpl<T>(this, key, TypeDescriptor.valueOf(memberType), MAY_NORMALISE);
	}
	
	private <T> ScatteredEntry<T> typedScatteredEntry(String key, Class<T> memberType) {		
		return new ScatteredEntryImpl<T>(this, key, TypeDescriptor.valueOf(memberType), DONT_NORMALISE);
	}
	
	@Override
	public ScatteredEntry<String> scatteredEntryOfStrings(String key) {	
		return typedScatteredEntry(key, String.class);
	}
	
	
	@Override
	public ScatteredEntry<Boolean> scatteredEntryOfBooleans(String key) {
		return typedScatteredEntry(key, Boolean.class);
	}
	
	
	@Override
	public ScatteredEntry<Integer> scatteredEntryOfIntegers(String key) {
		return typedScatteredEntry(key, Integer.class);
	}
	
	@Override
	public ScatteredEntry<Long> scatteredEntryOfLongs(String key) {
		return typedScatteredEntry(key, Long.class);
	}
	
	@Override
	public ScatteredEntry<Short> scatteredEntryOfShorts(String key) {
		return typedScatteredEntry(key, Short.class);
	}
	
	@Override
	public ScatteredEntry<Float> scatteredEntryOfFloats(String key) {
		return typedScatteredEntry(key, Float.class);
	}
	
	
	@Override
	public ScatteredEntry<Double> scatteredEntryOfDoubles(String key) {
		return typedScatteredEntry(key, Double.class);
	}
	
	@Override
	public ScatteredEntry<BigDecimal> scatteredEntryOfBigDecimal(String key) {
		return typedScatteredEntry(key, BigDecimal.class);
	}
	
	
	@Override
	public ScatteredEntry<Document> scatteredEntryOfDocuments(String key) {
		return new ScatteredEntryImpl<Document>(this, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class), DONT_NORMALISE);
	}
	
}
