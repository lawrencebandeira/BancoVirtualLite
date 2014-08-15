package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.repository.ConfigurationUtil;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;
import br.gastecnologia.bancovirtuallite.vasco.OTPEventListenerInterface;
import br.gastecnologia.bancovirtuallite.vasco.VascoHelper;

public class AccountActivity extends BaseActivity 
{	 
	private AlertDialog.Builder builder;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		builder = new Builder(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        
        progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getResources().getString(R.string.AccountActivity_1));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
        
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
	
	public void butTaid_Click (View view)
	{
		String TaidNumber = ConfigurationUtil.getTaidNumber(this);
		 try 
		 {
			
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse(String.format("tel:%s", TaidNumber)));
	        startActivity(callIntent);
		 } 
		 catch (Exception e) 
		 {
			 ShowMessage(getString(R.string.AccountActivity_2), String.format(getString(R.string.AccountActivity_3), TaidNumber));
		 }
	}
	
	public void butWithdraw_Click (View view)
	{
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		new AlertDialog.Builder(this)
	    .setTitle(R.string.AccountActivity_4)
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	        	dialog.dismiss();
	            String value = input.getText().toString();
	            try
	            {
	            	if(value.indexOf(",") != -1)
	            	{
	            		value = value.replace(",", ".");
	            	}
	            	double val = Double.parseDouble(value);
	            	if(val % 10 != 0)
	            	{
	            		ShowMessage(getString(R.string.AccountActivity_5), getString(R.string.AccountActivity_6));	
	            	}
	            	else
	            	{
	            		TaspServiceHelper helper = new TaspServiceHelper(AccountActivity.this);
	            		ATMAsyncTask task = new ATMAsyncTask(AccountActivity.this, helper);
	            		task.execute(value);
	            	}
	            }
	            catch (Exception e)
	            {
	            	ShowMessage(getString(R.string.AccountActivity_5), getString(R.string.AccountActivity_7));
	            }
	            
	        }
	    })
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int whichButton) 
	        {
	        	dialog.dismiss();
	        }
	    }).show();
	}
	
	public void butUnlockCard_Click (View view)
	{
		TaspServiceHelper Helper = new TaspServiceHelper(this);
		UnlockCardTask AsyncTask = new UnlockCardTask(this, Helper);
		AsyncTask.execute((Void)null);
	}
	
	public void butOOB_Click(View view)
	{		
		Intent I = new Intent(this,  PendingTransactionActivity.class);
		startActivity(I);		
	}
	
	void ShowMessage(String Title, String Message)
	{
		ArrayList<String> Messages = new ArrayList<String>();
		Messages.add(Message);
		ShowMessages(Title, Messages);
	}
	
	void ShowMessages(String Title, ArrayList<String> Messages)
	{
		if(Messages.size() > 0)
		{
			String errorMessage = "";
			for(String s : Messages)
			{
				errorMessage = String.format("%s\n%s", errorMessage, s);
			}
			builder.setMessage(errorMessage);
			builder.setTitle(Title);
			builder.setNeutralButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface DI, int arg1) {
					DI.dismiss();
				}
			});
			builder.show();
		}
		Messages.clear();
	}
	
	public void butTransfer_Click(View view)
	{
		Intent I = new Intent(this, TransferActivity.class);
		startActivity(I);
	}
	
	public void butVasco_Click(View view)
	{
		VascoHelper.setOTPEventListener(new OTPEventListenerInterface() {
			
			@Override
			public void OnOTPRetrievedEvent(String Result) 
			{
				ShowMessage(getString(R.string.AccountActivity_8), Result);
				
			}
		});
		VascoHelper.getOTP(this);
	}
	
	
	public void butToken_Click(View view)
	{
		
	}
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
		 try
	     {
		    	String contents = data.getStringExtra("SCAN_RESULT");
	    	}
	    	catch (Exception e)
	    	{
//	    		ShowMessage("Error", e.getMessage());
	    	}
	    	super.onActivityResult(requestCode, resultCode, data);
	    }
}
