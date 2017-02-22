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

import java.io.Closeable;

/**
 * <p>
 * An iterable for document entries that is also exposed as a resource (i.e., a
 * {@link Closeable}).
 * <p>In order to release internal resources once the iteration
 * is over, invoke the {@link #close() method}.
 */
public interface EntryIterableResource extends Iterable<KeyValue>, Closeable {
	void close();
}
