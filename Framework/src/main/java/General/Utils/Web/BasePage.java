package General.Utils.Web;

import General.Managers.ExceptionManager;
import General.Managers.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class BasePage {
  protected WebDriver driver;
  private int timeOut, staticTimeout;
  private WebDriverWait wait;


  // --------------------------------------------------------- Constructor
  // ------------------------------------------------------------
  public BasePage(WebDriver driver) {
    this.driver = driver;
    timeOut = WebDriverManager.getDynamic();
    staticTimeout = WebDriverManager.getStatic();
    wait = new WebDriverWait(driver, timeOut);
  }

  public abstract boolean verifyLoads();
  // Locators

  /**
   * Returns a WebElement based on a defined key or Null if there's an issue
   *
   * @param elementKey is the element to find
   * @throws Exception
   */
  protected WebElement findElement(By elementKey) {
    try {
      wait(staticTimeout);
      return wait.until(ExpectedConditions.presenceOfElementLocated(elementKey));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + name
        + ". It may not be available");
      return null;
    }
  }

  /**
   * Returns a WebElement based on a defined key to search for or Null if
   * there's an issue
   *
   * @param elementKey is the element to find
   * @throws Exception
   */
  protected WebElement findElement(final By elementKey, final WebElement element) {
    try {
      wait(staticTimeout);
      return wait.until(new ExpectedCondition<WebElement>() {
        public WebElement apply(WebDriver arg0) {
          return element.findElement(elementKey);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + name
        + ". It may not be available");
      return null;
    }
  }

  /**
   * Returns a list of WebElements (i.e menu, drop down) based on a defined
   * key to search for or Null if there's an issue
   *
   * @param elementListKey is the list of elements to find
   * @throws Exception
   */
  protected List<WebElement> findElementList(final By elementListKey, final WebElement element) {
    try {
      wait(staticTimeout);
      return wait.until(new ExpectedCondition<List<WebElement>>() {
        public List<WebElement> apply(WebDriver arg0) {
          return element.findElements(elementListKey);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding list " + name
        + ". It may not be available");
      return null;
    }
  }

  /**
   * Returns a list of WebElements (i.e menu, drop down) based on a defined
   * key to search for or Null if there's an issue
   *
   * @param elementListKey is the list of elements to find
   * @throws Exception
   */
  protected List<WebElement> findElementList(By elementListKey) {
    try {
      wait(staticTimeout);
      return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementListKey));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding list " + name
        + ". It may not be available");
      return null;
    }
  }

  /**
   * Returns the element in focus
   *
   * @throws Exception
   */
  protected WebElement getActiveElement() {
    try {
      wait(staticTimeout);
      return driver.switchTo().activeElement();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the element in focus");
      return null;
    }
  }

  // Conditions

  /**
   * Determines whether the WebDriver is null
   */
  protected boolean isNull() {
    return driver == null; // TODO Is this really necessary?
  }

  /**
   * Determines whether an element is present on the test.java.page
   *
   * @param element containing the locator for the element
   * @throws Exception
   */
  protected boolean isElementPresent(WebElement element) {
    try {
      return element != null && element.toString().contains("Proxy element for");
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem determining if element " + name
        + " is present on the test.java.page");
      return false;
    }
  }

  /**
   * Determines whether a element is enabled on the test.java.page
   *
   * @throws Exception
   */
  protected boolean isElementEnabled(WebElement element) {
    try {
      return isElementPresent(element) && element.isEnabled();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + name
        + " to determine if enabled");
      return false;
    }
  }

  /**
   * Determines whether a element is enabled on the test.java.page
   *
   * @param element is the element that is going to be select
   * @throws Exception
   */
  protected boolean isElementSelected(WebElement element) {
    try {
      return isElementPresent(element) && element.isSelected();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + name
        + " to determine if selected");
      return false;
    }
  }

  /**
   * Determines whether a element is displayed on the test.java.page
   *
   * @throws Exception
   * @paramWebElement element is the element that we are asking if it's
   * visible
   */
  protected boolean isElementVisible(WebElement element) {
    try {
      return isElementPresent(element) && element.isDisplayed();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + name
        + " to determine if visible");
      return false;
    }
  }

  /**
   * Determines whether an element is part of a list
   *
   * @param elements            containing the locator for the element
   *                            list
   * @param elementTextToSearch that will be compared against every element on the list
   *                            looking for a match
   * @throws Exception
   */
  protected boolean isElementPresentOnList(List<WebElement> elements, String elementTextToSearch) {
    try {
      waitForListMinimumSize(elements, 1);
      String tag = getTagNameFromElement(elements.get(0));
      return findElement(By.xpath("parent::*/" + tag + "[contains(text(), '" + elementTextToSearch + "')]"),
        elements.get(0)) != null;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding the element " + elementTextToSearch
        + " on list " + name + ". The list/element may not be available");
      return false;
    }
  }

  /**
   * Determines whether the text is contained on test.java.page source
   *
   * @param text Text to search for in the test.java.page source
   * @throws Exception
   */
  protected boolean isTextPresentOnSource(final String text) {
    try {
      return driver.getPageSource().contains(text);
    } catch (Exception e) {
      ExceptionManager
        .handleExeption(e, "[ERROR] The text: \"" + text + "\n can not be found on the test.java.page source");
      return false;
    }
  }

  protected boolean waitForElementPresentOnSource(final WebElement element) {
    return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver arg0) {
        return element != null && !element.toString().contains("Proxy element for");
      }
    });
  }

  protected boolean waitForElementNotPresent(final WebElement element) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return !isElementPresent(element);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForElementNotPresent(final By by) {
    try {
      return wait(staticTimeout) && wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForTextOnElement(final WebElement element, final String text) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.getText().equals(text);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForElementToContain(final WebElement element, final String text) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.getText().contains(text);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForUrl(final String url) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return driver.getCurrentUrl().contains(url);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForListMinimumSize(final List<WebElement> elements, final int minSize) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return elements.size() >= minSize;
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForListMatchSize(final List<WebElement> elements, final int size) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return elements.size() == size;
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  protected boolean waitForTextOnSource(final String text) {
    try {
      return wait(staticTimeout) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return driver.getPageSource().contains(text);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  /**
   * Finds an element (i.e text box) and returns the value of the given
   * attribute / null is there was issues
   *
   * @param element   is the element from where the atribute is taken
   * @param attribute Name of the element attribute to get the value from
   * @throws Exception
   */
  protected String waitForAttributeOnElement(final WebElement element, final String attribute) {
    try {
      if (waitForElementPresentOnSource(element)) {
        return wait.until(new ExpectedCondition<String>() {
          public String apply(WebDriver arg0) {
            return element.getAttribute(attribute);
          }
        });
      }
      return null;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return null;
    }
  }

  protected boolean waitForAttributeOnElementToBe(final WebElement element, final String attribute, final String value) {
    try {
      return waitForElementPresentOnSource(element) && wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.getAttribute(attribute).equals(value);
        }
      });
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "");
      return false;
    }
  }

  /**
   * Waits for an element to be clickable and return true if so / false
   * otherwise
   *
   * @param element that is going to be clicked
   * @throws Exception
   */
  protected boolean waitAndClick(final WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.isEnabled();
        }
      });
      element.click();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem waiting for element " + name
        + " to be clickable. It may not be available");
      return false;
    }
  }

  /**
   * Waits for an element to be invisible and return true if so / false
   * otherwise
   *
   * @param element we are waiting to be invisible
   * @throws Exception
   */
  protected boolean waitForElementInvisible(WebElement element) {
    try {
      return wait(staticTimeout) && wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem waiting for element " + name
        + " to be visible. It may not be available");
      return false;
    }
  }

  /**
   * Waits for an element to be visible and return true if so / false
   * otherwise
   *
   * @param element we are going to wait to be visible
   * @throws Exception
   */
  protected boolean waitForElementVisible(WebElement element) {
    try {
      return wait(staticTimeout) && wait.until(ExpectedConditions.visibilityOf(element)) != null;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem waiting for element " + name
        + " to be visible. It may not be available");
      return false;
    }
  }

  protected boolean waitForElementsVisible(WebElement... element) {
    boolean vlr = true;
    for (int i = element.length; i-- > 0 && (vlr = waitForElementVisible(element[i])); ) {
    }
    return vlr;
  }

  /**
   * Waits for the window title to change to the expected text and returns
   * true if so / false otherwise
   *
   * @param title Expected window title
   * @throws Exception
   */
  protected boolean waitForWindowTitle(String title) {
    try {
      return wait(staticTimeout) && wait.until(ExpectedConditions.titleIs(title));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem waiting for the window title to be " + name
        + ". It may have not changed");
      return false;
    }
  }

  // --------------------------------------------------------- Methods
  // ----------------------------------------------------------------

  /**
   * Returns the current instance of the Selenium WebDriver
   */
  protected WebDriver getWebDriver() {
    // TODO Research how to block this method
    return driver;
  }

  // *******************//AVANTICA WEBDRIVER METHODS//**********************//

  /**
   * Closes a browser alert by accepting it
   *
   * @throws Exception
   */
  protected boolean acceptAlert() {
    try {
      wait(staticTimeout);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().accept();
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem clicking on alert " + name
        + ".It may not be available");
      return false;
    }
  }

  /**
   * Closes a browser alert by dismissing it
   *
   * @throws Exception
   */
  protected boolean dismissAlert() {
    try {
      wait(staticTimeout);
      wait.until(ExpectedConditions.alertIsPresent());
      driver.switchTo().alert().dismiss();
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem clicking on alert " + name
        + ".It may not be available");
      return false;
    }
  }

  /**
   * Closes the currently used instance of the Selenium WebDriver
   *
   * @throws Exception
   */
  protected boolean close() {
    try {
      driver.close();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem closing the browser window. It may not be available");
      return false;
    }
  }

  /**
   * Closes all the open instances of the Selenium WebDriver
   *
   * @throws Exception
   */
  protected boolean closeAll() {
    try {
      driver.quit();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem closing all the browser instances. They may not be available");
      return false;
    }
  }

  /**
   * Returns the text from the element in focus or null if there's an issue
   *
   * @throws Exception
   */
  protected String getActiveElementText() {
    try {
      return getTextFromElement(getActiveElement());
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the text from active element");
      return null;
    }
  }

  /**
   * Finds an element (i.e text box) and returns the value of the given
   * attribute / null is there was issues
   *
   * @param element   is the element from where the atribute is taken
   * @param attribute Name of the element attribute to get the value from
   * @throws Exception
   */
  protected String waitAndGetAttributeFromElement(final WebElement element, final String attribute) {
    try {
      waitForElementPresentOnSource(element);
      return waitForAttributeOnElement(element, attribute);
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding element " + element
        + " for getting attribute " + attribute + ". It may not be available");
      return null;
    }
  }

  /**
   * Returns the current browser URL
   *
   * @throws Exception
   */
  protected String getCurrentUrl() {
    try {
      return driver.getCurrentUrl();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[FATAL] There was a problem getting current browser URL");
      return null;
    }
  }

  /**
   * Returns the tag name from the element in focus or null if there's an
   * issue
   *
   * @throws Exception
   */
  protected String getTagNameFromActiveElement() {
    try {
      return getActiveElement().getTagName();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the tagName from active element");
      return null;
    }
  }

  /**
   * Returns the tag name from the element in focus or null if there's an
   * issue
   *
   * @throws Exception
   */
  protected String getTagNameFromElement(WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      return element.getTagName();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the tagName from active element");
      return null;
    }
  }

  /**
   * Finds an element and returns the related text or null if not available
   *
   * @param element is the element that the text is going to be obtained
   * @throws Exception
   */
  protected String getTextFromElement(WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      return element.getText().trim();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the text from element " + name
        + ". It may not be available");
      return null;
    }
  }

  /**
   * Return the text for the element in the given position of a list, or null
   * if there was issues
   *
   * @param elements containing all the elements
   * @param position List position of the element to get the text from
   * @throws Exception
   */
  protected String getTextFromListElement(List<WebElement> elements, int position) {
    try {
      waitForListMinimumSize(elements, position);
      return elements.get(position).getText().trim();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem finding the item in position" + position
        + " on list " + name + ". The list/itemmay not be available");
      return null;
    }
  }

  /**
   * Returns current test.java.page title
   *
   * @throws Exception
   */
  protected String getTitle() {
    try {
      return driver.getTitle().trim();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem getting the current test.java.page title");
      return null;
    }
  }

  /**
   * Returns the dimensions of the current browser window or Null if fails
   *
   * @throws Exception
   */
  protected Dimension getWindowSize() {
    try {
      driver.manage().window().setPosition(new Point(0, 0));
      java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      return new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to get the browser window size");
      return null;
    }
  }

  /**
   * Returns the number of window handles (frames) available to be used
   *
   * @throws Exception
   */
  protected String[] getWindowHandles() {
    try {
      Set<String> handles = driver.getWindowHandles();
      return (String[]) handles.toArray();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem getting the number of frames available to be used");
      return null;
    }
  }

  /**
   * Moves the mouse over an element
   *
   * @throws Exception
   */
  protected boolean hoverOver(WebElement element) {
    try {
      Actions ac = new Actions(driver);
      waitForElementVisible(element);
      ac.moveToElement(element);
      ac.perform();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was an error hovering over the element: " + name);
      return false;
    }
  }

  /**
   * Clicks on a JavaScript element
   *
   * @param element that is going to be clicked
   * @throws Exception
   */
  protected boolean waitAndJsClick(WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return (boolean) js
        .executeScript(
          "var evt = document.createEvent('MouseEvents');"
            + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
            + "arguments[0].dispatchEvent(evt);", element);
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to click on JavaScript element "
        + name);
      return false;
    }
  }

  /**
   * Double clicks on a JavaScript element
   *
   * @param element that is going to be doble clicked
   * @throws Exception
   */
  protected boolean waitAndJsDoubleClick(WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return (boolean) js
        .executeScript(
          "var evt = document.createEvent('MouseEvents');"
            + "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
            + "arguments[0].dispatchEvent(evt);", element);
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem trying to double click on JavaScript element " + name);
      return false;
    }
  }

  /**
   * Right clicks on a JavaScript element
   *
   * @param element that is going to be right clicked
   * @throws Exception
   */
  protected boolean waitAndJsRightClick(WebElement element) {
    try {
      waitForElementPresentOnSource(element);
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return (boolean) js
        .executeScript(
          "var evt = document.createEvent('MouseEvents');"
            + "evt.initMouseEvent('contextmenu',true, true, window, 1, 300, 300, 300, 300, false, false, false, false, 0,null);"
            + "arguments[0].dispatchEvent(evt);", element);
      //return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem trying to right click on JavaScript element " + name + "\n");
      return false;
    }
  }

  /**
   * Executes a JavaScript script
   *
   * @param script Code of the JS script to be executed
   * @throws Exception
   */
  protected Object jsExecute(String script) {
    try {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return js.executeScript(script);
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem executing a JavaScript script");
      return null;
    }
  }

  /**
   * Scroll down to element with JavaScript
   *
   * @throws Exception
   */
  protected boolean jsScrollBottomPage() {
    try {
      waitForPageLoaded();
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return (boolean) js
        .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to Scroll down with JavaScript to element "
        + name);
      return false;
    }
  }

  /**
   * Scroll down to given element with JavaScript
   *
   * @throws Exception
   */
  protected boolean jsScrollToElement(WebElement element) {
    try {
      waitForPageLoaded();
      JavascriptExecutor js = (JavascriptExecutor) driver;
      return (boolean) js
        .executeScript("arguments[0].scrollIntoView();", element);
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to Scroll down with JavaScript to element "
        + name);
      return false;
    }
  }

  /**
   * Maximizes the browser window
   *
   * @throws Exception
   */
  protected boolean maximizeWindow() {
    try {
      driver.manage().window().maximize();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to maximize the browser window");
      return false;
    }
  }

  /**
   * Navigates back on the browser history
   *
   * @throws Exception
   */
  protected boolean navigateBack() {
    try {
      driver.navigate().back();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem navigating back on the browser history");
      return false;
    }
  }

  /**
   * Navigates forward on the browser history
   *
   * @throws Exception
   */
  protected boolean navigateForward() {
    try {
      driver.navigate().forward();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem navigating forward on the browser history");
      return false;
    }
  }

  /**
   * Navigates to the given URL
   *
   * @param url Destination URL
   * @throws Exception
   */
  protected boolean navigateTo(String url) {
    try {
      driver.get(url);
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[FATAL] There was a problem navigating to " + url);
      return false;
    }
  }

  /**
   * Resizes the browser window to the given dimension
   *
   * @param size Target size for the current window
   * @throws Exception
   */
  protected boolean resizeWindow(Dimension size) {
    try {
      driver.manage().window().setSize(size);
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to resize the browser window");
      return false;
    }
  }

  /**
   * Sets a default waiting time for element, if not available immediately
   *
   * @param seconds Number of seconds to wait for the elements
   */
  protected void setImplicitWait(int seconds) {
    driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS); // TODO Is this really necessary?
  }

  /**
   * Switches the focus of the windows to the default content
   *
   * @throws Exception
   */
  protected boolean switchToDefaultContent() {
    try {
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem switching the focus of the window to the default content");
      return false;
    }
  }

  /**
   * Switches the focus to a specific frame
   *
   * @param handle Name of the frame handler to switch to
   * @throws Exception
   */
  protected boolean switchToFrame(final String handle) {
    try {
      wait.until(new ExpectedCondition<WebDriver>() {
        public WebDriver apply(WebDriver arg0) {
          return driver.switchTo().frame(handle);
        }
      });
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem switching the focus to a specific frame by String " + handle);
      return false;
    }
  }

  /**
   * Switches the focus to a specific frame
   *
   * @param index Index of the frame to switch to
   * @throws Exception
   */
  protected boolean switchToFrame(final int index) {
    try {
      wait.until(new ExpectedCondition<WebDriver>() {
        public WebDriver apply(WebDriver arg0) {
          return driver.switchTo().frame(index);
        }
      });
      return true;
    } catch (Exception e) {
      ExceptionManager
        .handleExeption(e, "[ERROR] There was a problem switching the focus to the specified frame");
      return false;
    }
  }

  /**
   * Switches the focus to a specific window
   *
   * @param windowNumber Identifier of the window to switch to
   * @throws Exception
   */
  protected boolean switchToWindow(final int windowNumber) {
    try {
      wait(staticTimeout);
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return driver.getWindowHandles().size() > windowNumber;
        }
      });
      driver.switchTo().window(driver.getWindowHandles().toArray()[windowNumber].toString());
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem switching the focus to a specific window");
      return false;
    }
  }

  /**
   * Switches the focus to a specific window
   *
   * @param handle Name of the frame handler to switch to
   * @throws Exception
   */
  protected boolean switchToWindow(final String handle) {
    try {
      wait(staticTimeout);
      wait.until(new ExpectedCondition<WebDriver>() {
        public WebDriver apply(WebDriver arg0) {
          return driver.switchTo().window(handle);
        }
      });
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem switching the focus to a specific window");
      return false;
    }
  }

  /**
   * Takes a screen shot of the active browser and saves it on the images
   * package
   *
   * @param name   Name for the screen shot file to be created
   * @param driver driver we are working with
   * @throws Exception
   */
  public static boolean takeScreenShot(String name, WebDriver driver) {
    try {
      File scrFile = null;
      File image = null;
      if (WebDriverManager.getExecMode().toLowerCase().equalsIgnoreCase("remote")) {
        scrFile = ((TakesScreenshot) new Augmenter().augment(driver)).getScreenshotAs(OutputType.FILE);
      } else {
        scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      }
      image = new File(name + ".png");
      FileUtils.copyFile(scrFile, image);
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR] There was a problem creating the screenshot file for the failed execution of the script");
      return false;
    }
  }

  /**
   * Types the given text on a browser alert
   *
   * @param text Text to be entered on the alert
   * @throws Exception
   */
  protected boolean waitAndTypeOnAlert(String text) {
    try {
      Alert temp = wait.until(ExpectedConditions.alertIsPresent());
      temp.sendKeys(text);
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e,
        "[ERROR]  Cannot type the desired text on the alert. It may not be available");
      return false;
    }
  }

  public void sendTextToElement(final WebElement element, String txt) {
    waitForElementEnabled(element);
    element.sendKeys(txt);
  }


  private boolean typeOnElement(final WebElement element, String txt) {
    element.sendKeys(txt);
    return element.getTagName().equals("input") ? waitForAttributeOnElementToBe(element, "value", txt) : true;//waitForAttributeOnElementToBe(element, "innerText", txt);
  }

  public void enterTextOneByOne(WebElement element, String text) {
    for (char letter : text.toCharArray()) {
      element.sendKeys(CharBuffer.wrap(new char[]{letter}));
    }
  }

  /**
   * Types the desired text on the given element
   *
   * @param element in wich we are goin to type
   * @param text    Text to be typed on the given element
   * @throws Exception
   */
  protected boolean waitAndTypeOnElement(final WebElement element, String text) {
    try {
      wait(staticTimeout);
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.isEnabled();
        }
      });
      return typeOnElement(element, text);
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] Unable to type on element: " + name
        + ".It may not be available");
      return false;
    }
  }

  /**
   * Types the desired text on the given element after it has been cleared
   *
   * @param element we are going to clean and type on it
   * @param text    Text to be typed on the given element
   * @throws Exception
   */
  protected boolean waitAndTypeOnCleanElement(final WebElement element, String text) {
    try {
      wait(staticTimeout);
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.isEnabled();
        }
      });
      element.clear();
      return typeOnElement(element, text);
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR]  Cannot type the desired text on element " + name
        + ". It may not be available");
      return false;
    }
  }

  /**
   * Stops the thread execution the given number of seconds (Not recommended
   * in normal circumstances)
   *
   * @param secs (in seconds) the execution must wait
   * @throws Exception
   */
  protected boolean wait(int secs) {
    try {
      int time = secs * 1000;
      Thread.sleep(time);
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem forcing to explicit wait");
      return false;
    }
  }

  protected boolean acceptUntrustedCertIE() {
    try {
      driver.navigate().to("javascript:document.getElementById('overridelink').click()");
      return true;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem trying to accept the certificate");
      return false;
    }
  }

  protected boolean pressKey(WebElement element, Keys key) {
    try {
      waitForElementPresentOnSource(element);
      element.sendKeys(key);
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] Not able to press the key on the element: " + name);
      return false;
    }
  }

  protected boolean submit(WebElement element) {
    try {
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return isElementEnabled(element);
        }
      });
      element.submit();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] Not able to submit the form from element: " + name);
      return false;
    }
  }

  protected String getTextFromAlert() {
    try {
      return wait.until(ExpectedConditions.alertIsPresent()).getText();
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] Not able to get text from active alert");
      return null;
    }
  }

  protected boolean attachFile(final WebElement element, String filePath) {
    try {
      wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return element.isEnabled();
        }
      });
      element.sendKeys(filePath);
      //						System.out.println(element.getText());
      //						((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value','" + filePath + "');",
      //						        element);
      //			System.out.println(element.getText());
      //			((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value','" + filePath + "');",
      //			        element);
      //			((JavascriptExecutor) driver).executeScript("arguments[0].drop();", element);
      //element.submit();
      return true;
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] Unable to send path to element: " + name
        + ".It may not be available");
      return false;
    }
  }

  public boolean waitForPageLoaded() {
    try {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      boolean vlr = false;
      for (int i = 0; i < timeOut && !vlr; ++i) {
        Thread.sleep(1);
        vlr = js.executeScript("return document.readyState").equals("complete");
      }
      return vlr;
    } catch (Exception e) {
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem executing the js script ");
      return false;
    }
  }

  protected String getAlertText() throws Exception {
    try {
      wait.until(ExpectedConditions.alertIsPresent());
      Alert alert = driver.switchTo().alert();
      return alert.getText().trim();
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem clicking on alert " + name
        + ".It may not be available");
      return null;
    }
  }

  protected boolean waitDragNDrop(WebElement start, WebElement end) {
    try {
      final Actions action = new Actions(driver);
      action.clickAndHold(start)
        .moveByOffset(-1, -1) // To fix issue with drag and drop in Chrome V61.0.3163.79
        .moveToElement(end, end.getLocation().getX() + end.getSize().getWidth() / 2, end.getLocation().getY() + end.getSize().getHeight() / 2)
        .release(end)
        .build()
        .perform();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  protected boolean waitForElementEnabled(WebElement element) {
    try {
      return wait.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver arg0) {
          return isElementEnabled(element);
        }
      });
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Waits for an element to be clickable and return true if so / false
   * otherwise
   *
   * @param element we are waiting to be invisible
   * @throws Exception
   */
  protected boolean waitForElementClickable(WebElement element) {
    try {
      return wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(element)));
    } catch (Exception e) {
      String name = ExceptionManager.parseException(e);
      ExceptionManager.handleExeption(e, "[ERROR] There was a problem waiting for element " + name
        + " to be clickable. It may not be available");
      return false;
    }
  }

  /**
   * Get the Network traffic on the pages visited with Selenium
   *
   * @return the log entries
   */
  public LogEntries getNetworkTabInfo() {
    LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
    return entries;
  }


  /**
   * Get cookies from browser
   *
   * @return a Set of Cookie
   */
  public Set<Cookie> getCookies() {
    return driver.manage().getCookies();
  }
}
