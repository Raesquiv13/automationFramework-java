package General.Listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class GenericListener extends TestListenerAdapter
{
  @Override
  public void onTestSkipped(ITestResult tr)
  {
    tr.setStatus(ITestResult.FAILURE);
  }
}
