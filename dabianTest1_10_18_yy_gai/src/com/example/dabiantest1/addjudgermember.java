package com.example.dabiantest1;

import dabian.db.DBManager;
import dabian.db.Group;
import dabian.db.Judger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class addjudgermember extends Activity {
	EditText add_judgername;
	EditText add_judger_num;
	EditText add_judger_introduce;
	ImageButton add_judger_store;
	ImageButton add_judger_cancel;
	ImageButton add_judger_delete;
	Intent show;
	DBManager db;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addjudgermember);
		initial();// 初始化
		db = new DBManager(this);
		show = getIntent();
		if (show.getExtras() != null) {
			position = Integer.valueOf(show.getStringExtra("position"));
			Bundle bundle = show.getExtras();
			Judger judger = (Judger) bundle.getSerializable("query_judger");
			add_judger_num.setText(judger._id + "");
			add_judgername.setText(judger.judger_name);
			add_judger_introduce.setText(judger.judger_introduce);
		}
		add_judger_store.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (!IsEmpty()) {
					Judger judger1 = new Judger();
					judger1._id = add_judger_num.getText().toString();
					judger1.judger_name = add_judgername.getText().toString();
					judger1.judger_introduce = add_judger_introduce.getText()
							.toString();
					if (show.getExtras() != null) {
						Log.i("position", position + "");
						Judger judger = db.queryJudger(position);
						if (!DBHasThisJudger(judger)) {
							Log.i("DBHasThisJudger(judger)",
									DBHasThisJudger(judger) + "");
							Bundle bundle = new Bundle();
							bundle.putSerializable("judger", judger1);
							Intent added = getIntent();
							added.putExtras(bundle);
							addjudgermember.this.setResult(0, added);
							addjudgermember.this.finish();
						} else {
							db.updateJudger(judger1);
							Toast.makeText(getApplicationContext(), "修改成功！",
									1000).show();
							addjudgermember.this.finish();
						}
					} else {
						Bundle bundle = new Bundle();
						bundle.putSerializable("judger", judger1);
						Intent added = getIntent();
						added.putExtras(bundle);
						addjudgermember.this.setResult(0, added);
						addjudgermember.this.finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请填写完整！", 1000)
							.show();
				}

			}
		});
		add_judger_delete.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.delete_pressed);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.remove);
				}
				return false;
			}
		});
		add_judger_store.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.store_pressed);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.store);
				}
				return false;
			}
		});
		add_judger_cancel.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.cancel_pressed);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.cancel);
				}
				return false;
			}
		});
		add_judger_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addjudgermember.this.setResult(2, show);
				addjudgermember.this.finish();
			}
		});
		add_judger_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addjudgermember.this.setResult(1, show);
				addjudgermember.this.finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			addjudgermember.this.setResult(1, show);
			addjudgermember.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean DBHasThisJudger(Judger judger) {
		if (judger._id.equals(add_judger_num.getText().toString())) {
			return true;
		}
		return false;
	}

	private void initial() {
		add_judger_num = (EditText) findViewById(R.id.add_judger_num);
		add_judgername = (EditText) findViewById(R.id.add_judger_name);
		add_judger_introduce = (EditText) findViewById(R.id.add_judger_introduce);
		add_judger_store = (ImageButton) findViewById(R.id.add_judger_store);
		add_judger_cancel = (ImageButton) findViewById(R.id.add_judger_cancel);
		add_judger_delete = (ImageButton) findViewById(R.id.add_judger_delete);
	}

	private boolean IsEmpty() {
		if (String.valueOf(add_judger_num.getText().toString()).equals("")
				|| add_judgername.getText().toString().equals("")
				|| add_judger_introduce.getText().toString().equals("")) {
			return true;
		} else {
			return false;
		}

	}
}
