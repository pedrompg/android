package com.examsbuddy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartScreen extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inici);
		TextView t = (TextView)findViewById(R.id.iniciTextView);
		TextView t2 = (TextView)findViewById(R.id.iniciTextView2);
		Typeface tf = Typeface.createFromAsset(getAssets(),"4.ttf");
		t.setTypeface(tf);
		t2.setTypeface(tf);
		Button assig = (Button)findViewById(R.id.button1);
		Button notes = (Button)findViewById(R.id.button2);
		Button ajuda = (Button)findViewById(R.id.button3);
		assig.setOnClickListener(onClickAssig);
		notes.setOnClickListener(onClickNotes);
		ajuda.setOnClickListener(onClickAjuda);
	}
	
	private View.OnClickListener onClickAssig = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), ExamsBuddy.class);
	     	startActivity(i);
		}
	};
	
	private View.OnClickListener onClickNotes = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), Notes_list.class);
	     	startActivity(i);
		}
	};
	
	private View.OnClickListener onClickAjuda = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), Help.class);
	     	startActivity(i);
		}
	};
}
