package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.ErrorEventArgs;
import br.gastecnologia.bancovirtuallite.service.ErrorListener;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;

public class UnlockCardTask extends AsyncTask<Void, Void, String> 
{
	private AlertDialog.Builder builder;
	private TaspServiceHelper serviceHelper;
	private ProgressDialog progressDialog;
	private ArrayList<String> Errors;
	
	public UnlockCardTask(Context ctx, TaspServiceHelper Helper)
	{
		Errors = new ArrayList<String>();
		builder = new Builder(ctx);
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setMessage(ctx.getString(R.string.UnlockCardTask_1));
		progressDialog.setMessage(ctx.getString(R.string.UnlockCardTask_2));
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
	protected String doInBackground(Void... arg0) 
	{
		if(serviceHelper.unlockCard())
		{
			return Resources.getSystem().getString(R.string.UnlockCardTask_3);
		}
		return Resources.getSystem().getString(R.string.UnlockCardTask_4);
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
	
	@Override
	protected void onPostExecute(String result) 
	{
		
		progressDialog.dismiss();
		ShowMessage("Desbloqueio de cart��o", result);
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	

}
