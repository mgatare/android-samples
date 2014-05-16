package com.example.seekbarexample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CustomSeekBar extends Activity {
	SeekBar mybar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_seek_bar);
        mybar = (SeekBar) findViewById(R.id.seekBar1);
        
        mybar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				//add here your implementation
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				//add here your implementation
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				//add here your implementation
			}
		});
    }


}
