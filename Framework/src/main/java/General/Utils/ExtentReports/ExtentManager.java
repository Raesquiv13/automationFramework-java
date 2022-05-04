package General.Utils.ExtentReports;

import General.Listeners.TestListener;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager extends TestListener {
  public static final ExtentReports extentReports = new ExtentReports();

  public synchronized static ExtentReports createExtentReports() {

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    String dateWithoutTime = formatter.format(date).split(" ")[0],
      year = dateWithoutTime.split("/")[2],
      month = dateWithoutTime.split("/")[1],
      day = dateWithoutTime.split("/")[0],
      time = formatter.format(date).split(" ")[1];

    ExtentSparkReporter reporter = new ExtentSparkReporter("./extent-reports/" + getTestType() + "/" + year + "/" + month + "/" + day + "/extent-report_" + time + ".html");
    //remove blank space and change it to dash, after that, remove slashes and replaced with dashes
    String dateWithoutSlash = formatter.format(date).replace(' ', '-').replace('/', '-');
    reporter.config().setReportName("Execution Report: " + dateWithoutSlash);
    extentReports.attachReporter(reporter);
    extentReports.setSystemInfo("Author", "Ricardo Esquivel");
    return extentReports;
  }
}
