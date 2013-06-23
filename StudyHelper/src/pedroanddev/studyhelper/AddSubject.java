package pedroanddev.studyhelper;

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

		setContentView(R.layout.subject_form);
		
		dba = new DbAdapter(this);
		dba.open();
		
		name = (EditText)findViewById(R.id.name_subject_form);
		num = (EditText)findViewById(R.id.number_exams_subject_form);
		
		Button save=(Button)findViewById(R.id.save);
		save.setOnClickListener(onSave);
	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		public void onClick(View v) {
			String subject = name.getText().toString().toUpperCase();
			String  x = num.getText().toString().toUpperCase();
			if(!subject.equals("") && !x.equals("")) {
				int n = Integer.parseInt(num.getText().toString());
				double temp = (double) 1/n;
				BigDecimal weight = new BigDecimal(temp);
				weight = weight.setScale(4, BigDecimal.ROUND_HALF_UP);
				String day = "01-01-2011";
				String nom = getString(R.string.Exam);
				int i;
				try {
					dba.insertSubject(subject , num.getText().toString(), 0.0); 
				}
				catch (SQLiteConstraintException ex) {
				      Toast.makeText(AddSubject.this, getString(R.string.existing_subject)+ " " + subject, Toast.LENGTH_LONG).show();  
				}
				for	(i = 1; i <= n; i++) {
					dba.insertExam(nom +" "+ i, 0.0, weight.doubleValue(), day, subject);
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

