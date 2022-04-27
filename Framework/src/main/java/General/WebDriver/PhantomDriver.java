package General.WebDriver;

import General.Managers.ExceptionManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.ArrayList;

public class PhantomDriver {
  private WebDriver driver = null;

  // Use phantomjs --webdriver=8910 on command line to start up the phantomjs server too
  public PhantomDriver(String gridHub, String gridDriverPath, String os) {
    String driverName = "phantomjs";
    if (os.equalsIgnoreCase("windows")) {
      driverName += ".exe";
    }
    System.setProperty("phantomjs.binary.path", gridDriverPath + driverName);
    String gridHubPort = "8910";
    //		DesiredCapabilities capability = null;
    //		capability = DesiredCapabilities.phantomjs();
    DesiredCapabilities capability = new DesiredCapabilities().phantomjs();
    ArrayList<String> cliArgsCap = new ArrayList<String>();
    cliArgsCap.add("--web-security=false");
    cliArgsCap.add("--ssl-protocol=any");
    cliArgsCap.add("--ignore-ssl-errors=true");
    capability.setCapability("takesScreenshot", true);
    //capability.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
    //capability.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]
    //{"--logLevel=2"});
    //			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, localDriverPath + driverName);
    capability.setCapability("takesScreenshot", true);
    if (os.equalsIgnoreCase("")) {
      os = "windows";
    }
    switch (os.toLowerCase().trim()) {
      case "windows":
        capability.setPlatform(Platform.WINDOWS);
        break;
      case "mac":
        capability.setPlatform(Platform.MAC);
        break;
      case "linux":
        capability.setPlatform(Platform.LINUX);
        break;
    }
    try {
      if (gridHub.equalsIgnoreCase("")) {
        driver = new RemoteWebDriver(new URL("http://localhost:" + gridHubPort + "/wd/hub"), capability);
      } else {
        driver = new RemoteWebDriver(new URL("http://" + gridHub + ":" + gridHubPort + "/wd/hub"), capability);
      }
    } catch (Exception e) {
      ExceptionManager
        .handleExeption(e, "[ERROR] There was a problem setting up the WebDriver Grid hub");
      //Reporter.log("[ERROR] There was a problem setting up the WebDriver Grid hub");
      // TODO I believe it would be better to sent the exception here to have a better idea of what is going on
    }
  }

  public PhantomDriver(String localDriverPath, String os) {
    String driverName = "phantomjs";
    if (os.equalsIgnoreCase("windows")) {
      driverName += ".exe";
    }
    System.setProperty("phantomjs.binary.path", localDriverPath + driverName);
    try {
      DesiredCapabilities caps = new DesiredCapabilities().phantomjs();
      ArrayList<String> cliArgsCap = new ArrayList<String>();
      cliArgsCap.add("--web-security=false");
      cliArgsCap.add("--ssl-protocol=any");
      cliArgsCap.add("--ignore-ssl-errors=true");
      caps.setCapability("takesScreenshot", true);
      //caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
      //caps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]
      //{"--logLevel=2"});
      //			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, localDriverPath + driverName);
      caps.setCapability("takesScreenshot", true);
      // driver = new PhantomJSDriver(caps);
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem setting up the WebDriver");
    }
  }

  public WebDriver getDriver() {
    return driver;
  }
}
