package br.gastecnologia.bancovirtuallite;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.repository.StorageUtil;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;
import br.gastecnologia.bancovirtuallite.vasco.OTPEventListenerInterface;
import br.gastecnologia.bancovirtuallite.vasco.VascoHelper;

public class TransferActivity extends BaseActivity 
{
	Builder builder;
	EditText txtAg;
	EditText txtAc;
	EditText txtVal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		builder = new Builder(this);
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.transfer);
		
		txtAg = (EditText)findViewById(R.id.editText1);
		txtAc = (EditText)findViewById(R.id.editText2);
		txtVal = (EditText)findViewById(R.id.editText3);
		
	}
	
	public void butConfirmToken_Click(View view)
	{
		
		Succeeded = true;
		String Message = getString(R.string.TransferActivity_1);
		String Title = getString(R.string.TransferActivity_2);
		String[] maliciousApks = StorageUtil.getMaliciousApks(this);
		if(maliciousApks.length > 0)
		{
			Succeeded = false;
			Title = getString(R.string.TransferActivity_3);
			Message = getString(R.string.TransferActivity_4);
		}
		else
		{
			String ag = txtAg.getText().toString();
			String ac = txtAc.getText().toString();
			String val = txtVal.getText().toString();
			
			if(ag.equals("") || ac.equals("") || val.equals(""))
			{
				Succeeded = false;
				Title = getString(R.string.TransferActivity_3);
				Message = getString(R.string.TransferActivity_5);
			}
			else
			{
				VascoHelper.setOTPEventListener(OTPRetrieved);
				VascoHelper.getOTP(this);
				return;
			}
		}
		
		
		builder.setMessage(Message);
		builder.setTitle(Title);
		builder.setNeutralButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface DI, int arg1) 
			{
				DI.dismiss();
				if(Succeeded)
				{
					TransferActivity.this.finish();
				}
			}
		});
		builder.show();
	}
	
	OTPEventListenerInterface OTPRetrieved = new OTPEventListenerInterface() 
	{
		
		@Override
		public void OnOTPRetrievedEvent(String Result) {
			String Message = getString(R.string.TransferActivity_6) + Result;
			String Title = getString(R.string.TransferActivity_2);
			
			builder.setMessage(Message);
			builder.setTitle(Title);
			builder.setNeutralButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface DI, int arg1) 
				{
					DI.dismiss();
					if(Succeeded)
					{
						TransferActivity.this.finish();
					}
				}
			});
			builder.show();
		}
	};

	boolean Succeeded = false;
	public void butConfirm_Click(View view)
	{
		Succeeded = true;
		String Message = getString(R.string.TransferActivity_1);
		String Title = getString(R.string.TransferActivity_2);
		String[] maliciousApks = StorageUtil.getMaliciousApks(this);
		if(maliciousApks.length > 0)
		{
			Succeeded = false;
			Title = getString(R.string.TransferActivity_3);
			Message = getString(R.string.TransferActivity_4);
		}
		else
		{
			String ag = txtAg.getText().toString();
			String ac = txtAc.getText().toString();
			String val = txtVal.getText().toString();
			
			
			if(ag.equals("") || ac.equals("") || val.equals(""))
			{
				Succeeded = false;
				Title = getString(R.string.TransferActivity_3);
				Message = getString(R.string.TransferActivity_5);
			}
		}
		
		
		builder.setMessage(Message);
		builder.setTitle(Title);
		builder.setNeutralButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface DI, int arg1) 
			{
				DI.dismiss();
				if(Succeeded)
				{
					TransferActivity.this.finish();
				}
			}
		});
		builder.show();
		
	}
	
	public void butCancel_Click(View view)
	{
		TransferActivity.this.finish();
	}
	
}
