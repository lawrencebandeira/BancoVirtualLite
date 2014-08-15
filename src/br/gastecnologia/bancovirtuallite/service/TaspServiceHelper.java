package br.gastecnologia.bancovirtuallite.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.gastecnologia.bancovirtuallite.R;
import br.gastecnologia.bancovirtuallite.UserManagement;
import br.gastecnologia.bancovirtuallite.repository.ConfigurationUtil;
import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;

public class TaspServiceHelper {
	private ArrayList<ErrorListener> listenerList;
	private String device_id = "";
	private String SERVICE = "http://184.172.20.150/TaspService/Service1.svc";// real
	// private final String SERVICE =
	// "http://192.168.25.11:59212/Service1.svc";//ip
	private final String METHOD_NAME = "ExecuteCommand";
	private final String NAMESPACE = "http://tempuri.org/";
	private final String SOAP_ACTION = "http://tempuri.org/IService1/ExecuteCommand";
	private Context context;
	// private final String SOAP_ACTION =
	// "http://www.webserviceX.NET/ExecuteCommand";

	public TaspServiceHelper(Context Context) {
		SERVICE = ConfigurationUtil.getTaspServiceUrl(Context);
		listenerList = new ArrayList<ErrorListener>();
		device_id = Secure.getString(Context.getContentResolver(),
				Secure.ANDROID_ID);
		context = Context;
	}

