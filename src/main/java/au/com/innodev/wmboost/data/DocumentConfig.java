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

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>Document configuration, including conversion service and IData factory.
 */
final class DocumentConfig {

	private final ConversionService conversionService;
	private final ConversionService internalConversionService;	
	private final DirectIDataFactory directIDataFactory;
	
	public DocumentConfig(ConversionService conversionService, ConversionService internalConversionService, DirectIDataFactory directIDataFactory) {
		this.conversionService = Preconditions.checkNotNull(conversionService);
		this.internalConversionService = Preconditions.checkNotNull(internalConversionService);
		this.directIDataFactory = Preconditions.checkNotNull(directIDataFactory);
	}
	
	
	ConversionService getConversionService() {
		return conversionService;
	}
	
	ConversionService getInternalConversionService() {
		return internalConversionService;
	}
	
	public DirectIDataFactory getCustomIDataFactory() {
		return directIDataFactory;
	}
	
}
