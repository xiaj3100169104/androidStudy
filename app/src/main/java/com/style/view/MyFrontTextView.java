package com.style.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MyFrontTextView extends AppCompatTextView {

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
		Typeface TEXT_TYPE = Typeface.createFromAsset(getContext().getAssets(), "fronts/black_simplified.TTF");
		// 如果自定义typeface初始化失败，就用原生的typeface
		if(TEXT_TYPE == null){
			setTypeface(getTypeface()) ;
		}else{
			setTypeface(TEXT_TYPE) ;
		}
	}
}
