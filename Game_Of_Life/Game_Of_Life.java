import javax.swing.*; //引用Swing套件
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;


public class Game_Of_Life extends JFrame {
	public static JCheckBox[][] checkbox_matrixs = new JCheckBox[30][20];
	public static JCheckBox[][] next_matrixs = new JCheckBox[30][20];
	public static JRadioButton Glider = new JRadioButton("Glider"),
			Small_Exploder = new JRadioButton("Small Exploder"),
			Exploder = new JRadioButton("Exploder"),
			Cell_Row = new JRadioButton("10 Cell Row"),
			Lightweight_spaceship = new JRadioButton("Lightweight spaceship"),
			Tumbler = new JRadioButton("Tumbler"),
			Clear = new JRadioButton("Clear");
	public static JButton start_stopbtn = new JButton("start");
	public static JButton nextbtn = new JButton("next");
	public static JButton speedbtn = new JButton("speed x1");
	public static int change_speed = 1000;
	public static Boolean start = false;
	
	static MouseAdapter next_ML = new MouseAdapter(){
		public void mousePressed(MouseEvent e){
			produce();
		}
	};
	
	static MouseAdapter start_stop_ML = new MouseAdapter(){
		public void mousePressed(MouseEvent e){
			start = !start;
			if(start){
				start_stopbtn.setText("Stop");
			}else{
				start_stopbtn.setText("Start");	
			}
		}
	};
	
