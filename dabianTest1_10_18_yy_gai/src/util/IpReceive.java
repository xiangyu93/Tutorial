package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.content.SharedPreferences;
import android.graphics.YuvImage;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

public class IpReceive {
	private MulticastSocket multicastSocket = null;
	private static int BROADCAST_PORT = 9898;
	private static String BROADCAST_IP = "224.0.0.1";
	InetAddress inetAddress = null;
	private String ip;
	private String IP;

	public String getIp() {
		new deal().start();
		System.out.print("--------IPReceive----" + ip);
		return ip;
	}

	class deal extends Thread {
		public void run() {
			// TODO Auto-generated method stub
			try {
				Looper.prepare();
				Handler myHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						if (msg.what == 1) {
							IP = msg.obj.toString();
							System.out.print("-------IpReceive Handler----"
									+ IP);
						}
					}
				};
				multicastSocket = new MulticastSocket(BROADCAST_PORT);
				inetAddress = InetAddress.getByName(BROADCAST_IP);
				multicastSocket.joinGroup(inetAddress);
				Message msg = new Message();
				msg.what = 1;
				byte buf[] = new byte[1024];
				DatagramPacket dp = null;
				dp = new DatagramPacket(buf, buf.length, inetAddress,
						BROADCAST_PORT);
				multicastSocket.receive(dp);
				ip = new String(buf, 0, dp.getLength());
				Log.i("IpReceive", ip);
				if (ip.equals(null)) {
					for (;;) {
						multicastSocket.receive(dp);
						ip = new String(buf, 0, dp.getLength());
						if (!ip.equals(null))
							break;
					}
				}
				msg.obj = ip;
				myHandler.sendMessage(msg);
				System.out.println("¼ì²âµ½·þÎñ¶ËIP£º" + ip);

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Looper.loop();
		}
	}

}
