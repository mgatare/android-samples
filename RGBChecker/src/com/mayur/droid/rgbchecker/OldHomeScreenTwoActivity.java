package com.mayur.droid.rgbchecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class HomeScreenTwoActivity.
 */
public class OldHomeScreenTwoActivity extends ActionBarActivity implements
		OnClickListener {

	/** The image view. */
	private ImageView imageView;

	/** The txt rgb value. */
	private TextView txtRGBValue;

	/** The txt rgb color. */
	private TextView txtRGBColor;

	/** The btn get image. */
	private Button btnGetImage;

	/** The rel pixel container. */
	private RelativeLayout relPixelContainer;

	/** The btn pixel highlighter. */
	private Button btnPixelHighlighter;

	private Uri mOutputFileUri;

	/** The get image req code. */
	private int GET_IMAGE_REQ_CODE = 2;

	private int GET_IMAGE_REQ_CODE_KITKAT = 3;

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

		imageView = (ImageView) findViewById(R.id.imgPhotoHolder);
		txtRGBValue = (TextView) findViewById(R.id.txtRGBValue);
		txtRGBColor = (TextView) findViewById(R.id.txtRGBColor);
		btnGetImage = (Button) findViewById(R.id.btnGetImageCamera);
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
				txtRGBColor.setText("ARGB COLOR IS: " + "#"
						+ Integer.toHexString(touchedRGB));

				if (event.getAction() == MotionEvent.ACTION_UP) {

					btnPixelHighlighter.setVisibility(View.INVISIBLE);

				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {

					btnPixelHighlighter.setVisibility(View.VISIBLE);
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
					txtRGBColor.setTextColor(touchedRGB);

				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					btnPixelHighlighter.setBackgroundColor(touchedRGB);
					txtRGBColor.setTextColor(touchedRGB);
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

			// if (Build.VERSION.SDK_INT < 19) {
			// Intent intent = new Intent();
			// intent.setType("image/jpeg");
			// intent.setAction(Intent.ACTION_GET_CONTENT);
			// startActivityForResult(
			// Intent.createChooser(intent, "Select Image"),
			// GET_IMAGE_REQ_CODE);
			// } else {
			// Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			// intent.addCategory(Intent.CATEGORY_OPENABLE);
			// intent.setType("image/jpeg");
			// startActivityForResult(intent, GET_IMAGE_REQ_CODE_KITKAT);
			// }

			openPhotoChooser();

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
					imageView.setImageBitmap(getScaledBitmap(imageFilePath,
							300, 500));
				else
					Toast.makeText(OldHomeScreenTwoActivity.this,
							"Image not loaded properly. Try agin",
							Toast.LENGTH_LONG).show();
			} else if (requestCode == REQ_CAMERA) {
				Uri selectedImageUri = null;

				Log.v("TAG", "#onActivityResult req: " + requestCode);
				if (requestCode == REQ_CAMERA) {
					final boolean isCamera;
					if (data == null) {
						isCamera = true;
					} else {
						final String action = data.getAction();
						if (action == null) {
							isCamera = false;
						} else {
							isCamera = action
									.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						}
					}

					if (isCamera) {
						selectedImageUri = mOutputFileUri;
					} else {
						selectedImageUri = data == null ? null : data.getData();
					}

					// Log.v( TAG, "#onActivityResult selectedImageUri: " +
					// selectedImageUri );

					if (selectedImageUri != null) {

						String imageFilePath = selectedImageUri.getPath();
						imageView.setImageBitmap(getScaledBitmap(
								imageFilePath, 300, 500));

					} else {
						// TODO: show no image data received message
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

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

	private void openPhotoChooser() {

		// Determine Uri of camera image to save
		File root = getStorageDirectory(this, null);
		root.mkdirs();
		String fname = Long.toString(new Date().getTime());
		File sdImageMainDirectory = new File(root, fname);
		mOutputFileUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		List<Intent> cameraIntents = new ArrayList<Intent>();
		Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		PackageManager packageManager = getPackageManager();
		List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			String packageName = res.activityInfo.packageName;
			Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Select Source");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));
		startActivityForResult(chooserIntent, REQ_CAMERA);
	}

	public File getStorageDirectory(Context ctx, String dirName) {

		if (TextUtils.isEmpty(dirName)) {
			dirName = "atemp";
		}

		File f = null;

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			f = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
							+ "/" + dirName);
		} else {
			// media is removed, unmounted etc
			// Store image in
			// /data/data/<package-name>/cache/atemp/photograph.jpeg
			f = new File(ctx.getCacheDir() + "/" + dirName);
		}

		if (!f.exists()) {
			f.mkdirs();
		}

		return f;
	}
}
