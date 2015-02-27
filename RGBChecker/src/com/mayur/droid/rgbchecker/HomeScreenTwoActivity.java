package com.mayur.droid.rgbchecker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeScreenTwoActivity extends ActionBarActivity implements
		OnClickListener {

	private ImageView imageView;
	private TextView txtRGBValue;
	private TextView txtRGBColor;
	private Button btnGetImage;
	private RelativeLayout relPixelContainer;
	private Button btnPixelHighlighter;

	private int GET_IMAGE_REQ_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		imageView = (ImageView) findViewById(R.id.imgTest);
		txtRGBValue = (TextView) findViewById(R.id.txtRGBValue);
		txtRGBColor = (TextView) findViewById(R.id.txtRGBColor);
		btnGetImage = (Button) findViewById(R.id.btnGetImage);
		btnPixelHighlighter = (Button) findViewById(R.id.btnPixelHighlighter);
		relPixelContainer = (RelativeLayout) findViewById(R.id.relPixelContainer);

		btnGetImage.setOnClickListener(this);

		imageView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent event) {

				float eventX = event.getX();
				float eventY = event.getY();
				float[] eventXY = new float[] { eventX, eventY };

				Matrix invertMatrix = new Matrix();
				((ImageView) view).getImageMatrix().invert(invertMatrix);

				invertMatrix.mapPoints(eventXY);
				int x = Integer.valueOf((int) eventXY[0]);
				int y = Integer.valueOf((int) eventXY[1]);

				// System.out.println("touched position: "+
				// String.valueOf(eventX) + " / "+ String.valueOf(eventY));
				// System.out.println("touched position: " + String.valueOf(x)+
				// " / " + String.valueOf(y));

				// txtRGBValue.setText("Image Touched at posotion: " +
				// String.valueOf(x)+ " / " + String.valueOf(y));

				Drawable imgDrawable = ((ImageView) view).getDrawable();
				Bitmap bitmap = ((BitmapDrawable) imgDrawable).getBitmap();

				// System.out.println("drawable size: "+
				// String.valueOf(bitmap.getWidth()) + " / "+
				// String.valueOf(bitmap.getHeight()));

				// Limit x, y range within bitmap
				if (x < 0) {
					x = 0;
				} else if (x > bitmap.getWidth() - 1) {
					x = bitmap.getWidth() - 1;
				}

				if (y < 0) {
					y = 0;
				} else if (y > bitmap.getHeight() - 1) {
					y = bitmap.getHeight() - 1;
				}

				int touchedRGB = bitmap.getPixel(x, y);

				int pixel = bitmap.getPixel(x, y);
				int redValue = Color.red(pixel);
				int blueValue = Color.blue(pixel);
				int greenValue = Color.green(pixel);

				txtRGBValue.setText("RED: " + redValue + " GREEN: "+ greenValue + " BLUE:" + blueValue);
				txtRGBColor.setText("ARGB COLOR IS: " + "#"+ Integer.toHexString(touchedRGB));

				// System.out.println("touched color: " + "#"+
				// Integer.toHexString(touchedRGB));

				// Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888 ,true);
				// bmp.setPixel(x, y, 0xFF000000);
				// imageView.setImageBitmap(bmp);

				
				if (event.getAction() == MotionEvent.ACTION_UP) {

					relPixelContainer.setVisibility(View.INVISIBLE);

				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {

					relPixelContainer.setVisibility(View.VISIBLE);
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
					
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
				}

				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGetImage:

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					GET_IMAGE_REQ_CODE);

			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GET_IMAGE_REQ_CODE && data != null
				&& data.getData() != null) {
			Uri uri = data.getData();

			// User had pick an image.
			Cursor cursor = getContentResolver()
					.query(uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();

			// Link to the image
			final String imageFilePath = cursor.getString(0);
			cursor.close();
			Log.d("MAYUR", "imageFilePath///" + imageFilePath);

			imageView.setImageBitmap(null);
			imageView.setImageURI(uri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
