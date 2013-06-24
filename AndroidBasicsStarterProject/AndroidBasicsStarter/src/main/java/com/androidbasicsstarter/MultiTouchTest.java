package com.androidbasicsstarter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MultiTouchTest extends Activity implements View.OnTouchListener {
    StringBuilder builder = new StringBuilder();
    TextView textView;
    float[] x = new float[10];
    float[] y = new float[10];
    boolean[] touched = new boolean[10];
    int[] id = new int[10];

    private void updateTextView() {
        builder.setLength(0);
        for(int i = 0; i < 10; i++) {
            builder.append(touched[i]);
            builder.append(", ");
            builder.append(id[i]);
            builder.append(", ");
            builder.append(x[i]);
            builder.append(", ");
            builder.append(y[i]);
            builder.append('\n');
        }
        textView.setText(builder.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("Touch and drag");
        textView.setOnTouchListener(this);
        setContentView(textView);
        for(int i = 0; i < 10; i++) {
            id[i] = -1;
        }
        updateTextView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();
        int pointerIndex = motionEvent.getActionIndex();
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < 10; i++) {
            if(i >= pointerCount) {
                touched[i] = false;
                id[i] = -1;
                continue;
            }
            if((motionEvent.getAction() != MotionEvent.ACTION_DOWN) && (i != pointerIndex)) {
                continue;
            }
            int pointerId = motionEvent.getPointerId(i);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) motionEvent.getX(pointerIndex);
                    y[i] = (int) motionEvent.getY(pointerIndex);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) motionEvent.getX(pointerIndex);
                    y[i] = (int) motionEvent.getY(pointerIndex);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    touched[i] = false;
                    id[i] = -1;
                    x[i] = (int) motionEvent.getX(pointerIndex);
                    y[i] = (int) motionEvent.getY(pointerIndex);
                    break;
                default:
                    break;
            }
        }
        updateTextView();
        return true;
    }
}
