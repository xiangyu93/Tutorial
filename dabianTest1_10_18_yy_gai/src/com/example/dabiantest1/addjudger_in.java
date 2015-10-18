package com.example.dabiantest1;

import java.util.List;
import java.util.Map;

import com.example.dabiantest1.addgroup_in.GroupAdapter;

import dabian.db.DBManager;
import dabian.db.Group;
import dabian.db.Judger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class addjudger_in extends Activity {
	private ImageButton btn_add_judger;
	private ListView judger_list;
	DBManager dbManager;
	Cursor cursor;
	Judger judger;
	DBManager db;
	Judger query_judger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addjudger_in);
		db = new DBManager(this);
		cursor = db.queryTheJudgerCursor();
		judger_list = (ListView) findViewById(R.id.judger_list);

		JudgerAdapter judgerAdapter = new JudgerAdapter(
				getApplicationContext(), cursor);
		judger_list.setAdapter(judgerAdapter);
		findViewById(R.id.btn_add_judger).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.add_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.add);
				}
				return false;
			}
		});
		findViewById(R.id.btn_add_judger).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(addjudger_in.this,
								addjudgermember.class);
						startActivityForResult(i, 0);
					}
				});
		judger_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				query_judger = db.queryJudger(position);
				Intent query = new Intent();
				Bundle bundle = new Bundle();
				query.putExtra("position", String.valueOf(position));
				bundle.putSerializable("query_judger", query_judger);
				query.putExtras(bundle);
				query.setClass(addjudger_in.this, addjudgermember.class);
				startActivityForResult(query, 1);
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 添加小组
		if (requestCode == 0 && resultCode == 0) {
			Log.i("data", data.toString());
			Bundle bundle = data.getExtras();
			Judger judger = (Judger) bundle.getSerializable("judger");
			db.addJudger(judger);
			cursor.requery();
			judger_list.invalidateViews();
		}
		// 查询小组
		if (requestCode == 1 && resultCode == 1) {
			cursor.requery();
			judger_list.invalidateViews();
		}
		// 删除小组
		if (requestCode == 1 && resultCode == 2) {
			Judger judger = query_judger;
			db.deleteJudger(judger);
			cursor.requery();
			judger_list.invalidateViews();
		}
	}

	public class JudgerAdapter extends BaseAdapter {
		Context context;
		Cursor cursor;

		public JudgerAdapter(Context context, Cursor cursor) {
			this.context = context;
			this.cursor = cursor;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stuz
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View judger_list = layoutInflater.inflate(
					R.layout.add_judgerlist_bg, null);
			TextView group_num = (TextView) judger_list
					.findViewById(R.id.judger_num);
			Judger judger = db.queryJudger(position);
			group_num.setText(judger._id + "");
			return judger_list;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();// 退出程序时释放DB
		db.closeDB();

	}
}
