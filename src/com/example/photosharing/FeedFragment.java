package com.example.photosharing;
/*
 *  Fragment Code for Images Page (The Feed)
 *  The xml file concerning this java code is fragment_photos.xml
 *  Also, single_row.xml is used for maintaining a particular item of the list
 *  
 */

import java.util.ArrayList;
import java.util.List;

import com.example.photosharing.R.layout;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedFragment extends Fragment implements OnItemClickListener{
	 
	ListView list; //List used to fetch and show the images


	public FeedFragment(){}
	String[] image_titles;   //Titles of the images
	// Identities of images, currently images from Php return shows error hence hard coded certain images into the list
	int[] images={R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms};
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        Resources res=getResources();
        image_titles=res.getStringArray(R.array.titles);
        list=(ListView)rootView.findViewById(R.id.PhotosList);
        VivzAdapter adapter=new VivzAdapter(this.getActivity(),image_titles,images);
        list.setAdapter(adapter);
        list.setOnItemClickListener((OnItemClickListener) this);
        return rootView;
        
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View rooView, int position,
			long id) {
		// TODO Auto-generated method stub
		
			
		
		Toast toast = Toast.makeText( getActivity().getApplicationContext(), "Item " + (position + 1) + ": " ,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
		
		try {
		if(position==1){
			Intent intswitch=new Intent(getActivity().getApplicationContext(),Intent_from_home.class);
			startActivity(intswitch);
		}} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}	
		}
	
	
	}
// Adapter class for integrating image, title in a row of the list
class VivzAdapter extends ArrayAdapter<String>
{    
	Context context;
	int[] images;
	String[] titleArray;
	VivzAdapter(Context c,String[] titles,int imgs[])
	{
		super(c,R.layout.single_row,R.id.ImageTitle,titles);
		this.context=c;
		this.images=imgs;
		this.titleArray=titles;
		
	}
	class MyViewHolder
	{
		ImageView MyImage;
		TextView MyTitle;
		public MyViewHolder(View v) {
			// TODO Auto-generated constructor stub
			
			MyImage=(ImageView) v.findViewById(R.id.theImage);
			MyTitle=(TextView)v.findViewById(R.id.ImageTitle);
		}
		
		
	}
	private int lastPosition = -1;
	public View getView(int position,View convertView,ViewGroup parent){
		View row=convertView;
		MyViewHolder holder=null;
		if(row==null){
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		row=inflater.inflate(R.layout.single_row, parent,false);
		holder=new MyViewHolder(row);
		row.setTag(holder);
		
		}
		else
		{
			
			holder=(MyViewHolder) row.getTag();
		}
		holder.MyImage.setImageResource(images[position]);
		holder.MyTitle.setText(titleArray[position]);
		Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
	    row.startAnimation(animation);
	    lastPosition = position;
		return row;
		
	}
	
	
}

	