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

/**
 * Collection-related utilities
 */
final class CollectionUtil {

	private CollectionUtil() {
		
	}
	
	/**
	 * Returns whether all elements in the collection are of the given type 
	 * 
	 * @param collection collection
	 * @param type type to look for
	 * @return true if all elements are of type {@code type}; false, otherwise
	 */
	public static boolean areAllElementsOfType(Iterable<?> collection, Class<?> type) {
		for (Object object : collection) {
			if (! type.isInstance(object)) {
				return false;
			}
		}
		return true;
	}
}
