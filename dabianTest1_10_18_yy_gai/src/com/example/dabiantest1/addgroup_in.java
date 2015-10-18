package com.example.dabiantest1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import dabian.db.DBManager;
import dabian.db.Group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class addgroup_in extends Activity {
	private ImageButton btn_add_group;
	List<Map<String, Object>> listItems;
	SimpleAdapter simpleAdapter;
	private int num = 1;
	private ListView listView;
	DBManager db;
	 Cursor cursor;
	 Group query_group;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgroup_in);
		db = new DBManager(this);
		cursor = db.GroupSelect();
		listView = (ListView) findViewById(R.id.group_list);

		GroupAdapter groupAdapter = new GroupAdapter(getApplicationContext(),
				cursor);
		listView.setAdapter(groupAdapter);
		findViewById(R.id.btn_add_group).setOnTouchListener(new OnTouchListener() {
			
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
		findViewById(R.id.btn_add_group).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(addgroup_in.this,
								addgroupmember.class);
						// startActivity(i);
						startActivityForResult(i, 0);
						// Map<String, Object> map = new HashMap<String,
						// Object>();
						// map.put("group_num", num);
						// listItems.add(map);
						// listView.setAdapter(simpleAdapter);
						// num++;
					}
				});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				query_group = db.queryGroup(position);
				Intent query = new Intent();
				Bundle bundle = new Bundle();
				query.putExtra("position", String.valueOf(position));
				bundle.putSerializable("query_group", query_group);
				query.putExtras(bundle);
				query.setClass(addgroup_in.this, addgroupmember.class);
				startActivityForResult(query, 1);
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//添加小组
		if (requestCode == 0 && resultCode == 0) {
			Log.i("data", data.toString());
			Bundle bundle = data.getExtras();
			Group group = (Group) bundle.getSerializable("group");
			db.addGroup(group);
			cursor.requery();
			listView.invalidateViews();
		}
		//查询小组
		if(requestCode == 1 && resultCode == 1){
			cursor.requery();
			listView.invalidateViews();
		}
		//删除小组
		if(requestCode == 1 && resultCode == 2){
			Group group = query_group;
			db.deleteGroup(group);
			cursor.requery();
			listView.invalidateViews();
		}
	}

	public class GroupAdapter extends BaseAdapter {
		Context context;
		Cursor cursor;

		public GroupAdapter(Context context, Cursor cursor) {
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
			View group_list = layoutInflater.inflate(R.layout.add_grouplist_bg,null);
			TextView group_num = (TextView) group_list
					.findViewById(R.id.group_num);
			Group group = db.queryGroup(position);
			group_num.setText(group._id + "");
			return group_list;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();// 退出程序时释放DB
		db.closeDB();
		
	}
}
