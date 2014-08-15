package br.gastecnologia.bancovirtuallite.service;

import java.util.EventObject;

public class ErrorEventArgs extends EventObject
{
	private String _Message;
	public String getMessage()
	{
		return _Message;
	}
	
	public ErrorEventArgs(String ErrorMessage) 
	{
		super(ErrorMessage);
		_Message = ErrorMessage;
	}
}

