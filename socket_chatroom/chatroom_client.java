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

class CClient_send extends Thread{
	public static int flag_send=0;
	static Color client_font_color = Color.black;
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
			chatroom_client.tfName.addKeyListener(new KeyLis());
			for (int i=0 ; i<3;i++){
				chatroom_client.btn[i].addMouseListener(clickBtn);
			}
			Socket s = new Socket("127.0.0.1",1234);
			chatroom_client.doc.insertString(chatroom_client.doc.getLength(), "Connected with server for sending successfully!!\n", null);
			OutputStream out=s.getOutputStream();
			String str;
			while(true){
				if(flag_send==1){
					str=chatroom_client.tfName.getText();
					out.write(str.getBytes());
					flag_send = 0;
				}else if(server_red == 1){
					out.write("#Red#".getBytes());
					client_font_color = Color.red;
					server_red =0;
				}else if(server_green == 1){
					out.write("#Green#".getBytes());
					client_font_color = Color.green;
					server_green =0;
				}else if(server_blue == 1){
					out.write("#Blue#".getBytes());
					client_font_color = Color.blue;
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
        	try {
        		SimpleAttributeSet client_Attr = new SimpleAttributeSet();
    	        StyleConstants.setForeground(client_Attr, client_font_color);
				(chatroom_client.doc).insertString(chatroom_client.doc.getLength(), "Client: "+chatroom_client.tfName.getText()+"\n", client_Attr);
			} catch (BadLocationException e1) {}		
            chatroom_client.tfName.setText("\r");
         } 
      }     
   }
}

class CClient_Recv extends Thread{
	static Color server_font_color = Color.black;
   public void run(){
      byte buff[]= new byte[1024];
      try{
         Socket s=new Socket("127.0.0.1",1235);
         (chatroom_client.doc).insertString(chatroom_client.doc.getLength(), "Connected with server for receiving successfully!!\n", null);
         InputStream in=s.getInputStream();
         int n;
         SimpleAttributeSet server_Attr = new SimpleAttributeSet();
         while(true){
            n=in.read(buff);
            if (new String(buff,0,n).equals("#Red#")){
            	server_font_color = Color.red;
            }else if (new String(buff,0,n).equals("#Green#")){
            	server_font_color = Color.green;
            }else if (new String(buff,0,n).equals("#Blue#")){
            	server_font_color = Color.blue;
            }else{
            	StyleConstants.setForeground(server_Attr, server_font_color);
            	(chatroom_client.doc).insertString(chatroom_client.doc.getLength(), "Server: "+new String(buff,0,n)+"\n",server_Attr);
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


public class chatroom_client extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static CClient_Recv cr=new CClient_Recv();
	static CClient_send cs=new CClient_send();
	static JButton[] btn  = new JButton[4]; //建立元件
	static JTextField tfName = new JTextField(20);
	JLabel lbEnter = new JLabel();
	static JTextPane textpane= new JTextPane();
	JScrollPane textScrollPane = new JScrollPane(textpane);
	DefaultCaret caret = (DefaultCaret)textpane.getCaret();
	static StyledDocument doc = textpane.getStyledDocument();

	ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			textpane.setEditable(false);
			btn[3].setEnabled(false);
			cs.start();
			cr.start();
		}
	};
	
	public chatroom_client() throws Exception{
		btn[0] = new JButton("Red text");
		btn[1] = new JButton("Green text");
		btn[2] = new JButton("Blue text");
		btn[3] = new JButton("connect");
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
		new chatroom_client(); //產生視窗框架物件
	}
}