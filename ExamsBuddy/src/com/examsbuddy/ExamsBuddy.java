package com.examsbuddy;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ExamsBuddy extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private DbAdapter dba = null;
	private Cursor cursor = null;
	private String selectedRow = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	    TextView t = (TextView)findViewById(android.R.id.empty);
	    Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
		t.setTypeface(tf);
		int[] colors = {0, 0xFFFF0000, 0};
		this.getListView().setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		this.getListView().setDividerHeight(3);
		dba = new DbAdapter(this);
		dba.open();	
		fillData();
		getListView().setCacheColorHint(0);
		registerForContextMenu(getListView());
	}	

	
	private void fillData() {
		cursor = dba.fetchAllAssig();
		startManagingCursor(cursor);
		
		String[] from = new String[] { DbAdapter.KEY_ASSIG_NAME , DbAdapter.KEY_ASSIG_AVG };
		int[] to = new int[] { R.id.nom_row , R.id.mitja_row};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.examsbuddy_row, cursor, from , to);
	    notes.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(columnIndex == 1) {
					String s = cursor.getString(columnIndex);
					TextView t = (TextView) view;
					t.setText(s);
					Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
					t.setTypeface(tf);
					return true;
				}
				if(columnIndex == 3) {
					String s = cursor.getString(columnIndex);
					TextView t = (TextView) view;
					t.setText(getString(R.string.Average) + ": " + s);
					Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
					t.setTypeface(tf);
					return true;
				}
				return false;
			}
	    
	    });
		setListAdapter(notes);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dba != null) {
			dba.close();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.add) {
			startActivityForResult(new Intent(ExamsBuddy.this, AddSubject.class), ACTIVITY_CREATE);
			return(true);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    selectedRow = ((TextView) info.targetView.findViewById(R.id.nom_row)).getText().toString();
	    menu.setHeaderTitle(selectedRow);
	    menu.add(0, DELETE_ID, 0, getString(R.string.erase_subject));
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dba.deleteAssig(info.id);
			dba.deleteExamen(selectedRow);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
		
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor s = (Cursor) l.getItemAtPosition(position);
		s.moveToPosition(position);
		String c = s.getString(s.getColumnIndexOrThrow(DbAdapter.KEY_ASSIG_NAME));
		Intent i = new Intent(this, Subject.class);
		i.putExtra(DbAdapter.KEY_ASSIG_ROWID, id);
		i.putExtra("assig", c);
     	startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
 }
