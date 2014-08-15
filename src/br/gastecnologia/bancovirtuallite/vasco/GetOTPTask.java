package br.gastecnologia.bancovirtuallite.vasco;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;


//import com.vasco.digipass.sdk.DigipassSDK;
//import com.vasco.digipass.sdk.DigipassSDKConstants;
//import com.vasco.digipass.sdk.DigipassSDKReturnCodes;
//import com.vasco.digipass.sdk.responses.ActivationResponse;
//import com.vasco.digipass.sdk.responses.GenerationResponse;

import br.gastecnologia.bancovirtuallite.UserManagement;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetOTPTask extends AsyncTask<Void, Void, String>
{
//	private static String SERVER_URL = "http://184.172.20.150/VIRTUALBANK/Pages/vasco/getotp.aspx?time=%d:%d:%d:%d&user=%s";
	private static String SERVER_URL = "http://184.172.20.150/Pages/vasco/getotp.aspx?time=%d:%d:%d:%d&user=%s";
	private static String StaticVector = "380801070103564453021010101010101010100123456789ABCDEF03010104010405011006010107010108010309010F0A01010B01010C01000E01000F0100100101112F120100130101140101150C4170706C312020202020202016010117044080F8021801002901062A01062B01002C01021135120100130101140102150C4170706C32202020202020201601011704408BF8D21801011901062101062901062A01062B01002C0102115F120100130101140103150C4170706C33202020202020201601011704408BF8D21801081901011A01011B01011C01011D01011E01011F01012001012101102201102301102401102501102601102701102801102901062A01062B01002C0102";
	private static String SerialNumberSufix = "1000020";
	private static String ActivationCode = "371687765119514558605";
	private static String Password = "39673341";
	
	private ProgressDialog progressDialog;
	private Context context;
	
	public GetOTPTask(Context Context)
	{
		progressDialog = new ProgressDialog(Context);
		progressDialog.setTitle("Gerando Token VASCO...");
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);

		context = Context;
	}
	
	@Override
	protected void onPostExecute(String result) 
	{
		progressDialog.dismiss();
		VascoHelper.ShowOTP(context, result);
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(Void... arg0) {
		return getOTP();
	}
	
	private String httpGet(String URL)
	{
		try 
		{
//			Calendar ca = Calendar.getInstance();
//			int minute = ca.get(Calendar.MINUTE);
//			int hour = ca.get(Calendar.HOUR_OF_DAY);
//			int second = ca.get(Calendar.SECOND);
//			int millisecond =  ca.get(Calendar.MILLISECOND);
//			String user = UserManagement.getLoggedUser();
//			URL = String.format(URL, hour, minute, second, millisecond, user);
//			HttpClient client = new DefaultHttpClient();
//			HttpUriRequest request = new HttpGet(URL);
//			HttpResponse response;
//			response = client.execute(request);
//			InputStream is = response.getEntity().getContent();
//			Writer writer = new StringWriter();
//            char[] buffer = new char[1024];
//            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            int n;
//            while ((n = reader.read(buffer)) != -1) {
//                writer.write(buffer, 0, n);
//            }
//            return writer.toString();
		} 
		catch (Exception e) 
		{
			return "";
		}
		return "OTP VASCO ESTÁ DESABILITADO PARA ESTE APLICATIVO";
	}
	
	public String getOTP()
	{
//		String challengeResp = httpGet(SERVER_URL);
//		if (challengeResp != "" && challengeResp.indexOf("|") >= 0)
//		{
//			String Challenge = challengeResp.substring(0, challengeResp.indexOf("|"));
//			String tShift = challengeResp.substring(challengeResp.indexOf("|") + 1);
//
//			ActivationResponse response = DigipassSDK.activateOffline(StaticVector, SerialNumberSufix, ActivationCode, null, null, Password, null);
//			if (response.getReturnCode() == DigipassSDKReturnCodes.SUCCESS)
//			{
//				String plataformFingerPrint = null;
//				long TimeShift = Long.valueOf(tShift);
//				GenerationResponse genResponse = DigipassSDK.generateResponseFromChallenge(response.getStaticVector(), response.getDynamicVector(), Challenge, Password, TimeShift, DigipassSDKConstants.CRYPTO_APPLICATION_INDEX_APP_1, plataformFingerPrint);
//				if(genResponse.getReturnCode() == DigipassSDKReturnCodes.SUCCESS)
//				{
//					String OTP = genResponse.getResponse();
//					return OTP;
//				}
//			}
//			else
//			{
				return "FAIL";
//			}
//		}
//		else
//		{
//			return "FAIL";
//		}
//		return "";
	}

	@Override
	protected void onPreExecute() 
	{
		progressDialog.show();
		super.onPreExecute();
	}
}
