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

import com.wm.data.IData;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Implementation of {@link DocEntryIterableResource}
 */
class IDataEntryIterable implements DocEntryIterableResource {

	private final IData iData;
	private final List<IDataEntryIterator> iterators;

	IDataEntryIterable(IData iData) {
		Preconditions.checkNotNull(iData, "iData cannot be null");
		this.iData = iData;
		this.iterators = new LinkedList<IDataEntryIterator>();
	}
	
	@Override
	public Iterator<KeyValue> iterator() {		
		IDataEntryIterator iterator = new IDataEntryIterator(iData);
		iterators.add(iterator);
		return iterator;
	}

	@Override
	public void close() {
		for (IDataEntryIterator iterator : iterators) {
			iterator.close();
		}
	}

}
