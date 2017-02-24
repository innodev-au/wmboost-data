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

import java.util.Collections;
import java.util.List;

import org.springframework.core.convert.TypeDescriptor;

import com.google.common.base.Preconditions;

/**
 * Implementation of {@link CollectionEntry}.
 *
 * @param <E> element type
 */
class CollectionEntryImpl<E> extends BaseUnitEntryImpl<List<E>, Iterable<E>> implements CollectionEntry<E> {

	public CollectionEntryImpl(DocumentImpl document, String key, Class<?> accessorElementType, Class<?> mutatorType, NormaliseOption normaliseOption) {
		super(document, key, listAccessorType(accessorElementType), arrayMutatorType(mutatorType), normaliseOption);
	}
	
	private static TypeDescriptor listAccessorType(Class<?> accessorClass) {		
		return TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(accessorClass));
	}
	
	private static TypeDescriptor arrayMutatorType(Class<?> mutatorClass) {		
		return TypeDescriptor.array(TypeDescriptor.valueOf(mutatorClass));
	}

	public CollectionEntryImpl(DocumentImpl document, String key, Class<?> accessorType, NormaliseOption normaliseOption) {
		this(document, key, accessorType, accessorType, normaliseOption);
	}

	@Override
	public List<E> getValOrEmpty(NullValHandling nullHandling) {	
		return getValOrDefault(Collections.<E>emptyList(), nullHandling);
	}

	
	@Override
	public final List<E> getNonNullVal() {
		return doGetNonNullVal();
	}
	
	@Override
	public List<E> getVal() throws InexistentEntryException {	
		return doGetVal();
	}	

	@Override
	public List<E> getNonEmptyVal() {
		List<E> v = getNonNullVal();
		if (v.isEmpty()) {
			throw new UnexpectedEntryValueException("Unexpected empty collection value was found for key '" + getKey() + "' in document");
		}

		return v;
	}

	@Override
	public final List<E> getValOrDefault(List<? extends E> defaultValue, NullValHandling nullHandling) {
		Preconditions.checkNotNull(nullHandling);
		
		if (isAssigned()) {
			List<E> val =  internalGetVal();
			
			if (val == null) {
				switch (nullHandling) {
				case FAIL:
					throw new UnexpectedEntryValueException("Unexpected null value for entry with key '" + getKey() + "'");				
				case RETURN_NULL:
					return null;
				case TREAT_AS_DEFAULT:
					return doGetDefaultValue(defaultValue);
				default:
					throw new RuntimeException("Unsupported null handling option - " + nullHandling);
				}
			}
			else {
				return val;
			}
		} else {
			return (defaultValue!= null) ? doGetDefaultValue(defaultValue) : null;
		}
	}

	private List<E> doGetDefaultValue(List<? extends E> defaultValue) {
		return Collections.unmodifiableList(defaultValue);
	}

	@Override
	public final void put(Iterable<? extends E> value) {	
		super.doPut(value);
	}		

	@Override
	public void putConverted(Iterable<?> value) {
		super.putConverted(value);		
	}
}

