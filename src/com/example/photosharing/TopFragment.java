package com.example.photosharing;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.example.photosharing.R.layout;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

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

public class TopFragment extends Fragment implements OnItemClickListener{
	 
	ListView Top10list;


	public TopFragment(){}
	String []memetitles;
	int []images={R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms,R.drawable.almond_blossoms};
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        Resources res=getResources();
        memetitles=res.getStringArray(R.array.titles);
        Top10list=(ListView)rootView.findViewById(R.id.PhotosList);
        VivzAdapter adapter=new VivzAdapter(this.getActivity(),memetitles,images);
        Top10list.setAdapter(adapter);
        Top10list.setOnItemClickListener((OnItemClickListener) this);
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