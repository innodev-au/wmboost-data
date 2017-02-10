# wmboost-data

A _free and open source_ Java library that makes it easier to access and manipulate documents in webMethods. It can be used for either server or client-side code.

It provides an abstraction for Integration Server documents. Compared to coding directly with IData and IDataUtil, it:
* Reduces boilerplate code
* Provides an easy-to-use and consistent abstraction. For instance, there's usually no need to deal with IDataCursor
* Supports predictable and extensible type conversion mechanisms. For instance, you may get a boolean value from a string in a more reliable way than IDataUtil.getBoolean, which returns _false_ even for invalid string representations such as _hello_.
* Allows setting expectations about values being retrieved. For instance, it makes it easy to say that you want to retrieve a mandatory parameter in your service implementation

It doesn't go as far as radically changing the way you implement Java services. You still need to imperatively retrieve and set document entries, but it gives you a simplicity boost.

Internally, the abstraction is transparent and IData is used as expected by Integration Server.

## Example
The following example shows the implementation of a service that takes a number, _numberToAdd_ and adds it to each element of a list of numbers, _initialList_. The result is returned in the _newList_ pipeline variable. In the example, the two input parameters are mandatory but a more lenient implementation would be possible if this service was for a transformer.

```java
Document pipeDoc = Documents.wrap(pipeline);

int numToAdd = pipeDoc.entryOfInteger("numToAdd").getNonNullVal();
List<Integer> originalList = pipeDoc.entryOfIntegers("originalList").getVal();

List<Integer> newList = new ArrayList<>(originalList.size());

for(Integer listNum : originalList) {
  // Adds the number to the list element
  // Individual null values in the list are allowed, in which case null is assigned
  Integer newValue = (listNum != null) ? listNum + numToAdd : null;
  newList.add(newValue);
}

pipeDoc.entryOfStrings("newList").putConverted(newList);
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

* The code is more expressive and - evidently - shorter.
* _numToAdd_ is retrieved as an integer, with a conversion from a String or another type, if necessary. Retrieveing _numToAdd_ with IData requires retrieveing a String. We could've used IDataUtil.getInt but unfortunately there's no overloaded method without a default value. This would've forced us to compared a default value to see if the conversion actually had failed.
* One of the available value getter methods may be invoked to indicate whether the entry is expected to be present, both expected to be present and non-null as in the example, etc. If the expectation is not met, an exception is thrown. The exception message includes the key for easier troubleshooting.
* Collections are used directly. With IData, arrays are used.
* There's no need to wrap the code in try/finally to destroy the cursor. This is done internally by the library

You'll note that the second code excerpt includes a utility method for performing a conversion. Several aspects could be extracted into utility methods that way in order to simplify the code. In fact, this library started out as a set of utility methods for entry access, conversion and other aspects.
