package General.WebDriver;

import General.Managers.ExceptionManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

class SafariDriver
{
	private WebDriver	driver	= null;

	public SafariDriver(String gridHub, String gridDriverPath, String os)
	{
		DesiredCapabilities capability = null;
		String gridHubPort = "4444";
		capability = DesiredCapabilities.safari();
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

	public SafariDriver(String localDriverPath, String os)
	{
		String driverName = "";
		if (os.equalsIgnoreCase("windows"))
		{
			driverName = ".exe";
		}
		DesiredCapabilities cap = DesiredCapabilities.safari();
		try
		{
			driver = new org.openqa.selenium.safari.SafariDriver(cap);
		}
		catch (Exception e)
		{
			ExceptionManager
			        .handleExeption(e, "[ERROR] There was a problem setting up the WebDriver Grid hub");
		}
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
