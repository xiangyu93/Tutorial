package com.example.dabiantest1;

import java.util.List;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import dabian.db.DBManager;
import dabian.db.Group;

public class addgroupmember extends Activity {
	private EditText add_group_num, add_groupmember, add_group_project;
	private ImageButton add_group_store, add_group_cancel, add_group_delete;
	private Button beginjudge;
	Intent show;
	DBManager db;
	List<Group> list;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgroupmember);
		initial();// 初始化
		db = new DBManager(this);
		show = getIntent();
		if (show.getExtras() != null) {
			position = Integer.valueOf(show.getStringExtra("position"));
			Bundle bundle = show.getExtras();
			Group group = (Group) bundle.getSerializable("query_group");
			add_group_num.setText(group._id + "");
			add_groupmember.setText(group.group_members);
			add_group_project.setText(group.group_project);
		}
		add_group_cancel.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.cancel_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.cancel);
				}
				return false;
			}
		});
		add_group_store.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.store_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.store);
				}
				return false;
			}
		});
		add_group_delete.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.delete_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.remove);
				}
				return false;
			}
		});
		add_group_store.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (!IsEmpty()) {
					Group group1 = new Group();
					group1._id = Integer.valueOf(add_group_num.getText()
							.toString());
					group1.group_members = add_groupmember.getText().toString();
					group1.group_project = add_group_project.getText()
							.toString();
					if (show.getExtras() != null) {
						Log.i("position", position+"");
						Group group = db.queryGroup(position);
						Log.i("DBHasThisGroup(group)", DBHasThisGroup(group)+"");
						if (!DBHasThisGroup(group)) {
							
							Bundle bundle = new Bundle();
							bundle.putSerializable("group", group1);
							Intent added = getIntent();
							added.putExtras(bundle);
							addgroupmember.this.setResult(0, added);
							addgroupmember.this.finish();
						} else {
							db.updateGroup(group1);
							Toast.makeText(getApplicationContext(), "修改成功！",
									1000).show();
							addgroupmember.this.finish();
						}
					}else{
						Bundle bundle = new Bundle();
						bundle.putSerializable("group", group1);
						Intent added = getIntent();
						added.putExtras(bundle);
						addgroupmember.this.setResult(0, added);
						addgroupmember.this.finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "请填写完整！", 1000)
							.show();
				}

			}
		});

		add_group_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addgroupmember.this.setResult(2, show);
				addgroupmember.this.finish();
			}
		});
		add_group_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addgroupmember.this.setResult(1, show);
				addgroupmember.this.finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			addgroupmember.this.setResult(1, show);
			addgroupmember.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean DBHasThisGroup(Group group) {
		if (group._id == Integer.valueOf(add_group_num.getText().toString())) {
			return true;
		}
		return false;
	}

	private void initial() {
		add_group_num = (EditText) findViewById(R.id.add_group_num);
		add_groupmember = (EditText) findViewById(R.id.add_groupmember);
		add_group_project = (EditText) findViewById(R.id.add_group_project);
		add_group_store = (ImageButton) findViewById(R.id.add_group_store);
		add_group_cancel = (ImageButton) findViewById(R.id.add_group_cancel);
		add_group_delete = (ImageButton) findViewById(R.id.add_group_delete);
		beginjudge = (Button) findViewById(R.id.beginjudge);
	}

	private boolean IsEmpty() {
		if (String.valueOf(add_group_num.getText().toString()).equals("")
				|| add_groupmember.getText().toString().equals("")
				|| add_group_project.getText().toString().equals("")) {
			return true;
		} else {
			return false;
		}

	}
}
