package log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTools {
	private static boolean isDebug = true;
	private LogTools() {};
	public static void disDebug() {
		isDebug = false;
	}
	public static void INFO (Class c, Object msg) {
		if( !isDebug) return;
		SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
		String t = sdf.format(new Date());
		System.out.println("INFO: " + t + ": " + c.getSimpleName() + " : " + msg);
	}
	public static void ERROR(Class c, Object msg) {
		if( !isDebug) return;
		SimpleDateFormat sdf = new SimpleDateFormat("hh-FF-ss");
		String t = sdf.format(new Date());
		System.out.println("ERROR: " + t + ": " + c.getSimpleName() + " : " + msg);
	}
}
