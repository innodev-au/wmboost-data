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
 * A reference to a unit entry whose value is a single item. Allows access and
 * modification of the entry identified by the key.
 * <p>
 * It's suitable when an entry's value corresponds to a single item, such as a
 * string. In contrast, a {@link CollectionEntry} is used for an entry that
 * contains a collection of values, such as a string list.
 * 
 * <h3>Retrieving a Value</h3>
 * <p>
 * The <em>get</em> methods allow retrieving an entry's value. A value of type
 * {@code T} is returned. If the original value is of type {@code T}, it is
 * returned directly and the consuming code doesn't need to cast it. If the type
 * is different, a conversion is attempted. For example, you may use an integer
 * entry to retrieve an integer from a entry originally containing a string.
 *
 * <p>
 * The following getters are available:
 * <ul>
 * <li>{@link #getVal()}: use it when you expect the entry to exist</li>
 * <li>{@link #getNonNullVal()}: use it when you expect the value to exist and
 * also for it to be non-null</li>
 * <li>{@link #getValOrDefault(Object, NullValHandling)}: use it to retrieve an optional value.
 * If the entry doesn't exist, the provided default value is returned.</li>
 * <li>{@link #getValOrNull(NullValHandling)}: use it when you don't know if the entry exists.
 * A null value is returned when either the entry doesn't exist or the actual
 * entry value is null. Because there's no differentiation between those two
 * cases, this method is not used as often as other alternatives.</li>
 * </ul>
 * When you want to optionally retrieve values, you may use a combination of
 * {@link #isAssigned()} and {@link #getVal()}. For example, the
 * following code is a snippet of a transformer that takes a string and returns
 * a lower-case version. If the string is not provided, the pipeline is not
 * modified. If the value is {@code null}, {@code null} is returned:
 * 
 * <pre>
 * DocEntry&lt;String&gt; originalEntry = pipelineDoc.entryOfString("original");
 * if (originalEntry.isAssigned()) {
 * 	String originalString = originalEntry.getVal();
 * 	String lowerString = (originalString != null) ? originalString.toLowerCase() : null;
 * 	pipelineDoc.entryOfString("lower").put(lowerString);
 * }
 * </pre>
 * 
 * <h3>Setting a Value</h3>
 * <p>
 * Use one of the <em>put</em> methods to set the entry's value. If an entry
 * identified with the key doesn't exist, a value is created. Otherwise, the
 * current entry's value is replaced with the new value.
 * <p>
 * The following setters are available:
 * <ul>
 * <li>{@link #put(Object)}: used when you want the entry's value to be of type
 * {@code T} and you provide a value of type {@code T}</li>
 * <li>{@link #putConverted(Object)}: used when you want the entry's value to be
 * of type {@code T} but the value you provide is different. The provided value
 * is converted to type {@code T}.</li>
 * </ul>
 * <h3>Removing an Entry</h3>
 * <p>
 * Invoke {@link ItemEntry#remove()}. Note that the type {@code T} is not taken
 * into account when removing an entry.
 * <p>
 * For documents that contain more than one entry with the same key (which is
 * not very common), only the first entry is removed.
 * <h3>Other considerations</h3>
 * <p>
 * An instance manipulates a unit entry, that is, the first entry identified by
 * the key. This is the norm in most uses cases. In rare situations where
 * multiple entries for a key may exist and processing is required for all of
 * those entries, use of {@link ScatteredEntry} would be more appropriate.
 *
 * @param <T>
 *            type to treat the entry value as
 */
public interface ItemEntry<T> extends BaseUnitEntry, UnitEntryAccessor<T>, UnitEntryMutator<T> {

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
	 * Returns the <em>value</em> component of an existing entry.
	 * <p>
	 * Use this method when you expect an entry with the key <em>to exist</em>.
	 * <p>
	 * If an entry with the key doesn't exist, an exception is thrown.
	 * 
	 * @return entry value, possibly {@code null}
	 * @throws InexistentEntryException
	 *             if there's no entry associated with the key
	 */
	T getVal() throws InexistentEntryException;

	/**
	 * Returns the non-null <em>value</em> component of an existing entry.
	 * <p>
	 * Use this method when you expect an entry with the key <em>to exist</em>
	 * and also <em>to have a non-null value</em>.
	 * <p>
	 * If an entry with the key doesn't exist or if it contains a null value, an
	 * exception is thrown.
	 * 
	 * @return entry value, never {@code null}
	 * @throws InexistentEntryException
	 *             if there's no entry associated with the key
	 * @throws UnexpectedEntryValueException
	 *             if the entry contains a null value
	 * @see #getVal()
	 */
	T getNonNullVal() throws InexistentEntryException, UnexpectedEntryValueException;

	/**
	 * Returns the <em>value</em> component of the key/value entry in the
	 * document.
	 * <p>
	 * Use this method when you don't know if the entry exists and you don't
	 * want to distinguish between the case when the key is not assigned to an
	 * entry and the case when the value is {@code null}.
	 * 
	 * 
	 * @param nullValHandling
	 *            behaviour when entry contains a null value
	 * @return entry value or null if entry doesn't exist
	 * @see #getVal()
	 */
	T getValOrNull(NullValHandling nullValHandling);

	/**
	 * Returns the <em>value</em> component of the key/value entry in the
	 * document.
	 * <p>
	 * Use this method when you don't know if the entry exists and you want
	 * {@code defaultValue} to be returned if it doesn't.
	 * 
	 * @param defaultValue
	 *            value to return if entry doesn't exist
	 * @param nullValHandling
	 *            behaviour when entry contains a null value
	 * @return entry value or {@code defaultValue} if entry doesn't exist
	 * 
	 * @see #getVal()
	 */
	T getValOrDefault(T defaultValue, NullValHandling nullValHandling);

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
	void put(T value);

	/**
	 * Converts the provided value and sets the converted value as the entry's
	 * value.
	 * <p>
	 * Use this method in cases where the value type is different to the type
	 * you want to be stored in the entry. For example, if you wanted an
	 * {@code Integer} value of 5 to be stored as a {@code String} in the entry,
	 * you could use the following code:
	 * 
	 * <pre>
	 * doc.entryOfString("total").putConverted(5);
	 * </pre>
	 * <p>
	 * If the provided {@code value} can't be converted to the entry's type, an
	 * exception is thrown.
	 * 
	 * @param value
	 *            the new value to set for the entry
	 * 
	 * @see #put(Object)
	 */
	void putConverted(Object value);

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
