package General.Managers;

import org.testng.Reporter;
import org.testng.SkipException;

public class ExceptionManager {
  public static void handleExeption(Exception e, String msg) {
    String errorMsg = "";
    System.out.println();
    System.out.println(e.getMessage());
    StackTraceElement[] stack = Thread.currentThread().getStackTrace();
    for (int i = 2, lim = stack.length; i < lim && !errorMsg.contains("test"); ) {
      errorMsg = stack[i++].toString();
      System.out.println(" - at: " + errorMsg);
    }
    Reporter.log(errorMsg, true);
    Reporter.log(msg, true);
    System.out.println();
    if (TestManager.getExceptionDriven()) {
      throw new SkipException("Testing skip");
    }
  }

  public static void handleExeption(Exception e) {
    ExceptionManager.handleExeption(e, "");
  }

  public static String parseException(Exception e) {
    String msg = e.getMessage();
    if (msg != null) {
      msg = msg.split("\n")[0];
      if (msg.contains("no such element")) {
        msg = "null";
      }
    }
    return msg;
  }
}
