package pedroanddev.studyhelper;

import java.math.BigDecimal;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Subject extends ListActivity {
	
	private static final int ACTIVITY_EDIT_EXAMEN = 3;
	private DbAdapter dba = null;
	private Cursor cursor = null;
	private String subject = null;
	private Bundle extras = null;
	private double average = 0.0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		int[] colors = {0, 0xFFFF0000, 0};
		this.getListView().setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		this.getListView().setDividerHeight(5);
		dba = new DbAdapter(this);
		dba.open();
		extras = getIntent().getExtras();
		if(extras != null) {
			subject = extras.getString("assig");
		}		
		fillData();
		getListView().setCacheColorHint(0);
	}	
	
	private boolean comprova() {
		cursor = dba.fetchAllExams(subject);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		double pes = 0;
		do {
			double p = cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_WEIGHT));
			pes += p;
			
		}	while(cursor.moveToNext());
		double factor = 1e2; // = 1 * 10^5 = 100000.
		pes = Math.round(pes*factor)/factor;	
		return (pes == 1.0);
	}
	
	private void calcul() {
		cursor = dba.fetchAllExams(subject);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		BigDecimal avg = new BigDecimal(0.0);
		BigDecimal n = new BigDecimal(0.0);
		BigDecimal p = new BigDecimal(0.0);
		BigDecimal mult = new BigDecimal(0.0);
		do {
			n = new BigDecimal(cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_GRADE)));
			p = new BigDecimal(cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_WEIGHT)));
			mult = n.multiply(p);
			avg = avg.add(mult);
		}	while(cursor.moveToNext());
		avg = avg.setScale(2, BigDecimal.ROUND_HALF_UP);
		average = avg.doubleValue();
		dba.updateSubjectAvg(subject, average);
	}
	
	
	private void fillData() {
		cursor = dba.fetchAllExams(subject);
//		startManagingCursor(cursor);
		cursor.moveToFirst();
		String[] from = new String[] { DbAdapter.KEY_EXAMS_NAME, DbAdapter.KEY_EXAMS_GRADE, DbAdapter.KEY_EXAMS_WEIGHT, DbAdapter.KEY_EXAMS_DAY };
		int[] to = new int[] { R.id.nom_examen, R.id.nota_examen , R.id.pes_examen, R.id.dia_examen };

		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.subject_row, cursor, from , to);
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
				if(columnIndex == 2) {
					String s = cursor.getString(columnIndex);
					TextView t = (TextView) view;
					double avg = Double.valueOf(s).doubleValue();
					if(avg >= 5.0) {
						t.setTextColor(0xFF0000FF);
					}
					else {
						t.setTextColor(0xFFFF0000);
					}
					t.setText(getString(R.string.Grade) + ": " + s);
					Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
					t.setTypeface(tf);
					return true;
				}
				if(columnIndex == 3) {
					double d = cursor.getDouble(columnIndex);
					int s = (int)(d*100);
					TextView t = (TextView) view;
					t.setText(getString(R.string.Weight) + ": " + s +"%");
					Typeface tf = Typeface.createFromAsset(getAssets(),"1.ttf");
					t.setTypeface(tf);
					return true;
				}
				if(columnIndex == 4) {
					String s = cursor.getString(columnIndex);
					TextView t = (TextView) view;
					t.setText(getString(R.string.Day) + ": " + s);
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
		new MenuInflater(this).inflate(R.menu.subject_menu, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.calc) {
			if(comprova()) {
				calcul();
				String s = Double.toString(average);
				Toast.makeText(this,getString(R.string.Your_grade) + " " + s, Toast.LENGTH_LONG).show();
				return(true);
			}
			else {
				Toast.makeText(this, getString(R.string.incorrect_weight), Toast.LENGTH_LONG).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, Exam.class);
		i.putExtra(DbAdapter.KEY_EXAMS_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT_EXAMEN);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(comprova()) calcul();
		else Toast.makeText(this, getString(R.string.incorrect_weight), Toast.LENGTH_LONG).show();
		fillData();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dba != null) {
			dba.close();
		}
	}
}

