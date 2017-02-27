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

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Implementation of {@link ItemEntry}.
 *
 * @param <T> value type
 */
class ItemEntryImpl<T> extends BaseUnitEntryImpl<T,T> implements ItemEntry<T> {

	public ItemEntryImpl(DocumentImpl document, String key, TypeDescriptor accessorType, TypeDescriptor mutatorType, NormaliseOption normaliseOption) {
		super(document, key, accessorType, mutatorType, normaliseOption);
	}

	public ItemEntryImpl(DocumentImpl document, String key, Class<T> type, NormaliseOption normaliseOption) {
		this(document, key, TypeDescriptor.valueOf(type), null, normaliseOption);
	}
	
	@Override
	public final void put(T value) {
		doPut(value);
	}

	

	
	@Override
	public T getVal() throws InexistentEntryException {	
		return doGetVal();
	}
	
	@Override
	public T getNonNullVal() throws InexistentEntryException, UnexpectedEntryValueException {	
		return doGetNonNullVal();
	}
	
	@Override
	public T getValOrNull() {
		return getValOrNull(NullValHandling.RETURN_DEFAULT);
	}
	
	@Override
	public T getValOrNull(NullValHandling nullValHandling) {	
		return doGetValOrDefault(null, nullValHandling);
	}
	
	
	@Override
	public T getValOrDefault(T defaultValue) {
		return getValOrDefault(defaultValue, NullValHandling.RETURN_DEFAULT);
	}
	
	@Override
	public T getValOrDefault(T defaultValue, NullValHandling nullHandling) {
		return doGetValOrDefault(defaultValue, nullHandling);
	}
	
	private final T doGetValOrDefault(T defaultValue, NullValHandling nullHandling) {
		Preconditions.checkNotNull(nullHandling, "null handling parameter was not set");
		if (isAssigned()) {
			T value = internalGetVal();
			value = applyValNullHandling(value, nullHandling, defaultValue);
			return value;
		} else {
			return defaultValue;
		}
	}

	protected final T applyValNullHandling(T value, NullValHandling nullHandling, T defaultValue) {
		if (value == null) {
			switch(nullHandling) {
			case FAIL:
				throw new UnexpectedEntryValueException("Unexpected null value for entry with key '"+ getKey() + "'");
			case RETURN_DEFAULT: 
				return defaultValue;
			case RETURN_NULL:
				return null;
			default:
				throw new IllegalArgumentException("Unsupported null handling value: " + nullHandling);
			}
		}
		else {
			return value;
		}
	}

}
