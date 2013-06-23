package pedroanddev.studyhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	//Subjects table fields
	public static final String KEY_SUBJECTS_ROWID = "_id";
	public static final String KEY_SUBJECTS_NAME = "name";
	public static final String KEY_SUBJECTS_NUMBER = "number";
	public static final String KEY_SUBJECTS_AVG = "average";
	private static final String SUBJECTS_TABLE = "subjects";
	
	//Exams table fields
	public static final String KEY_EXAMS_ROWID = "_id";
	public static final String KEY_EXAMS_NAME = "name";
	public static final String KEY_EXAMS_GRADE = "grade";
	public static final String KEY_EXAMS_WEIGHT = "weight";
	public static final String KEY_EXAMS_DAY = "date";
	public static final String KEY_EXAMS_ASSIG = "subject";
	private static final String EXAMS_TABLE = "exams";
	
	//ToDo table fields
	public static final String KEY_TODO_ROWID = "_id";
	public static final String KEY_TODO_TITLE = "title";
	public static final String KEY_TODO_BODY = "body";
	private static final String TODO_TABLE = "todo";

	private Context context;
	private SQLiteDatabase database;
	private DbHelper dbHelper;

	public DbAdapter(Context context) {
		this.context = context;
	}

	public DbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/*********************************************
	  Subjects table operations
	*********************************************/
	public long insertSubject(String name, String number, double avg) {
		ContentValues values = new ContentValues();
		values.put(KEY_SUBJECTS_NAME, name);
		values.put(KEY_SUBJECTS_NUMBER, number);
		values.put(KEY_SUBJECTS_AVG, avg);
		return database.insertOrThrow(SUBJECTS_TABLE, null, values);
	}

	public boolean updateSubjectAvg(String name, double avg) {
		ContentValues values = new ContentValues();
		values.put(KEY_SUBJECTS_NAME, name);
		values.put(KEY_SUBJECTS_AVG, avg);
		return database.update(SUBJECTS_TABLE, values, KEY_SUBJECTS_NAME + "=" + "\"" +name+ "\"", null) > 0;
	}


	public boolean deleteSubject(long rowId) {
		return database.delete(SUBJECTS_TABLE, KEY_SUBJECTS_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllSubjects() {
		return database.query(SUBJECTS_TABLE, new String[] { KEY_SUBJECTS_ROWID, KEY_SUBJECTS_NAME, KEY_SUBJECTS_NUMBER, KEY_SUBJECTS_AVG }, null, null, null,
								null, null);
	}

	public Cursor fetchSubject(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, SUBJECTS_TABLE, new String[] { KEY_SUBJECTS_ROWID, KEY_SUBJECTS_NAME, KEY_SUBJECTS_NUMBER, KEY_SUBJECTS_AVG },
										KEY_SUBJECTS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	/*********************************************
	  Exams table operations
	*********************************************/
	public long insertExam(String name, double grade, double weight, String day, String subject) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXAMS_NAME, name);
		values.put(KEY_EXAMS_GRADE, grade);
		values.put(KEY_EXAMS_WEIGHT, weight);
		values.put(KEY_EXAMS_DAY, day);
		values.put(KEY_EXAMS_ASSIG, subject);
		return database.insert(EXAMS_TABLE, null, values);
	}

	public boolean updateExam(long rowId, String name, double grade, double weight, String day) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXAMS_NAME, name);
		values.put(KEY_EXAMS_GRADE, grade);
		values.put(KEY_EXAMS_WEIGHT, weight);
		values.put(KEY_EXAMS_DAY, day);
		return database.update(EXAMS_TABLE, values, KEY_EXAMS_ROWID + "=" + rowId, null) > 0;
	}


	public boolean deleteExam(String subject) {
		return database.delete(EXAMS_TABLE, KEY_EXAMS_ASSIG + "=" + "\"" +subject+ "\"", null) > 0;
	}

	public Cursor fetchAllExams(String subject) {
		return database.query(EXAMS_TABLE, new String[] { KEY_EXAMS_ROWID,	KEY_EXAMS_NAME, KEY_EXAMS_GRADE, KEY_EXAMS_WEIGHT, KEY_EXAMS_DAY, KEY_EXAMS_ASSIG },
								KEY_EXAMS_ASSIG + "=" + "\"" +subject+ "\"", null, null, null, null);
	}

	public Cursor fetchExam(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, EXAMS_TABLE, new String[] { KEY_EXAMS_ROWID,	KEY_EXAMS_NAME, KEY_EXAMS_GRADE, KEY_EXAMS_WEIGHT, KEY_EXAMS_DAY, KEY_EXAMS_ASSIG  },
										KEY_EXAMS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/*********************************************
	   ToDo table operations
	*********************************************/
	
	public long insertNote(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TODO_TITLE, title);
        initialValues.put(KEY_TODO_BODY, body);
        return database.insert(TODO_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {
        return database.delete(TODO_TABLE, KEY_TODO_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {
        return database.query(TODO_TABLE, new String[] {KEY_TODO_ROWID, KEY_TODO_TITLE, KEY_TODO_BODY}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = database.query(true, TODO_TABLE, new String[] {KEY_TODO_ROWID, KEY_TODO_TITLE, KEY_TODO_BODY}, KEY_TODO_ROWID + "=" + rowId, null,
        								null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TODO_TITLE, title);
        args.put(KEY_TODO_BODY, body);
        return database.update(TODO_TABLE, args, KEY_TODO_ROWID + "=" + rowId, null) > 0;
    }
}
