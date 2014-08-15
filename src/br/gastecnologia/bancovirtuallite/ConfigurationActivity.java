package br.gastecnologia.bancovirtuallite;

import java.util.Locale;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.repository.ConfigurationUtil;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;

public class ConfigurationActivity extends BaseActivity 
{
	TextView txtLogo;
//	Spinner spToken;
	EditText txtTaid, txtGetSeed, txtRegister, txtValidate, txtTasp;
	Spinner spTheme;
	LinearLayout mainLayout;
	String selectedImagePath;
	ImageView img_br, img_es, img_ru;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.configuration);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		spToken = (Spinner) this.findViewById(R.id.spinner1);
		mainLayout = (LinearLayout) this.findViewById(R.id.mainLayout);
		spTheme = (Spinner) this.findViewById(R.id.spinner2);
//		txtLogo = (TextView) this.findViewById(R.id.textView3);
		txtTaid = (EditText) this.findViewById(R.id.editText1);
		txtGetSeed = (EditText) this.findViewById(R.id.txtGetSeed);
		txtRegister = (EditText) this.findViewById(R.id.txtRegister);
		txtValidate = (EditText) this.findViewById(R.id.txtValidate);
		txtTasp = (EditText) this.findViewById(R.id.txtTasp);
		img_br = (ImageView)this.findViewById(R.id.imageView1);
		img_es = (ImageView) this.findViewById(R.id.imageView2);
		img_ru = (ImageView) this.findViewById(R.id.imageView3);
		img_br.setOnClickListener(listener);
		img_es.setOnClickListener(listener);
		img_ru.setOnClickListener(listener);
		img_br.requestFocus();
		
		fillValues();
	}
	
	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Locale locale;
				String message = "";
				switch (v.getId()) {
				case R.id.imageView1:
					message = "pt";
					locale = new Locale(message);
					break;
				case R.id.imageView2:
					message = "es";
					locale = new Locale(message);
					break;
				case R.id.imageView3:
					message = "en";
					locale = new Locale(message);
					break;
				default:
					message = "en";
					locale = new Locale(message);
					break;
				}
				BancoVirtualActivity.changeLocale(locale, getApplicationContext(), getBaseContext());
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void fillValues()
	{
		
//		TokenProvider provider = ConfigurationUtil.getTokenProvider(this);
//		int selection = 0;
//		if(provider == TokenProvider.AUTHENTICUS)
//		{
//			selection = 0;
//		}
//		else
//		{
//			selection = 1;
//		}
//		spToken.setSelection(selection);
		
//		String persistedLogo = ConfigurationUtil.getProtectorLogoString(this);
//		selectedImagePath = persistedLogo;
//		txtLogo.setText(persistedLogo);
		
	
		int Theme = ConfigurationUtil.getTheme(this);
		spTheme.setSelection(Theme);
		
		String taidNumber = ConfigurationUtil.getTaidNumber(this);
		txtTaid.setText(taidNumber);
		
		String seedUrl = ConfigurationUtil.getGetSeedUrl(this);
		txtGetSeed.setText(seedUrl);
		
		String registerUrl = ConfigurationUtil.getRegisterDeviceUrl(this);
		txtRegister.setText(registerUrl);
		
		String validateUrl = ConfigurationUtil.getValidateDeviceUrl(this);
		txtValidate.setText(validateUrl);
		
		String taspUrl = ConfigurationUtil.getTaspServiceUrl(this);
		txtTasp.setText(taspUrl);
	}
	

	public void butSave_Click(View view)
	{
//		int selection = spToken.getSelectedItemPosition();
//		if(selection == 0)
//		{
//			ConfigurationUtil.setTokenProvider(this, TokenProvider.AUTHENTICUS);
//		}
//		if(selection == 1)
//		{
//			ConfigurationUtil.setTokenProvider(this, TokenProvider.VASCO);
//		}
		
		int themeSelection = spTheme.getSelectedItemPosition();
		ConfigurationUtil.setTheme(this, themeSelection);
		
//		ConfigurationUtil.setProtectorLogo(this, selectedImagePath);
		
		ConfigurationUtil.setTaidNumber(this, txtTaid.getText().toString());
		
		ConfigurationUtil.setGetSeedUrl(this, txtGetSeed.getText().toString());
		ConfigurationUtil.setRegisterDeviceUrl(this, txtRegister.getText().toString());
		ConfigurationUtil.setValidateDeviceUrl(this, txtValidate.getText().toString());
		ConfigurationUtil.setTaspServiceUrl(this, txtTasp.getText().toString());
		this.finish();
	}
	
	public void butDefault_Click(View view)
	{
//		selectedImagePath = "";
//		txtLogo.setText(selectedImagePath);
	}
	
	public void butGetImage_Click(View view)
	{
//		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		startActivityForResult(i, 1);
	}
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == 1 && resultCode == RESULT_OK && data != null) 
	        {
	            Uri selectedImage = data.getData();
	            
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            selectedImagePath = cursor.getString(columnIndex);
	            cursor.close();

	            txtLogo.setText(selectedImagePath);
	        }
	    }
	
	public void butCancel_Click(View view)
	{
		this.finish();
	}
	
	public void butProtectorClearUpdateCache_Click(View view)
	{
			Toast.makeText(getApplicationContext(), "Database Clean", Toast.LENGTH_LONG).show();
	}
	
	public void butProtectorClearScanCache_Click(View view)
	{	
		Toast.makeText(getApplicationContext(), "Scan Clean", Toast.LENGTH_LONG).show();
	}
}
