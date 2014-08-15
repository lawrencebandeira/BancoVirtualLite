package br.gastecnologia.bancovirtuallite.util;

import java.util.ArrayList;

public final class ThemeEventHandler 
{
	private static ArrayList<ThemeEventListener> _listeners;
	public static void addListener(ThemeEventListener listener)
	{
		if(_listeners == null) _listeners = new ArrayList<ThemeEventListener>();
		_listeners.add(listener);
	}
	
	public static void RaiseThemeChanged(int Theme)
	{
		if (_listeners == null) return;
		for(int i = 0; i < _listeners.size(); i++)
		{
			ThemeEventListener item = _listeners.get(i);
			item.OnThemeChangedEvent(Theme);
		}
	}
}
