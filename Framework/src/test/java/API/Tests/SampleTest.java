package API.Tests;

import API.Utilities.BaseApi;
import General.Managers.TestManager;
import HttpUtilities.HttpResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * This is a website that we can use for testing purpose.
 * https://bookstore.toolsqa.com/swagger/
 * */

public class SampleTest extends TestManager {

  private BaseApi baseApi;
  private Header[] headers;
  private HttpResponse response;


  @BeforeTest
  @Parameters({"baseUrl"})
  public void setUpSampler(String baseUrl) {

    baseApi = new BaseApi(baseUrl, true);
    Header[] headers = {
      //new BasicHeader("Authorization", api_key),
      new BasicHeader("Content-Type", "application/json"),
      new BasicHeader("Accept", "application/json"),
    };
    this.headers = headers;
  }

  @Test
  public void firstApiTest() {
    response = baseApi.getResponse("/BookStore/v1/Books", "get", null, null);
    int statusCode = response.getStatusCode();
    assertTrue(statusCode == 200, "There is an issue with the API response, status code:" + statusCode);

    String responseMsg = response.getResponseMessage();
    JSONObject json = baseApi.getJsonObject(responseMsg);
    JSONArray books = baseApi.getJsonArrayComplete(json, "books");
    assertTrue(books.size() != 0, "There are not books in the response");

    for (int i = 0; i < books.size(); i++) {
      JSONObject booksData = (JSONObject) books.get(i);
      String title = (String) booksData.get("title");
      assertTrue(!title.isEmpty(), "There is a book without title in the position: " + i);
    }

    assertAll();
  }


}
