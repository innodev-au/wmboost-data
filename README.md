# wmboost-data

A free and open source Java library that makes it easier to access and manipulate documents in webMethods. It can be used for either server or client-side code.

It provides an abstraction for Integration Server documents. Compared to coding directly with out-of-the-box classes like IData and IDataUtil, it:
* Reduces boilerplate code
* Provides an easy-to-use and consistent abstraction. For instance, there's usually no need to deal with IDataCursor
* Supports predictable and extensible type conversion mechanisms. For instance, you may get a boolean value from an entry with a string in a more reliable way. IDataUtil.getBoolean returns _false_ even for invalid string representations such as _hello_.
* Collections are exposed instead of arrays
* Allows setting expectations about values being retrieved. For instance, it makes it easy to say that you want to retrieve a mandatory input parameter in your service implementation

It doesn't go as far as radically changing the way you implement Java services. You still need to imperatively retrieve and set document entries, but it gives you an important simplicity boost.

Internally, the abstraction is transparent and IData is used as expected by Integration Server.

## Project Status

The library is at a state where the functionality has been verified to work correctly and is stable enough to work in a production environment - with the exception of split (scattered) entries. However, until the release of version 1.0 (planned for March), some API changes may occur.

## Documentation

Refer to the [Wiki section](https://github.com/innodev-au/wmboost-data/wiki) for documentation. You may want to jump to the [Quick Start](https://github.com/innodev-au/wmboost-data/wiki/Quick%20Start) section directly.

## Motivational Example
The following example shows the implementation of a service that takes a number, _numberToAdd_ and adds it to each element of a list of numbers, _initialList_. The result is returned in the _newList_ pipeline variable. In the example, the two input parameters are mandatory but a more lenient implementation would be possible if this service was used as a transformer.

```java
public static final void addNumToList(IData pipeline) throws ServiceException {
  Document pipeDoc = Documents.wrap(pipeline);

  int numToAdd = pipeDoc.intEntry("numToAdd").getNonNullVal();
  List<Integer> originalList = pipeDoc.intsEntry("originalList").getNonNullVal();

  List<Integer> newList = new ArrayList<>(originalList.size());

  for(Integer listNum : originalList) {
    // Adds the number to the list element
    // Individual null values in the list are allowed, in which case null is assigned
    Integer newValue = (listNum != null) ? listNum + numToAdd : null;
    newList.add(newValue);
  }

  pipeDoc.stringsEntry("newList").putConverted(newList);
}
```

Compare this code with the equivalent implementation with out-of-the-box webMethods classes:
```java
public static final void addNumToList(IData pipeline) throws ServiceException {
  IDataCursor pipelineCursor = pipeline.getCursor();
  try {
    String numToAddString = IDataUtil.getString(pipelineCursor, "numToAdd");
    String[] originalList = IDataUtil.getStringArray(pipelineCursor, "originalList");
    
    if (numToAddString == null) {
      throw new RuntimeException("'numToAdd' parameter wasn't provided or was null");
    }  
    
    if (originalList == null) {
      throw new RuntimeException("'originalList' parameter wasn't provided");
    }
    
    int numToAdd = stringToNumber(numToAddString, "numToAdd");
  
    String[] newList = new String[originalList.length];
  
    for (int i = 0; i < originalList.length; i++) {
      String itemString = originalList[i];    
      Integer listNum = stringToNumber(itemString, "originalList");
    
      // Adds the number to the list element
      // Individual null values in the list are allowed, in which case null is assigned
      Integer newValue = (listNum != null) ? listNum + numToAdd : null;
    
      newList[i] = String.valueOf(newValue);
    }
  
    IDataUtil.put(pipelineCursor, "newList", newList);
  } finally {
    pipelineCursor.destroy();
  }
}

// In shared source code area
private static Integer stringToNumber(String stringValue, String field) {
  try {
    return Integer.valueOf(stringValue);
  }
  catch (NumberFormatException e) {
    throw new RuntimeException("Value in '" + field + "' is not a valid integer number");
  }
}
```

The example highlights some benefits, such as:

* *Less boilerplate*. The code is more expressive and, evidently, shorter.
* *Automatic type conversion*. _numToAdd_ is retrieved as an integer, with a conversion from a String or another type, if necessary. Retrieving _numToAdd_ with IData requires retrieving a String. We could've used IDataUtil.getInt but unfortunately there's no overloaded method without a default value. This would've forced us to check against an arbitrary value to see if the conversion had actually failed.
* *Input parameter expectations*. One of the available value getter methods may be invoked to indicate whether the entry is expected to be present, both expected to be present and non-null as in the example, etc. If the expectation is not met, an exception is thrown. The exception message includes the key for easier troubleshooting. These checks are a good practice even when _Validate Input_ is turned on for the service.
* *Collections are used directly*. With IData, arrays are used.
* *No try/finally*. There's no need to wrap the code in try/finally to destroy the cursor. This is done internally by the library

You'll note that the second code excerpt includes a utility method for performing a conversion. Several aspects could be extracted into utility methods that way in order to simplify the code. In fact, this library started out as a set of utility methods to cover entry access, conversion and other aspects.