	static ActionListener Glider_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[14][7].setSelected(true);
			checkbox_matrixs[15][8].setSelected(true);
			checkbox_matrixs[13][9].setSelected(true);
			checkbox_matrixs[14][9].setSelected(true);
			checkbox_matrixs[15][9].setSelected(true);
		}
	};
	static ActionListener Small_Exploder_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[14][7].setSelected(true);
			checkbox_matrixs[13][8].setSelected(true);
			checkbox_matrixs[14][8].setSelected(true);
			checkbox_matrixs[15][8].setSelected(true);
			checkbox_matrixs[13][9].setSelected(true);
			checkbox_matrixs[15][9].setSelected(true);
			checkbox_matrixs[14][10].setSelected(true);
		}
	};
	static ActionListener Exploder_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[12][7].setSelected(true);
			checkbox_matrixs[14][7].setSelected(true);
			checkbox_matrixs[16][7].setSelected(true);
			checkbox_matrixs[12][8].setSelected(true);
			checkbox_matrixs[16][8].setSelected(true);
			checkbox_matrixs[12][9].setSelected(true);
			checkbox_matrixs[16][9].setSelected(true);
			checkbox_matrixs[12][10].setSelected(true);
			checkbox_matrixs[16][10].setSelected(true);
			checkbox_matrixs[12][11].setSelected(true);
			checkbox_matrixs[14][11].setSelected(true);
			checkbox_matrixs[16][11].setSelected(true);
		}
	};
	static ActionListener Cell_Row_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[10][9].setSelected(true);
			checkbox_matrixs[11][9].setSelected(true);
			checkbox_matrixs[12][9].setSelected(true);
			checkbox_matrixs[13][9].setSelected(true);
			checkbox_matrixs[14][9].setSelected(true);
			checkbox_matrixs[15][9].setSelected(true);
			checkbox_matrixs[16][9].setSelected(true);
			checkbox_matrixs[17][9].setSelected(true);
			checkbox_matrixs[18][9].setSelected(true);
			checkbox_matrixs[19][9].setSelected(true);
		}
	};
	static ActionListener Lightweight_spaceship_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[13][7].setSelected(true);
			checkbox_matrixs[14][7].setSelected(true);
			checkbox_matrixs[15][7].setSelected(true);
			checkbox_matrixs[16][7].setSelected(true);
			checkbox_matrixs[12][8].setSelected(true);
			checkbox_matrixs[16][8].setSelected(true);
			checkbox_matrixs[16][9].setSelected(true);
			checkbox_matrixs[12][10].setSelected(true);
			checkbox_matrixs[15][10].setSelected(true);
		}
	};
	static ActionListener Tumbler_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
			checkbox_matrixs[12][6].setSelected(true);
			checkbox_matrixs[13][6].setSelected(true);
			checkbox_matrixs[15][6].setSelected(true);
			checkbox_matrixs[16][6].setSelected(true);
			checkbox_matrixs[12][7].setSelected(true);
			checkbox_matrixs[13][7].setSelected(true);
			checkbox_matrixs[15][7].setSelected(true);
			checkbox_matrixs[16][7].setSelected(true);
			checkbox_matrixs[13][8].setSelected(true);
			checkbox_matrixs[15][8].setSelected(true);
			checkbox_matrixs[11][9].setSelected(true);
			checkbox_matrixs[13][9].setSelected(true);
			checkbox_matrixs[15][9].setSelected(true);
			checkbox_matrixs[17][9].setSelected(true);
			checkbox_matrixs[11][10].setSelected(true);
			checkbox_matrixs[13][10].setSelected(true);
			checkbox_matrixs[15][10].setSelected(true);
			checkbox_matrixs[17][10].setSelected(true);
			checkbox_matrixs[11][11].setSelected(true);
			checkbox_matrixs[12][11].setSelected(true);
			checkbox_matrixs[16][11].setSelected(true);
			checkbox_matrixs[17][11].setSelected(true);
			
		}
	};
	static ActionListener Clear_AL = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			clear_matrix();
		}
	};
	
	Thread start_stop_thread = new Thread(){
		public void run(){
			try{
				while(true){
					while (start){
						produce();
						sleep(change_speed);
					}
					sleep(1000);
				}
			}catch(Exception e){}
		}
	};
	
	Game_Of_Life(){
		setcp(getContentPane());
		start_stop_thread.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(830,500);
		setVisible(true);
		setResizable(false);
	}
	
	public static void setcp(Container cp){
		ButtonGroup bG = new ButtonGroup();
		bG.add(Glider);
		bG.add(Small_Exploder);
		bG.add(Exploder);
		bG.add(Cell_Row);
		bG.add(Lightweight_spaceship);
		bG.add(Tumbler);
		bG.add(Clear);
		
		cp.setLayout(null);
		JPanel all_cp = new JPanel(null);
		all_cp.setBounds(0, 0, 830, 500);
		
		JPanel matrix = new JPanel(null);
		matrix.setBounds(0, 0,600,420);
		init_matrix(checkbox_matrixs,matrix,true);
		init_matrix(next_matrixs,matrix,false);
		all_cp.add(matrix);
		
		JPanel action_selection = new JPanel(null);
		action_selection.setBounds(600,0,230,420);
		Glider.setBounds(20,20,230,30);
		action_selection.add(Glider);
		Small_Exploder.setBounds(20,60,210,30);
		action_selection.add(Small_Exploder);
		Exploder.setBounds(20,100,210,30);
		action_selection.add(Exploder);
		Cell_Row.setBounds(20,140,210,30);
		action_selection.add(Cell_Row);
		Lightweight_spaceship.setBounds(20,180,210,30);
		action_selection.add(Lightweight_spaceship);
		Tumbler.setBounds(20,220,210,30);
		action_selection.add(Tumbler);
		Clear.setBounds(20,260,210,30);
		action_selection.add(Clear);
		all_cp.add(action_selection);
		
		Glider.addActionListener(Glider_AL);
		Small_Exploder.addActionListener(Small_Exploder_AL);
		Exploder.addActionListener(Exploder_AL);
		Cell_Row.addActionListener(Cell_Row_AL);
		Lightweight_spaceship.addActionListener(Lightweight_spaceship_AL);
		Tumbler.addActionListener(Tumbler_AL);
		Clear.addActionListener(Clear_AL);
		
		JPanel control = new JPanel(null);
		control.setBounds(0,430 ,850,70);
		nextbtn.setBounds(30,0, 150 ,30);
		control.add(nextbtn);
		start_stopbtn.setBounds(210,0 , 150, 30);
		control.add(start_stopbtn);
		
		final JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(new AbstractAction("x1"){
			public void actionPerformed(ActionEvent e){
				change_speed = 1000;
				speedbtn.setText("speed x1");
			}
		}));
		
		popup.add(new JMenuItem(new AbstractAction("x2"){
			public void actionPerformed(ActionEvent e){
				change_speed = 500;
				speedbtn.setText("speed x2");
			}
		}));
		
		popup.add(new JMenuItem(new AbstractAction("x4"){
			public void actionPerformed(ActionEvent e){
				change_speed = 200;
				speedbtn.setText("speed x4");
			}
		}));
		speedbtn.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				popup.show(e.getComponent(),e.getX(), e.getY());
			}
		});
		speedbtn.setBounds(390,0,150,30);
		control.add(speedbtn);
		
		all_cp.add(control);
		nextbtn.addMouseListener(next_ML);
		start_stopbtn.addMouseListener(start_stop_ML);
		
		cp.add(all_cp);
	}
	
	public static void init_matrix(JCheckBox[][] init,JPanel matrix,Boolean add){
		for(int row = 0; row<30;row++){
			for (int column = 0 ; column <20 ; column++){
				init[row][column] = new JCheckBox();
				init[row][column].setBounds(0+row*20, 1+column*21, 20, 20);
				init[row][column].setIcon(new ImageIcon("/home/maowen/workspace/week6/src/black.png"));
				init[row][column].setSelectedIcon(new ImageIcon("/home/maowen/workspace/week6/src/orange.png"));
				if(add)matrix.add(init[row][column]);
			}
		}
	}
	
	public static void clear_matrix(){
		for(int row = 0; row<30;row++){
			for (int column = 0 ; column <20 ; column++){
				checkbox_matrixs[row][column].setSelected(false);
			}
		}
	}
	
	public static void produce(){
		int row,column;
		for(row=0;row<30;row++){
			for(column=0;column <20;column++){
				condition(row,column);
			}
		}
		copy();
	}
	
	public static void condition(int row,int column){
		int count = 0;
		count= neighbors(row,column);
		if (!checkbox_matrixs[row][column].isSelected() && count==3){
			next_matrixs[row][column].setSelected(true);
		}else if (checkbox_matrixs[row][column].isSelected() && count == 2 || count == 3){
			next_matrixs[row][column].setSelected(true);
		}else{
			next_matrixs[row][column].setSelected(false);
		}
	}
	
	public static int neighbors(int row,int column){
		int[][] dirs = new int[][]{
				{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1}};
		int i,count=0;
		for (i= 0;i<8;i++){
			if(row-dirs[i][0]>= 0 && column-dirs[i][1]>=0 && row-dirs[i][0]<=29 &&column-dirs[i][1]<=19){
				if(checkbox_matrixs[row-dirs[i][0]][column-dirs[i][1]].isSelected()){
					count++;
				}
			}
		}
		return count;
	}

	public static void copy(){
		int row,column;
		for(row=0;row<30;row++){
			for(column =0; column<20;column++){
				checkbox_matrixs[row][column].setSelected(next_matrixs[row][column].isSelected());
			}
		}
	}
	
	public static void main(String args[])throws Exception{
		new Game_Of_Life();
	}
}
