package com.example.seekbarexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CustomSeekBar extends Activity {
	private SeekBar seeekBar;
	private TextView textView1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_seek_bar);
        
        textView1 = (TextView) findViewById(R.id.textView1);
       
        
        seeekBar = (SeekBar) findViewById(R.id.seekBar1);
        
        seeekBar.setMax(50000);
        seeekBar.setProgress(1000);
        seeekBar.setSecondaryProgress(5000);
        textView1.setText(""+seeekBar.getKeyProgressIncrement());
        
        
        seeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.i("", "-----onStopTrackingTouch----");
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.i("", "-----onStartTracking----");
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				textView1.setText(""+progress);
			}
		});
    }
}
