package com.example.dabiantest1;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import util.ServerUtil;

import MySocket.MyServerSocket;
import MySocket.MyServerSocketListener;
import MySocket.MyServerSocketManager;
import MySocket.ServerSend;
import MySocket.saveAllScores;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class BeginDemo extends Activity {
	EditText theme_edit;
	EditText rule_edit;
	EditText group_edit;
	EditText time_edit;
	String theme;
	String rule;
	int time;
	String groupName;
	Button btn_begin;
	Button btn_query;
	Button back_begin;
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	Vector<MyServerSocket> vector;
	MyServerSocket ms = null;
	Socket s;
	File file;
	final saveAllScores save = new saveAllScores();
	PrintWriter outToClient;
	JSONObject json = null;
	Handler handler;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin);
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x14) {
					Toast.makeText(getApplicationContext(), msg.obj + "成功连接！",
							1000).show();
				}
				super.handleMessage(msg);
			}
		};
		new MyServerSocketListener(handler).start();
		btn_query = (Button) findViewById(R.id.query);
		rule_edit = (EditText) findViewById(R.id.rule_edit);
		group_edit = (EditText) findViewById(R.id.group_edit);
		time_edit = (EditText) findViewById(R.id.time_edit);
		btn_begin = (Button) findViewById(R.id.btn_start);
		back_begin = (Button) findViewById(R.id.back_begin);
		back_begin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		MyServerSocketManager MSS = MyServerSocketManager
				.getMyServerSocketManager();
		this.vector = MSS.vector;
		btn_begin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ServerUtil serverUtil = ServerUtil.getServerUtil();
				serverUtil.removeJson();
				// TODO Auto-generated method stub
				rule = rule_edit.getText().toString();
				groupName = group_edit.getText().toString();
				time = Integer.parseInt(time_edit.getText().toString()
						.trim());
				json = new JSONObject();
				try {
					json.put("group_name", groupName);
					json.put("time", time);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("----------vector Size---------", vector.size() + "");
				if (!groupName.equals("")
						&&!time_edit.getText().toString().equals("")) {
					for (int i = 0; i < vector.size(); i++) {
						ms = vector.get(i);
						ms.sendToClient(json.toString());
					}
					time = Integer.parseInt(time_edit.getText().toString()
							.trim());
					Intent to_Datimer = new Intent();
					to_Datimer.putExtra("time", time);
					to_Datimer.putExtra("group_name", groupName);
					to_Datimer.setClass(BeginDemo.this, DaTimer.class);
					startActivity(to_Datimer);
					finish();
				} else {
					Toast.makeText(BeginDemo.this, "对不起，你的信息未输入完，不能发起答辩",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				file = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "答辩" + File.separator + "Scores.txt");
				if (!file.getParentFile().exists()) {
					Toast.makeText(BeginDemo.this, "对不起，您查找的文件不存在!!!",
							Toast.LENGTH_SHORT).show();
				} else {
					new ServerSend().start();
					System.out.print("----------发送了！！！----------" + "\n");
					Intent intent = new Intent();
					intent.setClass(BeginDemo.this, GroupsScore.class);
					startActivity(intent);
				}
			}
		});
	}
}
