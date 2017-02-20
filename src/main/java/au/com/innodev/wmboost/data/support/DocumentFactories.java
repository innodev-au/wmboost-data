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
package au.com.innodev.wmboost.data.support;

import au.com.innodev.wmboost.data.DocumentFactory;
import au.com.innodev.wmboost.data.DocumentFactoryBuilder;
import au.com.innodev.wmboost.data.convert.ConversionServiceUtils;

/**
 * <p>Provides pre-defined {@link DocumentFactory} implementations.
 * 
 */
public class DocumentFactories {
	private final static DocumentFactory DEFAULT_DOCUMENT_FACTORY;
	
	static {
		
		DocumentFactoryBuilder factoryBuilder = new DocumentFactoryBuilder();
		factoryBuilder.setConversionService(ConversionServiceUtils.createDefaultConversionService());
		factoryBuilder.setDirectIDataFactory(DirectIDataFactories.getWmDefaultRuntimeFactory());
		
		DEFAULT_DOCUMENT_FACTORY = factoryBuilder.build();
	}
	
	/**
	 * Returns the default document factory
	 *  
	 * @return the default document factory
	 */
	public static DocumentFactory getDefault() {
		return DEFAULT_DOCUMENT_FACTORY;
	}
	
	private DocumentFactories() {
		// Non-instantiable
	}
	
}
