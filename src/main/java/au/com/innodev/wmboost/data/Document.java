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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import com.wm.data.IData;

/**
 * Represents a document that contains key/value entries, as an abstraction of
 * webMethods' {@link IData}.
 * 
 * <p>
 * Allows easy access and manipulation of an {@link IData} document. It provides
 * a simplified mechanism to deal with aspects such as type conversion and
 * determining whether entries are present, while providing more predictable
 * behaviour than some {@link com.wm.data.IDataUtil} methods, such as
 * {@link com.wm.data.IDataUtil#getBoolean}.
 * <p>
 * It also provides easier abstraction for manipulation of collection values and
 * nested documents.
 * <p>
 * The following example shows how one can use it to implement a service that
 * adds two integer values:
 * 
 * <pre>
 * public static final void addInts(IData pipeline) throws ServiceException {
 * 	Document pipeDoc = Documents.wrap(pipeline);
 * 	Integer number1 = pipeDoc.intEntry("number1").getNonNullVal();
 * 	Integer number2 = pipeDoc.intEntry("number2").getNonNullVal();
 * 
 * 	Integer result = number1 + number2;
 * 	pipeDoc.intEntry("result").put(result);
 * }
 * </pre>
 * 
 * The example highlights some important features:
 * <ul>
 * <li>There's no need to retrieve and destroy IDataCursor instances</li>
 * <li>The numbers are retrieved in a way that ensures both that values exist in
 * the pipeline document and that they are non-null. There's no need for
 * associated {@code if} statements.</li>
 * <li>Automatic conversion to Integer is performed for the input numbers. For
 * instance, if {@code number1} was actually stored as a string, it's converted
 * automatically to an integer. Similarly, if a value of Long, BigDecimal or
 * similar had been found, the conversion would've been done automatically.
 * <li>It's possible to return the result as an integer as shown in the example.
 * If one wanted to return a string, then one would call
 * {@code pipelineDoc.stringEntry("result").putConverted(result)} and the
 * integer value would be automatically converted to String.
 * </ul>
 * 
 * <h3>Automatic Conversion</h3>
 * <p>
 * The automatic type conversion depends on the configuration defined while
 * creating the document through the originating {@link DocumentFactory}.
 * Out-of-box conversions include:
 * <ul>
 * <li>String to boolean: values such as "true", "false", "yes", "no" are
 * converted. In order to ensure predictability, unsupported string values such
 * as "maybe" throw an exception. Contrast this with the implementation of
 * {@link com.wm.data.IDataUtil#getBoolean}, which returns false.</li>
 * <li>String to numbers, such as Integer, Long, BigDecimal, etc. and vice-versa
 * </li>
 * <li>Numbers to numbers: such as Integer to Long</li>
 * <li>File object to strings</li>
 * <li>etc.</li>
 * </ul>
 * 
 * All conversions are internally performed by an instance of
 * {@link org.springframework.core.convert.ConversionService}. The
 * {@link DocumentFactory} that created the document instance determines the
 * conversion service to be used.
 * 
 * <h3>Implementation</h3>
 * <p>
 * An internal implementation for this {@link Document} interface is provided.
 * This interface is not meant to be implemented outside this library.
 * 
 * <p>
 * Thread-safety is not guaranteed by implementations of this interface and the
 * associated entry interfaces.
 * 
 * @see au.com.innodev.wmboost.data.preset.Documents
 */
public interface Document {

	/**
	 * Returns whether the provided key exists in at least one of the document
	 * entries.
	 * 
	 * @param key
	 *            name of the key
	 * @return {@code true} if an entry with the key exists; {@code false},
	 *         otherwise
	 */
	boolean containsKey(String key);

	/**
	 * Returns a set of keys contained in the document.
	 * <p>
	 * Even if multiple entries existed for a particular key, that key would be
	 * returned only once.
	 * 
	 * @return a read-only set of keys that correspond to the document entries
	 */
	Collection<String> getKeys();

	/**
	 * Returns {@code true} if the document has no entries.
	 * 
	 * @return {@code true} if empty; {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns the number of all entries.
	 * <p>
	 * If each key is unique in the document (which is the most common case),
	 * then this method returns the same number as the size of elements in
	 * {@link #getKeys()}.
	 * <p>
	 * However, for certain documents, such as deserialised XML content with
	 * lists, more than one entry per key may exist. In those cases, the
	 * returned value will be greater than the size of {@link #getKeys()}.
	 * 
	 * @return the number of entries in the document
	 */
	int getTotalEntries();

