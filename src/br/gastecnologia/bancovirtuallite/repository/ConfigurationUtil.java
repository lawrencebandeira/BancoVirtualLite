package br.gastecnologia.bancovirtuallite.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import br.gastecnologia.bancovirtuallite.util.ThemeEventHandler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class ConfigurationUtil 
{
	private static String FILENAME = "VIRTUALBANK_CONFIGURATION";
	
	public static void setTokenProvider(Context context, TokenProvider Value)
	{
		save(context, "TOKEN_PROVIDER", Value.name());
	}
	
	
	public static TokenProvider getTokenProvider(Context context)
	{
		String persisted = get(context, "TOKEN_PROVIDER");
		if(persisted.length() > 0)
		{
			return TokenProvider.valueOf(get(context, "TOKEN_PROVIDER"));
		}
		else
		{
			return TokenProvider.VASCO;
		}
	}
	
	public static String getTaidNumber(Context context)
	{
		String persisted = get(context, "TAID_NUMBER");
		if(persisted.length() == 0)
		{
			return "6133401868";
		}
		else
		{
			return persisted;
		}
	}
	
	
	public static void setTaidNumber(Context context, String value)
	{
		save(context, "TAID_NUMBER", value);
	}
	
	
	public static String getGetSeedUrl(Context context)
	{
		String persisted = get(context, "GET_SEED_URL");
		if(persisted.length() == 0)
		{
//			return "http://www3.gastecnologia.com.br/newVirtualBank/pages/authenticus/identifydevice.aspx?deviceInfo=%s";
			return "http://192.168.43.201/pages/authenticus/identifydevice.aspx?deviceInfo=%s";
		}
		else
		{
			return persisted;
		}
	}
	
	
	public static void setGetSeedUrl(Context context, String value)
	{
		save(context, "GET_SEED_URL", value);
	}
	
	public static String getRegisterDeviceUrl(Context context)
	{
		String persisted = get(context, "REGISTER_DEVICE_URL");
		if(persisted.length() == 0)
		{
//			return "http://www3.gastecnologia.com.br/newVirtualBank/pages/authenticus/registerDevice.aspx?deviceInfo=%s&agency=%s&account=%s&deviceId=%s";
			return "http://192.168.43.201/pages/authenticus/registerDevice.aspx?deviceInfo=%s&agency=%s&account=%s&deviceId=%s";
		}
		else
		{
			return persisted;
		}
	}
	
	public static void setRegisterDeviceUrl(Context context, String value)
	{
		save(context, "REGISTER_DEVICE_URL", value);
	}
	
	public static String getValidateDeviceUrl(Context context)
	{
		String persisted = get(context, "VALIDATE_DEVICE_URL");
		if(persisted.length() == 0)
		{
//			return "http://www3.gastecnologia.com.br/newVirtualBank/pages/authenticus/loginDevice.aspx?deviceInfo=%s&agency=%s&account=%s&password=%s";
			return "http://192.168.43.201/pages/authenticus/loginDevice.aspx?deviceInfo=%s&agency=%s&account=%s&password=%s";
		}
		else
		{
			return persisted;
		}
	}
	
	
	public static void setValidateDeviceUrl(Context context, String value)
	{
		save(context, "VALIDATE_DEVICE_URL", value);
	}
	
	
	public static String getTaspServiceUrl(Context context)
	{
		String persisted = get(context, "TASP_SERVICE_URL");
		if(persisted.length() == 0)
		{
//			return "http://www3.gastecnologia.com.br/TaspService/Service1.svc";
			return "http://192.168.43.201/TaspService/Service1.svc";
		}
		else
		{
			return persisted;
		}
	}
	
	
	public static void setTaspServiceUrl(Context context, String value)
	{
		save(context, "TASP_SERVICE_URL", value);
	}
	
	public static void setTheme(Context context, int value)
	{
		save(context, "THEME", Integer.toString(value));
		ThemeEventHandler.RaiseThemeChanged(value);
	}
	
	public static int getTheme(Context context)
	{
		String persisted = get(context, "THEME");
		if(persisted.length() > 0)
		{
			return Integer.parseInt(persisted);
		}
		else
		{
			return 0;
		}
	}
	
	
	public static void setProtectorLogo(Context context, String value)
	{
		save(context, "PROTECTOR_LOGO", value);
	}
	
	public static String getProtectorLogoString(Context context)
	{
		return get(context, "PROTECTOR_LOGO");
	}
	
	public static Bitmap getProtectorLogo(Context context)
	{
		try
		{
			String persistedPath = get(context, "PROTECTOR_LOGO");
			if(persistedPath.length() > 0)
			{
				return BitmapFactory.decodeFile(persistedPath);
			}
			else 
			{
				return null;
			}
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	private static String getAll(Context context)
	{
		FileInputStream fis;
		try 
		{
			fis = context.openFileInput(FILENAME);
			int length = fis.available();
			if(length > 0)
			{
				byte[] buffer = new byte[length];
				fis.read(buffer);
				fis.close();
				return new String(buffer);
			}
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	private static void save(Context context, String key, String Value)
	{
		FileOutputStream fos;
		try 
		{
			String persistingKey = String.format("\n%s:", key);
			String persistedContent = getAll(context);
			int index = persistedContent.indexOf(persistingKey);
			if(index >= 0)
			{
				int endIndex = persistedContent.indexOf("\n", index + 1);
				if(endIndex == -1) endIndex = persistedContent.length();
				persistedContent =  String.format("%s%s%s%s", persistedContent.substring(0, index), persistingKey, Value, persistedContent.substring(endIndex));
			}
			else
			{
				persistedContent = String.format("%s%s%s", persistedContent, persistingKey, Value);
			}
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(persistedContent.getBytes());
			fos.flush();
			fos.close();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private static String get(Context context, String key)
	{
		try
		{
			String persistingKey = String.format("\n%s:", key);
			String persistedContent = getAll(context);
			if(persistedContent.length() > 0)
			{
				int index = persistedContent.indexOf(persistingKey);
				if(index >= 0)
				{
					index = persistedContent.indexOf(":", index) + 1;
					int endIndex = persistedContent.indexOf("\n", index + 1);
					if (endIndex == -1) endIndex = persistedContent.length(); 
					return persistedContent.substring(index, endIndex);
				}
			}
			
		}
		catch (Exception ex)
		{
			
		}
		return "";
	}
}
