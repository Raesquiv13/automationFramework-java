package General.Managers;

import General.Logs.Log;
import General.Utils.JsonManager;
import General.Utils.OSValidator;
import General.Utils.Web.BasePage;
import General.WebDriver.DriverBuilder;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.IAnnotationTransformer;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebDriverManager extends TestManager implements IAnnotationTransformer {
  private OSValidator osValidator = new OSValidator();
  private static ThreadLocal<WebDriver> threadDriver;
  private static ThreadLocal<Boolean> thread;
  private static String executionMode, browser, platform, gridHubIP, localDriverPath,
    gridDriverPath;
  private static int staticTimeout, dynamicTimeout;
  private static boolean geckoDriver;
  public static String template;
  public static String baseURL;
  public static String utilityAPIUrl;
  private JsonManager jsonManager = new JsonManager();


  public static int getStatic() {
    return staticTimeout;
  }

  public static int getDynamic() {
    return dynamicTimeout;
  }

  public static String getExecMode() {
    return executionMode;
  }

  public static ThreadLocal<WebDriver> getThreadDriver() {
    return threadDriver;
  }

  private void createDriver() {
    if (executionMode.equals("local")) {
      DriverBuilder driverBuilder = new DriverBuilder(localDriverPath, platform, browser, geckoDriver);
      threadDriver.set(driverBuilder.getDriver());
    } else if (executionMode.equals("remote")) {
      DriverBuilder driverBuilder = new DriverBuilder(browser, gridHubIP, gridDriverPath, platform);
      threadDriver.set(driverBuilder.getDriver());
    } else if (executionMode.equals("cbt")) {
      JSONObject credentials = jsonManager.getJsonObjectFromFile("src/main/resources/cbt.json");
      DriverBuilder driverBuilder = new DriverBuilder(browser, platform, credentials.get("username").toString(),
        credentials.get("authkey").toString());
      threadDriver.set(driverBuilder.getDriver());
    } else {
      System.out.println(" [ERROR] Execution Mode: " + executionMode);
    }
    if (threadDriver.get() != null) {
      System.out.println(">> Driver Created!");
    }
  }

  protected void deleteDriver() {
    threadDriver.get().quit();
  }

  protected WebDriver getDriver() {
    if (!thread.get()) {
      thread.set(true);
      return threadDriver.get();
    }
    return null;
  }

  @BeforeSuite
  protected final void setupMaps() {
    threadDriver = new ThreadLocal<WebDriver>();
    thread = new ThreadLocal<Boolean>();
  }

  @BeforeTest
  @Parameters(
    {"baseURL", "executionMode", "browser", "platform", "gridHubIP", "gridDriverPath", "localDriverPath", "staticTimeout",
      "dynamicTimeout", "geckoDriver", "template", "utilityAPIUrl"})
  public void setUpParameters(String baseURL, String executionMod, String browser, @Optional String platform, String gridHubIP,
                              String gridDriverPath, String localDriverPath, @Optional("0") int staticTimeout, int dynamicTimeout,
                              @Optional("true") boolean geckoDriver, @Optional("true") String template,
                              @Optional("true") String utilityAPIUrl) {
    this.executionMode = executionMod;
    this.browser = browser.toLowerCase().trim();
    this.platform = osValidator.getPlatform(platform);
    this.gridHubIP = gridHubIP;
    this.gridDriverPath = gridDriverPath;
    this.localDriverPath = localDriverPath + this.platform + "/";
    this.staticTimeout = staticTimeout;
    this.dynamicTimeout = dynamicTimeout;
    this.geckoDriver = geckoDriver;
    this.template = template;
    this.baseURL = baseURL;
    this.utilityAPIUrl = utilityAPIUrl;
  }

  @BeforeMethod
  protected final void setupWebDriver() {
    Log.info("Tests is starting!");
    createDriver();
    thread.set(false);
    threadDriver.get().manage().deleteAllCookies();
    threadDriver.get().manage().window().maximize();
    threadDriver.get().navigate().to(this.baseURL);
  }

  @AfterMethod
  protected final void closeWebDriver(ITestResult result) {
    deleteDriver();
    Log.info("Tests are ending!");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == 2) {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
      Date date = new Date();
      String name = "./test-output/FailureScreenShots/"
        + (result.getMethod().getMethodName()).replaceAll("\\s+", "_") + "@" + this.browser;
      BasePage.takeScreenShot(name, threadDriver.get());
    }
  }

}


