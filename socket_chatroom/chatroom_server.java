import javax.swing.*; //引用Swing套件
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*; //引用處理事件的event套件

import java.net.*;
import java.io.*;

class CServer_send extends Thread{
	public static int port = 1235;
	static int flag_send=0;
	static Color server_font_color = Color.black;
	static int server_red = 0;
	static int server_green = 0;
	static int server_blue = 0;
	
	MouseAdapter clickBtn = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			JButton source = (JButton)e.getSource();
			if (source.getText() == "Red text"){
				server_red = 1;
			}else if( source.getText() == "Green text"){
				server_green = 1;
			}else if( source.getText() == "Blue text"){
				server_blue = 1;
			}
		}
	};
	
	public void run(){
		try{
			chatroom_server.tfName.addKeyListener(new KeyLis());
			for (int i=0 ; i<3;i++){
				chatroom_server.btn[i].addMouseListener(clickBtn);
			}
			ServerSocket ss = new ServerSocket(port);
			Socket s=ss.accept();
			chatroom_server.doc.insertString(chatroom_server.doc.getLength(),"Clinet connecting for sending successfully!!\n",null);
			OutputStream out=s.getOutputStream();
			String str;
			while(true){
				if(flag_send==1){
					str=chatroom_server.tfName.getText();
					out.write(str.getBytes());
					flag_send=0;
				}else if(server_red == 1){
					out.write("#Red#".getBytes());
					server_font_color = Color.red;
					server_red =0;
				}else if(server_green == 1){
					out.write("#Green#".getBytes());
					server_font_color = Color.green;
					server_green =0;
				}else if(server_blue == 1){
					out.write("#Blue#".getBytes());
					server_font_color = Color.blue;
					server_blue =0;
				}
            sleep((int)(100*Math.random())); 
         }
         //in.close();
         //out.close();
         //s.close();
      }
      catch(Exception e){
         System.out.println("Error:"+e);
      }
   }
   static class KeyLis extends KeyAdapter{
      public void keyPressed(KeyEvent e){
         if(e.getKeyCode()==KeyEvent.VK_ENTER){ 
            flag_send=1;
         }
      }
      public void keyReleased(KeyEvent e){  
         if(e.getKeyCode()==KeyEvent.VK_ENTER){
        	 try{
        		 SimpleAttributeSet server_Attr = new SimpleAttributeSet();
        		 StyleConstants.setForeground(server_Attr, server_font_color);
        		 (chatroom_server.doc).insertString(chatroom_server.doc.getLength(), "Server: "+chatroom_server.tfName.getText()+"\n",server_Attr);
        	 }catch(BadLocationException e1) {}    
        	 chatroom_server.tfName.setText("\r");
         } 
      }  
   }
}

class CServer_Recv extends Thread{
	public static int port = 1234;
	static Color client_font_color = Color.black;
   public void run(){
      byte buff[] = new byte[1024];
      try{
         ServerSocket svs = new ServerSocket(port);
         Socket s=svs.accept();
         chatroom_server.doc.insertString(chatroom_server.doc.getLength(), "Clinet connecting for receiving successfully!!\n",null);
         InputStream in=s.getInputStream();
         SimpleAttributeSet client_Attr = new SimpleAttributeSet();
         int n;
         while(true){
            n=in.read(buff);
            if (new String(buff,0,n).equals("#Red#")){
            	client_font_color = Color.red;
            }else if (new String(buff,0,n).equals("#Green#")){
            	client_font_color = Color.green;
            }else if (new String(buff,0,n).equals("#Blue#")){
            	client_font_color = Color.blue;
            }else{
            	StyleConstants.setForeground(client_Attr, client_font_color);
            	chatroom_server.doc.insertString(chatroom_server.doc.getLength(),"Client: "+new String(buff,0,n)+"\n",client_Attr);
            }
            sleep((int)(100*Math.random())); 
         }
         //in.close();
         //s.close();
      }
      catch(Exception e){
         System.out.println("Error:"+e);
      }
   }
}

public class chatroom_server extends JFrame {
	static CServer_send ss=new CServer_send();
	static CServer_Recv sr=new CServer_Recv();
	static JButton[] btn  = new JButton[4]; //建立元件
	static JTextField tfName = new JTextField(20);
	JLabel lbEnter = new JLabel();
	static JTextPane textpane= new JTextPane();
	JScrollPane textScrollPane = new JScrollPane(textpane);
	DefaultCaret caret = (DefaultCaret)textpane.getCaret();
	static StyledDocument doc = textpane.getStyledDocument();
	
	ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			btn[3].setEnabled(false);
			textpane.setEditable(false);
            ss.start();
            sr.start();
		}
	};
	
	public chatroom_server(){
		btn[0] = new JButton("Red text");
		btn[1] = new JButton("Green text");
		btn[2] = new JButton("Blue text");
		btn[3] = new JButton("Start");
		btn[0].setBackground(Color.red);
		btn[1].setBackground(Color.green);
		btn[2].setBackground(Color.blue);
		for (int i =0; i<3;i++){
			btn[i].setPreferredSize(new Dimension(110,50));
			btn[i].setFont(new Font("Arial", Font.PLAIN, 15));
		}
		Container cp = getContentPane(); //取得內容面版
		cp.setLayout(new BoxLayout(cp,BoxLayout.PAGE_AXIS));
		
		JPanel colorpanel = new JPanel(new FlowLayout());
		for (int i =0; i<3;i++){
			colorpanel.add(btn[i]);
			
		}
		cp.add(colorpanel);
		
		JPanel inputpanel = new JPanel (new FlowLayout());
		inputpanel.add(btn[3]);
		inputpanel.add(new JLabel("Input :"));
		inputpanel.add(tfName);
		cp.add(inputpanel);
		
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JPanel messengepanel = new JPanel (new FlowLayout());
		textScrollPane.setPreferredSize(new Dimension(330,100));
		messengepanel.add(textScrollPane);
		cp.add(messengepanel);
		textpane.setEditable(false);
		btn[3].addActionListener(al);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,250);
		setVisible(true);
	}
	
	public static void main(String args[]) throws Exception {
		new chatroom_server(); //產生視窗框架物件
	}
}