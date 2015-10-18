package MySocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import util.ClientUtil;

import com.example.dabiantest1.GroupsScore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ClientSocket extends Thread {
	protected ClientReceiveFile CRF = null;
	public Socket socket;
	public BufferedWriter bw;
	public BufferedReader br;

	private MulticastSocket multicastSocket = null;
	private static int BROADCAST_PORT = 9898;
	private static String BROADCAST_IP = "224.0.0.1";
	InetAddress inetAddress = null;
	public String ip;
	public Handler revHandler;
	private Handler mHandler;
	private final int COMMIT = 0;
	private final int QUERY = 1;
	private final int GROUP_NAME = 2;
	private final int CONNECT = 3;
	private final int DISCONNECTED = 4;
	private final int FINISHJUDGE = 6;
	private final int OVER = 5;//该组答辩完成标志
	String info;
	Context context;
	JSONObject json = null;
	OutputStream os;
	private PrintWriter out;
	String groupName;
	StringBuffer sb = null;
	ClientUtil clientUtil = null;
	public ClientSocket(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
	}
	@SuppressLint("HandlerLeak")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			sb = new StringBuffer();
			clientUtil = ClientUtil.getClientUtil();
			multicastSocket = new MulticastSocket(BROADCAST_PORT);
			inetAddress = InetAddress.getByName(BROADCAST_IP);
			multicastSocket.joinGroup(inetAddress);
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
			System.out.println("检测到服务端IP：" + ip);
			socket = new Socket(ip, 8888);
			mHandler.obtainMessage(CONNECT, ip).sendToTarget();
			try {
				socket.sendUrgentData(0);
			} catch (IOException e) {
				mHandler.obtainMessage(DISCONNECTED).sendToTarget();
			}
			InputStream is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			new Thread() {
				public void run() {
					try {
						String line = null;
						while ((line = br.readLine()) != null) {
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = line;
							mHandler.sendMessage(msg);
							json = new JSONObject(line);
							if(json.has("finishJudge"))
							{
								mHandler.obtainMessage(FINISHJUDGE).sendToTarget();
							}
							if(json.has("OVER"))
							{
								mHandler.obtainMessage(OVER).sendToTarget();
							}
							if (json.has("groupName")&&sb.indexOf(line)==-1) {
								sb.append(line + "\n");
							}
							clientUtil.setSb(sb);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();
			Looper.prepare();
			revHandler = new Handler() {
				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case QUERY:
						CRF = new ClientReceiveFile();
						CRF.start();
						Log.i("------ClientThread CRF1111----", CRF + "");
						Intent to_GroupsScore = new Intent();
						to_GroupsScore.setClass(context, GroupsScore.class);
						to_GroupsScore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(to_GroupsScore);
						break;
					case COMMIT:
						try {
							os = socket.getOutputStream();
							bw = new BufferedWriter(new OutputStreamWriter(os,
									"UTF-8"));
							out = new PrintWriter(bw);
							Log.i("--------ClientThread------", msg.obj + "");
							out.println(msg.obj.toString());
							out.flush();
							bw.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					default:
						break;
					}
				}
			};
			Looper.loop();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}