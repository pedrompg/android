package pedroanddev.studyhelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudyHelperMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView t = (TextView)findViewById(R.id.mainTextView1);
		TextView t2 = (TextView)findViewById(R.id.mainTextView2);
		Typeface tf = Typeface.createFromAsset(getAssets(),"4.ttf");
		t.setTypeface(tf);
		t2.setTypeface(tf);
		Button subjects = (Button)findViewById(R.id.button1);
		Button toDo = (Button)findViewById(R.id.button2);
		subjects.setOnClickListener(onClickAssig);
		toDo.setOnClickListener(onClickNotes);
		
    }
    
    private View.OnClickListener onClickAssig = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), SubjectList.class);
	     	startActivity(i);
		}
	};
	
	private View.OnClickListener onClickNotes = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(v.getContext(), NotesList.class);
	     	startActivity(i);
		}
	};
	
}