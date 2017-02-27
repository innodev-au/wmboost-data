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

import static au.com.innodev.wmboost.data.NormaliseOption.DONT_NORMALISE;

import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IData;

class NestedDocEntryImpl extends ItemEntryImpl<Document> implements NestedDocEntry {

	private final DocumentFactory documentFactory;

	public NestedDocEntryImpl(DocumentImpl document, DocumentFactory factory, String key) {
		super(document, key, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(IData.class), DONT_NORMALISE);
		this.documentFactory = factory;
	}
	
	public Document putNew() {
		Document document = documentFactory.create();
		put(document);
		
		return document;
	}
}
