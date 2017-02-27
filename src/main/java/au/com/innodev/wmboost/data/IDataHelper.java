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

import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import com.wm.data.IData;

class EntryUtil {

	public static Object normaliseValueForPut(Object value, ConversionService conversionService) {
		if (value instanceof Document) {
			return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData.class));
		}
		else if (value instanceof Document[]) {
			return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData[].class));
		}
		else if (value instanceof Iterable<?>) {
			if (CollectionUtil.areAllElementsOfType((Collection<?>) value, Document.class)) {
				return conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(IData[].class));
			}
			else {
				return value;
			}
			 
		}
		else {
			return value;
		}
	}
	
	public static <A> A normaliseValueForGet(A value, ConversionService conversionService) {
		if (value instanceof IData) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), TypeDescriptor.valueOf(Document.class));
			return normalised;
		}		
		else if (value instanceof IData[]) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), getDocListType());
			return normalised;
		}
		else if (value instanceof Object[]) {
			@SuppressWarnings("unchecked")
			A normalised = (A) conversionService.convert(value, TypeDescriptor.forObject(value), getObjectListType());
			return normalised;
		}
		else {
			return value;
		}
	}
	
	private static TypeDescriptor getObjectListType() {
		TypeDescriptor docListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Object.class));
		return docListType;
	}
	
	private static TypeDescriptor getDocListType() {
		TypeDescriptor docListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Document.class));
		return docListType;
	}
}
