/**
 * Portions copied and adapted from Apache Commons Lang.
 * 
 */

/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package au.com.innodev.wmboost.data.internal;

/**
 * Text utilities
 */
public class TextUtil {

	public static String abbreviate(final String str, int offset, final int maxWidth) {
		if (str == null) {
		      return null;
		  }
		  if (maxWidth < 4) {
		      throw new IllegalArgumentException("Minimum abbreviation width is 4");
		  }
		  if (str.length() <= maxWidth) {
		      return str;
		  }
		  if (offset > str.length()) {
		      offset = str.length();
		  }
		  if (str.length() - offset < maxWidth - 3) {
		      offset = str.length() - (maxWidth - 3);
		  }
		  final String abrevMarker = "...";
		  if (offset <= 4) {
		      return str.substring(0, maxWidth - 3) + abrevMarker;
		  }
		  if (maxWidth < 7) {
		      throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
		  }
		  if (offset + maxWidth - 3 < str.length()) {
		      return abrevMarker + abbreviate(str.substring(offset), maxWidth - 3);
		  }
		  return abrevMarker + str.substring(str.length() - (maxWidth - 3));

	}
	
	public static String abbreviate(final String str, final int maxWidth) {
		 return abbreviate(str, 0, maxWidth);
	}

	public static Object abbreviateObj(Object value, int maxWidth) {		
		return (value != null) ? abbreviate(value.toString(), maxWidth) : null;
	}

}
