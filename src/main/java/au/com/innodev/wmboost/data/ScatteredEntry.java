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

import java.util.Collection;

/**
 * Allows access and modification of a group of entries that share the same key.
 *
 * @param <E> element type
 */
public interface ScatteredEntry<E> {
	String getKey();
	boolean isAssigned();
	
	/**
	 * Returns all values for the key
	 *  
	 * @return a non-null collection
	 */
	Collection<E> getValues();
	
	// TODO getNonEmptyValues ?
	
	void put(Iterable<E> values);
	void putConverted(Iterable<?> values);
	void remove();
	
}
