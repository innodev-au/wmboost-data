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

import java.util.Date;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.StringUtils;

import com.wm.data.MBoolean;
import com.wm.data.MByte;
import com.wm.data.MDouble;
import com.wm.data.MFloat;
import com.wm.data.MInteger;
import com.wm.data.MLong;
import com.wm.data.MShort;

/**
 * Provides utilities to create {@link ConversionService} instances.
 * <p>
 * You may use methods in this class to create your own instances based on the
 * ones provided by default.
 */
public final class ConversionServiceUtils {

	public static ConfigurableConversionService createDefaultConversionService() {
		GenericConversionService conversionService = new GenericConversionService();

		addDateConverters(conversionService);

		addWmConverters(conversionService);

		DefaultConversionService.addDefaultConverters(conversionService);

		return conversionService;
	}

	private static void addDateConverters(GenericConversionService conversionService) {

		conversionService.addConverter(Date.class, String.class, new Converter<Date, String>() {
			@Override
			public String convert(Date dateVal) {
				return ValueConversionUtil.dateToIsoString(dateVal);
			}
		});
		conversionService.addConverter(String.class, Date.class, blankConverter(new Converter<String, Date>() {
			@Override
			public Date convert(String stringVal) {
				return ValueConversionUtil.isoStringToDate(stringVal);
			}
		}));

	}

	static <T> Converter<String, T> blankConverter(final Converter<String, T> converter) {
		return new Converter<String, T>() {
			@Override
			public T convert(String string) {
				return (StringUtils.hasText(string)) ? converter.convert(string) : null;
			}
		};
	}

	private static void addWmConverters(ConfigurableConversionService conversionService) {
		addMScalars(conversionService);

	}

	private static void addMScalars(ConfigurableConversionService conversionService) {
		/** boolean **/
		conversionService.addConverter(MBoolean.class, Boolean.class, new Converter<MBoolean, Boolean>() {
			@Override
			public Boolean convert(MBoolean mboolean) {
				return mboolean.booleanValue();
			}
		});
		conversionService.addConverter(Boolean.class, MBoolean.class, new Converter<Boolean, MBoolean>() {
			@Override
			public MBoolean convert(Boolean booleanVal) {
				return new MBoolean(booleanVal);
			}
		});

		/** Numbers **/
		NumberConverterGenerator<MLong, Long> longGen = new NumberConverterGenerator<MLong, Long>(MLong.class, Long.class,
				conversionService);
		longGen.addToMConverters(new Converter<Long, MLong>() {
			@Override
			public MLong convert(Long value) {
				return new MLong(value);
			}
		});

		NumberConverterGenerator<MInteger, Integer> intGen = new NumberConverterGenerator<MInteger, Integer>(MInteger.class,
				Integer.class, conversionService);
		intGen.addToMConverters(new Converter<Integer, MInteger>() {
			@Override
			public MInteger convert(Integer value) {
				return new MInteger(value);
			}
		});

		NumberConverterGenerator<MShort, Short> shortGen = new NumberConverterGenerator<MShort, Short>(MShort.class, Short.class,
				conversionService);
		shortGen.addToMConverters(new Converter<Short, MShort>() {
			@Override
			public MShort convert(Short value) {
				return new MShort(value);
			}
		});

		NumberConverterGenerator<MByte, Byte> byteGen = new NumberConverterGenerator<MByte, Byte>(MByte.class, Byte.class,
				conversionService);
		byteGen.addToMConverters(new Converter<Byte, MByte>() {
			@Override
			public MByte convert(Byte value) {
				return new MByte(value);
			}
		});

		NumberConverterGenerator<MDouble, Double> doubleGen = new NumberConverterGenerator<MDouble, Double>(MDouble.class,
				Double.class, conversionService);
		doubleGen.addToMConverters(new Converter<Double, MDouble>() {
			@Override
			public MDouble convert(Double value) {
				return new MDouble(value);
			}
		});

		NumberConverterGenerator<MFloat, Float> floatGen = new NumberConverterGenerator<MFloat, Float>(MFloat.class, Float.class,
				conversionService);
		floatGen.addToMConverters(new Converter<Float, MFloat>() {
			@Override
			public MFloat convert(Float value) {
				return new MFloat(value);
			}
		});

	}

	private ConversionServiceUtils() {
		// Non-instantiable
	};
}
