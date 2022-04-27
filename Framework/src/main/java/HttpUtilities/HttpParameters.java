package HttpUtilities;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpParameters {
  private List<NameValuePair> params;
  private Object	            data;
  private String	            type;

  /*
   only params = 1
   json format = 2
   xml format  = 3
   */
  public HttpParameters(String type)
  {
    this.type = type.replace(" ", "").toLowerCase();
    params = new ArrayList<NameValuePair>();
  }

  public void addParameter(String key, String value)
  {
    params.add(new BasicNameValuePair(key, value));
  }

  public void setDataObject(Object data)
  {
    this.data = data;
  }

  public void reset()
  {
    data = new Object();
    params = new ArrayList<NameValuePair>();
  }

  public boolean setData(String data)
  {
    this.data = data;
    return true;
  }

  public boolean setDataFromFile(String filePath)
  {
    switch (this.type)
    {
      case "json":
        this.data = readJsonFile(filePath);
        return true;
      case "xml":
        this.data = readXMLFile(filePath);
        return true;
      default:
        return false;
    }
  }

  public HttpEntity getParams()
  {
    try
    {
      StringEntity httpEntity;
      switch (this.type)
      {
        case "urlencoded":
          return new UrlEncodedFormEntity(params, "UTF-8");
        case "json":
          httpEntity = new StringEntity(data.toString());
          httpEntity.setContentType("application/json");
          return httpEntity;
        case "xml":
          httpEntity = new StringEntity(data.toString());
          httpEntity.setContentType("text/xml");
          return httpEntity;
        default:
          System.out.println("Wrong parameters type: " + type);
          break;
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
    return null;
  }

  private String readJsonFile(String path)
  {
    try
    {
      JSONParser parser = new JSONParser();
      FileReader fileReader = new FileReader(path);
      JSONObject json = (JSONObject) parser.parse(fileReader);
      return json.toString();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

  private String readXMLFile(String path)
  {
    try
    {
      File xmlFile = new File(path);
      Reader fileReader = new FileReader(xmlFile);
      BufferedReader bufReader = new BufferedReader(fileReader);
      StringBuilder sb = new StringBuilder();
      String line = bufReader.readLine();
      while (line != null)
      {
        sb.append(line).append("\n");
        line = bufReader.readLine();
      }
      String xml2String = sb.toString();
      bufReader.close();
      return xml2String;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
