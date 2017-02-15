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

import com.wm.data.IData;

import au.com.innodev.wmboost.data.support.DirectIDataFactories;

/**
 * <p>Base factory for the creation of {@link com.wm.data.IData} instances. Subclasses instantiate
 * a specific {@link com.wm.data.IData} implementation (subclass).
 * 
 * <p>Use {@link DirectIDataFactories} for pre-defined implementations. You may
 * also provide a new implementation if you need a specific {@link com.wm.data.IData} implementation
 * to be created when manipulating {@link Document} content.
 * 
 * <p>Note that this factory differs from {@link com.wm.data.IDataFactory}.
 *
 */
public interface DirectIDataFactory {

	public IData create();
}
