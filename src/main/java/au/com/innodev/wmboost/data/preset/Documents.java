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
package au.com.innodev.wmboost.data.preset;

import com.wm.data.IData;

import au.com.innodev.wmboost.data.Document;
import au.com.innodev.wmboost.data.DocumentFactory;
import au.com.innodev.wmboost.data.internal.Preconditions;

/**
 * <p>
 * Convenience class for the creation of {@link Document} classes.
 * <p>
 * If you'd like to use an existing {@link com.wm.data.IData} instance, use the
 * {@link #wrap(IData)} method. The typical use case is to wrap a pipeline
 * {@code IData} instance when implementing a Java service in webMethods.
 * <p>
 * If you'd like to create a new document (with it's) associated {@code IData}
 * instance, invoke the {@link #create()} method.
 * <p>
 * It provides static versions of the {@link DocumentFactory} methods provided
 * by {@link DocumentFactories#getDefault()}
 * <p>
 * This class is used when the default behaviour is enough. If you need to
 * customise the type conversion or the {@link com.wm.data.IData} instances that are
 * created, you may create an alternative convenience class that returns your
 * own that {@link DocumentFactory} instance.
 * 
 * @see Document
 */
public final class Documents {

	/**
	 * Wraps an existing {@link com.wm.data.IData} instance for easy manipulation in a
	 * {@link Document}.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * public static final void sayHello(IData pipeline) throws ServiceException {
	 * 	  Document pipelineDoc = Documents.wrap(pipeline);
	 * 	  pipelineDoc.entry("message").put("hello");
	 * }
	 * </pre>
	 * 
	 * Note that in the example there's not need to wrap the code in try-finally because no cursors are used.
	 * 
	 * @param iData
	 *            the {@code IData} document to wrap
	 * @return a wrapping {@code Document} instance.
	 */
	public static Document wrap(IData iData) {
		Preconditions.checkNotNull(iData, "iData cannot be null");
		return DocumentFactories.getDefault().wrap(iData);
	}

	/**
	 * Creates an initially-empty {@link Document}.
	 * 
	 * @return a new {@code Document}
	 */
	public static Document create() {
		return DocumentFactories.getDefault().create();
	}


	private Documents() {
		// Non-instantiable
	}

}
