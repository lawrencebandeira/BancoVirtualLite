package br.gastecnologia.bancovirtuallite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.util.BaseActivity;

public class OutOfBandActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oob);
	}
	
	
	public void butTransaction_Click(View view)
	{
		Intent I = new Intent(this, PendingTransactionActivity.class);
		startActivity(I);
				
	}
}
