package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.ErrorEventArgs;
import br.gastecnologia.bancovirtuallite.service.ErrorListener;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;
import br.gastecnologia.bancovirtuallite.service.Transaction;

public class AuthorizePaymentTask extends AsyncTask<Transaction, Void, Boolean>
{
	private PendingTransactionActivity actualActivity;
	private TaspServiceHelper serviceHelper;
	private ProgressDialog progressDialog;
	private ArrayList<String> Errors;
	
	public AuthorizePaymentTask(PendingTransactionActivity ActualActivity, TaspServiceHelper Helper)
	{
		Errors = new ArrayList<String>();
		progressDialog = new ProgressDialog(ActualActivity);
		progressDialog.setMessage(ActualActivity.getString(R.string.AuthorizePaymentTask_1));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		actualActivity = ActualActivity;
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
	protected void onPostExecute(Boolean result) 
	{
		progressDialog.dismiss();
		if(result)
		{
			actualActivity.GetPendingList();
		}
		else
		{
			actualActivity.ShowErrors(actualActivity.getString(R.string.AuthorizePaymentTask_2));
		}
		super.onPostExecute(result);
	}
	
	
	@Override
	protected void onPreExecute() 
	{
		progressDialog.show();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Transaction... arg0) 
	{
		boolean retVal = true;
		for(Transaction Trans : arg0)
		{
			retVal = retVal && serviceHelper.AuthorizeTransacton(Trans);
		}
		return retVal;
	}
	

}
