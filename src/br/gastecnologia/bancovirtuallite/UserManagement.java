package br.gastecnologia.bancovirtuallite;

import android.content.SharedPreferences;

public class UserManagement 
{
	private static String loggedAgency;
	private static String loggedAccount;
	private static String loggedPassword;
	private static String loggedUser;
	
	private static SharedPreferences settings;

	public static void setSharedPreferences(SharedPreferences Prefs)
	{
		settings = Prefs;
	}
	
	public static String getLoggedUser()
	{
		if(settings != null)
		{
			if (loggedUser == null || loggedUser.equals(""))
			{
				loggedUser = settings.getString("LOGIN_USER", "");
			}
		}
		else
		{
			loggedUser = "";
		}
		return loggedUser;
	}
	
	public static String getLoggedAccount()
	{
		if(settings != null)
		{
			if (loggedAccount == null || loggedAccount.equals(""))
			{
				loggedAccount = settings.getString("LOGIN_ACCOUNT", "");
			}
		}
		else
		{
			loggedAccount = "";
		}
		return loggedAccount;
	}
	
	public static String getLoggedAgency()
	{
		if(settings != null)
		{
			if (loggedAgency == null || loggedAgency.equals(""))
			{
				loggedAgency = settings.getString("LOGIN_AGENCY", "");
			}
		}
		else
		{
			loggedAgency = "";
		}
		return loggedAgency;
	}
	
	
	public static void setLoggedAgency(String value)
	{
		if(!loggedAgency.equals(value))
		{
			loggedAgency = value;
			if(settings != null)
			{
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LOGIN_AGENCY", value);
				editor.commit();
			}
		}
	}

	
	
	public static String getLoggedPassword()
	{
		if(settings != null)
		{
			if (loggedPassword == null || loggedPassword.equals(""))
			{
				loggedPassword = settings.getString("LOGIN_PASSWORD", "");
			}
		}
		else
		{
			loggedPassword = "";
		}
		return loggedPassword;
	}
	
	public static void setLoggedUser(String value)
	{
		if(loggedUser == null || !loggedUser.equals(value))
		{
			loggedUser = value;
			if(settings != null)
			{
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LOGIN_USER", value);
				editor.commit();
			}
		}
	}
	
	
	public static void setLoggedPassword(String value)
	{
		if(!loggedPassword.equals(value))
		{
			loggedPassword = value;
			if(settings != null)
			{
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LOGIN_PASSWORD", value);
				editor.commit();
			}
		}
	}
	
	public static void setLoggedAccount(String value)
	{
		if(!loggedAccount.equals(value))
		{
			loggedAccount = value;
			if(settings != null)
			{
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LOGIN_ACCOUNT", value);
				editor.commit();
			}
		}
	}
}
