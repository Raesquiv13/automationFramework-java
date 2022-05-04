package General.Managers;

import General.Listeners.GenericListener;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class TestManager extends GenericListener {
  private static ThreadLocal<SoftAssert> asserts;
  private static ThreadLocal<Boolean> assertsCalled;
  private static ThreadLocal<Boolean> verificationsCalled;
  private static boolean exceptionDriven;

  public static boolean getExceptionDriven() {
    return exceptionDriven;
  }

  private void verifyAsserts(ITestResult result) {
    String msg;
    msg = result.getMethod().toString();
    if (!verificationsCalled.get()) {
      result.setStatus(ITestResult.FAILURE);
      System.out
        .println(msg.substring(0, msg.indexOf("("))
          + ": [WARNING] You need to have a verification method ( assertTrue / assertFalse ) before calling an assertAll");
    }
    if (!assertsCalled.get()) {
      result.setStatus(ITestResult.FAILURE);
      System.out
        .println(msg.substring(0, msg.indexOf("("))
          + ": [WARNING] You didn't call the assertAll method after your last verification method ( assertTrue / assertFalse )");
    }
  }

  protected void assertTrue(boolean vlr, String msg) {
    verificationsCalled.set(true);
    assertsCalled.set(false);
    asserts.get().assertTrue(vlr, msg + "\n");
  }

  protected void assertFalse(boolean vlr, String msg) {
    verificationsCalled.set(true);
    assertsCalled.set(false);
    asserts.get().assertFalse(vlr, msg);
  }

  protected void assertAll() {
    assertsCalled.set(true);
    asserts.get().assertAll();
  }

  @BeforeTest
  @Parameters(
    {"exceptionDriven"})
  public void setUpTestManager(@Optional("false") String exception) {
    this.exceptionDriven = exception.toLowerCase().equals("true");
  }

  @BeforeSuite
  protected final void setSuiteManager() {
    asserts = new ThreadLocal<SoftAssert>();
    verificationsCalled = new ThreadLocal<Boolean>();
    assertsCalled = new ThreadLocal<Boolean>();
  }

  @BeforeMethod
  protected final void setMethodManager(Method method) {
    asserts.set(new SoftAssert());
    verificationsCalled.set(false);
    assertsCalled.set(false);
  }

  @AfterMethod
  protected final void endMethodManager(ITestResult result, Method method) {
    verifyAsserts(result);
    System.out.println("Completed test: " + method.getName());
  }


}
