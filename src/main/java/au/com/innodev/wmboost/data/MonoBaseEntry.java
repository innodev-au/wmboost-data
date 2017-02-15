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

import com.wm.data.IDataUtil;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Base class for an entry that contains a single value.
 *
 * @param <A> Accessor type
 * @param <M> Mutator type
 */
class MonoBaseEntry<A, M> extends BaseEntry<A,M> {

	private final TypeDescriptor accessorType;
	// May be null, in which case no pre-conversion is done for 'put' 
	private final TypeDescriptor mutatorType;
	
	public MonoBaseEntry(DocumentImpl document, String key, TypeDescriptor accessorType, TypeDescriptor mutatorType) {
		super(document, key);
		
		this.accessorType = Preconditions.checkNotNull(accessorType);
		this.mutatorType = mutatorType;
	}

	public final A getValOrNull() {
		return doGetValOrDefault(null);
	}

	public final A getNonNullVal() {

		A v = getVal();
		if (v == null) {
			throw new UnexpectedEntryValueException("Unexpected null value was found for key '" + getKey() + "' in document");
		}

		return v;
	}

	public final A getVal() {

		if (!isAssigned()) {
			throw new InexistentEntryException(
					"Unable to retrieve value for key '" + getKey() + "'. Entry doesn't exist in document");
		}

		return doGetVal();
	}

	protected final A doGetVal() {
		IDataCursorResource cursorRes = newCursorResource();
		try {
			Object value = IDataUtil.get(cursorRes.getCursor(), getKey());
			return getConvertedValue(value, accessorType);
		}
		finally {
			cursorRes.close();
		}
	}

	public final A doGetValOrDefault(A defaultValue) {

		if (isAssigned()) {
			return doGetVal();
		} else {
			return defaultValue;
		}
	}

	protected void doPut(Object value) {
		Object valueToPut = value;

		if (mutatorType != null) {
			valueToPut = getConvertedValue(value, mutatorType);
		}

		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataUtil.put(cursorRes.getCursor(), getKey(), valueToPut);
		}
		finally {
			cursorRes.close();
		}
	}

	public final void putConverted(Object value) {

		A convertedValue = getConvertedValue(value, accessorType);

		doPut(convertedValue);

	}

	public final void remove() {
		// TODO see if this will return false or throw an exception if key
		// doesn't exist
		IDataCursorResource cursorRes = newCursorResource();
		try {
			IDataUtil.remove(cursorRes.getCursor(), getKey());
		}
		finally {
			cursorRes.close();
		}
	}
}