	/**
	 * Returns <em>all</em> document entries, including multiple entries sharing
	 * the same key (i.e. split entries).
	 * <p>
	 * The number of returned elements matches the one given by
	 * {@link #getTotalEntries()}.
	 * 
	 * <p>
	 * Note that the returned value is both an {@link java.lang.Iterable} and a
	 * {@link java.io.Closeable}. This means that the {@code close()} method
	 * needs to be invoked after processing is complete.
	 * 
	 * <p>
	 * In Java 7 or higher it can be used in a foreach loop but it needs to be
	 * surrounded by a try-with-resources block.
	 * 
	 * <p>
	 * <em>Java 7 or Higher Example</em>
	 * 
	 * <pre>
	 * try (EntryIterableResource entries = document.getAllEntries()) {
	 * 	 for (KeyValue entry : entries) {
	 * 		// do something
	 * 	 }
	 * }
	 * </pre>
	 * 
	 * <p>
	 * <em>Java 6 Example</em>
	 * 
	 * <pre>
	 * EntryIterableResource entries = document.getAllEntries();
	 * try {
	 * 	  while (entries.hasNext()) {
	 * 		KeyValue entry = entries.next();
	 * 		// do something
	 * 	  }
	 * } finally {
	 * 	  entries.close();
	 * }
	 * 
	 * </pre>
	 * 
	 * @return an iterable resource containing all entries
	 */
	EntryIterableResource getAllEntries();

	/**
	 * Returns unit entries. If multiple entries exist for a given key, only the
	 * first entry is retrieved for that key.
	 * <p>
	 * Note that this method is consistent with {@link #entry(String, Class)}
	 * and its variations.
	 * <p>
	 * The number of returned elements is the same as the size of
	 * {@link #getKeys()}.
	 * 
	 * @return unit entries
	 */
	Iterable<KeyValue> getUnitEntries();

	/**
	 * Removes all document entries
	 * 
	 */
	public void clear();

	/**
	 * Retrieves the underlying {@link com.wm.data.IData} instance that this
	 * document is wrapping.
	 * 
	 * <p>
	 * Since a {@link Document} always acts on an {@link com.wm.data.IData}, the
	 * value returned by this method is never {@code null}.
	 * 
	 * @return the underlying {@link com.wm.data.IData} instance
	 */
	IData getIData();

	/* ---------- Entry Section ----------------------------------------- */

	/**
	 * Returns a reference to a single entry identified by the {@code key}.
	 * <p>
	 * Through this reference, the entry value can be retrieved, set or removed.
	 * <p>
	 * When a value is returned, it is checked to be of type {@code T}. If it
	 * isn't, a conversion is performed. If that conversion fails, an exception
	 * is thrown.
	 * <p>
	 * Use this method for types for which convenience methods haven't been
	 * defined. {@link #stringEntry(String)} is recommended for string entries.
	 * However, this method is suitable for types such as <em>enums</em>,
	 * special types such as AtomicInteger and File. Domain objects (e.g. a
	 * Client object) are also supported but no out-of-the-box conversion is
	 * provided for them.
	 * <p>
	 * For example, to retrieve an automatically-converted value of a custom
	 * Enum such as {@link java.math.RoundingMode}, you may use the following
	 * code:
	 * 
	 * <pre>
	 *  import java.math.RoundingMode;
	 *	//...
	 *	RoundingMode mode = document.entry("roundingMode", RoundingMode.class).getNonNullVal();
	 * </pre>
	 * <p>
	 * Note that this and similar methods operate on a <em>unit entry</em>. If
	 * there were multiple entries associated to the key, only <em>the first
	 * one</em> would be referenced. This behaviour supports typical use cases;
	 * for situations where multiple entries for a key may exist and processing
	 * is required for all those entries, use of
	 * {@link #splitEntry(String, Class)} would be more appropriate.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * @param type
	 *            type of the <em>value</em> component of the document entry
	 * @param <T>
	 *            type of the <em>value</em> component of the document entry
	 * @return an entry reference
	 * 
	 * @see ItemEntry
	 */
	<T> ItemEntry<T> entry(String key, Class<T> type);

