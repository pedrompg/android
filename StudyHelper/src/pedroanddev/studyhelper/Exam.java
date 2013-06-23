package pedroanddev.studyhelper;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		setContentView(R.layout.exam);
		dba = new DbAdapter(this);
		dba.open();
		nom = (EditText)findViewById(R.id.exam_name);
		nota = (EditText)findViewById(R.id.exam_grade);
		pes = (EditText)findViewById(R.id.exam_weight);
		dia = (EditText)findViewById(R.id.exam_day);
		Button save = (Button)findViewById(R.id.examen_save);
		save.setOnClickListener(onSave);
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			String s = null;
			double d = 0.0;
			rowId = extras.getLong(DbAdapter.KEY_EXAMS_ROWID);
			cursor = dba.fetchExam(rowId);
			s = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_NAME));
			nom.setText(s);
			d = cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_GRADE));
			nota.setText(d+"");
			BigDecimal w = new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_WEIGHT)));
			BigDecimal hundred = new BigDecimal(100);
			w = w.multiply(hundred);
			w = w.setScale(2, BigDecimal.ROUND_HALF_UP);
			pes.setText(w+"");
			s = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.KEY_EXAMS_DAY));
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
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			boolean ok_name = false;
			boolean ok_weight = false;
			boolean ok_grade = false;
			boolean ok_day = false;
			String name = nom.getText().toString();
			ok_name = true;
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
			if(weight <= 1.0 && weight >= 0.0) ok_weight = true;
			double grade = Double.parseDouble(nota.getText().toString());
			if(grade >= 0.0 && grade <= 10.0) ok_grade = true;
			String day = dia.getText().toString();
			if(day.charAt(2) == '-' || day.charAt(5) == '-') ok_day = true;
			if(ok_name && ok_weight && ok_grade && ok_day){
				dba.updateExam(rowId, name, grade, weight, day);
				dba.close();
				finish();
			}
			else if(!ok_weight) {
				Toast.makeText(Exam.this, getString(R.string.incorrect_weight_range), Toast.LENGTH_LONG).show();
			}
			else if(!ok_grade) {
				Toast.makeText(Exam.this, getString(R.string.incorrect_grade_range), Toast.LENGTH_LONG).show();
			}
			else if(!ok_day) {
				Toast.makeText(Exam.this, getString(R.string.incorrect_day), Toast.LENGTH_LONG).show();
			}
		}
	};
}
