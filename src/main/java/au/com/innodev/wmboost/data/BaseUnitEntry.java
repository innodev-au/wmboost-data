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
 * Base interface for unit entry references, which are the most common type of
 * entry references.
 * <p>
 * Direct use of leaf interfaces {@link ItemEntry} and {@link CollectionEntry}
 * is more common. However, this interface may be used in cases when shared
 * functionality is required.
 * <p>
 * A unit entry handles only one entry per key. Contrast this with
 * {@link ScatteredEntry}, which handles cases when the key is shared across
 * more than one entry.
 * 
 */
public interface BaseUnitEntry extends HasKey {

	/**
	 * Indicates whether there's an entry with the key.
	 * <p>
	 * Note that this method doesn't take into account the type of the value in
	 * the key/value pair.
	 * 
	 * @return true if an entry with the key exists; false, otherwise
	 */
	boolean isAssigned();
}
