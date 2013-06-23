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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NotesList extends ListActivity {
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    private DbAdapter dba;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
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
        Cursor notesCursor = dba.fetchAllNotes();
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{DbAdapter.KEY_TODO_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.note_list_row, notesCursor, from, to);
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
				return false;
			}
	    
	    });
        setListAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, getString(R.string.New_note)); 
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String selectedRow = ((TextView) info.targetView.findViewById(R.id.text1)).getText().toString();
        menu.setHeaderTitle(selectedRow);
        menu.add(0, DELETE_ID, 0, getString(R.string.Delete_note));
        menu.add(1, EDIT_ID, 1, getString(R.string.Edit_note));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info;
    	switch(item.getItemId()) {
            case DELETE_ID:
                info = (AdapterContextMenuInfo) item.getMenuInfo();
                dba.deleteNote(info.id);
                fillData();
                return true;
            case EDIT_ID:
            	info = (AdapterContextMenuInfo) item.getMenuInfo();
            	Intent i = new Intent(this, EditNote.class);
                i.putExtra(DbAdapter.KEY_TODO_ROWID, info.id);
                startActivityForResult(i, ACTIVITY_EDIT);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, EditNote.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Note.class);
        i.putExtra(DbAdapter.KEY_TODO_ROWID, id);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
