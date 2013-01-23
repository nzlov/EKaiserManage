package ekaiser.nzlov.ekaisermanage.frame;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ekaiser.nzlov.ekaisermanage.main.ClientMain;
import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.notepad.data.NotepadData;

public class MainFrame extends JFrame{
	private JList<String> l_msg = null;
	private JTextField t_info = null;
	private JButton b_send = null;
	private JScrollPane jsp = null;
	
	private ClientMain clientMain = null;
	
	private Vector<String> listData = null;
	
	public MainFrame(ClientMain clientmain){
		this.clientMain = clientmain;
		
		this.listData = new Vector<String>();
		
		init();
		regeListener();
		
		EMethodMapManage.addMethodMap("showMessage", this, "showMessage");
	}
	
	private void init(){
		setLayout(new BorderLayout());
		
		l_msg = new JList<String>();
		jsp = new JScrollPane(l_msg);
		
		JPanel south = new JPanel();
		t_info = new JTextField();
		b_send = new JButton("Send");
		south.setLayout(new BorderLayout());
		south.add(t_info,BorderLayout.CENTER);
		south.add(b_send,BorderLayout.EAST);

		this.add(jsp,BorderLayout.CENTER);
		this.add(south,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 640, 480);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		
		this.setVisible(true);
	}
	private void regeListener(){
		this.b_send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str = t_info.getText();
				if(!str.trim().equals("")){
					String[] ss = str.split("@@");
					NotepadData data = new NotepadData(ss[0]);
					for(int i=1;i<ss.length;i++){
						data.putString(ss[i], "123");
					}
					clientMain.send(data);
				}
			}
		});
		
		this.t_info.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String str = t_info.getText();
					if(!str.trim().equals("")){
						String[] ss = str.split("@@");
						NotepadData data = new NotepadData(ss[0]);
						for(int i=1;i<ss.length;i++){
							data.putString(ss[i], "123");
						}
						clientMain.send(data);
					}
				}
			}
		});
		
		l_msg.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(l_msg.getSelectedIndex()>-1){
					String str = l_msg.getSelectedValue();
					JOptionPane.showMessageDialog(MainFrame.this, str);
				}
			}
		});
		
	}
	
	public void showMessage(EMethodMessage msg){
		NotepadData data = (NotepadData)msg.getParameter();
		
		String name = data.getName();
		
		for(int i = 0;i<data.getDataBlockSize();i++){
			try {
				name = name +"\r\n" + data.getDataBlock(i, "123").getDataToString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		listData.add(name);
		l_msg.setListData(listData);
		
		int lastIndex = listData.size();

		Rectangle rect=l_msg.getCellBounds(lastIndex-1,lastIndex-1);

		jsp.getViewport().scrollRectToVisible(rect);
	}
}
