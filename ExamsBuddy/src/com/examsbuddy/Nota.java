package com.examsbuddy;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Nota extends Activity{
	
	long rowId = 0;
	DbAdapter dba = null;
	Cursor cursor = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nota);
		dba = new DbAdapter(this);
		dba.open();
		TextView tv = (TextView)findViewById(R.id.textView_nota);
		Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
		tv.setTypeface(tf);
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			rowId = extras.getLong(DbAdapter.KEY_NOTES_ROWID);
			cursor = dba.fetchNote(rowId);
			tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_NOTES_BODY)));
		}
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DbAdapter.KEY_NOTES_ROWID, rowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
