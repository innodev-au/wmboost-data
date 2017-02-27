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

import org.springframework.util.StringUtils;

class StringEntryImpl extends ItemEntryImpl<String> implements StringEntry {

	public StringEntryImpl(DocumentImpl document, String key) {
		super(document, key, String.class, NormaliseOption.DONT_NORMALISE);
	}

	@Override
	public String getNonEmptyVal() throws InexistentEntryException, UnexpectedEntryValueException {
		String val = getNonNullVal();
		if (val.isEmpty()) {
			throw new UnexpectedEntryValueException(
					"Unexpected value was found for entry with key '" + getKey() + "'. Value was an empty string.");
		}
		return val;
	}

	@Override
	public String getNonBlankVal() throws InexistentEntryException, UnexpectedEntryValueException {
		String val = getNonNullVal();
		if (! StringUtils.hasText(val)) {
			throw new UnexpectedEntryValueException(
					"Unexpected value was found for entry with key '" + getKey() + "'. Value was [" + val + "]");
		}
		return val;
	}

}
