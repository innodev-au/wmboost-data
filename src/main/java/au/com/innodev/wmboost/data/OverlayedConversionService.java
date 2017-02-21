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

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * A set of layers of {@link ConversionService}.
 * 
 * <p>Allows adding conversion services on top of base one. 
 */
class OverlayedConversionService implements ConversionService {

	private final List<ConversionService> conversionServices;
	private final List<ConversionService> allButLast;
	private final ConversionService last;
	
	public OverlayedConversionService(List<ConversionService> conversionServices) {
		super();
		this.conversionServices = Preconditions.checkNotEmpty(conversionServices);
		this.last = conversionServices.get(conversionServices.size() - 1);
		this.allButLast = new ArrayList<ConversionService>(conversionServices.size() - 1);
		
		for (int i = 0; i < conversionServices.size() - 1; i++) {
			allButLast.add(conversionServices.get(i));
		}
		
	}

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return canConvert(TypeDescriptor.valueOf(sourceType), TypeDescriptor.valueOf(targetType));
	}

	@Override
	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		for (ConversionService conversionService : conversionServices) {
			if (conversionService.canConvert(sourceType, targetType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T convert(Object source, Class<T> targetType) {
		return (T) convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
		
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		for (ConversionService conversionService : allButLast) {
			if (conversionService.canConvert(sourceType, targetType)) {
				return conversionService.convert(source, sourceType, targetType);			
			}
		}
		
		return last.convert(source, sourceType, targetType);
	}

}
