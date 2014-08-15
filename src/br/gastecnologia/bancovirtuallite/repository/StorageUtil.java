package br.gastecnologia.bancovirtuallite.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public final class StorageUtil 
{
	private static String PROTECTOR_PERSISTENCE = "protector_result";
	private static String PROTECTOR_PACKAGE_SCANNED = "protector_package_scanned";
	private static String FILENAME = "protector_version";
	public static void saveLastUpdate(Context Ctx, String Content)
	{
		FileOutputStream fos;
		try 
		{
			fos = Ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(Content.getBytes());
			fos.flush();
			fos.close();
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getLastUpdate(Context Ctx)
	{
		FileInputStream fis;
		try {
			fis = Ctx.openFileInput(FILENAME);
			int length = fis.available();
			if(length > 0)
			{
				byte[] buffer = new byte[length];
				fis.read(buffer);
				fis.close();
				return new String(buffer);
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void addMaliciousApk(Context Ctx, String MaliciousApk)
	{
		String persistence = getProtectorPersistence(Ctx);
		if(!persistence.contains(MaliciousApk))
		{
			persistence = persistence + MaliciousApk + "\n";
			saveProtectorPersistence(Ctx, persistence);
		}
	}
	
	public static void removeMaliciousApk(Context Ctx, String MaliciousApk)
	{
		String persistence = getProtectorPersistence(Ctx);
		if(persistence.contains(MaliciousApk))
		{
			persistence = persistence.replace(MaliciousApk + "\n", "");
			saveProtectorPersistence(Ctx, persistence);
		}
	}
	
	public static String[] getMaliciousApks(Context Ctx)
	{
		String[] retVal = new String[0];
		String persistence = getProtectorPersistence(Ctx);
		if(persistence.length() > 0)
		{
			retVal = persistence.split("\n");
		}
		return retVal;
	}
	
	private static void saveProtectorPersistence(Context Ctx, String Content)
	{
		FileOutputStream fos;
		try 
		{
			fos = Ctx.openFileOutput(PROTECTOR_PERSISTENCE, Context.MODE_PRIVATE);
			fos.write(Content.getBytes());
			fos.flush();
			fos.close();
			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getProtectorPersistence(Context Ctx)
	{
		FileInputStream fis;
		try {
			fis = Ctx.openFileInput(PROTECTOR_PERSISTENCE);
			int length = fis.available();
			if(length > 0)
			{
				byte[] buffer = new byte[length];
				fis.read(buffer);
				fis.close();
				return new String(buffer);
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static void setPackageScannedList(Context Ctx, List<String>packageScannedList){
		try {
			FileOutputStream fos = Ctx.openFileOutput(PROTECTOR_PACKAGE_SCANNED, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(packageScannedList);
			os.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getPackageScannedList(Context Ctx){
		FileInputStream fis;
		ObjectInputStream is;
		List<String> packageScannedList = new ArrayList<String>();
		try {
			fis = Ctx.openFileInput(PROTECTOR_PACKAGE_SCANNED);
			is = new ObjectInputStream(fis);
			packageScannedList = (List<String>) is.readObject();
			if (packageScannedList == null){
				packageScannedList = new ArrayList<String>();
			}
			is.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			packageScannedList = new ArrayList<String>();
		}
		return packageScannedList;
		
	}
}