package ekaiser.nzlov.notepad.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ekaiser.nzlov.util.CompressionUtil;
import ekaiser.nzlov.util.MathUtil;
import ekaiser.nzlov.util.NotepadDataUtil;

public class Test {
	public static void main(String[] a ) throws IOException{
			NotepadData data = new NotepadData("Test");
			System.out.println("dateText:"+data.putString("1001", "123"));
			for(int i=0;i<20;i++)
				System.out.println("illegalInfoText:"+data.putString("2012年09月09日 22:07:00", "123"));
			System.out.println("photoInfoText:"+data.putString("Test", "123"));
	        System.out.println("压缩前大小："+data.getAllData("123", false).length);
	        System.out.println("压缩后大小："+data.getAllData("123", true).length);
	        NotepadDataUtil.writeNotepadData(data, "", true);
	}
}
