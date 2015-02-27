package com.mayur.droid.rgbchecker;
//package com.example.rgbchecker;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ImageView;
//
//
//public class HomeScreenActivity extends ActionBarActivity {
//
//	private ImageView imgView;
//	private int[][] rgbValues;
//	private  Bitmap bitmap;
//	
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_screen);
//        
//        imgView = (ImageView) findViewById(R.id.imgTest);
//       
//        Drawable d = imgView.getDrawable();
//        bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        d.draw(canvas);
//        
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);  
//        
//    	//define the array size
//    	rgbValues = new int[bitmap.getWidth()][bitmap.getHeight()];
//
//    	//Print in LogCat's console each of one the RGB and alpha values from the 4 corners of the image
//    	//Top Left
//    	Log.i("Pixel Value", "Top Left pixel: " + Integer.toHexString(bitmap.getPixel(0, 0)));
//    	//Top Right
//    	Log.i("Pixel Value", "Top Right pixel: " + Integer.toHexString(bitmap.getPixel(99, 0)));
//    	//Bottom Left
//    	Log.i("Pixel Value", "Bottom Left pixel: " + Integer.toHexString(bitmap.getPixel(0, 99)));
//    	//Bottom Right
//    	Log.i("Pixel Value", "Bottom Right pixel: " + Integer.toHexString(bitmap.getPixel(99, 99)));
//
//    	//get the ARGB value from each pixel of the image and store it into the array
//    	for(int i=0; i < bitmap.getWidth(); i++)
//    	{
//    		for(int j=0; j < bitmap.getHeight(); j++)
//    		{
//    			//This is a great opportunity to filter the ARGB values
//    			rgbValues[i][j] = bitmap.getPixel(i, j);
//    		}
//    	}
//    	
////        if(null!=bitmap) {
////        	getRGBValue(bitmap);	
////        } else {
////        	Toast.makeText(this, "Bitmap Empty", Toast.LENGTH_LONG).show();
////        }
//    }
//
//
//		imageView.setOnTouchListener(new ImageView.OnTouchListener() {
//			@SuppressLint("ClickableViewAccessibility")
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				int x = (int) event.getX();
//				int y = (int) event.getY();
//
//				ImageView imageView = ((ImageView) v);
//				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable())
//						.getBitmap();
//				int pixel = bitmap.getPixel(x, y);
//				int redValue = Color.red(pixel);
//				int blueValue = Color.blue(pixel);
//				int greenValue = Color.green(pixel);
//				if (pixel == Color.RED) {
//
//				}
//
//				Toast.makeText(
//						HomeScreenTwoActivity.this,
//						" :R: " + redValue + " G: " + blueValue + " B:"
//								+ greenValue, Toast.LENGTH_SHORT).show();
//
//				txtRGBValue.setText(" :RED: " + redValue + " GREEN: "
//						+ blueValue + " BLUE:" + greenValue);
//
//				return true;
//			}
//		});

////    private void getRGBValue(Bitmap bitmap) {
////        int redColors = 0;
////        int greenColors = 0;
////        int blueColors = 0;
////        int pixelCount = 0;
////
////        for (int y = 0; y < bitmap.getHeight(); y++)
////        {
////            for (int x = 0; x < bitmap.getWidth(); x++)
////            {
////                int c = bitmap.getPixel(x, y);
////                pixelCount++;
////                redColors += Color.red(c);
////                greenColors += Color.green(c);
////                blueColors += Color.blue(c);
////            }
////        }
////        
////        // calculate average of bitmap r,g,b values
////        int red = (redColors/pixelCount);
////        int green = (greenColors/pixelCount);
////        int blue = (blueColors/pixelCount);
////        
////        Toast.makeText(this, "RED:"+red+"::GREEN::"+green+"::BLUE::"+blue, Toast.LENGTH_LONG).show();
////       
////    }
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_screen, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
// }
