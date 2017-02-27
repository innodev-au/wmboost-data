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
 * A reference to an entry with a string as the value.
 */
public interface StringEntry extends ItemEntry<String> {

	/**
	 * Returns the non-null and non-empty string value of an existing entry.
	 * <p>
	 * Use this method when you expect an entry with the key <em>to exist</em>
	 * and also the string value to be <em>non-null</em> and <em>non-empty</em>.
	 * If any of those expectations isn't met, an exception is thrown.
	 * <p>A string is considered empty if its length is 0.
	 * 
	 * @return a non-empty string value
	 * @throws InexistentEntryException
	 *             if there's no entry associated with the key
	 * @throws UnexpectedEntryValueException
	 *             if the entry contains a null or empty value
	 * @see #getVal()
	 */
	String getNonEmptyVal() throws InexistentEntryException, UnexpectedEntryValueException;
	
	/**
	 * Returns the non-null and non-blank string value of an existing entry.
	 * <p>
	 * Use this method when you expect an entry with the key <em>to exist</em>
	 * and also the string value to be <em>non-null</em> and <em>non-blank</em>.
	 * If any of those expectations isn't met, an exception is thrown.
	 * <p>A string is considered blank if it is null, empty or contains only whitespace characters.
	 * 
	 * @return a non-blank string value
	 * @throws InexistentEntryException
	 *             if there's no entry associated with the key
	 * @throws UnexpectedEntryValueException
	 *             if the entry contains a null, empty or blank value
	 * @see #getVal()
	 */
	String getNonBlankVal() throws InexistentEntryException, UnexpectedEntryValueException;
}
