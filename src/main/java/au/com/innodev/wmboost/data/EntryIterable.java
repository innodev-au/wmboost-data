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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Implementation of {@link EntryIterableResource}
 */
class EntryIterable implements EntryIterableResource {

	private final List<EntryIterator> iterators;
	private final DocumentImpl document;

	EntryIterable(DocumentImpl document) {
		Preconditions.checkNotNull(document, "document cannot be null");
		this.document = document;
		this.iterators = new LinkedList<EntryIterator>();
	}
	
	@Override
	public Iterator<KeyValue> iterator() {		
		EntryIterator iterator = new EntryIterator(document);
		iterators.add(iterator);
		return iterator;
	}

	@Override
	public void close() {
		for (EntryIterator iterator : iterators) {
			iterator.close();
		}
	}

}
