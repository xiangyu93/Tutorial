package com.example.dabiantest1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class addinformation extends Activity {
	private ImageButton addgroup;
	private ImageButton addjudger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmember);
		findViewById(R.id.addjudger).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.addjudger_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.addjudger);
				}
				return false;
			}
		});
		findViewById(R.id.addgroup).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.addgroup_pressed);
				} else if(event.getAction() == MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.addgroup);
				}
				return false;
			}
		});
		findViewById(R.id.addgroup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(addinformation.this,addgroup_in.class);
				startActivity(i);
			}
		});
	findViewById(R.id.addjudger).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(addinformation.this,addjudger_in.class);
				startActivity(i);
			}
		});
	}
}
