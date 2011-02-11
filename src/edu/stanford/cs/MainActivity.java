package edu.stanford.cs;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author Naren
 * The activity with the search results
 * 
 */

public class MainActivity extends Activity {
	protected Photo[] photoResults;
	protected float screenDensity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        photoResults = Server.search("india");
        
        //Get the galleries set up - Guess I will need 4 of them.
        int galleryIds[] = {R.id.Gallery01, R.id.Gallery02, R.id.Gallery03, R.id.Gallery04};
        int startPos = 0;
        
        for(int gIds : galleryIds){
        	Gallery g = (Gallery) findViewById(gIds);
        	g.setAdapter(new ImageAdapter(this, startPos));

        	g.setOnItemClickListener(new OnItemClickListener() {
        		public void onItemClick(AdapterView parent, View v, int position, long id) {
        			//Toast.makeText(MainActivity.this, "1: " + position, Toast.LENGTH_SHORT).show();
        		}
        	});
        	
        	startPos += Constants.MAX_PHOTOS/galleryIds.length;
        	
        }
        
        //Set the density value - used later
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
    }
    
    //From android example
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        private int mStartPos;

        public ImageAdapter(Context c, int startPos) {
            mContext = c;
            TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery1);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.Gallery1_android_galleryItemBackground, 0);
            a.recycle();
            mStartPos = startPos;
        }

        public int getCount() {
        	return photoResults.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageDrawable(loadImageFromUrl(photoResults[position + mStartPos].getThumbUrl()));
            Log.d("Main ", "IMG Url : " + position + " : " + photoResults[position].getThumbUrl()
            	+ " screenDensity : " + screenDensity);

            //Support for different screen resolutions
            int imgSize = (int)(120*screenDensity); 
            i.setLayoutParams(new Gallery.LayoutParams(imgSize, imgSize));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setBackgroundResource(mGalleryItemBackground);
            return i;
        }
    }

    public Drawable loadImageFromUrl(String thumbUrl) {
    	try
    	{
    		InputStream is = (InputStream) new URL(thumbUrl).getContent();
    		Drawable d = Drawable.createFromStream(is, "src name");
    		return d;
    	}catch (Exception e) {
    		return null;
    	}
    }

}