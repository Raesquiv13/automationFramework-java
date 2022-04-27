package General.WebDriver;

import General.Managers.ExceptionManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

class IEDriver
{
	private WebDriver	driver	= null;

	// CBT
	public IEDriver(String os,String username, String authkey)
	{
		DesiredCapabilities capability = null;
		capability = DesiredCapabilities.internetExplorer();
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
			driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"),capability);
		}
		catch (Exception e)
		{
			ExceptionManager
			        .handleExeption(e, "[ERROR] There was a problem setting up the WebDriver Grid hub");
			//Reporter.log("[ERROR] There was a problem setting up the WebDriver Grid hub");
			// TODO I believe it would be better to sent the exception here to have a better idea of what is going on
		}
	}

	public IEDriver(String localDriverPath, String os)
	{
		String driverName = "";
		if (os.equalsIgnoreCase("windows"))
		{
			driverName = ".exe";
		}
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability("ignoreZoomSetting", true);
		System.setProperty("webdriver.ie.driver", localDriverPath + "IEDriverServer.exe");
		try
		{
			driver = new InternetExplorerDriver(cap);
		}
		catch (Exception e)
		{
			ExceptionManager.handleExeption(e, "[ERROR] There was a problem setting up the WebDriver");
			//Reporter.log("[ERROR] There was a problem setting up the Avantica WebDriver");
		}
	}

	public WebDriver getDriver()
	{
		return driver;
	}
}
