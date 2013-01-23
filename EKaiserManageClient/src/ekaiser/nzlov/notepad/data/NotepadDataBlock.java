package ekaiser.nzlov.notepad.data;

import java.io.UnsupportedEncodingException;

import ekaiser.nzlov.util.MathUtil;


public class NotepadDataBlock{
		private byte type;
		private byte length = (byte)0x04;
		private byte[] dLength;
		private byte[] data;
		
		public NotepadDataBlock(byte type,byte[] data){
			this.type = type;
			this.data = data;
			sum();
		}
		private void sum(){
			Integer sum = Integer.parseInt(data.length+"", 10);
			dLength= MathUtil.intToByteArray(sum);
		}
		public byte getType() {
			return type;
		}
		public byte getLength() {
			return length;
		}
		public byte[] getdLength() {
			return dLength;
		}
		public byte[] getData() {
			return data;
		}
		public String getDataToString() throws UnsupportedEncodingException{
			return new String(data,"UTF-8");
		}
}
