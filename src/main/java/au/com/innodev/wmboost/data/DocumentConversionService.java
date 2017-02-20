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

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import com.wm.data.IData;

/**
 * <p>Converts {@link Document} to {@link com.wm.data.IData} and vice-versa.
 * 
 * <p>This implementation overcomes a limitation of not being able to access certain
 * {@link DefaultConversionService} in Spring version 3.x (used by wM, at least as of 9.12). Otherwise, a simple
 * DefaultConversionService instance with custom converters could've been used.
 * 
 */
class DocumentConversionService implements ConversionService {

	private DefaultConversionService internalService = new DefaultConversionService();

	public DocumentConversionService(final DocumentFactory docFactory) {
		super();

		internalService.addConverter(IData.class, Document.class, new Converter<IData, Document>() {
			@Override
			public Document convert(IData idata) {
				return docFactory.wrap((IData) idata);
			}
		});
		internalService.addConverter(Document.class, IData.class, new Converter<Document, IData>() {
			@Override
			public IData convert(Document document) {
				return ((Document) document).getIData();
			}
		});
	}

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return (sourceType != null && targetType != null)
				&& ((Document.class.isAssignableFrom(sourceType) && IData.class.isAssignableFrom(targetType))
						|| (IData.class.isAssignableFrom(sourceType) && Document.class.isAssignableFrom(targetType)));
	}

	public boolean internalCanConvert(Class<?> sourceType, Class<?> targetType) {
		return (sourceType == null || targetType == null)
				|| ((Document.class.isAssignableFrom(sourceType) && IData.class.isAssignableFrom(targetType))
						|| (IData.class.isAssignableFrom(sourceType) && Document.class.isAssignableFrom(targetType)));
	}

	@Override
	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (!isDescriptorOK(sourceType)) {
			return false;
		}

		if (!isDescriptorOK(targetType)) {
			return false;
		}

		return internalCanConvert(getType(sourceType), getType(targetType));
	}

	private boolean isDescriptorOK(TypeDescriptor typeDescriptor) {
		if (typeDescriptor == null) {
			return false;
		} else if (typeDescriptor.isArray() || typeDescriptor.isCollection()) {
			TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
			if (elementTypeDesc != null) {
				return isClassAcceptable(elementTypeDesc.getType());
			} else {
				return true;
			}
		} else {
			return isClassAcceptable(typeDescriptor.getType());
		}
	}

	private boolean isClassAcceptable(Class<?> type) {
		return Document.class.isAssignableFrom(type) || IData.class.isAssignableFrom(type);
	}

	private Class<?> getType(TypeDescriptor typeDescriptor) {

		if (typeDescriptor == null) {
			return null;
		} else if (typeDescriptor.isArray() || typeDescriptor.isCollection()) {
			TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
			if (elementTypeDesc != null) {
				return elementTypeDesc.getType();
			} else {
				return null;
			}
		} else {
			return typeDescriptor.getObjectType();
		}

	}

	@Override
	public <T> T convert(Object source, Class<T> targetType) {
		return internalService.convert(source, targetType);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return internalService.convert(source, sourceType, targetType);
	}

}
