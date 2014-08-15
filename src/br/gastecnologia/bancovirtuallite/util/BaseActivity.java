package br.gastecnologia.bancovirtuallite.util;

import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.repository.ConfigurationUtil;
import android.app.Activity;
import android.view.View;

public class BaseActivity extends Activity 
{
	public BaseActivity()
	{
		ThemeEventHandler.addListener(new ThemeEventListener() {
			
			@Override
			public void OnThemeChangedEvent(int theme) 
			{
				BaseActivity.this.setStyle(theme);
			}
		});
	}
	
	@Override
	public void onContentChanged() 
	{
		// TODO Auto-generated method stub
		super.onContentChanged();
		setStyle(ConfigurationUtil.getTheme(this));
	}
	
	public void setStyle(int theme)
	{
		View mainView = BaseActivity.this.findViewById(R.id.mainLayout);
		if(mainView != null)
		{
			if(theme == 0)
			{
				mainView.setBackgroundResource(R.drawable.bg);
			}
			if(theme == 1)
			{
				mainView.setBackgroundResource(R.drawable.bg_3);
			}
			if(theme == 2)
			{
				mainView.setBackgroundResource(R.drawable.bg_4);
			}
			if(theme == 3)
			{
				mainView.setBackgroundResource(R.drawable.bg_5);
			}
			if(theme == 4)
			{
				mainView.setBackgroundResource(R.drawable.bg_6);
			}
			if(theme == 5)
			{
				mainView.setBackgroundResource(R.drawable.bg_2);
			}
		}
	}
}
