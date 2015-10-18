package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.util.Log;

public class IpSend {
	public static String ip; // 服务端ip
	private static int BROADCAST_PORT = 9898;
	private static String BROADCAST_IP = "224.0.0.1";
	InetAddress inetAddress1 = null;
	Thread t = null;
	/* 发送广播端的socket */
	MulticastSocket multicastSocket1 = null;
	private volatile boolean isRuning = true;

	public void sendIp() {
		t = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				DatagramPacket dataPacket;

				try {
					inetAddress1 = InetAddress.getByName(BROADCAST_IP);
					multicastSocket1 = new MulticastSocket(BROADCAST_PORT);
					multicastSocket1.setTimeToLive(1);
					multicastSocket1.joinGroup(inetAddress1);
					dataPacket = null;
					String ip1 = getLocalIPAddress();
					Log.i("IpSend", ip1);
					byte[] data = ip1.getBytes();
					dataPacket = new DatagramPacket(data, data.length,
							inetAddress1, BROADCAST_PORT);
					while (true) {
						if (isRuning) {
							try {
								multicastSocket1.send(dataPacket);
								Thread.sleep(3000);
								System.out.println("再次发送ip地址广播:.....");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		t.start();
	}

	private String getLocalIPAddress() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()
						&& inetAddress instanceof Inet4Address) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "null";
	}

}
