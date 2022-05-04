package UI.Tests;

import General.Managers.WebDriverManager;
import UI.Pages.SamplePage;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static General.Utils.ExtentReports.ExtentTestManager.startTest;

public class SampleTest extends WebDriverManager
{

  @Test
  public void firstTest(Method method){
    startTest(method.getName(), "This is the first test we will use as sample");
    SamplePage google = new SamplePage(getDriver());
    assertTrue(google.verifyPageLoads(),"There is an issue loading google page");
    assertAll();
  }
}
