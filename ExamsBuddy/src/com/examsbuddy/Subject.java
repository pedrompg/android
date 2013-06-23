package com.examsbuddy;

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
	private String assig = null;
	private Bundle extras = null;
	private double mitja = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		int[] colors = {0, 0xFFFF0000, 0}; // red for the example
		this.getListView().setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		this.getListView().setDividerHeight(5);
		dba = new DbAdapter(this);
		dba.open();
		extras = getIntent().getExtras();
		if(extras != null) {
			assig = extras.getString("assig");
		}		
		fillData();
		getListView().setCacheColorHint(0);
	}	
	
	private double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	
	private boolean comprova() {
		cursor = dba.fetchAllExamens(assig);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		double pes = 0;
		do {
			double p = cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_WEIGHT));
			pes += p;
			
		}	while(cursor.moveToNext());
		double factor = 1e2; // = 1 * 10^5 = 100000.
		pes = Math.round(pes*factor)/factor;	
		return (pes == 1);
	}
	
	private void calcul() {
		cursor = dba.fetchAllExamens(assig);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		mitja = 0;
		do {
			double n = cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_GRADE));
			n = round(n,  3, BigDecimal.ROUND_HALF_UP);
			double p = cursor.getFloat(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_WEIGHT));
			p = round(p,  3, BigDecimal.ROUND_HALF_UP);
			mitja += n*p;
			mitja = round(mitja,  3, BigDecimal.ROUND_HALF_UP);
		}	while(cursor.moveToNext());
		dba.updateAssigAvg(assig, mitja);
	}
	
	
	private void fillData() {
		cursor = dba.fetchAllExamens(assig);
		startManagingCursor(cursor);
		cursor.moveToFirst();
		String[] from = new String[] { DbAdapter.KEY_EXAMENS_NAME, DbAdapter.KEY_EXAMENS_GRADE, DbAdapter.KEY_EXAMENS_WEIGHT, DbAdapter.KEY_EXAMENS_DAY };
		int[] to = new int[] { R.id.nom_examen, R.id.nota_examen , R.id.pes_examen, R.id.dia_examen };

		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.row, cursor, from , to);
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
				if(columnIndex == 2) {
					String s = cursor.getString(columnIndex);
					TextView t = (TextView) view;
					t.setText(getString(R.string.Grade) + ": " +s);
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
					t.setText("Dia : "+s);
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
		new MenuInflater(this).inflate(R.menu.assig_option, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.calc) {
			if(comprova()) {
				calcul();
				String s = Double.toString(mitja);
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
		i.putExtra(DbAdapter.KEY_EXAMENS_ROWID, id);
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
