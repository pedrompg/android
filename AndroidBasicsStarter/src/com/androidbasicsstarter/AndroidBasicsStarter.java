package com.androidbasicsstarter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AndroidBasicsStarter extends ListActivity {

    private String tests[] =  {"LifeCycle", "SingleTouch", "MultiTouch", "Key", "Accelerometer", "Assets",
            "ExternalStorage", "SoundPool", "MediaPlayer", "FullScreen", "RenderView", "Shape",
            "Bitmap", "Font", "SurfaceView"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                this.tests));
    }

    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        String testName = this.tests[position];
        try {
            Class<?> c = Class.forName("com.androidbasicsstarter." + testName);
            Intent intent = new Intent(this,c);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
