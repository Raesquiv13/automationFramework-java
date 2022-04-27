package HttpUtilities;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.testng.annotations.Optional;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpSampler {
  private String	    host;
  private String	    port;
  private HttpClient httpClient	= HttpClientBuilder.create().build();
  private CookieStore cookieStore	= new BasicCookieStore();
  private HttpContext httpContext	= new BasicHttpContext();

  @SuppressWarnings({"deprecation"})
  public HttpSampler(String host, String port)  {
    try {
      //   System.setProperty("jsse.enableSNIExtension", "false");
      SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
        SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
        NoopHostnameVerifier.INSTANCE);
      httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();

      //	System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
      this.port = port;//optional?
      this.host = host;
      CookieHandler.setDefault(new CookieManager());
      httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace(); }
    catch (KeyManagementException e) {
      e.printStackTrace(); }
    catch (KeyStoreException e) {
      e.printStackTrace();
    }
  }

  public HttpResponse get(String protocol, String path, boolean redirect, boolean keepAlive, @Optional Header[] headers)
  {
    try
    {
      if (!redirect)
      {
        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
          SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
          NoopHostnameVerifier.INSTANCE);
        httpClient = HttpClientBuilder.create().disableRedirectHandling()
          .setDefaultRequestConfig(RequestConfig.custom()
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build())
          .setSSLSocketFactory(scsf)
          .build();
      }
      HttpGet get = new HttpGet(protocol + "://" + host + path);
      System.out.println("URL is: " + get);
      if (!keepAlive)
      {
        get.setHeader("Connection", "close");
      }
      if (headers != null) {
        get.setHeaders(headers);
        //get.setHeader(new BasicHeader("User-Agent", ""));
      }
      return new HttpResponse(httpClient.execute(get, httpContext));
    }
    catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e)
    {
      e.printStackTrace();
      return null;
    }
  }


  public HttpResponse get(String host, String protocol, String path, boolean redirect, boolean keepAlive, @Optional Header[] headers)
  {
    try
    {
      if (!redirect)
      {
        httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
      }
      HttpGet get = new HttpGet(protocol + "://" + host + path);
      System.out.println("URL is: " + get);
      if (!keepAlive)
      {
        get.setHeader("Connection", "close");
      }
      if (headers != null) {
        get.setHeaders(headers);
        //get.setHeader(new BasicHeader("User-Agent", ""));
      }
      return new HttpResponse(httpClient.execute(get, httpContext));
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }


  public HttpResponse patch(String protocol, HttpParameters params, String path, boolean keepAlive, @Optional Header[] headers)
  {
    try
    {
      HttpPatch patch = new HttpPatch(protocol + "://" + host + path);
      patch.setEntity(params.getParams());
      if (!keepAlive)
      {
        patch.setHeader("Connection", "close");
        //patch.setHeader(new BasicHeader("User-Agent", ""));
      }
      if (headers != null) {
        patch.setHeaders(headers);
        //patch.setHeader(new BasicHeader("User-Agent", ""));
      }
      return new HttpResponse(httpClient.execute(patch, httpContext));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }


  public HttpResponse post(String protocol, HttpParameters params, String path, boolean redirect, boolean keepAlive, @Optional Header[] headers)
  {
    try
    {
      if (redirect)
      {
        httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
      }
      HttpPost post = new HttpPost(protocol + "://" + host + path);
      System.out.println("URL is: " + post);
      if (params != null){
        post.setEntity(params.getParams());
      }

      if (!keepAlive)
      {
        post.setHeader("Connection", "close");
      }
      if (headers != null) {
        post.setHeaders(headers);
        //post.setHeader(new BasicHeader("User-Agent", ""));
      }
      return new HttpResponse(httpClient.execute(post, httpContext));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public HttpResponse delete(String protocol, HttpParameters params, String path, boolean redirect, boolean keepAlive)
  {
    try
    {
      if (redirect)
      {
        httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
      }
      HttpDelete delete = new HttpDelete(protocol + "://" + host + port + path);
      if (!keepAlive)
      {
        delete.setHeader("Connection", "close");
      }
      return new HttpResponse(httpClient.execute(delete, httpContext));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public HttpResponse put(String protocol, String path, HttpParameters params, boolean redirect, boolean keepAlive)
  {
    try
    {
      if (redirect)
      {
        httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
      }
      HttpPut put = new HttpPut(protocol + "://" + host + ":" + port + path);
      put.setEntity(params.getParams());
      if (!keepAlive)
      {
        put.setHeader("Connection", "close");
        //put.setHeader(new BasicHeader("User-Agent", ""));
      }
      return new HttpResponse(httpClient.execute(put, httpContext));
    }
    catch (ClientProtocolException e)
    {
      e.printStackTrace();
      return null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }
}
