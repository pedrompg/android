package pedroanddev.studyhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "db";

	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("create table exams (_id integer primary key autoincrement, " +
				"name text not null , grade real not null, weight real not null, date text not null, " +
				"subject text not null);");
		database.execSQL("create table subjects (_id integer primary key autoincrement, name text not null unique, " +
				"number text not null, average not null)");
		database.execSQL("create table todo (_id integer primary key autoincrement, title text not null, body text not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
