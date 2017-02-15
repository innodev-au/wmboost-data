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

import com.wm.data.IData;

import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * Default implementation of {@link DocumentFactory}.
 * 
 * <p>Create a new instance if you wish to customise either the conversion or how {@code IData} instances are created. 
 */
public class DefaultDocumentFactory implements DocumentFactory {

	private final DocumentConfig config;
	
	public DefaultDocumentFactory(ConversionService conversionService, DirectIDataFactory directIDataFactory) {
		Preconditions.checkNotNull(conversionService, "ConversionService cannot be null");
		Preconditions.checkNotNull(directIDataFactory, "directIDataFactory cannot be null");
		
		this.config = new DocumentConfig(conversionService, createInternalConversionService(conversionService), directIDataFactory);
	}
	
	@Override
	public Document create() {
		IData iData = config.getCustomIDataFactory().create();
		return wrap(iData);
	}

	@Override
	public Document wrap(IData iData) {
		Preconditions.checkNotNull(iData, "iData cannot be null");
		return new DocumentImpl(iData, config);
	}

	private ConversionService createInternalConversionService(ConversionService baseConversionService) {
		List<ConversionService> conversionServices = new ArrayList<ConversionService>();
		conversionServices.add(createIDataResourceConversionService());
		conversionServices.add(baseConversionService);
		
		return new OverlayedConversionService(conversionServices);
		
	}

	private ConversionService createIDataResourceConversionService() {
		return new DocumentConversionService(this);
	}

}
