package UI.Tests;

import General.Managers.WebDriverManager;
import UI.Pages.SamplePage;
import org.testng.annotations.Test;

public class SampleTest extends WebDriverManager
{

  @Test
  public void firstTest(){
    SamplePage google = new SamplePage(getDriver());
    assertTrue(google.verifyPageLoads(),"There is an issue loading google page");
    assertAll();
  }
}
