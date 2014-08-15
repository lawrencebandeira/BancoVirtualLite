package br.gastecnologia.bancovirtuallite.service;

import java.util.ArrayList;

public class Transaction 
{
	private String _ID;
	public String getID()
	{
		return _ID;
	}
	public void setID(String value)
	{
		_ID = value;
	}
	
	private String _ActionID;
	public String getActionID()
	{
		return _ActionID;
	}
	public void setActionID(String value)
	{
		_ActionID = value;
	}
	
	private ArrayList<String> _Options;
	public ArrayList<String> getOptions()
	{
		return _Options;
	}
	public void setOptions(ArrayList<String> value)
	{
		_Options = value;
	}
	
	private String _Message;
	public String getMessage()
	{
		return _Message;
	}
	public void setMessage(String value)
	{
		_Message = value;
	}

	public String getType()
	{
		if(_Message.startsWith("Cartão"))
		{
			return "Cartão de crédito";
		}
		else if(_Message.startsWith("Saque"))
		{
			return "Saque Terminal";
		}
		return "Transferência";
	}
	
	public String getValue()
	{
		String Message = getMessage();
		if(Message.indexOf(":") != -1)
		{
			Message = Message.substring(Message.lastIndexOf(":") + 1);
			
			int length = Message.length();
			String newMessage = Message;
			if(newMessage.contains(","))
			{
				length = newMessage.indexOf(",");
			}
			int ct = 0;
			int j = length;
			for(int i = length - 1; i >= 0; i--)
			{
				if(Message.charAt(i) == '$')
				{
					break;
				}
				
				if(ct == 3)
				{
					ct = 0;
					newMessage = newMessage.substring(0, j) + "." + newMessage.substring(j);
					
				}
				ct++;
				j--;
			}
			Message = newMessage;
			
			if (!Message.contains(","))
			{
				Message = Message + ",00";
			}
			return Message;
		}
		return "ERROR 1.1";
	}
	
	public String getAg()
	{
		String Message = getMessage();
		int index = Message.indexOf(":");
		if(index != -1)
		{
			return Message.substring(index + 1, Message.indexOf(" ", index + 1));
		}
		return "";
	}
	
	public String getCC()
	{
		String Message = getMessage();
		int index = Message.indexOf(":");
		if(index != -1)
		{
			index = Message.indexOf(":", index + 1);
			if(index != -1)
			{
				return Message.substring(index + 1, Message.indexOf(" ", index + 1));
			}
		}
		return "";
	}
	
	
	public Transaction(String Id, String ActionId, String message)
	{
		setID(Id);
		setActionID(ActionId);
		setMessage(message);
		setOptions(new ArrayList<String>());
	}
}
