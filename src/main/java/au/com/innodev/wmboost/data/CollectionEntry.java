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
 * <p>
 * A reference to a unit entry whose value is a collection. Allows access and
 * modification of the entry identified by the key.
 * <p>
 * This type is suitable when the entry's value consists in a collection of
 * elements, such as a list if strings. In contrast, a {@link ItemEntry}
 * instance is used for an entry that contains a single value, such as a string.
 *
 * <p>
 * The behaviour of get, put, and other methods in this class is very similar to
 * {@link ItemEntry}. Refer to its documentation for more information.
 * <h3>Other considerations</h3>
 * <p>
 * For simplicity, this class exposes values as collections. Internally, values
 * are stored as arrays, as expected by webMethods.
 * <p>
 * An instance manipulates a unit entry, that is, the first entry identified by
 * the key. This is the norm in most uses cases. In rare situations where
 * multiple entries for a key may exist and processing is required for all of
 * those entries, use of {@link ScatteredEntry} would be more appropriate.
 *
 * @param <E>
 *            collection element type to treat the entry value as
 */
public interface CollectionEntry<E>
		extends BaseUnitEntry, UnitEntryAccessor<List<E>>, UnitEntryMutator<Iterable<? extends E>> {

	/** -------- Accessors ------------------------------------------ */

	/**
	 * @see HasKey#getKey()
	 */
	String getKey();

	/**
	 * @see BaseUnitEntry#isAssigned()
	 */
	boolean isAssigned();

	/**
	 * @see ItemEntry#getVal()
	 */
	List<E> getVal() throws InexistentEntryException;

	/**
	 * @see ItemEntry#getNonNullVal()
	 */
	List<E> getNonNullVal() throws InexistentEntryException, UnexpectedEntryValueException;

	/**
	 * Returns a non-empty list <em>value</em> of an existing entry.
	 * <p>
	 * Use this method when you expect an entry with the key <em>to exist</em>,
	 * and the value to be both <em>non-null</em> and <em>a non-empty
	 * collection</em>. If any of those expectations isn't met, an exception is
	 * thrown.
	 * 
	 * @return entry value, never {@null} and never an empty list
	 * @throws InexistentEntryException
	 *             if there's no entry associated with the key
	 * @throws UnexpectedEntryValueException
	 *             if the entry contains a null value or is empty
	 * @see #getVal()
	 */
	List<E> getNonEmptyVal() throws InexistentEntryException, UnexpectedEntryValueException;

	/**
	 * Returns the <em>value</em> component of an existing entry or an empty
	 * list.
	 * <p>
	 * Use this method when you don't know whether the entry exists and you want
	 * an empty list to be returned if it doesn't.
	 * 
	 * @return entry value
	 * 
	 * @see #getVal()
	 */
	List<E> getValOrEmpty();

	/**
	 * Returns the <em>value</em> component of an existing entry or a default
	 * value.
	 * <p>
	 * Use this method when you don't know if the entry exists and you want
	 * {@code defaultValue} to be returned if it doesn't.
	 * 
	 * @param defaultValue
	 *            value to return if entry doesn't exist
	 * 
	 * @return entry value
	 * 
	 * @see #getVal()
	 */
	List<E> getValOrDefault(List<? extends E> defaultValue);

	/** -------- Mutators ------------------------------------------ */

	/**
	 * Sets the provided {@code value} as the entry's value.
	 * <p>
	 * If an entry identified with the key doesn't exist, a value is created.
	 * Otherwise, the entry's value is replaced with the provided {@code value}.
	 * 
	 * @param value
	 *            the new value to set for the entry
	 */
	void put(Iterable<? extends E> value);

	/**
	 * Converts the provided value and sets the converted value as the entry's
	 * value.
	 * <p>
	 * Use this method in cases where the value type is different to the type
	 * you want to be stored in the entry.
	 * 
	 * <p>
	 * If the provided {@code value} can't be converted to the entry's type, an
	 * exception is thrown.
	 * 
	 * @param value
	 *            the new value to set for the entry
	 * 
	 * @see #put(Iterable)
	 */
	void putConverted(Iterable<?> value);

	/**
	 * Deletes the entry in strict mode.
	 * 
	 * @see #remove(RemoveEntryOption)
	 */
	void remove() throws InexistentEntryException;

	/**
	 * Deletes the entry identified by the element’s key.
	 * 
	 * <p>
	 * Note that type used for the entry (e.g. String) is not taken into account
	 * when removing the entry. It's only done by key.
	 * 
	 * <p>
	 * Because this is a unit entry reference, if multiple elements exist in the
	 * document for the given key, only the first entry is deleted.
	 * 
	 * @param removeOption
	 *            strict or lenient removal option
	 *
	 */
	void remove(RemoveEntryOption removeOption) throws InexistentEntryException;

}
