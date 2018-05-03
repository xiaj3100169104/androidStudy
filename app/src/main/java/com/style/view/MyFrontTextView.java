package com.style.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.style.app.AppManager;

public class MyFrontTextView extends TextView {

	public MyFrontTextView(Context context) {
		super(context);
		setTypeface() ;
	}
	
	public MyFrontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface() ;
	}
	
	public MyFrontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTypeface() ;
	}
	
	private void setTypeface(){
		// 如果自定义typeface初始化失败，就用原生的typeface
		if(AppManager.TEXT_TYPE == null){
			setTypeface(getTypeface()) ;
		}else{
			setTypeface(AppManager.TEXT_TYPE) ;
		}
	}
}
