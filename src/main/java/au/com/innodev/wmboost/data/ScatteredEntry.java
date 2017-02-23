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

import java.util.List;

/**
 * Allows access and modification of a group of entries that share the same key.
 * <p>
 * Use it in cases where you need to process all entries, such as deserialised
 * XML list. For most common uses cases, use a unit entry, i.e., {@link ItemEntry}
 * or {@link CollectionEntry}.
 * 
 * @param <E>
 *            element type
 */
public interface ScatteredEntry<E> extends HasKey, EntryMutator<Iterable<? extends E>> {
	
	/**
	 * @see HasKey#getKey()
	 */
	String getKey();

	/**
	 * Returns the values for the scattered entry.
	 * <p>
	 * If there are no entries with the key, an empty collection is returned.
	 * 
	 * @return a non-null list
	 */
	List<E> getValOrEmpty();

	/**
	 * Sets or replaces all values in the scattered entry with the provided
	 * ones.
	 * 
	 * @param values
	 *            new values for the scattered entry
	 */
	void put(Iterable<? extends E> values);

	/**
	 * Sets or replaces all values in the scattered entry with the provided
	 * ones.
	 * 
	 * <p>
	 * Use this method in cases where the value type is different to the type
	 * you want to be stored in the entry. For example, if you wanted a list of
	 * integers 5 and 8 to be stored as a {@code String} in the entry, you could
	 * use the following code:
	 * 
	 * <pre>
	 * List&lt;Integer&gt; list = new ArrayList&lt;Integer&gt;();
	 * list.add(5);
	 * list.add(8);
	 * doc.scatteredOfString("values").putConverted(list);
	 * </pre>
	 * 
	 * @param values
	 *            new values for the scattered entry
	 */
	void putConverted(Iterable<?> values);

	/**
	 * Removes all values in the scattered entry in strict mode.
	 * 
	 * @see #remove(RemoveEntryOption)
	 * 
	 */
	void remove();

	/**
	 * Removes all values in the scattered entry.
	 * 
	 * <p>
	 * Note that type used for the entry (e.g. String) is not taken into account
	 * when removing the entry. It's only done by key.
	 * 
	 * <p>
	 * Because this is an entry reference that takes into account all entries
	 * associated to a key, it removes all of those entries.
	 */
	void remove(RemoveEntryOption removeOption);

}
