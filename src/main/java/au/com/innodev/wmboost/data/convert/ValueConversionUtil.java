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
package au.com.innodev.wmboost.data.convert;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.springframework.core.convert.ConversionException;

import au.com.innodev.wmboost.data.internal.Preconditions;

class ValueConversionUtil {

	static class InvalidValueConversionException extends ConversionException {

		private static final long serialVersionUID = -5083814465544409140L;

		public InvalidValueConversionException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public static String dateToIsoString(Date date) {
		Preconditions.checkNotNull(date);
		
		return ISO8601Utils.format(date, true);
		
	}

	public static Date isoStringToDate(String stringVal) {
		Preconditions.checkHasLength(stringVal);
		
		try {
			return ISO8601Utils.parse(stringVal, new ParsePosition(0));
		} catch (ParseException e) {
			throw new InvalidValueConversionException("Unable to convert string to date. String was not a valid ISO date [" + stringVal + "]", e);
		}
		
	}
}
