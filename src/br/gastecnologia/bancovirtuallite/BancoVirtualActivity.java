package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;

public class BancoVirtualActivity extends BaseActivity 
{
	
	private TaspServiceHelper Helper;
	private String android_id;
	private AlertDialog.Builder builder;
	private EditText txtAgency;
	private EditText txtAccount;
	private EditText txtPassword;
	static boolean changedLanguage = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {	
    	Helper = new TaspServiceHelper(this);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	builder = new AlertDialog.Builder(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        changedLanguage = false;
        
        android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
        
        txtAgency = (EditText)findViewById(R.id.editText1);
        txtPassword = (EditText)findViewById(R.id.editText2);
        txtAccount = (EditText)findViewById(R.id.editText3);
        
        UserManagement.setSharedPreferences(getSharedPreferences("VIRTUAL_BANK_CONFIGURATION", 0));
        txtAccount.setText(UserManagement.getLoggedAccount());
        txtAgency.setText(UserManagement.getLoggedAgency());
        txtPassword.setText(UserManagement.getLoggedPassword());

    }
    
    
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    
    @Override
	protected void onResume() {
		super.onResume();
		
		if(changedLanguage == true){
			restartActivity();
		}
	}
    

	public void butQrCode_Click (View view)
    {
    }
    
    public void butProtector_Click(View view) 
    {
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Intent I = null;
		if(item.getItemId() == R.id.item1)
		{
			I = new Intent(this,  ConfigurationActivity.class);
		}
		else
		{
			//I = new Intent(this,  ProtectorActivity.class);
		}
		startActivity(I);
		return super.onOptionsItemSelected(item);
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    void ShowErrors(ArrayList<String> Errors)
	{
		if(Errors.size() > 0)
		{
			String errorMessage = "";
			for(String s : Errors)
			{
				errorMessage = String.format("%s\n%s", errorMessage, s);
			}
			ShowMessage(getString(R.string.BancoVirtualActivity_2), errorMessage);
		}
		Errors.clear();
	}
    

    void ShowMessage(String Title, String Message)
	{
	
			builder.setMessage(Message);
			builder.setTitle(Title);
			builder.setNeutralButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface DI, int arg1) {
					DI.dismiss();
				}
			});
			builder.show();
	}
    
    public void processCompleted(Boolean result, ArrayList<String> errors)
    {
    	
    	if(result)
    	{
    		ShowMessage(getString(R.string.BancoVirtualActivity_3), getString(R.string.BancoVirtualActivity_4));
    	}
    	else
    	{
    		ShowErrors(errors);
    	}
    }
    
    public void butAccess_Click(View view)
    {
    	String Agency = txtAgency.getText().toString();
    	String Account = txtAccount.getText().toString();
    	String Password = txtPassword.getText().toString();
    	
    	// -- Send Broadcast with user's informations			
    	Broadcast broadcast = new Broadcast(this);				
    	broadcast.sendBradcast(Agency, Account, Password);
    	
    	Intent I = new Intent(this, AccountActivity.class);
		startActivity(I);
    }
    
    public static void changeLocale(Locale locale, Context appctx, Context basectx){
    	changedLanguage = true;
    	Locale.setDefault(locale);
    	Configuration config = new Configuration();
    	config.locale = locale;
    	appctx.getResources().updateConfiguration(config, basectx.getResources().getDisplayMetrics());
    }
    
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}