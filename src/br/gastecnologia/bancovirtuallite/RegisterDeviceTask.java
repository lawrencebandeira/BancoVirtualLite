package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.service.ErrorEventArgs;
import br.gastecnologia.bancovirtuallite.service.ErrorListener;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;

public class RegisterDeviceTask extends AsyncTask<Void, Void, Boolean> 
{

	private BancoVirtualActivity actualActivity;
	private TaspServiceHelper serviceHelper;
	private ProgressDialog progressDialog;
	private ArrayList<String> Errors;
	private String agency, account, deviceId;
	
//	public RegisterDeviceTask(BancoVirtualActivity ActualActivity, TaspServiceHelper Helper, MGBasHelper GBasHelper, String User)
//	Agency, String Account, String Password, String DeviceId
	public RegisterDeviceTask(BancoVirtualActivity ActualActivity, TaspServiceHelper Helper, String Agency, String Account, String DeviceId)	
	{
		Errors = new ArrayList<String>();
		progressDialog = new ProgressDialog(ActualActivity);
		progressDialog.setMessage(ActualActivity.getString(R.string.RegisterDeviceTask_1));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		actualActivity = ActualActivity;
		serviceHelper = Helper;
		agency = Agency;
		account = Account;
		deviceId = DeviceId;
		
		serviceHelper.addErrorListener(new ErrorListener() {
				
				@Override
				public void OnError(ErrorEventArgs e) 
				{
					Errors.add(e.getMessage());
				}
			});
	}	
	
	@Override
	protected Boolean doInBackground(Void... arg0) 
	{
		return true;
	}

	@Override
	protected void onPreExecute() 
	{
		progressDialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Boolean result) 
	{
		progressDialog.dismiss();
		actualActivity.processCompleted(result, Errors);
		super.onPostExecute(result);
	}
	
}
