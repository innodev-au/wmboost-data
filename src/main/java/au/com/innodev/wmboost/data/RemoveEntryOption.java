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

/**
 * Strictness option for removing entries.
 */
public enum RemoveEntryOption {

	/** Throws an exception if the entry can't be removed, such as when the entry doesn't actually exist */
	STRICT, 
	
	/** Attempts to remove the entry, but if it doesn't exist or can't be removed for some other reason, it doesn't throw an exception */
	LENIENT;
	
}