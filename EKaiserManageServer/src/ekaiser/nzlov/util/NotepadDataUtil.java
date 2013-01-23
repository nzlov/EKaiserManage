package ekaiser.nzlov.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import ekaiser.nzlov.notepad.data.NotepadData;

public class NotepadDataUtil {
	public static NotepadData readNotepadData(String path){
		return readNotepadData(new File(path));
	}
	public static NotepadData readNotepadData(File f){
		NotepadData data = null;	
		if(!f.exists())
			return null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			data = (NotepadData)ois.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ois = null;
			}
		}
		return data;
	}
		
	public static boolean writeNotepadData(NotepadData data,String path,boolean isCover){
		return writeNotepadData(data, new File(path+data.getName()+".end"), isCover);
	}
	public static boolean writeNotepadData(NotepadData data,File f,boolean isCover){
		ObjectOutputStream oos = null;
		try {
			if(f.exists())
				if(isCover)
					f.delete();
				else
					return false;
			oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(data);
			oos.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
			
		}finally{
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				oos = null;
			}
		}
		return true;
	}
}
