package com.examsbuddy;

import java.math.BigDecimal;
import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends Activity {
	
	private DbAdapter dba = null;
	private EditText name = null;
	private EditText num = null; 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.assig_form);
		
		dba = new DbAdapter(this);
		dba.open();
		
		name = (EditText)findViewById(R.id.nom_assig_form);
		num = (EditText)findViewById(R.id.num_examens_assig_form);
		
		Button save=(Button)findViewById(R.id.save);
		save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		public double round(double unrounded, int precision, int roundingMode)
		{
		    BigDecimal bd = new BigDecimal(unrounded);
		    BigDecimal rounded = bd.setScale(precision, roundingMode);
		    return rounded.doubleValue();
		}
		
		public void onClick(View v) {
			String assignatura = name.getText().toString().toUpperCase();
			String  x = num.getText().toString().toUpperCase();
			if(!assignatura.equals("") && !x.equals("")) {
				int n = Integer.parseInt(num.getText().toString());
				double grade = 0.0;
				double weight = (double) 1/n;
				weight = round(weight, 4, BigDecimal.ROUND_HALF_UP); 
				String day = "01-01-2011";
				String nom = getString(R.string.Exam);
				int i;
				try {
					dba.insertAssig(assignatura , num.getText().toString(), 0.0); 
				}
				catch (SQLiteConstraintException ex) {
				      Toast.makeText(AddSubject.this, getString(R.string.existing_subject)+ " " + assignatura, Toast.LENGTH_LONG).show();  
				}
				for	(i = 1; i <= n; i++) {
					dba.insertExamen(nom +" "+ i, grade, weight, day, assignatura);
				}
				dba.close();
				finish();
			}
			else {
				Toast.makeText(AddSubject.this, getString(R.string.incomplete_subject_form), Toast.LENGTH_LONG).show();
			}
		}
	};
}
