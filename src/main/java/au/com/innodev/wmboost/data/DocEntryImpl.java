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

/**
 * <p>Implementation of {@link DocEntry}.
 *
 * @param <T> value type
 */
class DocEntryImpl<T> extends MonoBaseEntry<T,T> implements DocEntry<T> {

	public DocEntryImpl(DocumentImpl document, String key, TypeDescriptor accessorType, TypeDescriptor mutatorType) {
		super(document, key, accessorType, mutatorType);
	}

	public DocEntryImpl(DocumentImpl document, String key, TypeDescriptor typeSpec) {
		this(document, key, typeSpec, null);
	}

	@Override
	public final void put(T value) {
		doPut(value);
	}

	@Override
	public T getValOrDefault(T defaultValue) {
		return super.doGetValOrDefault(defaultValue);
	}


}
