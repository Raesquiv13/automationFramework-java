package General.WebDriver;

import org.openqa.selenium.WebDriver;

public class DriverBuilder {

  private WebDriver driver = null;

  public DriverBuilder(String localDriverPath, String os, String browserType, boolean... isGecko) {
    switch (browserType.toLowerCase()) {
      case "firefox":
        driver = new FireFoxDriver(localDriverPath, os, isGecko).getDriver();
        break;
      case "internetexplorer":
        driver = new IEDriver(localDriverPath, os).getDriver();
        break;
      case "safari":
        driver = new SafariDriver(localDriverPath, os).getDriver();
        break;
      case "chrome":
        driver = new ChromeDriver(localDriverPath, os).getDriver();
        break;
      case "phantomjs":
        driver = new PhantomDriver(localDriverPath, os).getDriver();
        break;
      default:
        System.out.println("[ERROR] There was a problem with the browser name: " + browserType);
    }
  }

  public DriverBuilder(String browserType, String os, String username, String authkey) {
    switch (browserType.toLowerCase()) {
      case "firefox":
        //		driver = new FireFoxDriver(gridHubIP, gridDriverPath, os).getDriver();
        break;
      case "internetexplorer":
        driver = new IEDriver(os, username, authkey).getDriver();
        break;
      case "safari":
        //	driver = new SafariDriver(gridHubIP, gridDriverPath, os).getDriver();
        break;
      case "chrome":
        driver = new ChromeDriver(os, username, authkey).getDriver();
        break;
      case "phantomjs":
        //		driver = new PhantomDriver(gridHubIP, gridDriverPath, os).getDriver();
        break;
      default:
        System.out.println("[ERROR] There was a problem with the browser name: " + browserType);
    }
  }

  public WebDriver getDriver() {
    return driver;
  }
}

