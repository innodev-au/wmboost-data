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

import com.wm.data.IData;

import au.com.innodev.wmboost.data.DirectIDataFactory;

/**
 * <p>
 * Provides pre-defined {@link DirectIDataFactory} implementations.
 * 
 */
public class DirectIDataFactories {

	private static final WmDefaultDataFactory WM_DEFAULT = new WmDefaultDataFactory();

	/**
	 * Returns the default webMethods runtime factory.
	 * 
	 * As of webMethods 9.x, default {@link com.wm.data.IData} implementations are of type {@link com.wm.util.data.MemData}.
	 * 
	 * @return the default webMethods IData factory
	 */
	public static final DirectIDataFactory getWmDefaultRuntimeFactory() {
		return WM_DEFAULT;
	}

	private static class WmDefaultDataFactory implements DirectIDataFactory {

		@Override
		public IData create() {
			return com.wm.data.IDataFactory.create();
		}

	}

	private DirectIDataFactories() {
		// Non-instantiable
	}
}
