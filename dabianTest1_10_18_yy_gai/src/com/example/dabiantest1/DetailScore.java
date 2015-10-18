package com.example.dabiantest1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailScore extends Activity {
	TextView Score1, Score2, Score3, group;
	String Score1Str, Score2Str, Score3Str, groupName;
	Button back_Detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_score);
		Score1 = (TextView) findViewById(R.id.score1);
		Score2 = (TextView) findViewById(R.id.score2);
		Score3 = (TextView) findViewById(R.id.score3);
		group = (TextView) findViewById(R.id.judge_title);
		back_Detail = (Button) findViewById(R.id.back_Detail);
		back_Detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent getScore = getIntent();
		Score1Str = getScore.getStringExtra("AvScore1");
		Score2Str = getScore.getStringExtra("AvScore2");
		Score3Str = getScore.getStringExtra("AvScore3");
		groupName = getScore.getStringExtra("group_name");
		group.setText(groupName + "×éÏêÏ¸µÃ·Ö");
		Score1.setText(Score1Str);
		Score2.setText(Score2Str);
		Score3.setText(Score3Str);
	}
}
