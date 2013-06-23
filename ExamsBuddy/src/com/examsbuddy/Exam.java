package com.examsbuddy;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Exam extends Activity {
	
	EditText nom = null;
	EditText nota = null;
	EditText pes = null;
	EditText dia = null;
	private Cursor cursor = null;
	private DbAdapter dba = null;
	private Long rowId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examen);
		dba = new DbAdapter(this);
		dba.open();
		nom = (EditText)findViewById(R.id.examen_nom);
		nota = (EditText)findViewById(R.id.examen_nota);
		pes = (EditText)findViewById(R.id.examen_pes);
		dia = (EditText)findViewById(R.id.examen_dia);
		Button save = (Button)findViewById(R.id.examen_save);
		save.setOnClickListener(onSave);
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			String s = null;
			double d = 0.0;
			rowId = extras.getLong(DbAdapter.KEY_EXAMENS_ROWID);
			cursor = dba.fetchExamen(rowId);
			s = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_NAME));
			nom.setText(s);
			d = cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_GRADE));
			nota.setText(d+"");
			d = cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_WEIGHT));
			d = d*100; 
			pes.setText(d+"");
			s = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMENS_DAY));
			dia.setText(s);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dba != null) {
			dba.close();
		}
	}
	
	public double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			String name = nom.getText().toString();
			String exp = pes.getText().toString();
			double weight = 0;
			if(exp.contains("*")) {
			    String op = "*";
				StringTokenizer parts = new StringTokenizer(exp,op);
				weight = (Double.parseDouble(parts.nextToken().toString()))/100;
				while(parts.hasMoreTokens()) {
					weight *= (Double.parseDouble(parts.nextToken().toString()))/100;
				}
			}
			else {
				weight = (Double.parseDouble(pes.getText().toString()))/100;
			}
			double grade = Double.parseDouble(nota.getText().toString());
			
			String day = dia.getText().toString();
			dba.updateExamen(rowId, name, grade, weight, day);
			dba.close();
			finish();
		}
	};
}
