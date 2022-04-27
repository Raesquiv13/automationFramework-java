package General.WebDriver;

import General.Managers.ExceptionManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.logging.Level;

class ChromeDriver
{
	private WebDriver	driver	= null;

	// CBT
    public ChromeDriver(String os,String username, String authkey)
    {
        DesiredCapabilities capability = null;
        capability = DesiredCapabilities.chrome();
        if (os.equalsIgnoreCase(""))
        {
            os = "windows";
        }
        switch (os.toLowerCase().trim())
        {
            case "windows":
                capability.setPlatform(Platform.WINDOWS);
                capability.setCapability("version", "latest");
                break;
            case "mac":
                capability.setPlatform(Platform.MAC);
                break;
            case "linux":
                capability.setPlatform(Platform.LINUX);
                break;
        }
        try
        {
            driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"),capability);
        }
        catch (Exception e)
        {
            ExceptionManager
                    .handleExeption(e, "[ERROR] There was a problem setting up the Avantica WebDriver for Crossbrowsertesting");
            //Reporter.log("[ERROR] There was a problem setting up the Avantica WebDriver Grid hub");
            // TODO I believe it would be better to sent the exception here to have a better idea of what is going on
        }
    }


    public ChromeDriver(String localDriverPath, String os)
	{
		System.out.print("Operating System:" + os);
		System.out.print(localDriverPath);
		String driverName = "";


		if (!localDriverPath.equals(""))
		{
			if (os.equalsIgnoreCase("windows"))
			{
				driverName = ".exe";
			}
			driverName = "chromedriver" + driverName;
			System.setProperty("webdriver.chrome.driver", localDriverPath + driverName);
		}
		try
		{
			ChromeOptions options = new ChromeOptions();
			options.setHeadless(false);
			options.addArguments("window-size=1980,1080");
			options.addArguments("--incognito");
			options.addArguments("ignore-certificate-errors");
//			options.addArguments("user-agent=AvanTest Tests/1.0 (The Atlantic)");

			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
			options.setCapability( "goog:loggingPrefs", logPrefs );

			driver = new org.openqa.selenium.chrome.ChromeDriver(options);
		}
		catch (Exception e)
		{
			ExceptionManager.handleExeption(e, "[ERROR] There was a problem setting up the Avantica WebDriver");
		}
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
