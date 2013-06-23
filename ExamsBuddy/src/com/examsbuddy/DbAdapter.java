package com.examsbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {

	// Camps de la taula d'assignatures
	public static final String KEY_ASSIG_ROWID = "_id";
	public static final String KEY_ASSIG_NAME = "name";
	public static final String KEY_ASSIG_NUMBER = "number";
	public static final String KEY_ASSIG_AVG = "average";
	private static final String ASSIG_TABLE = "assignatures";
	
	//Camps de la taula d'examens
	public static final String KEY_EXAMENS_ROWID = "_id";
	public static final String KEY_EXAMENS_NAME = "name";
	public static final String KEY_EXAMENS_GRADE = "grade";
	public static final String KEY_EXAMENS_WEIGHT = "weight";
	public static final String KEY_EXAMENS_DAY = "date";
	public static final String KEY_EXAMENS_ASSIG = "assignatura";
	private static final String EXAMENS_TABLE = "examens";
	
	//Camps de la taula de notes
	public static final String KEY_NOTES_ROWID = "_id";
	public static final String KEY_NOTES_TITLE = "titol";
	public static final String KEY_NOTES_BODY = "contingut";
	private static final String NOTES_TABLE = "notes";

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
	   Operacions sobre la taula d'assignatures
	*********************************************/
	public long insertAssig(String name, String number, double avg) {
		ContentValues values = new ContentValues();
		values.put(KEY_ASSIG_NAME, name);
		values.put(KEY_ASSIG_NUMBER, number);
		values.put(KEY_ASSIG_AVG, avg);
		return database.insertOrThrow(ASSIG_TABLE, null, values);
	}

	public boolean updateAssigAvg(String name, double avg) {
		ContentValues values = new ContentValues();
		values.put(KEY_ASSIG_NAME, name);
		values.put(KEY_ASSIG_AVG, avg);

		return database.update(ASSIG_TABLE, values, KEY_ASSIG_NAME + "=" + "\"" +name+ "\"", null) > 0;
	}


	public boolean deleteAssig(long rowId) {
		
		return database.delete(ASSIG_TABLE, KEY_ASSIG_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllAssig() {
		return database.query(ASSIG_TABLE, new String[] { KEY_ASSIG_ROWID,	KEY_ASSIG_NAME, KEY_ASSIG_NUMBER, KEY_ASSIG_AVG }, null, null, null,
				null, null);
	}

	public Cursor fetchAssig(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, ASSIG_TABLE, new String[] { KEY_ASSIG_ROWID,	KEY_ASSIG_NAME, KEY_ASSIG_NUMBER, KEY_ASSIG_AVG },
				KEY_ASSIG_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	/*********************************************
	   Operacions sobre la taula d'examens
	*********************************************/
	public long insertExamen(String name, double grade, double weight, String day, String assignatura) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXAMENS_NAME, name);
		values.put(KEY_EXAMENS_GRADE, grade);
		values.put(KEY_EXAMENS_WEIGHT, weight);
		values.put(KEY_EXAMENS_DAY, day);
		values.put(KEY_EXAMENS_ASSIG, assignatura);

		return database.insert(EXAMENS_TABLE, null, values);
	}

	public boolean updateExamen(long rowId, String name, double grade, double weight, String day) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXAMENS_NAME, name);
		values.put(KEY_EXAMENS_GRADE, grade);
		values.put(KEY_EXAMENS_WEIGHT, weight);
		values.put(KEY_EXAMENS_DAY, day);

		return database.update(EXAMENS_TABLE, values, KEY_EXAMENS_ROWID + "=" + rowId, null) > 0;
	}


	public boolean deleteExamen(String assig) {
		return database.delete(EXAMENS_TABLE, KEY_EXAMENS_ASSIG + "=" + "\"" +assig+ "\"", null) > 0;
	}

	public Cursor fetchAllExamens(String assig) {
		return database.query(EXAMENS_TABLE, new String[] { KEY_EXAMENS_ROWID,	KEY_EXAMENS_NAME, KEY_EXAMENS_GRADE, KEY_EXAMENS_WEIGHT, KEY_EXAMENS_DAY, KEY_EXAMENS_ASSIG },
				KEY_EXAMENS_ASSIG + "=" + "\"" +assig+ "\"", null, null, null, null);
	}

	public Cursor fetchExamen(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, EXAMENS_TABLE, new String[] { KEY_EXAMENS_ROWID,	KEY_EXAMENS_NAME, KEY_EXAMENS_GRADE, KEY_EXAMENS_WEIGHT, KEY_EXAMENS_DAY, KEY_EXAMENS_ASSIG  },
				KEY_EXAMENS_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/*********************************************
	   Operacions sobre la taula de notes 
	*********************************************/
	
	public long insertNota(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOTES_TITLE, title);
        initialValues.put(KEY_NOTES_BODY, body);

        return database.insert(NOTES_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {

        return database.delete(NOTES_TABLE, KEY_NOTES_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return database.query(NOTES_TABLE, new String[] {KEY_NOTES_ROWID, KEY_NOTES_TITLE,
                KEY_NOTES_BODY}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = database.query(true, NOTES_TABLE, new String[] {KEY_NOTES_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY}, KEY_NOTES_ROWID + "=" + rowId, null,
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
        args.put(KEY_NOTES_TITLE, title);
        args.put(KEY_NOTES_BODY, body);

        return database.update(NOTES_TABLE, args, KEY_NOTES_ROWID + "=" + rowId, null) > 0;
    }
}

