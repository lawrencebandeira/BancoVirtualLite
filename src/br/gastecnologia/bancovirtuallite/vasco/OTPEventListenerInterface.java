package br.gastecnologia.bancovirtuallite.vasco;

import java.util.EventListener;

public interface OTPEventListenerInterface extends EventListener 
{
	public void OnOTPRetrievedEvent(String Result);
}
