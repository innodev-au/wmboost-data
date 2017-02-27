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
import java.util.List;

import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IDataCursor;

import au.com.innodev.wmboost.data.internal.Preconditions;

class SplitEntryImpl<E> extends BaseEntry<E, E> implements SplitEntry<E> {

	private final TypeDescriptor accessorType;
	private final TypeDescriptor mutatorType;

	public SplitEntryImpl(DocumentImpl document, String key, TypeDescriptor accessorType,
			TypeDescriptor mutatorType, NormaliseOption normaliseOption) {
		super(document, key, normaliseOption);

		this.accessorType = Preconditions.checkNotNull(accessorType);
		this.mutatorType = mutatorType;
	}

	public SplitEntryImpl(DocumentImpl document, String key, Class<?> memberType, NormaliseOption normaliseOption) {
		this(document, key, TypeDescriptor.valueOf(memberType), null, normaliseOption);
	}

	@Override
	public List<E> getNonEmptyVal() {
		List<E> val = getValOrEmpty();
		if (val.isEmpty()) {
			throw new InexistentEntryException(
					"No value found in split entry with key '" + getKey() + "'");
		}

		return val;
	}

	@Override
	public List<E> getValOrEmpty() {
		List<E> list = new ArrayList<E>();

		IDataCursorResource cursorRes = newCursorResource();
		try {
			boolean hasMore = cursorRes.getCursor().first(getKey());
			while (hasMore) {
				Object value = cursorRes.getCursor().getValue();
				E converted = convertAndNormaliseValForGet(value, accessorType);

				list.add(converted);
				hasMore = cursorRes.getCursor().next(getKey());
			}
		} finally {
			cursorRes.close();
		}
		return list;
	}

	@Override
	public void put(Iterable<? extends E> values) {
		doPut(values);
	}

	@Override
	public void putConverted(Iterable<?> values) {
		TypeDescriptor listAccessor = TypeDescriptor.collection(List.class, accessorType);

		Iterable<?> convertedValues = getConvertedValue(values, listAccessor);

		doPut(convertedValues);
	}

	private void doPut(Iterable<?> values) {

		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataCursor cursor = cursorRes.getCursor();

			boolean continueLoop;

			/* Remove previous entries */
			do {
				boolean hasValWithKey = cursor.first(getKey());

				if (hasValWithKey) {
					continueLoop = cursor.delete();
				} else {
					continueLoop = false;
				}
			} while (continueLoop);

			cursor.last();

			for (Object individualVal : values) {
				Object normalisedIndividualVal = convertAndNormaliseValForPut(individualVal, mutatorType);
				cursor.insertAfter(getKey(), normalisedIndividualVal);
			}
		} finally {
			cursorRes.close();
		}
	}

	public final void remove() {
		remove(RemoveEntryOption.STRICT);
	}

	@Override
	public final void remove(RemoveEntryOption removeOption) {
		Preconditions.checkNotNull(removeOption, "Remove option cannot be null");
		boolean hasMore;

		IDataCursorResource cursorRes = newCursorResource();
		try {

			IDataCursor cursor = cursorRes.getCursor();

			boolean hasFirst = cursor.first(getKey());

			if (!hasFirst && RemoveEntryOption.STRICT.equals(removeOption)) {
				throw new InexistentEntryException(
						"Entry with key '" + getKey() + "' doesn't exist and can't be removed");
			}

			hasMore = hasFirst;

			while (hasMore) {
				deleteCurrentEntry(cursor);

				hasMore = cursor.first(getKey());
			}
		} finally {
			cursorRes.close();
		}

	}

}
