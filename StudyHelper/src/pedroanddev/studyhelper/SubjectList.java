package pedroanddev.studyhelper;

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

public class SubjectList extends ListActivity {
	
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private DbAdapter dba = null;
	private Cursor cursor = null;
	private String selectedRow = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_list);
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dba != null) {
			dba.close();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.subject_list_menu, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.add) {
			startActivityForResult(new Intent(SubjectList.this, AddSubject.class), ACTIVITY_CREATE);
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
			dba.deleteSubject(info.id);
			dba.deleteExam(selectedRow);
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
		String c = s.getString(s.getColumnIndexOrThrow(DbAdapter.KEY_SUBJECTS_NAME));
		Intent i = new Intent(this, Subject.class);
		i.putExtra(DbAdapter.KEY_SUBJECTS_ROWID, id);
		i.putExtra("assig", c);
     	startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
	
	private void fillData() {
		cursor = dba.fetchAllSubjects();
		startManagingCursor(cursor);
		
		String[] from = new String[] { DbAdapter.KEY_SUBJECTS_NAME , DbAdapter.KEY_SUBJECTS_AVG };
		int[] to = new int[] { R.id.nom_row , R.id.mitja_row};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.subject_list_row, cursor, from , to);
	    notes.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
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
						TextView t = (TextView) view;
						String s = cursor.getString(columnIndex);
//						Log.v("value.db.average","value retrieved :" + s);
						double avg = Double.valueOf(s).doubleValue();
						if(avg >= 5.0) {
							t.setTextColor(0xFF0000FF);
						}
						else {
							t.setTextColor(0xFFFF0000);
						}
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
}
