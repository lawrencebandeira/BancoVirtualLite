package br.gastecnologia.bancovirtuallite.vasco;


import android.content.Context;


public final class VascoHelper 
{
	static OTPEventListenerInterface EventListener;
	public static void setOTPEventListener(OTPEventListenerInterface listener)
	{
		EventListener = listener;
	}
	 
	public static void ShowOTP(Context context, String OTP)
	{
		if(EventListener != null)
		{
			EventListener.OnOTPRetrievedEvent(OTP);
		}
	}
	
	public static void getOTP(Context context)
	{
		GetOTPTask task = new GetOTPTask(context);
		task.execute((Void)null);
	}
}
