package ekaiser.nzlov.notepad.data;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ekaiser.nzlov.util.CompressionUtil;
import ekaiser.nzlov.util.MD5;
import ekaiser.nzlov.util.MathUtil;

public class NotepadData  implements Externalizable{
	private static final long serialVersionUID = 201205301714L;
	public static final byte TYPE_Image = (byte)0x01;
	public static final byte TYPE_String = (byte)0x02;

	private String name = "";
	private String pwd = "202cb962ac59075b964b07152d234b70";
	private int dLength = 0;
	
	private ArrayList<NotepadDataBlock> infoMap;

	public NotepadData(){
		infoMap = new ArrayList<NotepadDataBlock>();
	}
	
	public NotepadData(String name){
		this();
		this.name = name;
	}
	
	public NotepadData(byte[] data,boolean isUn) throws IOException{
		this();
		setData(data,isUn);
	}
	/**
	 * 设置所有数据为data
	 * @param data
	 * @throws IOException 
	 */
	public void setData(byte[] data,boolean isUn) throws IOException{
		if(isUn)
			data = CompressionUtil.unZLib(data);
		if(data[0]==(byte)0x89 && data[1]==(byte)0x29){//判断文件头
			//读取名字
			byte nl = data[2];
			byte[] na = new byte[nl & 0xff];
			System.arraycopy(data, 3, na, 0, na.length);
			try {
				name = new String(na, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//读取密码
			int i = 3+nl & 0xff;
			na = new byte[16];
			System.arraycopy(data, i, na, 0, 16);
			pwd = "";
			for(int a = 0;a<16;a++){
				pwd = pwd +MathUtil.toHex(na[a]);
			}
			//pwd = "202cb962ac59075b964b07152d234b70";
			i = i+16;
			
			int max = data.length;
			byte type = 0;
			byte length = 0;
			byte[] dlength;
			byte[] d;
			while(i<max){
				type = data[i];
				length = data[i+1];
				dlength = new byte[length & 0xff];
				System.arraycopy(data, i+2, dlength, 0, (int)length);
				d = new byte[MathUtil.byteArrayToInt(dlength, 0)];
				System.arraycopy(data, i+2+(int)length, d, 0, d.length);
				putData(type, d);
				i = i+2+(int)length+d.length;
			}
		}
	}
	/**
	 * 压入byte[]数据 
	 * @param type 0x01图像 0x02文字
	 * @param data 数据内容
	 */
	public boolean putData(byte type,byte[] data,String pwd){
		if(isPwd(pwd)){
//			infoMap.put(infoMap.size()+"", new NotepadDataBlock(type, data));
			infoMap.add( new NotepadDataBlock(type, data));
			return true;
		}
		return false;
	}
	/**
	 * 压入byte[]数据 
	 * @param type 0x01图像 0x02文字
	 * @param data 数据内容
	 */
	private void putData(byte type,byte[] data){
		infoMap.add( new NotepadDataBlock(type, data));
//		infoMap.put(infoMap.size()+"", new NotepadDataBlock(type, data));
	}
	/**
	 * 压入字符串
	 * @param str
	 */
	public boolean putString(String str,String pwd){
		try {
			return putData(TYPE_String, str.getBytes("UTF-8"),pwd);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 压入byte[]数据
	 * @param index 压入位置
	 * @param type 数据类型
	 * @param data 数据内容
	 * @param pwd 密码
	 * @return
	 */
	public boolean putData(int index,byte type,byte[] data,String pwd){
		if(isPwd(pwd)){
			infoMap.set(index, new NotepadDataBlock(type, data));
			return true;
		}
		return false;
	}
	/**
	 * 删除数据
	 * @param index 数据索引
	 * @param pwd 密码
	 * @return
	 */
	public boolean remove(int index,String pwd){
		if(isPwd(pwd)){
			infoMap.remove(index);
			return true;
		}
		return false;
	}
	/**
	 * 获得所有数据
	 * @return {@link Byte} All datas;
	 * @throws IOException 
	 */
	public byte[] getAllData(String pwd,boolean isCom) throws IOException{
		if(!isPwd(pwd))
			return null;
		return getAllData(isCom);
	}
	private byte[] getAllData(boolean isCom) throws IOException{
		byte[] datas = new byte[sumAllLength()];
		
		//文件头
		int i = 0;
		datas[0] = (byte)0x89;datas[1] = (byte)0x29;
		datas[2] = Byte.parseByte(name.getBytes().length+"", 10);
//		System.out.println("datas2:"+MathUtil.toHex(datas[2]));
		i = 3;
		System.arraycopy(name.getBytes(), 0,datas, i, name.getBytes().length);
		i = i+name.getBytes().length;
		System.arraycopy(getPwd(), 0,datas, i, 16);
		i =i+ 16;
		for(int a=0;a<infoMap.size();a++){
			i = i+setDataToArray(datas, i, a);
		}
		
//		System.out.println("数据总长度为："+sumAllLength());
//		System.out.println("数据写入长度为："+i);
//		for(int a=0;a<datas.length/13;a++){
//			for(int b=0;b<13;b++)
//				System.out.print("\t"+MathUtil.toHex(datas[a*13+b]));
//			System.out.println("");
//		}
//		if(datas.length%13>0){
//			for(int a = datas.length-datas.length%13;a<datas.length;a++){
//				System.out.print("\t"+MathUtil.toHex(datas[a]));
//			}
//			System.out.println("");
//		}
		if(isCom)
			return  CompressionUtil.zLib(datas);
		return datas;
	}
	/**
	 * 压入数据到总数据数组
	 * @param src
	 * @param srcPos
	 * @param i
	 */
	private int setDataToArray(byte[] src,int srcPos,int i){
		NotepadDataBlock d = infoMap.get(i);
		src[srcPos] = d.getType();
		src[srcPos+1] = d.getLength();
		System.arraycopy(d.getdLength(), 0, src, srcPos+2, d.getdLength().length);
		System.arraycopy(d.getData(), 0, src, srcPos+2+d.getdLength().length, d.getData().length);
		return 2+d.getdLength().length+d.getData().length;
	}	
	/**
	 * 计算数据总长度
	 * @return
	 */
	private int sumAllLength(){
		int i = 19+ name.getBytes().length;
		NotepadDataBlock d = null;
//		for(String s:infoMap.keySet()){
//			d = infoMap.get(s);
//			i = i + 1+1+d.getdLength().length+d.getData().length;
//		}
		for(int j=0;j<infoMap.size();j++){
			d = infoMap.get(j);
			i = i + 1+1+d.getdLength().length+d.getData().length;
		}
		dLength = i;
		return i;
	}

	/**
	 *  获得数据块
	 * @param i
	 * @return
	 */
	public NotepadDataBlock getDataBlock(int i,String pwd){
		if(isPwd(pwd))
			return infoMap.get(i);
		return null;
	}
	private byte[] getPwd(){
		byte[] pwd = new byte[16];
		String t;
		for(int i = 0;i<16;i++){
			t = this.pwd.substring(2*i, 2*(i+1));
//			System.out.println("t:"+t);
			
			pwd[i] = MathUtil.hexStringToBytes(t)[0];
//			System.out.println(MathUtil.toHex(pwd[i]));
		}
		return pwd;
	}
	public boolean  isPwd(String pwd) {
	//	System.out.println("原密码："+this.pwd);
		if(this.pwd.equals(MD5.MD5Encode(pwd.trim()))){
	//		System.out.println("判断正确");
			return true;
		}
		//System.out.println("判断失败");
		return false;
	}

	public boolean setPwd(String pwd,String npwd) {
		if(isPwd(pwd.trim())){
			this.pwd = MD5.MD5Encode(npwd);
			return true;
		}
		return false;
	}
	public void clean(){
		infoMap.clear();
	}
	public String getName() {
		return name;
	}
	public int getDataBlockSize(){
		return infoMap.size();
	}
	public boolean setName(String name,String pwd) {
		if(isPwd(pwd)){
			this.name = name;
			return true;
		}
		return false;
	}

	public int getdLength() {
		return dLength;
	}

	public void readExternal(ObjectInput in) throws IOException,
		ClassNotFoundException {
		setData((byte[])in.readObject(),true);	
	}
	public void writeExternal(ObjectOutput out) throws IOException {
		//out.write(getAllData(), 0, dLength);
		out.writeObject(getAllData(true));
	}
}