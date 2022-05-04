package UI.Pages;

import General.Utils.Web.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SamplePage extends BasePage {
  /**
   * This is the url path related to the page we are building here.
   */
  public static String url = "/";

  /**
   * Locators.
   */
  @FindBy(xpath = "//img[@alt='Google']")
  private WebElement googleImg;

  @FindBy(xpath = "//input[@title='Buscar']")
  private WebElement searchInput;


  /**
   * Constructor.
   */
  public SamplePage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);

  }

  @Override
  public boolean verifyPageLoads() {
    return waitForElementVisible(googleImg)
      && waitForElementVisible(searchInput);
  }
}
