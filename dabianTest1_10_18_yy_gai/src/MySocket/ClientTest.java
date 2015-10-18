package MySocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import util.IpReceive;
import android.os.Message;
import android.util.Log;

public class ClientTest {
	public Socket socket;
	public BufferedWriter bw;
	public BufferedReader br;

	private MulticastSocket multicastSocket = null;
	private static int BROADCAST_PORT = 9898;
	private static String BROADCAST_IP = "224.0.0.1";
	InetAddress inetAddress = null;
	public String ip;

	
	public static final ClientTest CT = new ClientTest();
	public static ClientTest getClientSoket(){
		return CT;
	}
	public void connect() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
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
					// msg.obj = ip;
					// myHandler.sendMessage(msg);
					System.out.println("检测到服务端IP：" + ip);
					socket = new Socket(ip, 12345);
					Log.i("ClientSocket", socket.toString());
					OutputStream os = socket.getOutputStream();
					bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					InputStream is = socket.getInputStream();
					br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					System.out.print("--------已经连上服务器咯！！！！！！--------");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}
}
