package HttpUtilities;

import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpResponse {
  private org.apache.http.HttpResponse	httpResponse;
  private String	                     contentType;
  private String	                     responseMessage;
  private int	                         statusCode;
  private String						 reasonPhrase;
  private String						 responseBody;
  ResponseHandler<String> handler = new BasicResponseHandler();

  public HttpResponse(org.apache.http.HttpResponse httpResponse)
  {
    this.httpResponse = httpResponse;
    statusCode = this.httpResponse.getStatusLine().getStatusCode();
    if (this.httpResponse.getEntity().getContentType() != null){
      contentType = this.httpResponse.getEntity().getContentType().getValue();}
    if (this.httpResponse.getStatusLine().getReasonPhrase() != null){
      reasonPhrase = this.httpResponse.getStatusLine().getReasonPhrase();}
  }



  public String getResponseBody(){
    try {
      responseBody = EntityUtils.toString(httpResponse.getEntity());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return responseBody;
  }

  public String getContentType()
  {
    return contentType;
  }

  public String getReasonPhrase()
  {
    return reasonPhrase;
  }

  public int getStatusCode()
  {
    return statusCode;
  }

  public String getResponseMessage()
  {
    if (httpResponse != null)
    {
      try
      {
        return EntityUtils.toString(httpResponse.getEntity());
      }
      catch (Exception e)
      {
        System.out.println(e.getMessage());
      }
    }
    return null;
  }

  public Header[] getAllHeaders()
  {
    return httpResponse.getAllHeaders();
  }

  public Header getHeader(String headerName)
  {
    return httpResponse.getFirstHeader(headerName);
  }
}