	/**
	 * Returns a reference to an entry. Use it when you don't know have an
	 * assumption about the type of the entry's value or for other special
	 * cases.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Object> entry(String key);

	/**
	 * Returns a reference to an entry with a nested document as the value.
	 * 
	 * <p>
	 * Note that because {@link Document} is just an abstraction, the value is
	 * internally stored as an {@link com.wm.data.IData} instance, as expected
	 * by webMethods.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	NestedDocEntry docEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link String}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	StringEntry stringEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Boolean}
	 * .
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Boolean> booleanEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as an
	 * {@link Integer}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Integer> intEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Long}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Long> longEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Short}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Short> shortEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Float}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Float> floatEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Double}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Double> doubleEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a
	 * {@link BigDecimal}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<BigDecimal> bigDecimalEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a {@link Date}.
	 * <p>
	 * As of 2017, webMethods still uses this class for dates but it's legacy as
	 * of Java 8.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	ItemEntry<Date> legacyDateEntry(String key);

	/*
	 * ---------- Collection Entry Section -------------------------------------
	 */

	/**
	 * Returns a reference to an entry identified by the {@code key} and whose
	 * value is a collection.
	 * <p>
	 * Through this reference, the entry value can be retrieved, set or removed.
	 * <p>
	 * When a value is returned, it is checked to be of a collection of
	 * {@code type} instances. If it isn't, a conversion is performed. If that
	 * conversion fails, an exception is thrown.
	 * <p>
	 * Note that this method operates on a <em>unit entry</em> (albeit that
	 * entry's value is a collection). If there were multiple entries associated
	 * to the key, only the <em>first one</em> would be referenced. This
	 * behaviour supports typical use cases; for situations where multiple
	 * entries per key are expected, use {@link #splitEntry(String, Class)}
	 * instead.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * @param memberType
	 *            type of the elements in a collection in the <em>value</em>
	 *            component of the document entry
	 * @param <E>
	 *            type of the elements in a collection in the <em>value</em>
	 *            component of the document entry
	 * @return an entry reference
	 * 
	 * @see CollectionEntry
	 */
	<E> CollectionEntry<E> collectionEntry(String key, Class<E> memberType);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * objects. Use it when you don't know have an assumption about the type of
	 * the elements in the entry's value or for other special cases.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Object> collectionEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link String}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<String> stringsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Boolean}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Boolean> booleansEntry(String key);

	/**
	 * Returns a reference to an entry with a nested collection of documents as
	 * the value.
	 * 
	 * <p>
	 * Note that because {@link Document} is just an abstraction, the collection
	 * value is internally stored as an {@link com.wm.data.IData} array, as
	 * expected by webMethods.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Document> docsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Integer}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Integer> intsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Long}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Long> longsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Short}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Short> shortsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Float}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Float> floatsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Double}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Double> doublesEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link BigDecimal}.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<BigDecimal> bigDecimalsEntry(String key);

	/**
	 * Returns a reference to an entry with a value treated as a collection of
	 * {@link Date}.
	 * <p>
	 * As of 2017, webMethods still uses this class for dates but it's legacy as
	 * of Java 8.
	 * 
	 * @param key
	 *            key that identifies the document entry
	 * 
	 * @return an entry reference
	 * 
	 * @see #entry(String, Class)
	 */
	CollectionEntry<Date> legacyDatesEntry(String key);

	/*
	 * ---------- Split Entry Section ----------------
	 * 
	 */

	/**
	 * Returns a reference to a split entry.
	 * <p>
	 * Through this reference, the split entry value can be retrieved, set or
	 * removed.
	 * <p>
	 * As opposed to {@link #entry(String, Class)}, a split entry handles
	 * <em>all</em> values associated wit a key, even if the key is shared
	 * across multiple entries.
	 * <p>
	 * When a value is returned, all of the elements are checked to be of type
	 * {@code T}. If it isn't, a conversion is performed. If that conversion
	 * fails, an exception is thrown.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * @param memberType
	 *            type of each individual entry that makes up a split entry
	 * @param <E>
	 *            memberType
	 * @return a split entry reference
	 */
	<E> SplitEntry<E> splitEntry(String key, Class<E> memberType);

	/**
	 * Returns a reference to a split entry. Use it when you don't know have an
	 * assumption about the type of the entry's value or for other special
	 * cases.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 */
	SplitEntry<Object> splitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Boolean} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Boolean> booleansSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Integer} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Integer> intsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Long} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Long> longsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Short} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Short> shortsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Double} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Double> doublesSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Float} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Float> floatsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link BigDecimal} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<BigDecimal> bigDecimalsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link Date} instances.
	 * <p>
	 * As of 2017, webMethods still uses this class for dates but it's legacy as
	 * of Java 8.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Date> legacyDatesSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of {@link String} instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<String> stringsSplitEntry(String key);

	/**
	 * Returns a reference to a split entry of nested document instances.
	 * 
	 * @param key
	 *            key that identifies the split entry
	 * @return a split entry reference
	 * 
	 * @see #splitEntry(String, Class)
	 */
	SplitEntry<Document> docsSplitEntry(String key);

}