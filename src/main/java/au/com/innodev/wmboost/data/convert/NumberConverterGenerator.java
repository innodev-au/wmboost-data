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
package au.com.innodev.wmboost.data.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;

import au.com.innodev.wmboost.data.internal.Preconditions;

final class NumberConverterGenerator<M, S extends Number> {

	private final Class<S> scalarType;
	private final Class<M> mutableType;
	private final ConfigurableConversionService conversionService;
		
	public NumberConverterGenerator(Class<M> mutableType, Class<S> scalarType, ConfigurableConversionService conversionService) {
		super();
		this.scalarType = Preconditions.checkNotNull(scalarType);
		this.mutableType = Preconditions.checkNotNull(mutableType);
		this.conversionService = Preconditions.checkNotNull(conversionService);
	}
	
	// Converts number to the expected intermediate type (e.g. Integer). Then it converts it to the target type (e.g. MInteger)
	
	public void addToMConverters(Converter<S, M> scalarToTargetConverter)  {
		addNumberToMConverter(scalarToTargetConverter);
		
		addStringToMConverter(scalarToTargetConverter);
	}
	
	private void addNumberToMConverter(final Converter<S, M> scalarToTargetConverter) {
					
			Converter<Number, M> numberToMConverter = 
				new Converter<Number, M>() {
				@Override
				public M convert(Number number) {
					S intermediateValue = conversionService.convert(number, scalarType);
					return scalarToTargetConverter.convert(intermediateValue);	
				}
			};
			
		conversionService.addConverter(Number.class, mutableType, numberToMConverter);		
	}
	
	private void addStringToMConverter(final Converter<S, M> scalarToTargetConverter) {
		Converter<String, M> numberToMConverter = 
				new Converter<String, M>() {
			@Override
			public M convert(String numberStr) {
				S intermediateValue = conversionService.convert(numberStr, scalarType);
				return scalarToTargetConverter.convert(intermediateValue);
				
			}
		};
			
		conversionService.addConverter(String.class, mutableType, ConversionServiceUtils.blankConverter(numberToMConverter));
	}
	
	
}


