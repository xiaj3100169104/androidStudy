package test.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.style.framework.R;
import com.style.view.CustomNotifyView;


public class TestNotifyViewActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifyview);
		CustomNotifyView customNotifyView = (CustomNotifyView) findViewById(R.id.custom);
		customNotifyView.setNotifyText("99");
		customNotifyView.setNotifyTextColor(Color.BLACK);

		final CustomNotifyView view2 = (CustomNotifyView) findViewById(R.id.view_change);
		view2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				view2.setNotifyCount(10);
			}
		});
	}
}
