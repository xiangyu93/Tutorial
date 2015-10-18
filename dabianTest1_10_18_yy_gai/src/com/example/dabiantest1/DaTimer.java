package com.example.dabiantest1;

import java.io.File;
import java.net.Socket;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import util.DaTimerHandlerUtil;
import util.ServerUtil;

import MySocket.MyServerSocket;
import MySocket.MyServerSocketManager;
import MySocket.saveAllScores;
import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent.OnFinished;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DaTimer extends Activity {
	private TextView timeView;
	private TextView groupName;
	private Button next_group, finish;
	private int time;
	private String group_name;
	private ImageButton back_DaTimer;
	private ImageView image;
	Context context;
	Handler handler = null;
	saveAllScores save;
	MyCountTimer myCountTimer;
	private static final int judge_over = 0x159;
	MyServerSocketManager MSSM = null;
	Vector<MyServerSocket> vector = null;
	MyServerSocket MSS = null;
	JSONObject json;
	boolean flag = false;// 所有老师提交完成标志

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running);
		image = (ImageView) findViewById(R.id.time_left_view);
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x23) {
					Toast.makeText(DaTimer.this, msg.obj + "老师完成评分！", 1000)
							.show();
				}
				super.handleMessage(msg);
			}
		};
		DaTimerHandlerUtil contextUtil = DaTimerHandlerUtil.getDa();
		contextUtil.setHandler(handler);
		next_group = (Button) findViewById(R.id.start_next);
//		groupName = (TextView) findViewById(R.id.runing_group);
		timeView = (TextView) findViewById(R.id.time_view);
		back_DaTimer = (ImageButton) findViewById(R.id.back_DaTimer);
		finish = (Button) findViewById(R.id.finish_judge);
		back_DaTimer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishJudge();
				json = new JSONObject();
				try {
					json.put("finishJudge", "finishJudge");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				hostSendToClient(json);
			}
		});
		Intent intent = getIntent();
		group_name = intent.getStringExtra("group_name");
		groupName.setText(group_name);
		time = intent.getIntExtra("time", 0);
		myCountTimer = new MyCountTimer(time * 60000, 1000, timeView);
		myCountTimer.start();
		startAnimation(image);
	}

	class MyCountTimer extends CountDownTimer {
		TextView refreshTime;

		public MyCountTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		public MyCountTimer(long millisInFuture, long countDownInterval,
				TextView Timeview) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
			this.refreshTime = Timeview;
		}

		@Override
		public void onTick(final long millisUntilFinished) {
			// TODO Auto-generated method stub
			refreshTime.setText(millisUntilFinished / 60000 + ":"
					+ (millisUntilFinished % 60000) / 1000);
			refreshTime.setTextColor(Color.RED);
			next_group.setOnClickListener(new OnClickListener() {

				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if ((millisUntilFinished % 60000) / 1000 != 0
							|| (millisUntilFinished / 60000) != 0) {
						Toast.makeText(getApplicationContext(),
								"请等到该组答辩完成再继续下一组", 3000).show();
					}
				}

			});
		}

		@SuppressLint("ShowToast")
		@Override
		public void onFinish() {
			stopAnimation(image);
			json = new JSONObject();
			try {
				json.put("OVER", "OVER");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hostSendToClient(json);
			refreshTime.setTextSize(30);
			refreshTime.setText("评委正在评分！请耐心等待！");
			refreshTime.setTextColor(Color.RED);
			// 震动
			Vibrator vibrator;
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(1000);
			next_group.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					if (MyServerSocketManager.getMyServerSocketManager()
							.is_JudgeOver()) {
						save = new saveAllScores();
						save.setGroupName(group_name);
						save.SaveAllScores();
						File file = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "答辩" + File.separator + "Scores.txt");
						if (!file.getParentFile().exists()) {
							Toast.makeText(DaTimer.this, "保存失败！！！",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(DaTimer.this, "保存成功！！！",
									Toast.LENGTH_SHORT).show();
							Intent back_BeginDemo = new Intent();
							back_BeginDemo.setClass(DaTimer.this,
									BeginDemo.class);
							startActivity(back_BeginDemo);
							finish();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"还有老师未完成评分，请耐心等待！", 1000).show();
					}
				}
			});
		}
	}

	public void finishJudge() {
		myCountTimer.cancel();
		myCountTimer.onFinish();
	}

	public void hostSendToClient(JSONObject json){
		MSSM = MyServerSocketManager.getMyServerSocketManager();
		vector = MSSM.vector;
		for (int x = 0; x < vector.size(); x++) {
			MSS = vector.get(x);
			MSS.sendToClient(json.toString());
		}
	}
	public void startAnimation(View view) {
		final RotateAnimation animation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(1700);// 设置动画持续时间
		animation.setRepeatCount(10000000);
		view.setAnimation(animation);
	}

	public void stopAnimation(View view) {
		view.clearAnimation();
	}
}
