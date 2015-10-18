package util;

import android.content.Context;
import android.os.Handler;

public class DaTimerHandlerUtil {
	Handler handler;
	public static final DaTimerHandlerUtil  Da = new DaTimerHandlerUtil();
	
	public static DaTimerHandlerUtil getDa() {
		return Da;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public Handler getHandler() {
		return handler;
	}
}
