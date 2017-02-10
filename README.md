# wmboost-data

A _free and open source_ Java library that makes it easier to access and manipulate documents in webMethods. It can be used for either server or client-side code.

It provides an abstraction for Integration Server documents. Compared to coding directly with IData and IDataUtil, it:
* Reduces boilerplate code
* Provides an easy-to-use and consistent abstraction. For instance, there's usually no need to deal with IDataCursor
* Supports predictable and extensible type conversion mechanisms. For instance, you may get a boolean value from a string in a more reliable way than IDataUtil.getBoolean, which returns _false_ even for invalid string representations such as _hello_.
* Allows setting expectations about values being retrieved. For instance, it makes it easy to say that you want to retrieve a mandatory parameter in your service implementation

It doesn't go as far as radically changing the way you implement Java services - you still need to imperatively retrieve and set document entries - but it makes it simpler.

Internally, the abstraction is transparent and IData is used as expected by Integration Server.

## Example
The following example shows the implementation of a service that takes a number, _numberToAdd_ and adds it to each element of a list of numbers, _initialList_. The result is returned in the _newList_ pipeline variable. In the example, the two input parameters are mandatory (a more lenient ):

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

Compare this code with the equivalent implementation with regular webMethods classes:
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
 
