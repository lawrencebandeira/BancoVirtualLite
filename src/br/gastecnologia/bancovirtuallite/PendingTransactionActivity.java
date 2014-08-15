package br.gastecnologia.bancovirtuallite;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.repository.StorageUtil;
import br.gastecnologia.bancovirtuallite.service.TaspServiceHelper;
import br.gastecnologia.bancovirtuallite.service.Transaction;
import br.gastecnologia.bancovirtuallite.service.TransactionListAdapter;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;

public class PendingTransactionActivity extends BaseActivity 
{
	private TaspServiceHelper Helper;
	private ListView TransactionList;
	private TransactionListAdapter Adapter;
	private AlertDialog.Builder builder;

	private TaspAsyncTask AsyncTask;
	private AuthorizePaymentTask AuthorizeAsyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		Helper = new TaspServiceHelper(this);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendingtransaction);
        
        builder = new AlertDialog.Builder(this);

        TransactionList = (ListView)this.findViewById(R.id.listView1);
      
        
        GetPendingList();
	
	}
	
	public void GetPendingList()
	{
		AsyncTask = new TaspAsyncTask(this, Helper);
		AsyncTask.execute((Void)null);
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
			builder.setMessage(errorMessage);
			builder.setTitle(R.string.PendingTransactionActivity_1);
			builder.setNeutralButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface DI, int arg1) {
					DI.dismiss();
				}
			});
			builder.show();
		}
		Errors.clear();
	}
	
	OnItemClickListener OnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int pos,long arg3) 
		{
			String[] maliciousApks = StorageUtil.getMaliciousApks(PendingTransactionActivity.this);
			if(maliciousApks.length > 0)
			{
				ShowErrors(getString(R.string.PendingTransactionActivity_2));
			}
			else
			{
				Transaction clickedTransaction = Adapter.getItem(pos);
				AuthorizeAsyncTask = new AuthorizePaymentTask(PendingTransactionActivity.this, Helper);
				AuthorizeAsyncTask.execute(clickedTransaction);
			}
		}
	};
	
	public void UpdateListAdapter(ArrayList<Transaction> Transactions, ArrayList<String> Errors)
	{
		if(Errors.size() > 0)
		{
			ShowErrors(Errors);
		}
		else
		{
			Adapter = new TransactionListAdapter(this.getBaseContext(), 0, Transactions);
        	TransactionList.setAdapter(Adapter);
        	TransactionList.setOnItemClickListener(OnItemClick);
		}
	}

	public void ShowErrors(String string) 
	{
		ArrayList<String> Errors = new ArrayList<String>();
		Errors.add(string);
		ShowErrors(Errors);
		
	}
	
}
