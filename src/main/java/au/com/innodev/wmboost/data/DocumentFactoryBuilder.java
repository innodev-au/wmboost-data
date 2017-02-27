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
 * Builds a {@link DocumentFactory}.
 * 
 */
public class DocumentFactoryBuilder {

	private ConversionService conversionService;
	private DirectIDataFactory directIDataFactory;	

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public void setDirectIDataFactory(DirectIDataFactory directIDataFactory) {
		this.directIDataFactory = directIDataFactory;
	}

	public DocumentFactory build()
	{
		Preconditions.checkNotNull(conversionService, "ConversionService cannot be null");
		Preconditions.checkNotNull(directIDataFactory, "directIDataFactory cannot be null");
				
		return new DefaultDocumentFactory(conversionService, directIDataFactory);
	}
	
	
}
