package API.Utilities;

import General.Utils.JsonManager;
import HttpUtilities.HttpParameters;
import HttpUtilities.HttpResponse;
import HttpUtilities.HttpSampler;
import org.apache.http.Header;
import org.json.simple.JSONArray;
import org.testng.annotations.Optional;


public class BaseApi extends JsonManager {

  protected HttpSampler sampler;
  JSONArray jsonArray = new JSONArray();

  public BaseApi(String url, boolean isStaging) {
    if (isStaging) {
      sampler = new HttpSampler(url, "");
    }
  }

  public BaseApi() {
  }

  // Want to return an object with response code, response headers, and response body
  public HttpResponse getResponse(String endpoint, String method, @Optional HttpParameters params, @Optional Header[] headers) {
    HttpResponse response;
    switch (method) {
      case "get":
        response = sampler.get("https", endpoint, false, false, headers);
        return response;
      case "getRedirect":
        response = sampler.get("https", endpoint, true, true, headers);
        return response;
      case "getHTTP":
        response = sampler.get("http", endpoint, false, false, headers);
        return response;
      case "getHTTPFollowsRedirect":
        response = sampler.get("http", endpoint, true, true, headers);
        return response;
      case "post":
        response = sampler.post("https", params, endpoint, false, false, headers);
        return response;
      case "patch":
        response = sampler.patch("https", params, endpoint, false, headers);
        return response;
      case "put":
        response = sampler.put("https", endpoint, params, true, true);
        return response;
      case "delete":
        response = sampler.delete("https", params, endpoint, true, true);
        return response;
      default:
        response = null;
        System.out.print("Invalid param -- must be get, post, patch, put, or delete");
        return response;
    }
  }

  // HTTPS GET request with redirect
  public HttpResponse getResponseRedirect(String endpoint, HttpParameters params, Header[] headers) {
    HttpResponse response = sampler.get("https", endpoint, true, true, headers);
    return response;
  }

  // Want to return an object with response code, response headers, and response body
  public HttpResponse getResponse(String host, String endpoint, String method, @Optional HttpParameters params, @Optional Header[] headers) {
    HttpResponse response;
    int responseCode;
    switch (method) {
      case "get":
        response = sampler.get("https", endpoint, false, false, headers);
        return response;
      case "getHTTP":
        response = sampler.get(host, "http", endpoint, false, false, headers);

      default:
        response = null;
        System.out.print("Invalid param -- must be get, post, patch, put, or delete");
        return response;
    }
  }

}
