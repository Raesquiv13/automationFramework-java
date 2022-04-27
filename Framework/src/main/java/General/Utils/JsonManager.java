package General.Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;


public class JsonManager {

  String strOutput;
  boolean result;
  JSONObject jsonObj;
  JSONParser jsonParser = new JSONParser();

  public JSONObject getJsonObject(String object) {
    try {
      this.jsonObj = (JSONObject) jsonParser.parse(object);
    } catch (ParseException e) {

      e.printStackTrace();
    }
    return this.jsonObj;
  }

  public JSONObject getJsonObjectFromFile(String file) {
    try {
      Object obj;
      obj = jsonParser.parse(new FileReader(file));
      this.jsonObj = (JSONObject) obj;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.jsonObj;
  }

  public JSONArray getJsonArray_FromString(String object) {
    JSONArray jsonArr = new JSONArray();
    try {
      jsonParser = new JSONParser();
      jsonArr = (JSONArray) jsonParser.parse(object);
    } catch (ParseException e) {

      e.printStackTrace();
    }
    return jsonArr;
  }

  public JSONObject createJsonObject() {
    JSONObject json = new JSONObject();
    return json;
  }

  public JSONArray createJsonArray() {
    JSONArray jsonArr = new JSONArray();
    return jsonArr;
  }

  public String getJsonObjectValue(JSONObject json, String object) {
    if (json == null) {
      strOutput = "null";
    } else {
      strOutput = json.get(object) != null ? json.get(object).toString() : "null";
    }
    return strOutput;
  }

  public JSONObject getJsonObject_SecondLevelJsonObject(JSONObject json, String object1, String object2) {
    if (json == null) {
      this.jsonObj = new JSONObject();
    } else {
      this.jsonObj = getJsonObject(json, object1);
      this.jsonObj = getJsonObject(this.jsonObj, object2);
    }
    return this.jsonObj;
  }

  public String getValue_SecondLevenJsonObject(JSONObject json, String object1, String object2) {
    if (json == null) {
      strOutput = "null";
      this.jsonObj = new JSONObject();
    } else {
      this.jsonObj = getJsonObject(json, object1);
      strOutput = getJsonObjectValue(this.jsonObj, object2) != null ? getJsonObjectValue(this.jsonObj, object2).toString() : "null";
    }
    return strOutput;
  }

  public String getValue_ThirdLevenJsonObject(JSONObject json, String object1, String object2, String objeto3) {
    if (json == null) {
      strOutput = "null";
      this.jsonObj = new JSONObject();
    } else {
      this.jsonObj = getJsonObject_SecondLevelJsonObject(json, object1, object2);
      strOutput = getJsonObjectValue(this.jsonObj, objeto3) != null ? getJsonObjectValue(this.jsonObj, objeto3).toString() : "null";
    }
    return strOutput;
  }

  public boolean compareJsonObjectValue(JSONObject json, String valor) {
    result = json.values().toString().replaceAll("\\\\/", "/").replaceAll("\\\\", "").replaceAll("\n", "").contains(valor);
    return result;
  }

  public JSONObject getJsonObject(JSONObject json, String object) {
    this.jsonObj = (JSONObject) json.get(object);
    return this.jsonObj;
  }

  public JSONObject getJsonArray(JSONObject json, String object) {
    JSONArray jsonarray;
    jsonarray = (JSONArray) json.get(object);
    if (!jsonarray.isEmpty()) {
      this.jsonObj = (JSONObject) jsonarray.get(0);
    } else {
      this.jsonObj = null;
    }

    return this.jsonObj;
  }

  public JSONObject getJsonArrayByIndex(JSONObject json, String object, int index) {
    JSONArray jsonarray;
    jsonarray = (JSONArray) json.get(object);
    if (!jsonarray.isEmpty()) {
      this.jsonObj = (JSONObject) jsonarray.get(index);
    } else {
      this.jsonObj = null;
    }

    return this.jsonObj;
  }

  public JSONArray getJsonArray_FromArray_ByIndex(JSONArray jsonarray, int index) {
    JSONArray jsonArr = new JSONArray();
    jsonArr = (JSONArray) jsonarray.get(index);
    return jsonArr;
  }

  public JSONObject getJsonObject_FromArray_ByIndex(JSONArray jsonarray, int index) {
    this.jsonObj = (JSONObject) jsonarray.get(index);
    return this.jsonObj;
  }

  public JSONArray getJsonArrayComplete(JSONObject json, String obj) {
    JSONArray jsonarray = new JSONArray();
    jsonarray = (JSONArray) json.get(obj);

    return jsonarray;
  }

  public JSONObject getJsonArrayElements(JSONArray jsonarray) {
    JSONObject jsonObj = new JSONObject();
    for (int i = 0; i < jsonarray.size(); i++) {
      jsonObj.put(null, jsonarray.get(i));
    }
    return jsonObj;
  }

  public JSONObject getJsonArrayComplete2(JSONObject json, String obj) {
    JSONObject jsonObj = new JSONObject();
    jsonObj = (JSONObject) json.get(obj);

    return jsonObj;
  }

  public JSONArray addJsonObjectInJsonArray(JSONArray jsonarray, Object obj) {
    jsonarray.add(obj);

    return jsonarray;
  }

  public JSONArray addJsonObjectInJsonArray(JSONArray jsonarray, String elementName, JSONObject json) {
    jsonarray = addJsonArrayInJsonArray(jsonarray, elementName, json);
    return jsonarray;
  }

  public JSONArray addValueInJsonArray(JSONArray jsonarray, int index, String element) {
    jsonarray.add(index, element);
    return jsonarray;
  }

  public JSONArray addJsonArrayElementInJsonArray(JSONArray jsonarray, String elementName, JSONArray arrayToAdd) {
    jsonarray = addJsonArrayInJsonArray(jsonarray, elementName, arrayToAdd);
    return jsonarray;
  }

  public JSONArray getJsonArrayOfJsonObject(JSONObject json) {
    JSONArray jsonarray = new JSONArray();
    jsonarray.add(json);
    return jsonarray;
  }

  public JSONObject convertJsonArrayToJsonObject(JSONArray jsonArray) {
    JSONObject json = new JSONObject();
    json = addJsonArrayInJsonObject(json, "Arr", jsonArray);
    json = getJsonArrayComplete2(json, "Arr");
    return json;
  }

  public JSONObject editValue_JsonObject(JSONObject json, String object, String value) {
    Iterator<?> keys = (Iterator<?>) json.keySet().iterator();
    while (keys.hasNext()) {
      String currentKey = (String) keys.next();
      if (currentKey.equals(object)) {
        json.replace(object, value);
      }
    }
    return json;
  }

  public JSONArray editValue_JsonArray(JSONArray jsonarray, String object, String value) {
    for (int i = 0; i < jsonarray.size(); i++) {
      jsonObj = getJsonObject_FromArray_ByIndex(jsonarray, i);
      jsonObj = editValue_JsonObject(jsonObj, object, value);
      jsonarray.remove(i);
      jsonarray.add(jsonObj);
    }

    return jsonarray;
  }

  public JSONObject addJsonObjectInJsonObject(JSONObject json, String object, JSONObject values) {
    json.put(object, values);
    return json;
  }

  public JSONObject addJsonObjectInJsonObject(JSONObject json, String object, int value) {
    json.put(object, value);
    return json;
  }

  public JSONObject addJsonObjectInJsonObject(JSONObject json, String object, String value) {
    json.put(object, value);
    return json;
  }

  public JSONObject addJsonObjectInJsonObject(JSONObject json, String object, Object values) {
    json.put(object, values);
    return json;
  }

  public JSONObject addJsonArrayInJsonObject(JSONObject json, String object, JSONArray values) {
    json.put(object, (Object) values);
    return json;
  }

  public JSONArray addJsonArrayInJsonArray(JSONArray jsonArray, String object, Object values) {
    jsonObj = getJsonObject_FromArray_ByIndex(jsonArray, 0);
    jsonObj = addJsonObjectInJsonObject(jsonObj, object, values);

    jsonArray.remove(0);
    jsonArray.add(jsonObj);
    return jsonArray;
  }

  public JSONObject removeJsonObjectOfJsonObject(JSONObject json, String object) {
    json.remove(object);
    return json;
  }

  public JSONArray removeElementOfJsonArray(JSONArray jsonArray, int element) {
    jsonArray.remove(element);
    return jsonArray;
  }

  public JSONArray removeJsonArrayOfJsonObject(JSONArray jsonarray, String object) {
    jsonObj = getJsonObject_FromArray_ByIndex(jsonarray, 0);
    jsonObj = removeJsonObjectOfJsonObject(jsonObj, object);

    jsonarray.remove(0);
    jsonarray.add(jsonObj);
    return jsonarray;
  }

  public String createJsonObjectFile(JSONObject json, String filePath) {
    try {
      FileWriter file = new FileWriter(filePath);
      file.write(json.toJSONString());
      file.flush();
      file.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return filePath;
  }

  public JSONObject getJsonObjectOfJsonArray_Desc(JSONObject jsonObject, String description, String object, String valor) {
    String comp;
    int cont = 0;
    this.jsonObj = new JSONObject();
    comp = getJsonObjectValue(jsonObj, object);
    JSONArray jsonArray = getJsonArrayComplete(jsonObject, description);
    while (!comp.contains(valor)) {
      this.jsonObj = (JSONObject) jsonArray.get(cont);
      comp = getJsonObjectValue(this.jsonObj, object);
      cont++;
    }
    return this.jsonObj;
  }

}
