package pedroanddev.studyhelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditNote extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private DbAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dba = new DbAdapter(this);
        dba.open();

        setContentView(R.layout.edit_note);

        mTitleText = (EditText) findViewById(R.id.nota_titol);
        mBodyText = (EditText) findViewById(R.id.nota_contingut);

        Button confirmButton = (Button) findViewById(R.id.nota_guardar);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DbAdapter.KEY_TODO_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(DbAdapter.KEY_TODO_ROWID): null;
		}

		populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = dba.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(note.getColumnIndexOrThrow(DbAdapter.KEY_TODO_TITLE)));
            mBodyText.setText(note.getString(note.getColumnIndexOrThrow(DbAdapter.KEY_TODO_BODY)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(DbAdapter.KEY_TODO_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (mRowId == null) {
        	if(title.length() != 0 && body.length() != 0) {
        		long id = dba.insertNote(title, body);
                if (id > 0) {
                    mRowId = id;
                }
        	}
        } else {
            dba.updateNote(mRowId, title, body);
        }
    }

}
