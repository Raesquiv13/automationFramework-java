package General.Utils;

public class OSValidator {
  final private static String OS = System.getProperty("os.name").toLowerCase();

  public String getPlatform(String platform) {
    String os = "";
    if (platform!=null) {
      os = platform;
    } else if (isWindows()) {
      os = "windows";
    } else if (isMac()) {
      os = "mac";
    }
    return os;
  }

  public static boolean isWindows() {
    return (OS.indexOf("win") >= 0);
  }

  public static boolean isMac() {
    return (OS.indexOf("mac") >= 0);
  }


}
