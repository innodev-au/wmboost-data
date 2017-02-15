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

/**
 * <p>
 * Provides factory methods for the creation of {@link Document} instances. It
 * allows the creation a {@link Document} based on an existing {@link com.wm.data.IData}
 * instance and also the creation of a {@link Document} from scratch.
 *
 * <p>
 * An internal implementation for this interface is provided. This interface is not meant to
 * be implemented outside this library.
 */
public interface DocumentFactory {

	Document wrap(IData iData);

	Document create();
}
