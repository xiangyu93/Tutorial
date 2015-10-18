package MySocket;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MyServerSocketManager {
	double totalScore;
	private JSONObject[] jsons = null;
	private int[] scores1 = null;
	private int[] scores2 = null;
	private int[] scores3 = null;
	private String[] name = null;
	double AvScore1;
	double AvScore2;
	double AvScore3;
	private MyServerSocket MSS;
	private MyServerSocket mss;
	private Socket socket;
	public boolean flag = false;
	JSONObject json = null;

	private MyServerSocketManager() {
	}

	public static final MyServerSocketManager MSManager = new MyServerSocketManager();

	public static MyServerSocketManager getMyServerSocketManager() {
		return MSManager;
	}

	public Vector<MyServerSocket> vector = new Vector<MyServerSocket>();

	public void add(MyServerSocket MSS) throws UnsupportedEncodingException {
		// vector.add(MSS);
		// 通过接入服务器监听的socket得到客户端ip
		int count = 0;
		if (vector.size() == 0) {
			vector.add(MSS);
		} else {
			for (int i = 0; i < vector.size(); i++) {
				mss = vector.get(i);
				socket = mss.ServerS;
				Log.i("=====judge=====", JudgeIp(socket, MSS.ServerS) + "");
				if (JudgeIp(socket, MSS.ServerS)) {
					if (!socket.isConnected()) {
						vector.add(MSS);
					} else {
						count = 1;
						break;
					}
				}
			}
			if (count == 0) {
				vector.add(MSS);
			}
		}
		Log.i("-------MSSM vectorSize-------", vector.size() + "");
	}
  
	public boolean is_JudgeOver() {
		int num = 0;
		for (int i = 0; i < vector.size(); i++) {
			MSS = vector.get(i);
			Log.i("is_JudgeOver===i", MSS.i+"");
			num += MSS.i;
			
			Log.i("is_JudgeOver===num", num+"");
		}
	
		if (num == vector.size()) {
			for(int i = 0; i < vector.size(); i++)
			{
				MSS.clear_i();
			}
			return true;
		} else {
			return false;
		}
	}

	public void Caculate() {
		jsons = new JSONObject[vector.size()];
		scores1 = new int[vector.size()];
		scores2 = new int[vector.size()];
		scores3 = new int[vector.size()];
		name = new String[vector.size()];
		Log.i("-------managerVector---------", vector.size() + "");
		for (int i = 0; i < vector.size(); i++) {
			try {
				MSS = vector.get(i);
				jsons[i] = MSS.serverUtil.getJsonArray().getJSONObject(i);
				Log.i("========jsons[i]======", jsons[i] + "");
				scores1[i] = jsons[i].getInt("score1");
				scores2[i] = jsons[i].getInt("score2");
				scores3[i] = jsons[i].getInt("score3");
				name[i] = jsons[i].getString("name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.AvScore1 = Average(scores1);
			this.AvScore2 = Average(scores2);
			this.AvScore3 = Average(scores3);
			this.totalScore = (AvScore1 + AvScore2 + AvScore3) / 3;
			Log.i("--------Manager--------", totalScore + "");

		}
	}

	public double Average(int[] score) {
		int size;
		float avScore;
		float TScore = 0;
		size = score.length;
		for (int i = 0; i < size; i++) {
			TScore = TScore + score[i];
		}
		avScore = TScore / size;
		return avScore;
	}

	public boolean JudgeIp(Socket s1, Socket s2)
			throws UnsupportedEncodingException {
		String ip1 = s1.getInetAddress().getHostAddress();
		String ip2 = s2.getInetAddress().getHostAddress();
		System.out.print("======ip1====" + ip1 + "\n");
		System.out.print("======ip2====" + ip2 + "\n");
		if (ip1.equals(ip2)) {
			return true;
		} else {
			return false;
		}
	}
}