	private String EncodeBase64(String value) {
		try {
			byte[] ValueUtf = value.getBytes("UTF-8");
			String base64 = Base64.encodeToString(ValueUtf, 2);
			return base64;
		} catch (Exception ex) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_1));
		}
		return "";
	}

	private String DecodeBase64(String base64) {
		try {
			byte[] resultDataBytes = Base64.decode(base64, 2);
			return new String(resultDataBytes);
		} catch (Exception ex) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_2));
		}
		return "";
	}

	private ArrayList<Transaction> CreateEntities(String result) {
		ArrayList<Transaction> RetVal = new ArrayList<Transaction>();
		try {
			String resultData = DecodeBase64(result);
			String[] resultParts = resultData.split("\n");
			String Part = "";
			for (int i = 0; i < resultParts.length; i++) {
				Part = resultParts[i];
				String Key = Part.substring(0, Part.indexOf(":")).toUpperCase();
				String Value = DecodeBase64(Part.substring(Key.length() + 1));

				if (Key.equals("CODE")) {
					if (Value != "0") {
						// LOG ERROR
					}
				}
				if (Key.equals("CONTENT")) {
					Value = DecodeBase64(Value);
					RetVal = ParseResponseXml(Value);
				}

			}

		} catch (Exception ex) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_3));
		}
		return RetVal;
	}

	private ArrayList<Transaction> ParseResponseXml(String Xml) {
		ArrayList<Transaction> RetVal = new ArrayList<Transaction>();
		try {
			Document XmlDoc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(Xml));
			XmlDoc = db.parse(is);

			NodeList nl = XmlDoc.getElementsByTagName("REQUEST");
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				NodeList childNodes = node.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					Node ChildNode = childNodes.item(j);
					if (ChildNode.getNodeName().equals("TRANSACTION")) {
						if (ChildNode.hasAttributes()) {
							NamedNodeMap attributes = ChildNode.getAttributes();

							String ID = attributes.getNamedItem("ID")
									.getNodeValue();
							String ActionID = "";
							Node ActionIDNode = attributes
									.getNamedItem("ACTION_ID");
							if (ActionIDNode != null) {
								ActionID = ActionIDNode.getNodeValue();
							}
							String Message = attributes.getNamedItem("MESSAGE")
									.getNodeValue();
							Transaction transaction = new Transaction(ID,
									ActionID, Message);
							RetVal.add(transaction);
						} else {
							// LOG ERROR
						}

					}
				}
				// node
			}
		} catch (Exception e) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_4));
		}
		return RetVal;

	}

	private ArrayList<Transaction> SendPendingCommand(int Command,
			Map<String, Object> Params) {
		try {
			String base64 = PrepareCommand(Command, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

				return CreateEntities(result.toString());

			}
		} catch (Exception e) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_5));
			// log error
		}
		return new ArrayList<Transaction>();

	}

	private String PrepareCommand(int Command, Map<String, Object> Params) {
		return PrepareCommand(Command, Params, true);
	}

	private String PrepareCommand(int Command, Map<String, Object> Params,
			boolean IncludeCommand) {

		String result = "";
		try {
			if (IncludeCommand) {
				result = String.format("COMMAND:%d", Command);
			}
			Object[] ParamsKeys = Params.keySet().toArray();
			for (int i = 0; i < Params.size(); i++) {
				String key = (String) ParamsKeys[i];
				Object value = Params.get(key);
				String base64 = EncodeBase64(value.toString());
				result = String.format("%s\n%s:%s", result, key, base64);
			}
			byte[] ResultUTF = result.getBytes("UTF-8");
			return Base64.encodeToString(ResultUTF, 2);

			// return
			// "Q09NTUFORDowCkRFVklDRV9JRDpZakZtWldFMU5HSm1PRFpoTVdRMFlXSTNPV1k1WVdWa1pHSTBaVGN6T0RNeE1qRmxaREUzWWc9PQpVU0VfTE9HSU46YkdWdg==";
		} catch (Exception ex) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_6));
		}
		return "";
	}

	public String AuthroizePendingATM(String UserLogin, String TransactionValue) {
		int min = 10000;
		int max = 99999;

		Random r = new Random();
		int i1 = r.nextInt(max - min + 1) + min;

		try {
			Hashtable<String, Object> Params = new Hashtable<String, Object>();
			Params.put("USER_LOGIN", UserLogin);

			Params.put("TRANSACAO_VALOR", TransactionValue);
			Params.put("TOKEN", i1);

			String base64 = PrepareCommand(5, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
				String resultData = result.toString();
				if (resultData.length() > 0) {
					resultData = DecodeBase64(resultData);
					String[] resultParts = resultData.split("\n");
					String Part = "";
					for (int i = 0; i < resultParts.length; i++) {
						Part = resultParts[i];
						String Key = Part.substring(0, Part.indexOf(":"))
								.toUpperCase();
						String Value = DecodeBase64(Part
								.substring(Key.length() + 1));

						if (Key.equals("CODE")) 
						{
							if (!Value.equals("0")) 
							{
								return String.format(context.getString(R.string.TaspServiceHelper_7), Value);
							}
							else
							{
								return String.format(context.getString(R.string.TaspServiceHelper_8), i1);
							}
						}

						if (Key.equals("CONTENT")) 
						{
							if(Value.indexOf(".") == -1 && Value.indexOf(",") == -1)
							{
								Value = Value + ",00";
							}
							return String.format(context.getString(R.string.TaspServiceHelper_9, i1, Value));
						}

					}
				} else {
					return context.getString(R.string.TaspServiceHelper_10);
				}
			}

		}
		catch (Exception ex) 
		{
			return context.getString(R.string.TaspServiceHelper_5);
		}
		return "";
	}

	public String GetLoginUser(String Agency, String Account, String Password) {
		try {
			Hashtable<String, Object> Params = new Hashtable<String, Object>();
			Params.put("USER_AGENCY", Agency);
			Params.put("USER_ACCOUNT", Account);
			Params.put("USER_PASSWORD", Password);

			String base64 = PrepareCommand(4, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
				String resultData = result.toString();
				if (resultData.length() > 0) {
					resultData = DecodeBase64(resultData);
					String[] resultParts = resultData.split("\n");
					String Part = "";
					for (int i = 0; i < resultParts.length; i++) {
						Part = resultParts[i];
						String Key = Part.substring(0, Part.indexOf(":"))
								.toUpperCase();
						String Value = DecodeBase64(Part
								.substring(Key.length() + 1));

						if (Key.equals("CODE")) {
							if (!Value.equals("0")) {
								return String.format(
										context.getString(R.string.TaspServiceHelper_11), Value);
							}
						}

						if (Key.equals("CONTENT")) {
							return Value;
						}

					}
				} else {
					return context.getString(R.string.TaspServiceHelper_10);
				}
			}
		} catch (Exception e) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_5));
			// log error
		}
		return "";
	}

	public ArrayList<Transaction> GetPendingTransactions() {
		String udid = device_id;
		String user = UserManagement.getLoggedUser();
		Hashtable<String, Object> Params = new Hashtable<String, Object>();
		Params.put("DEVICE_ID", udid);
		Params.put("USER_LOGIN", user);

		ArrayList<Transaction> PendingTransactions = SendPendingCommand(0,
				Params);
		return PendingTransactions;
	}

	public void addErrorListener(ErrorListener l) {
		listenerList.add(l);
	}

	public void removeErrorListener(ErrorListener l) {
		listenerList.remove(l);
	}

	// Roughly analogous to .net OnEvent protected virtual method pattern -
	// call this method to raise the event
	protected void raiseOnError(String ErrorMessage) {
		ErrorEventArgs args = new ErrorEventArgs(ErrorMessage);
		for (ErrorListener l : listenerList) {
			l.OnError(args);
		}
	}

	public boolean AuthorizeTransacton(Transaction trans) {
		try {
			Hashtable<String, Object> Params = new Hashtable<String, Object>();
			Params.put("TRANSACTION_ID", trans.getID());
			Params.put("USER_LOGIN", UserManagement.getLoggedUser());
			Params.put("DEVICE_ID", device_id);

			int ActionId = 2;
			if (trans.getMessage().startsWith("Cart��o")) {
				ActionId = 3;
			}
			String base64 = PrepareCommand(ActionId, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

				return ParseAuthorizeResult(result.toString());

			}
		} catch (Exception e) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_5));
			// log error
		}
		return false;
	}

	private boolean ParseAuthorizeResult(String result) {
		result = DecodeBase64(result);
		String[] resultParts = result.split("\n");
		String Part = "";
		for (int i = 0; i < resultParts.length; i++) {
			Part = resultParts[i];
			String Key = Part.substring(0, Part.indexOf(":")).toUpperCase();
			String Value = DecodeBase64(Part.substring(Key.length() + 1));

			if (Key.equals("CODE")) {
				if (Value.equals("0")) {
					return true;
				} else {
					raiseOnError(context.getString(R.string.TaspServiceHelper_12)
							+ Value.toString());
				}
			}
		}
		return false;
	}

	public boolean registerDevice(Map<String, Object> Params) {
		try {
			String base64 = PrepareCommand(1, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();

				return ParseAuthorizeResult(result.toString());

			}
		} catch (Exception e) {
			raiseOnError(context.getString(R.string.TaspServiceHelper_5));
		}
		return false;

	}

	public boolean unlockCard() {
		try {
			Hashtable<String, Object> Params = new Hashtable<String, Object>();
			Params.put("USER_LOGIN", UserManagement.getLoggedUser());

			String base64 = PrepareCommand(3, Params);
			if (base64 != "") {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				request.addProperty("Data", base64);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SERVICE);
				androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
				String resultData = result.toString();
				if (resultData.length() > 0) {
					resultData = DecodeBase64(resultData);
					String[] resultParts = resultData.split("\n");
					String Part = "";
					for (int i = 0; i < resultParts.length; i++) {
						Part = resultParts[i];
						String Key = Part.substring(0, Part.indexOf(":"))
								.toUpperCase();
						String Value = DecodeBase64(Part
								.substring(Key.length() + 1));

						if (Key.equals("CODE")) {
							if (Value.equals("0")) {
								return true;
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			Log.w("TASP", ex.getMessage());
		}
		return false;
	}
}
