package com.mayur.droid.rgbchecker;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeScreenTwoActivity.
 */
public class HomeScreenTwoActivity extends ActionBarActivity implements
		OnClickListener {

	/** The image view. */
	private ImageView ivPhotoHolder;

	/** The txt rgb value. */
	private TextView txtRGBValue;

	/** The txt rgb color. */
	private TextView txtRGBColor;

	/** The btn get image. */
	private Button btnGetImageCamera;

	/** The btn get image gallery. */
	private Button btnGetImageGallery;

	/** The btn pixel highlighter. */
	private Button btnPixelHighlighter;

	/** The str current photo path. */
	private String strCurrentPhotoPath;

	/** The get image req code. */
	private int GET_IMAGE_REQ_CODE = 2;

	/** The get image req code kitkat. */
	private int GET_IMAGE_REQ_CODE_KITKAT = 3;

	/** The req camera. */
	private int REQ_CAMERA = 4;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen_new);

		ivPhotoHolder = (ImageView) findViewById(R.id.imgPhotoHolder);
		txtRGBValue = (TextView) findViewById(R.id.txtRGBValue);
		txtRGBColor = (TextView) findViewById(R.id.txtRGBColor);
		btnGetImageGallery = (Button) findViewById(R.id.btnGetImageGallery);
		btnGetImageCamera = (Button) findViewById(R.id.btnGetImageCamera);
		btnPixelHighlighter = (Button) findViewById(R.id.btnPixelHighlighter);

		btnGetImageGallery.setOnClickListener(this);
		btnGetImageCamera.setOnClickListener(this);

		ivPhotoHolder.setOnTouchListener(new OnTouchListener() {
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

				Drawable imgDrawable = ((ImageView) view).getDrawable();
				Bitmap bitmap = ((BitmapDrawable) imgDrawable).getBitmap();

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

				txtRGBValue.setText("RED: " + redValue + " GREEN: "
						+ greenValue + " BLUE:" + blueValue);
				// txtRGBColor.setText("RGB COLOR IS: " + "#" +
				// Integer.toHexString(touchedRGB));

				if (event.getAction() == MotionEvent.ACTION_UP) {

					btnPixelHighlighter.setVisibility(View.INVISIBLE);

				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {

					btnPixelHighlighter.setVisibility(View.VISIBLE);
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
					// txtRGBColor.setTextColor(touchedRGB);
					txtRGBValue.setTextColor(touchedRGB);

				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
					// txtRGBColor.setTextColor(touchedRGB);
					txtRGBValue.setTextColor(touchedRGB);
				}

				return true;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGetImageCamera:

			// Intent takePictureIntent = new Intent(
			// MediaStore.ACTION_IMAGE_CAPTURE);
			// // Ensure that there's a camera activity to handle the intent
			// if (takePictureIntent.resolveActivity(getPackageManager()) !=
			// null) {
			// // Create the File where the photo should go
			// File photoFile = null;
			// try {
			// photoFile = createImageFile();
			// } catch (IOException ex) {
			// // Error occurred while creating the File
			// Log.e("MAYUR", ">>>" + ex.getMessage());
			// }
			// // Continue only if the File was successfully created
			// if (photoFile != null) {
			// takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
			// startActivityForResult(takePictureIntent, REQ_CAMERA);
			// }
			// }

			Toast.makeText(HomeScreenTwoActivity.this,
					"Functionality not implemented yet.", Toast.LENGTH_LONG)
					.show();

			break;

		case R.id.btnGetImageGallery:

			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent();
				intent.setType("image/jpeg");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Image"),
						GET_IMAGE_REQ_CODE);
			} else {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				startActivityForResult(intent, GET_IMAGE_REQ_CODE_KITKAT);
			}

			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			if ((requestCode == GET_IMAGE_REQ_CODE || requestCode == GET_IMAGE_REQ_CODE_KITKAT)
					&& data != null && data.getData() != null) {

				Uri uri = null;
				String imageFilePath = null;

				String[] projection = { MediaStore.Images.Media.DATA };

				if (requestCode == GET_IMAGE_REQ_CODE) {

					uri = data.getData();

					Cursor cursor = getContentResolver().query(uri, projection,
							null, null, null);
					int col = cursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					if (col >= 0 && cursor.moveToFirst())
						imageFilePath = cursor.getString(col);

					cursor.close();

				} else if (requestCode == GET_IMAGE_REQ_CODE_KITKAT) {

					uri = data.getData();

					// get the id of the image selected by the user
					String wholeID = DocumentsContract.getDocumentId(data
							.getData());
					String id = wholeID.split(":")[1];

					String whereClause = MediaStore.Images.Media._ID + "=?";
					Cursor cursor = getContentResolver().query(getUri(),
							projection, whereClause, new String[] { id }, null);
					if (cursor != null) {
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						if (cursor.moveToFirst()) {
							imageFilePath = cursor.getString(column_index);
						}

						cursor.close();
					} else {
						imageFilePath = uri.getPath();
					}
				}

				Log.d("MAYUR", "imageFilePath///" + imageFilePath);

				if (null != imageFilePath)
					ivPhotoHolder.setImageBitmap(getScaledBitmap(imageFilePath,
							300, 500));
				else
					Toast.makeText(HomeScreenTwoActivity.this,
							"Image not loaded properly. Try agin",
							Toast.LENGTH_LONG).show();

			} else if (requestCode == REQ_CAMERA) {

				// Bundle extras = data.getExtras();
				// Bitmap imageBitmap = (Bitmap) extras.get("data");
				// ivPhotoHolder.setImageBitmap(imageBitmap);

				setPic(ivPhotoHolder);
				galleryAddPic();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Gets the uri.
	 * 
	 * @return the uri
	 */
	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	/**
	 * Gets the scaled bitmap.
	 * 
	 * @param picturePath
	 *            the picture path
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return the scaled bitmap
	 */
	private Bitmap getScaledBitmap(String picturePath, int width, int height) {
		BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		sizeOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picturePath, sizeOptions);

		int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

		sizeOptions.inJustDecodeBounds = false;
		sizeOptions.inSampleSize = inSampleSize;

		return BitmapFactory.decodeFile(picturePath, sizeOptions);
	}

	/**
	 * Calculate in sample size.
	 * 
	 * @param options
	 *            the options
	 * @param reqWidth
	 *            the req width
	 * @param reqHeight
	 *            the req height
	 * @return the int
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * Sets the pic.
	 * 
	 * @param mImageView
	 *            the new pic
	 */
	private void setPic(ImageView mImageView) {
		// Get the dimensions of the View
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(strCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		// bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory
				.decodeFile(strCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
	}

	/**
	 * Creates the image file.
	 * 
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File createImageFile() throws IOException {
		// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
		// Date());

		String extr = Environment.getExternalStorageDirectory().toString();
		File mFolder = new File(extr + "/rgbchecker_image/");

		String s = "rgbimage.jpg";
		File fileImage = new File(mFolder.getAbsolutePath(), s);
		if (!fileImage.exists()) {
			fileImage.mkdir();
		}

		// Save a file: path for use with ACTION_VIEW intents
		strCurrentPhotoPath = "file:" + fileImage.getAbsolutePath();
		Log.d("MAYUR", "ABSOLUTE PATH>>>" + strCurrentPhotoPath);
		return fileImage;
	}

	/**
	 * Gallery add pic.
	 * 
	 * @param strCurrentPhotoPath2
	 */
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(strCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
}
