package pedroanddev.studyhelper;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;


public class Note extends Activity{
	
	long rowId = 0;
	DbAdapter dba = null;
	Cursor cursor = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
		dba = new DbAdapter(this);
		dba.open();
		TextView tv = (TextView)findViewById(R.id.textView_nota);
		Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
		tv.setTypeface(tf);
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			rowId = extras.getLong(DbAdapter.KEY_TODO_ROWID);
			cursor = dba.fetchNote(rowId);
			tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_TODO_BODY)));
		}
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DbAdapter.KEY_TODO_ROWID, rowId);
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

