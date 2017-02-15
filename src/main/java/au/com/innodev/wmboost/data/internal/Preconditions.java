package au.com.innodev.wmboost.data.internal;
/*
 * Adapted from parts of the Preconditions class in Guava project
 * 
 * ------------------------------------------------------
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.Collection;

import org.springframework.util.Assert;


/**
 * NOT INTENDED FOR USE OUTSIDE THIS LIBRARY
 */
public final class Preconditions {

	  /**
	   * Ensures that an object reference passed as a parameter to the calling method is not null.
	   *
	   * @param reference an object reference
	   * @param <T> object type
	   * @return the non-null reference that was validated
	   * @throws NullPointerException if {@code reference} is null
	   */
	  
	  public static <T> T checkNotNull(T reference) {
	    if (reference == null) {
	      throw new NullPointerException();
	    }
	    return reference;
	  }

	  /**
	   * Ensures that an object reference passed as a parameter to the calling method is not null.
	   *
	   * @param reference an object reference
	   * @param errorMessage the exception message to use if the check fails; will be converted to a
	   *     string using {@link String#valueOf(Object)}
	   * @param <T> object type
	   * @return the non-null reference that was validated
	   * @throws NullPointerException if {@code reference} is null
	   */
	  
	  public static <T> T checkNotNull(T reference, String errorMessage) {
	    if (reference == null) {
	      throw new NullPointerException(errorMessage);
	    }
	    return reference;
	  }
	  
	  
	  public static String checkHasLength(String value) {
		  Assert.hasLength(value);
		  return value;
	  }
	  
	  public static String checkHasLength(String value, String errorMessage) {
		  Assert.hasLength(value, errorMessage);
		  return value;
	  }
	  
	  public static <T extends Collection<?>> T checkNotEmpty(T value) {
		  Assert.notEmpty(value);
		  return value;
	  }
	  
	  public static <T extends Collection<?>> T checkNotEmpty(T value, String errorMessage) {
		  Assert.notEmpty(value, errorMessage);
		  return value;
	  }
	  
	  
}
