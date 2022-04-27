package General.WebDriver;

import General.Managers.ExceptionManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;

class FireFoxDriver
{
	private WebDriver	driver	= null;

	public FireFoxDriver(String gridHub, String gridDriverPath, String os)
	{
		DesiredCapabilities capability = null;
		String gridHubPort = "4444";
		capability = DesiredCapabilities.firefox();
		if (os.equalsIgnoreCase(""))
		{
			os = "windows";
		}
		switch (os.toLowerCase().trim())
		{
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
		try
		{
			if (gridHub.equalsIgnoreCase(""))
			{
				driver = new RemoteWebDriver(new URL("http://localhost:" + gridHubPort + "/wd/hub"), capability);
			}
			else
			{
				driver = new RemoteWebDriver(new URL("http://" + gridHub + ":" + gridHubPort + "/wd/hub"), capability);
			}
		}
		catch (Exception e)
		{
			ExceptionManager
			        .handleExeption(e, "[ERROR] There was a problem setting up the WebDriver Grid hub");
			//Reporter.log("[ERROR] There was a problem setting up the WebDriver Grid hub");
			// TODO I believe it would be better to sent the exception here to have a better idea of what is going on
		}
	}

	public FireFoxDriver(String localDriverPath, String os, boolean... isGecko)
	{
		String driverName = "";
		if (os.equalsIgnoreCase("windows"))
		{
			driverName = ".exe";
		}
		String propValue = "";					// TODO This should be a parameter from the XML
		FirefoxProfile profile;
		File profileDir = null;
		if (!propValue.equalsIgnoreCase(""))
		{
			profileDir = new File(propValue);
		}
		if (isGecko.length > 0 && isGecko[0])
		{
			driverName = "geckodriver" + driverName;
			System.setProperty("webdriver.gecko.driver", localDriverPath + driverName);
		}
		//		driverName = "firefox" + driverName;
		//		System.setProperty("webdriver.firefox.bin", driverPath + driverName);
		profile = new FirefoxProfile(profileDir);
		try
		{
			//driver = new FireFoxDriver(localDriverPath, os, isGecko);
			//driver = (WebDriver) new FireFoxDriver(driverName, "", true);
			driver = new FirefoxDriver();
		}
		catch (Exception e)
		{
			ExceptionManager.handleExeption(e, "[ERROR] There was a problem setting up the WebDriver");
		}
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
