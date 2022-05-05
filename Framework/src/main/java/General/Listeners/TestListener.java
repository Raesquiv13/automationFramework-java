package General.Listeners;

import General.Logs.Log;
import General.Managers.WebDriverManager;
import General.Utils.ExtentReports.ExtentManager;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Objects;

import static General.Utils.ExtentReports.ExtentTestManager.getTest;

public class TestListener extends WebDriverManager implements ITestListener {
  private static String testType;
  private static boolean reportEnabled;

  public static String getTestType() {
    return testType;
  }

  public void setTestType(String testType) {
    this.testType = testType;
  }

  public static boolean isReportEnabled() {
    return reportEnabled;
  }

  public void setReportEnabled(boolean reportEnabled) {
    this.reportEnabled = reportEnabled;
  }

  private static String getTestMethodName(ITestResult iTestResult) {
    return iTestResult.getMethod().getConstructorOrMethod().getName();
  }

  @Override
  public void onStart(ITestContext iTestContext) {
    Log.info("I am in onStart method " + iTestContext.getName());
    //Verify if report creation is enabled.
    String enableReportParam = iTestContext.getCurrentXmlTest().getSuite().getParameter("enableReport");
    boolean isReportEnabled;
    if (enableReportParam != null) {
      isReportEnabled = enableReportParam.equalsIgnoreCase("true") ? true : false;
    } else {
      System.out.println();
      System.out.println();
      System.err.println("[WARNING] >> The parameter enableReport is not set in the test configuration xml file");
      System.out.println();
      System.out.println();
      isReportEnabled = false;
    }
    setReportEnabled(isReportEnabled);


    //Set the type of test
    String testType = iTestContext.getSuite().getAllMethods().get(0).getTestClass().getName();
    if (testType.contains("API.Tests")) {
      setTestType("API");
    } else if (testType.contains("UI.Tests")) {
      setTestType("UI");
    } else {
      setTestType("General");
    }
    if (this.getThreadDriver() != null) {
      iTestContext.setAttribute("WebDriver", this.getThreadDriver().get());
    }
  }

  @Override
  public void onFinish(ITestContext iTestContext) {
    Log.info("I am in onFinish method " + iTestContext.getName());
    //Do tier down operations for ExtentReports reporting!
    ExtentManager.extentReports.flush();
  }

  @Override
  public void onTestStart(ITestResult iTestResult) {
    Log.info(getTestMethodName(iTestResult) + " test is starting.");
  }

  @Override
  public void onTestSuccess(ITestResult iTestResult) {
    Log.info(getTestMethodName(iTestResult) + " test is succeed.");
    //ExtentReports log operation for passed tests.
    getTest().log(Status.PASS, "Test passed");
  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    Log.info(getTestMethodName(iTestResult) + " test is failed.");
    if (this.getThreadDriver() != null) {
      //Get driver from BaseTest and assign to local webdriver variable.
      /*
       * Object testClass = iTestResult.getInstance();
       * Not sure what i need to use instead of BaseTest
       * WebDriver driver = ((BaseTest) testClass).getDriver();
       */
      WebDriver driver = this.getThreadDriver().get();
      //Take base64Screenshot screenshot for extent reports
      String base64Screenshot =
        "data:image/png;base64," + ((TakesScreenshot) Objects.requireNonNull(driver)).getScreenshotAs(OutputType.BASE64);
      //ExtentReports log and screenshot operations for failed tests.
      getTest().log(Status.FAIL, "Test Failed",
        getTest().addScreenCaptureFromBase64String(base64Screenshot).getModel().getMedia().get(0));
    }
  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {
    Log.info(getTestMethodName(iTestResult) + " test is skipped.");
    //ExtentReports log operation for skipped tests.
    getTest().log(Status.SKIP, "Test Skipped");
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    Log.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
  }
}
