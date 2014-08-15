package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.ErrorEventArgs;
import br.gastecnologia.bancovirtuallite.service.ErrorListener;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;
import br.gastecnologia.bancovirtuallite.service.Transaction;

public class TaspAsyncTask extends AsyncTask<Object, Void, ArrayList<Transaction>> 
{
	private PendingTransactionActivity actualActivity;
	private TaspServiceHelper serviceHelper;
	private ProgressDialog progressDialog;
	private ArrayList<String> Errors;
	
	public TaspAsyncTask(PendingTransactionActivity ActualActivity, TaspServiceHelper Helper)
	{
		Errors = new ArrayList<String>();
		progressDialog = new ProgressDialog(ActualActivity);
		progressDialog.setMessage(ActualActivity.getString(R.string.TaspAsyncTask_1));
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
	protected void onPreExecute() 
	{
		progressDialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<Transaction> doInBackground(Object... arg0) 
	{
		return serviceHelper.GetPendingTransactions();
	}
	

	
	@Override
	protected void onPostExecute(ArrayList<Transaction> result) 
	{
		
		progressDialog.dismiss();
		actualActivity.UpdateListAdapter(result, Errors);
		
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	

}
