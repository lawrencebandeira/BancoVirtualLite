package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.ErrorEventArgs;
import br.gastecnologia.bancovirtuallite.service.ErrorListener;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;

public class ATMAsyncTask extends AsyncTask<String, Void, String> 
{
	private TaspServiceHelper serviceHelper;
	private AlertDialog.Builder builder;
	private ProgressDialog progressDialog;
	private ArrayList<String> Errors;
	
	public ATMAsyncTask(Context context, TaspServiceHelper Helper)
	{
		Errors = new ArrayList<String>();
		
		builder = new Builder(context);
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getString(R.string.ATMAsyncTask_1));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		
		serviceHelper = Helper;
		serviceHelper.addErrorListener(new ErrorListener() {
				
				@Override
				public void OnError(ErrorEventArgs e) 
				{
					Errors.add(e.getMessage());
				}
			});
	}	
	

	@Override
	protected void onPreExecute() 
	{
		progressDialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... arg0) 
	{
		return serviceHelper.AuthroizePendingATM(UserManagement.getLoggedUser(), arg0[0]);
	}
	

	
	@Override
	protected void onPostExecute(String result) 
	{
		
		progressDialog.dismiss();
		ShowMessage("Result", result);
		
		super.onPostExecute(result);
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

}
