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

import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Base class for an entry that contains a single value.
 *
 * @param <A> Accessor type
 * @param <M> Mutator type
 */
class BaseUnitEntryImpl<A, M> extends BaseEntry<A,M> {

	private final TypeDescriptor accessorType;
	// May be null, in which case no pre-conversion is done for 'put' 
	private final TypeDescriptor mutatorType;
	
	
	public BaseUnitEntryImpl(DocumentImpl document, String key, TypeDescriptor accessorType, TypeDescriptor mutatorType, NormaliseOption normaliseOption) {
		super(document, key, normaliseOption);
		
		this.accessorType = Preconditions.checkNotNull(accessorType);
		this.mutatorType = mutatorType;
	}

	public boolean isAssigned() {
		return getDocument().containsKey(getKey());
	}
	

	public final A doGetNonNullVal() {

		A v = doGetVal();
		if (v == null) {
			throw new UnexpectedEntryValueException("Unexpected null value was found for key '" + getKey() + "' in document");
		}

		return v;
	}

	public final A doGetVal() {

		if (!isAssigned()) {
			throw new InexistentEntryException(
					"Unable to retrieve value for key '" + getKey() + "'. Entry doesn't exist in document");
		}

		return internalGetVal();
	}

	protected final A internalGetVal() {
		IDataCursorResource cursorRes = newCursorResource();
		try {
			Object value = IDataUtil.get(cursorRes.getCursor(), getKey());
			return convertAndNormaliseValForGet(value, accessorType);
		}
		finally {
			cursorRes.close();
		}
	}	
	
	

	protected void doPut(Object value) {
		Object valueToPut = convertAndNormaliseValForPut(value, mutatorType);

		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataUtil.put(cursorRes.getCursor(), getKey(), valueToPut);
		}
		finally {
			cursorRes.close();
		}
	}

	public final void putConverted(Object value) {
		doPutConverted(value);
	}

	private void doPutConverted(Object value) {
		A convertedValue = getConvertedValue(value, accessorType);

		doPut(convertedValue);
	}

	public final void remove() {
		remove(RemoveEntryOption.STRICT);
	}
	
	public final void remove(RemoveEntryOption removeOption) {
		Preconditions.checkNotNull(removeOption, "Remove option cannot be null");
		
		IDataCursorResource cursorRes = newCursorResource();

		try {
			IDataCursor cursor = cursorRes.getCursor();

			boolean exists = cursor.first(getKey());

			if (!exists && RemoveEntryOption.STRICT.equals(removeOption)) {
					throw new InexistentEntryException(
							"Entry with key '" + getKey() + "' doesn't exist and can't be removed");
			}
			deleteCurrentEntry(cursor);
		} finally {
			cursorRes.close();
		}
	}
	
}
