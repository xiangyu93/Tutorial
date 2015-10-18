package MySocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import util.DaTimerHandlerUtil;
import util.ServerUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyServerSocket extends Thread {
	public Socket ServerS;
	private BufferedReader br;
	private JSONObject json;
	Handler handler;
	ServerUtil serverUtil;
	PrintWriter outToClient;
	BufferedWriter bw;
	private static final int judge_over = 0x159;
	MyServerSocketManager MSSM;
	Context context;
	public int i;

	public MyServerSocket(Socket s) {
		this.ServerS = s;
	}

	public void sendToClient(String group_name) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					ServerS.getOutputStream(), "UTF-8"));
			outToClient = new PrintWriter(bw);
			outToClient.println(group_name);
			outToClient.flush();
			bw.flush();
			Log.i("=========Begin====", group_name);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void run() {
		super.run();
		try {
			serverUtil = ServerUtil.getServerUtil();
			br = new BufferedReader(new InputStreamReader(
					ServerS.getInputStream(), "UTF-8"));
			String line = null;
			Log.i("br----mss", br + "");
			try {
				while ((line = br.readLine()) != null) {
					MSSM = MyServerSocketManager.getMyServerSocketManager();
					json = new JSONObject(line);
					Log.i("----------MSS--------", json.toString());
					if (json.has("name")) {
						DaTimerHandlerUtil contextUtil = DaTimerHandlerUtil
								.getDa();
						handler = contextUtil.getHandler();
					}
					String name = json.getString("name");
					Message msg = Message.obtain();
					msg.what = 0x23;
					msg.obj = name;
					handler.sendMessage(msg);
					System.out.println("NAME====" + name);
					serverUtil.setJson(json);
					i = 1;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  void clear_i() {
		i = 0;

	}
}
