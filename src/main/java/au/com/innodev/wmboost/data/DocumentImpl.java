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
import java.util.Date;
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

	private final DocumentFactory factory;
	private final IData iData;
	private final ConversionService internalConversionService;

	DocumentImpl(IData document, DocumentFactory factory, DocumentConfig config) {
		this.factory = factory;
		this.iData = Preconditions.checkNotNull(document);
		this.internalConversionService = config.getInternalConversionService();
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
		return internalConversionService;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
			int count = getTotalEntries();

			sb.append("{Document with ");
			sb.append(count);
			if (count == 1) {
				sb.append(" entry");
			} else {
				sb.append(" entries");
			}
			sb.append(", wrapping IData of type ");
			sb.append(iData.getClass().getCanonicalName());
			sb.append("}");

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
	public int getTotalEntries() {
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
	public EntryIterableResource getAllEntries() {
		return new EntryIterable(this);
	}
	
	@Override
	public Iterable<KeyValue> getUnitEntries() {
		return new UnitEntryIterable(this);
	}
	

	/* **************** Entries section *********************/	
	public ItemEntry<Object> entry(String key) {
		return new ItemEntryImpl<Object>(this, key, Object.class, MAY_NORMALISE);
	}

	public <T> ItemEntry<T> entry(String key, Class<T> type) {
		Preconditions.checkNotNull(type);
		
		
		if (Document.class.isAssignableFrom(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) docEntry(key);
			return entry;
		}
		else if (Object.class.equals(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) entry(key);
			return entry;
		}
		else if (Collection.class.isAssignableFrom(type)) {
			@SuppressWarnings("unchecked")
			ItemEntry<T> entry = (ItemEntry<T>) collectionEntry(key);
			return entry;
		}		
		else {
			return specificTypeEntry(key, type);
		}		
		
	}
	
	public NestedDocEntry docEntry(String key) {
		return new NestedDocEntryImpl(this, factory, key);
	}
	
	private <T> ItemEntry<T> specificTypeEntry(String key, Class<T> type) {
		return new ItemEntryImpl<T>(this, key, type, DONT_NORMALISE);
	}
	
	public StringEntry stringEntry(String key) {
		return new StringEntryImpl(this, key);
	}

	@Override
	public ItemEntry<Boolean> booleanEntry(String key) {
		return specificTypeEntry(key, Boolean.class);
	}
	
	@Override
	public ItemEntry<Integer> intEntry(String key) {	
		return specificTypeEntry(key, Integer.class);
	}
	
	@Override
	public ItemEntry<Long> longEntry(String key) {	
		return specificTypeEntry(key, Long.class);
	}
	
	@Override
	public ItemEntry<Short> shortEntry(String key) {	
		return specificTypeEntry(key, Short.class);
	}
	
	@Override
	public ItemEntry<Float> floatEntry(String key) {
		return specificTypeEntry(key, Float.class);
	}
	
	@Override
	public ItemEntry<Double> doubleEntry(String key) {
		return specificTypeEntry(key, Double.class);
	}
	
	@Override
	public ItemEntry<BigDecimal> bigDecimalEntry(String key) {
		return specificTypeEntry(key, BigDecimal.class);
	}
	
	@Override
	public ItemEntry<Date> legacyDateEntry(String key) {
		return specificTypeEntry(key, Date.class);
	}
	
	public <E> CollectionEntry<E> collectionEntry(String key, Class<E> memberType) {
		
		if (Document.class.isAssignableFrom(memberType)) {
			@SuppressWarnings("unchecked")
			CollectionEntry<E> entry = (CollectionEntry<E>) docsEntry(key);
			return entry;
		}
		else if (Object.class.equals(memberType)) {
			@SuppressWarnings("unchecked")
			CollectionEntry<E> entry = (CollectionEntry<E>) collectionEntry(key);
			return entry;
		}		
		else {
			return typedCollectionEntry(key, memberType);
		}		
		
	}
	
	@Override
	public CollectionEntry<Object> collectionEntry(String key) {	
		return new CollectionEntryImpl<Object>(this, key, Object.class, MAY_NORMALISE);
	}
	

	private <E> CollectionEntry<E> typedCollectionEntry(String key, Class<E> memberType) {
		return new CollectionEntryImpl<E>(this, key, memberType, DONT_NORMALISE);
	}
	
	@Override
	public CollectionEntry<Document> docsEntry(String key) {
		return new CollectionEntryImpl<Document>(this, key, Document.class, IData.class, DONT_NORMALISE);
	}

	@Override
	public CollectionEntry<String> stringsEntry(String key) {
		return typedCollectionEntry(key, String.class);
	}
	
	@Override
	public CollectionEntry<Boolean> booleansEntry(String key) {
		return typedCollectionEntry(key, Boolean.class);		
	}
	
	@Override
	public CollectionEntry<Integer> intsEntry(String key) {
		return typedCollectionEntry(key, Integer.class);		
	}
	
	@Override
	public CollectionEntry<Long> longsEntry(String key) {
		return typedCollectionEntry(key, Long.class);		
	}
	
	@Override
	public CollectionEntry<Short> shortsEntry(String key) {
		return typedCollectionEntry(key, Short.class);		
	}
	
	@Override
	public CollectionEntry<Float> floatsEntry(String key) {
		return typedCollectionEntry(key, Float.class);		
	}	
	
	@Override
	public CollectionEntry<Double> doublesEntry(String key) {
		return typedCollectionEntry(key, Double.class);		
	}
	
	@Override
	public CollectionEntry<BigDecimal> bigDecimalsEntry(String key) {
		return collectionEntry(key, BigDecimal.class);		
	}
	
	@Override
	public CollectionEntry<Date> legacyDatesEntry(String key) {
		return collectionEntry(key, Date.class);		
	}
	
	@Override
	public SplitEntry<Object> splitEntry(String key) {
		return new SplitEntryImpl<Object>(this, key, Object.class, MAY_NORMALISE);		
	}
	
	@Override
	public <T> SplitEntry<T> splitEntry(String key, Class<T> memberType) {		
		Preconditions.checkNotNull(memberType);
		
		if (Document.class.isAssignableFrom(memberType)) {
			@SuppressWarnings("unchecked")
			SplitEntry<T> entry = (SplitEntry<T>) docsSplitEntry(key);
			return entry;
		}
		else if (Object.class.equals(memberType)) {
			@SuppressWarnings("unchecked")
			SplitEntry<T> entry = (SplitEntry<T>) splitEntry(key);
			return entry;
		}
		else {
			return typedSplitEntry(key, memberType);
		}		
	}
	
	private <T> SplitEntry<T> typedSplitEntry(String key, Class<T> memberType) {		
		return new SplitEntryImpl<T>(this, key, memberType, DONT_NORMALISE);
	}
	
	@Override
	public SplitEntry<String> stringsSplitEntry(String key) {	
		return typedSplitEntry(key, String.class);
	}
	
	
	@Override
	public SplitEntry<Boolean> booleansSplitEntry(String key) {
		return typedSplitEntry(key, Boolean.class);
	}
	
	
	@Override
	public SplitEntry<Integer> intsSplitEntry(String key) {
		return typedSplitEntry(key, Integer.class);
	}
	
	@Override
	public SplitEntry<Long> longsSplitEntry(String key) {
		return typedSplitEntry(key, Long.class);
	}
	
	@Override
	public SplitEntry<Short> shortsSplitEntry(String key) {
		return typedSplitEntry(key, Short.class);
	}
	
	@Override
	public SplitEntry<Float> floatsSplitEntry(String key) {
		return typedSplitEntry(key, Float.class);
	}
	
	
	@Override
	public SplitEntry<Double> doublesSplitEntry(String key) {
		return typedSplitEntry(key, Double.class);
	}
	
	@Override
	public SplitEntry<BigDecimal> bigDecimalsSplitEntry(String key) {
		return typedSplitEntry(key, BigDecimal.class);
	}
	
	@Override
	public SplitEntry<Date> legacyDatesSplitEntry(String key) {
		return typedSplitEntry(key, Date.class);
	}
	
	@Override
	public SplitEntry<Document> docsSplitEntry(String key) {
		return new SplitEntryImpl<Document>(this, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class), DONT_NORMALISE);
	}
	
}
