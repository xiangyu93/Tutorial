package com.example.dabiantest1;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dabiantest1.DaTimer.MyCountTimer;

import MySocket.ClientSocket;
import MySocket.MyServerSocketManager;
import MySocket.saveAllScores;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JudgeDemo extends Activity {
	private TextView Judge_Title;
	private EditText Score1;
	private EditText Score2;
	private EditText Score3;
	private EditText Name_t;
	private Button Commit;
	private Button reCommit;
	private Button Query;
	private ImageView image;
	private ImageButton back_JudgeDemo;
	private int score1Str;
	private int score2Str;
	private int score3Str;
	private String name_t;
	private String Scores;
	JSONObject param1;
	private Handler mHandler;
	private final int COMMIT = 0;
	private final int QUERY = 1;
	private final int CONNECT = 3;
	private final int DISCONNECTED = 4;
	private final int OVER = 5;// 该组答辩完成标志
	private final int FINISHJUDGE = 6;
	private TextView timeView;
	ClientSocket CS = null;
	JSONObject json = null;
	MyCountTimer myCountTimer;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judge);
		timeView = (TextView) findViewById(R.id.time);
		image = (ImageView) findViewById(R.id.imagejudge);
		back_JudgeDemo = (ImageButton) findViewById(R.id.back_JudgeDemo);
		back_JudgeDemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		init();
		mHandler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				try {
					if (msg.what == 0x123) {
						json = new JSONObject(msg.obj.toString());
						String groupName = json.getString("group_name");
						int time = json.getInt("time");
						myCountTimer = new MyCountTimer(time * 60000, 1000,
								timeView);// 开启倒计时
						myCountTimer.start();
						startAnimation(image);
						Log.i("=======C Thread json=======", groupName);
						Judge_Title.setText(groupName + "正在答辩");
						Vibrator vibrator;
						vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(1000);
						Toast.makeText(getApplicationContext(),
								"小组：" + groupName + ",正在答辩，请等到该组答辩完成后为他评分！",
								1000).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (msg.what == CONNECT) {
					Toast.makeText(getApplicationContext(), "成功连接至" + msg.obj,
							1000).show();
				}
				if (msg.what == DISCONNECTED) {
					Toast.makeText(getApplicationContext(), "连接已断开！", 1000)
							.show();
				}
				if (msg.what == OVER) {
					Toast.makeText(getApplicationContext(), "该组完成答辩，请为该组评分！",
							1000).show();
				}
				if (msg.what == FINISHJUDGE) {
					Toast.makeText(getApplicationContext(), "答辩提前完成，请为该组评分！",
							1000).show();
					finishJudge();
				}
			}
		};
		CS = new ClientSocket(getApplicationContext(), mHandler);
		CS.start();
		param1 = new JSONObject();

		reCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Score1.setText("");
				Score2.setText("");
				Score3.setText("");
				Name_t.setText("");
			}
		});
		Query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("======revHandler-------", CS.revHandler + "");
				CS.revHandler.obtainMessage(QUERY).sendToTarget();
			}
		});
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
			Commit.setOnClickListener(new OnClickListener() {
				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) {
					if ((millisUntilFinished % 60000) / 1000 != 0
							|| (millisUntilFinished / 60000) != 0) {
						Toast.makeText(getApplicationContext(), "答辩还未完成，不能评分！",
								3000).show();
					}
				}
			});
		}

		@Override
		public void onFinish() {
			timeView.setText("0:0");
			stopAnimation(image);
			Commit.setOnClickListener(new OnClickListener() {
				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) {
					try {
						if (Score1.getText().toString().equals("")
								|| Score2.getText().toString().equals("")
								|| Score3.getText().toString().equals("")) {
							Toast.makeText(JudgeDemo.this, "对不起，您还未完成评分",
									Toast.LENGTH_SHORT).show();
						} else {
							writeDatas();
							param1.put("score1", score1Str);
							param1.put("score2", score2Str);
							param1.put("score3", score3Str);
							param1.put("name", name_t);
							CS.revHandler.obtainMessage(COMMIT,
									param1.toString()).sendToTarget();
							Toast.makeText(getApplicationContext(), "评分成功！",
									1000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	private void init() {
		Judge_Title = (TextView) findViewById(R.id.judge_title);
		Score1 = (EditText) findViewById(R.id.score1);
		Score2 = (EditText) findViewById(R.id.score2);
		Score3 = (EditText) findViewById(R.id.score3);
		Commit = (Button) findViewById(R.id.commit);
		reCommit = (Button) findViewById(R.id.recommit);
//		Name_t = (EditText) findViewById(R.id.name_t);
		Query = (Button) findViewById(R.id.Query);
	}

	private void writeDatas() {
		name_t = Name_t.getText().toString();
		score1Str = Integer.parseInt(Score1.getText().toString().trim());
		score2Str = Integer.parseInt(Score2.getText().toString().trim());
		score3Str = Integer.parseInt(Score3.getText().toString().trim());
	}

	public void startAnimation(View view) {
		final RotateAnimation animation = new RotateAnimation(0f, 360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(1700);// 设置动画持续时间
		animation.setRepeatCount(10000000);
		view.setAnimation(animation);
	}

	public void finishJudge() {
		myCountTimer.cancel();
		myCountTimer.onFinish();
	}

	public void stopAnimation(View view) {
		view.clearAnimation();
	}

}
