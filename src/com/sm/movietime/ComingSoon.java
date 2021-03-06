package com.sm.movietime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComingSoon extends Activity {

	List<Integer> soon;
	List<String> soondetails;
	
	TextView details;
	Integer currentMovie;
	Button moreinfo;
	Intent btn_intnt;
    
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        currentMovie=0;
        
        soondetails = new ArrayList<String>();
        
        soondetails.add("Fast and Furious 6");
        soondetails.add("GI Joe 2");
        soondetails.add("Iron Man 3");
        soondetails.add("UFO");
        
        Field[] fields = R.drawable.class.getFields();
        soon = new ArrayList<Integer>();
        for (Field field : fields) {
            // Take only those with name starting with "fr"
            if (field.getName().startsWith("soon_")) {
                try {
					soon.add(field.getInt(null));
					//soondetails.add(String.valueOf(field.getInt(null)));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        details = (TextView)findViewById(R.id.details);
        details.setText(soondetails.get(0));
                
        final TextView nowshowing = (TextView)findViewById(R.id.home_now);
        nowshowing.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intnt = new Intent(v.getContext(), NowShowing.class);
        		startActivity(intnt);
			}
		});
        
        final TextView nextattract = (TextView)findViewById(R.id.home_next);
        nextattract.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intnt = new Intent(v.getContext(), NextAttraction.class);
        		startActivity(intnt);
				
			}
		});
        
        final TextView comingsoon = (TextView)findViewById(R.id.home_soon);
        comingsoon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intnt = new Intent(v.getContext(), ComingSoon.class);
        		startActivity(intnt);
				
			}
		});
        
        CoverFlow coverFlow = new CoverFlow(this);
        coverFlow.setBackgroundColor(Color.TRANSPARENT);
        coverFlow.setAdapter(new ImageAdapter(this));
        coverFlow.setGravity(Gravity.CENTER);
        
        ImageAdapter coverImageAdapter =  new ImageAdapter(this);
        coverImageAdapter.createReflectedImages();
        
        coverFlow.setAdapter(coverImageAdapter);
        
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
        	coverFlow.setSpacing(-50);
        	coverFlow.setMaxRotationAngle(100);
        } else {
	        coverFlow.setSpacing(0);
	        coverFlow.setSelection(0, true); //Sets the default selected Gallery Item
        }
        
        coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				details.setText(soondetails.get(arg2));
				currentMovie = arg2;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				btn_intnt = new Intent(getBaseContext(), MovieDetails.class);
				btn_intnt.putExtra("MovieTitle", soondetails.get(currentMovie));
				btn_intnt.putExtra("MoviePoster", soon.get(currentMovie));
				btn_intnt.putExtra("status", "soon");
				startActivity(btn_intnt);
			}
		});
        
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        RelativeLayout r = (RelativeLayout)findViewById(R.id.coming_soon_layout);
        r.addView(coverFlow, 2, lp);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_coming_soon, menu);
        return true;
    }
    
    public class ImageAdapter extends BaseAdapter {
		 int mGalleryItemBackground;
	     private Context mContext;
	
	     private ImageView[] mImages;
	     
	     public ImageAdapter(Context c) {
	      mContext = c;
	      mImages = new ImageView[soon.size()];
	     }
	     
	     public boolean createReflectedImages() {
	          //The gap we want between the reflection and the original image
	    	 final int reflectionGap = 4;
	          
	          
	    	 int index = 0;
	         for (int imageId : soon) {
	        	 Bitmap originalImage = BitmapFactory.decodeResource(getResources(), imageId);
	        	 int width = originalImage.getWidth();
	        	 int height = originalImage.getHeight();
	           
	     
		           //This will not scale but will flip on the Y axis
		           Matrix matrix = new Matrix();
		           matrix.preScale(1, -1);
		           
		           //Create a Bitmap with the flip matrix applied to it.
		           //We only want the bottom half of the image
		           Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
		           
		               
		           //Create a new bitmap with same width but taller to fit reflection
		           Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);
		         
		          //Create a new Canvas with the bitmap that's big enough for
		          //the image plus gap plus reflection
		          Canvas canvas = new Canvas(bitmapWithReflection);
		          //Draw in the original image
		          canvas.drawBitmap(originalImage, 0, 0, null);
		          //Draw in the gap
		          Paint deafaultPaint = new Paint();
		          canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		          //Draw in the reflection
		          canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
		          
		          //Create a shader that is a linear gradient that covers the reflection
		          Paint paint = new Paint(); 
		          LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
		            bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
		            TileMode.CLAMP); 
		          //Set the paint to use this shader (linear gradient)
		          paint.setShader(shader); 
		          //Set the Transfer mode to be porter duff and destination in
		          paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
		          //Draw a rectangle using the paint with our linear gradient
		          canvas.drawRect(0, height, width, 
		            bitmapWithReflection.getHeight() + reflectionGap, paint); 
		          
		          ImageView imageView = new ImageView(mContext);
		          imageView.setImageBitmap(bitmapWithReflection);
		          imageView.setLayoutParams(new CoverFlow.LayoutParams(130, 130));
		          imageView.setScaleType(ScaleType.MATRIX);
		          mImages[index++] = imageView;
	          }//end of for
	       return true;
	     }
	
	     public int getCount() {
	         return soon.size();
	     }
	
	     public Object getItem(int position) {
	         return position;
	     }
	
	     public long getItemId(int position) {
	         return position;
	     }
	
	     public View getView(int position, View convertView, ViewGroup parent) {
	
	      //Use this code if you want to load from resources
	         ImageView i = new ImageView(mContext);
	         i.setImageResource(soon.get(position));
	         Display display = getWindowManager().getDefaultDisplay();
	         int w = display.getWidth();
	         
	         if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) 
	        		 == Configuration.SCREENLAYOUT_SIZE_SMALL) {     
	        	 i.setLayoutParams(new CoverFlow.LayoutParams(130,130));
	         }
	         else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) 
	        		 == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
	        	 i.setLayoutParams(new CoverFlow.LayoutParams(500,500));
	         }
	         else if (w>600){
	        	 i.setLayoutParams(new CoverFlow.LayoutParams(700, 700));
	         }
	         else {
	        	 i.setLayoutParams(new CoverFlow.LayoutParams(700,500));
	         }
	         
	         i.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
	         
	         //Make sure we set anti-aliasing otherwise we get jaggies
	         BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
	         drawable.setAntiAlias(true);
	         return i;
	      
	      //return mImages[position];
	     }
	   /** Returns the size (0.0f to 1.0f) of the views 
	      * depending on the 'offset' to the center. */ 
	      public float getScale(boolean focused, int offset) { 
	        /* Formula: 1 / (2 ^ offset) */ 
	          return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
	      } 
	
	 }
}
