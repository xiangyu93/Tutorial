package com.example.dabiantest1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MySocket.GetAllScoresFromFile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GroupsScore extends Activity {
	double[] TotalScore;
	String[] groupName;
	private ListView LV;
	double[] AvScore1;
	double[] AvScore2;
	double[] AvScore3;
	View dialogView;
	TextView dialog;
	GetAllScoresFromFile GetScores;
	SimpleAdapter simpleAdapter;
	private Button back_GroupScore;
	GetDatasFromServer GDFS;
	List<Map<String, Object>> listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_list);
		back_GroupScore = (Button) findViewById(R.id.back_GroupScore);
		back_GroupScore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listItems = new ArrayList<Map<String, Object>>();
		simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.detail_list, new String[] { "group_name",
						"group_score" }, new int[] { R.id.group_name,
						R.id.group_score });
		GetScores = GetAllScoresFromFile.get_GetAllScoresFromFile();
		GDFS = new GetDatasFromServer();
		GDFS.getdatas(simpleAdapter, listItems);

	}

	class GetDatasFromServer {
		@SuppressLint("InflateParams")
		public void getdatas(final SimpleAdapter simpleAdapter,
				final List<Map<String, Object>> listItems) {
			GetScores = GetAllScoresFromFile.get_GetAllScoresFromFile();
			View dialogView = getLayoutInflater()
					.inflate(R.layout.dialog, null);
			dialog = (TextView) dialogView.findViewById(R.id.dialog);
			GetScores.getData();
			TotalScore = GetScores.getTotalScore();
			groupName = GetScores.getGroupName();
			AvScore1 = GetScores.getAvScore1();
			AvScore2 = GetScores.getAvScore2();
			AvScore3 = GetScores.getAvScore3();
			if (TotalScore != null) {
				for (int i = 0; i < TotalScore.length; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("group_name", groupName[i]);
					listItem.put("group_score", translate(TotalScore[i]));
					listItems.add(listItem);
				}
				LV = (ListView) findViewById(R.id.score_list);
				LV.setAdapter(simpleAdapter);
				LV.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent to_detaiScore = new Intent();
						to_detaiScore.putExtra("group_name",
								groupName[position]);
						to_detaiScore.putExtra("AvScore1", translate(AvScore1[position]));
						to_detaiScore.putExtra("AvScore2", translate(AvScore2[position]));
						to_detaiScore.putExtra("AvScore3", translate(AvScore3[position]));
						to_detaiScore.setClass(GroupsScore.this,
								DetailScore.class);
						startActivity(to_detaiScore);
					}
				});
			}
			if (groupName == null) {
				new AlertDialog.Builder(GroupsScore.this)
						.setView(dialogView)
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// 回调自己的方法，从复执行自己的方法，获得数据
										getdatas(simpleAdapter, listItems);
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								}).show();
			}
		}
	}
	public String translate(double num){//保留一位小数
		DecimalFormat df = new DecimalFormat("0.0");
		String result = df.format(num);
		return result;
	}
}
