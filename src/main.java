import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.swing.SwingConstants;

public class main {
	public static void main(String arg[]) {
		new showMainWin();
	}

}

class showMainWin extends Frame{
	int maxNumber ;
	int top_score ;
	int latticesNum ;
	lattice[][] latticebox = new lattice[4][4];
	Panel panel ;
	Button newGame ;
	Label score_label;
	Label addScore_label ;
	Label name_topScore ;
	Reader reader ; 
	Properties p ;
	Font f_main = new Font("Arial",Font.BOLD,38);
	showMainWin(){
		setBounds(200,200,600,800);
		setVisible(true);
		setTitle("2048_Lxa");
		setLayout(null);
		setBackground(new Color(255,255,224));
		this.setResizable(false);
		Label name_lable = new Label("2048");
		Label score_labelTitle = new Label("SCORE",Label.CENTER);
		Label name_topScoreTitle = new Label("BEST",Label.CENTER);
		Font f_title = new Font("Arial",Font.BOLD,16);
		Font f_score = new Font("Arial",Font.BOLD,32);
		score_labelTitle.setFont(f_title);
		name_topScoreTitle.setFont(f_title);
		score_labelTitle.setForeground(Color.WHITE);
		name_topScoreTitle.setForeground(Color.WHITE);
		score_labelTitle.setBackground(new Color(211,211,211));
		name_topScoreTitle.setBackground(new Color(211,211,211));
		score_labelTitle.setBounds(250,50,150,20);
		name_topScoreTitle.setBounds(425,50,150,20);
		name_lable.setBounds(35,35,200,100);
		name_lable.setForeground(new Color(210,180,140));
		Font f = new Font("Arial",Font.BOLD,78);
		name_lable.setFont(f);
		name_topScore = new Label("",Label.CENTER);
		name_topScore.setBackground(new Color(245,222,179));
		name_topScore.setBounds(425,70,150,45);
		name_topScore.setFont(f_score);
		name_topScore.setForeground(Color.WHITE);
		score_label = new Label("0", Label.CENTER);
		score_label.setBounds(250,70,150,45);
		score_label.setBackground(new Color(245,222,179));
		score_label.setFont(f_score);
		score_label.setForeground(Color.WHITE);
		addScore_label = new Label("",Font.CENTER_BASELINE);
		addScore_label.setForeground(Color.white);
		addScore_label.setBackground(new Color(245,222,179));
		Font f_addScore = new Font("Arial",Font.BOLD,22);
		addScore_label.setFont(f_addScore);
		addScore_label.setBounds(360,70,35,25);
		newGame = new Button();
		this.add(name_lable);
		this.add(addScore_label);
		this.add(score_label);
		this.add(name_topScore);
		this.add(score_labelTitle);
		this.add(name_topScoreTitle);
		name_topScore.setText(showTopScore());
		LaunchMainLabel();
		this.add(panel);
		//每个小格125*125
		for(int a = 0 ;a<4;a++) {
			for(int b=0;b<4;b++) {
				lattice lat = new lattice();
				lat.setLocation(a*10+a*125+10,b*10+b*125+10);
				panel.add(lat);
				lat.loc_x = a+1 ;
				lat.loc_y = b+1 ;
				latticebox[a][b] = lat ;
			}
		}
		launchLattice(2);
		addKeyListener(new keyMove());

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});	
	}
	public void LaunchMainLabel() {
		panel = new Panel();
		panel.setVisible(true);
		panel.setLayout(null);
		panel.setBackground(new Color(238,232,170));
		panel.setBounds(25,155,550,550);
		panel.requestFocus();
	}
	public void launchLattice(int latNum){
		List emptyList = new ArrayList();
		for(int a=0;a<4;a++) {
			for(int b=0;b<4;b++) {
				if(latticebox[a][b].numValue == 0) {
					emptyList.add(latticebox[a][b]);
				}
			}
		}
		Iterator i = emptyList.iterator();
		Collections.shuffle(emptyList); 
		//随机打乱ArrayList
		try {
			if(latNum==1) {
				lattice latChangeNum = (lattice)i.next();
				latChangeNum.numValue = 2 ;
				try {
					latChangeNum.setBackground(Color.WHITE);
					latChangeNum.setForeground(Color.BLACK);
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				latChangeNum.setForeground(Color.WHITE);
				changeLattice(latChangeNum);
			}else if(latNum==2) { 
				//进行第一次初始化
				lattice latChangeNum1 = (lattice)i.next();
				latChangeNum1.numValue = 2 ;
				lattice latChangeNum2 = (lattice)i.next();
				latChangeNum2.numValue = 2 ;
				changeLattice(latChangeNum1);
				changeLattice(latChangeNum2);
			}
		}catch(NoSuchElementException e) {
			cheakDead();
		}
		
	}
	public void changeLattice(lattice lat) {
		int Num = lat.numValue ;
		lat.setAlignment(java.awt.Label.CENTER);
		if(Num==0) {
			lat.setBackground(new Color(255,250,205));
			lat.setText("");
		}else {
			int c_Num = 0 ;
			switch (Num){
			case 2 : c_Num=1;break;
			case 4 : c_Num=2;break;
			case 8 : c_Num=3;break;
			case 16 : c_Num=4;break;
			case 32 : c_Num=5;break;
			case 64 : c_Num=6;break;
			case 128 : c_Num=7;break;
			case 256 : c_Num=8;break;
			case 512 : c_Num=9;break;
			case 1024 : c_Num=10;break;
			case 2048 : c_Num=11;
			}
			int c_a = 235 ;
			int c_b = 230-c_Num*16 ;
			int c_c = 230-c_Num*16 ;
			lat.setBackground(new Color(c_a,c_b,c_c));
			lat.setText(""+Num);
		}
	}
	class lattice extends Label {
		int numValue = 0 ;
		int loc_x ;
		int loc_y ;
		lattice(){
			//这里设置默认lattice
			setSize(125,125);
			this.setAlignment(java.awt.Label.CENTER);
			setBackground(new Color(255,250,205));
			Font f_latNum = new Font("Arial",Font.BOLD,52);
			this.setForeground(Color.white);
			this.setFont(f_latNum);
			this.setText("");
			this.setAlignment(SwingConstants.CENTER);
		}
	}
	class keyMove extends KeyAdapter{
		List w_list = new ArrayList();
		List d_list = new ArrayList();
		List r_list = new ArrayList();
		List l_list = new ArrayList();
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_W) {
				moveUp();
				launchLattice(1);
				latticesNum ++ ;
				if(latticesNum == 16) {
					cheakDead();
				}
			}else if(e.getKeyCode()==KeyEvent.VK_S) {
				moveDown();
				launchLattice(1);
				latticesNum ++ ;
				if(latticesNum == 16) {
					cheakDead();
				}
			}else if(e.getKeyCode()==KeyEvent.VK_A) {
				moveLeft();
				launchLattice(1);
				latticesNum ++ ;
				if(latticesNum == 16) {
					cheakDead();
				}
			}else if(e.getKeyCode()==KeyEvent.VK_D) {
				moveRight();
				launchLattice(1);
				latticesNum ++ ;
				if(latticesNum == 16) {
					cheakDead();
				}
			}
		}
		public void moveUp() {
			for(int a=0;a<4;a++) {
				for(int b=0;b<4;b++) {
					if(latticebox[a][b].numValue!=0) {
						w_list.add(latticebox[a][b]);
					}
				}
			}
			Iterator w_i = w_list.iterator();
			lattice lat_around ;
			while(w_i.hasNext()) {
				lattice w_lat = (lattice)w_i.next();
				while(w_lat.loc_y >= 2) {
					System.out.println(w_lat.loc_x+","+w_lat.loc_y);
//					System.out.println("!!!");
					int x = w_lat.loc_x ;
					int y = w_lat.loc_y ;
//					System.out.println(x+"要移动之前"+y);
					lat_around = latticebox[x-1][y-2];
//					System.out.println(lat_around.loc_x+"上方的"+lat_around.loc_y);
					if(lat_around.numValue==0) {
						int numValue = w_lat.numValue ;
						//向上移动一格
						lattice j ; 
						j = w_lat ;
						j.numValue = 0 ;
						w_lat = lat_around ;
						w_lat.numValue = numValue ;
						changeLattice(j);
						changeLattice(w_lat);
					}else if(lat_around.numValue == w_lat.numValue) {
//							System.out.println(lat_around.loc_x+"*"+lat_around.loc_y);
							int numValue = w_lat.numValue ;
							lattice k ;
							k = w_lat ;
							k.numValue = 0 ;
							w_lat = lat_around ;
							w_lat.numValue = numValue * 2;
							changeLattice(w_lat);
							changeLattice(k);
							addScore(numValue*2);
							cheakWin(w_lat);
							latticesNum -- ;
					}else {
					break ;
					}
				}				
			}		
		}
		public void moveDown() {
			for(int a=0;a<4;a++) {
				for(int b=3;b>=0;b--) {
					if(latticebox[a][b].numValue!=0) {
//						System.out.println(latticebox[a][b].loc_x+"存在数字的"+latticebox[a][b].loc_y);
						d_list.add(latticebox[a][b]);
					}
				}
			}
			Iterator d_i = d_list.iterator();
			lattice lat_around ;
			while(d_i.hasNext()) {
				lattice d_lat = (lattice)d_i.next();
				while(d_lat.loc_y <= 3) {
					System.out.println(d_lat.loc_x+","+d_lat.loc_y);
//					System.out.println("!!!");
					int x = d_lat.loc_x ;
					int y = d_lat.loc_y ;
//					System.out.println(x+"要移动之前"+y);
					lat_around = latticebox[x-1][y];
//					System.out.println(lat_around.loc_x+"上方的"+lat_around.loc_y);
					if(lat_around.numValue==0) {
						int numValue = d_lat.numValue ;
						//向上移动一格
						lattice j ; 
						j = d_lat ;
						j.numValue = 0 ;
						d_lat = lat_around ;
						d_lat.numValue = numValue ;
						changeLattice(j);
						changeLattice(d_lat);
					}else if(lat_around.numValue == d_lat.numValue) {
//							System.out.println(lat_around.loc_x+"*"+lat_around.loc_y);
							int numValue = d_lat.numValue ;
							lattice k ;
							k = d_lat ;
							k.numValue = 0 ;
							d_lat = lat_around ;
							d_lat.numValue = numValue * 2;
							changeLattice(d_lat);
							changeLattice(k);
							addScore(numValue*2);
							cheakWin(d_lat);
							latticesNum -- ;
					}else {
						break ;
					}
				}				
			}		
		}
		public void moveLeft() {
			for(int a=0;a<4;a++) {
				for(int b=0;b<4;b++) {
					if(latticebox[a][b].numValue!=0) {
						l_list.add(latticebox[a][b]);
					}
				}
			}
			Iterator l_i = l_list.iterator();
			lattice lat_around ;
			while(l_i.hasNext()) {
				lattice l_lat = (lattice)l_i.next();
				while(l_lat.loc_x >= 2) {
					int x = l_lat.loc_x ;
					int y = l_lat.loc_y ;
					lat_around = latticebox[x-2][y-1];
					if(lat_around.numValue==0) {
						int numValue = l_lat.numValue ;
						//向上移动一格
						lattice j ; 
						j = l_lat ;
						j.numValue = 0 ;
						l_lat = lat_around ;
						l_lat.numValue = numValue ;
						changeLattice(j);
						changeLattice(l_lat);
					}else if(lat_around.numValue == l_lat.numValue) {
//							System.out.println(lat_around.loc_x+"*"+lat_around.loc_y);
							int numValue = l_lat.numValue ;
							lattice k ;
							k = l_lat ;
							k.numValue = 0 ;
							l_lat = lat_around ;
							l_lat.numValue = numValue * 2;
							changeLattice(l_lat);
							changeLattice(k);
							addScore(numValue*2);
							cheakWin(l_lat);
							latticesNum -- ;
					}else {
						break ;
					}
				}				
			}		
		}
		public void moveRight() {
			for(int a=3;a>=0;a--) {
				for(int b=0;b<4;b++) {
					if(latticebox[a][b].numValue!=0) {
						r_list.add(latticebox[a][b]);
					}
				}
			}
			Iterator r_i = r_list.iterator();
			lattice lat_around ;
			while(r_i.hasNext()) {
				lattice r_lat = (lattice)r_i.next();
				while(r_lat.loc_x <= 3) {
					int x = r_lat.loc_x ;
					int y = r_lat.loc_y ;
					lat_around = latticebox[x][y-1];
					if(lat_around.numValue==0) {
						int numValue = r_lat.numValue ;
						//向上移动一格
						lattice j ; 
						j = r_lat ;
						j.numValue = 0 ;
						r_lat = lat_around ;
						r_lat.numValue = numValue ;
						changeLattice(j);
						changeLattice(r_lat);
					}else if(lat_around.numValue == r_lat.numValue) {
//							System.out.println(lat_around.loc_x+"*"+lat_around.loc_y);
							int numValue = r_lat.numValue ;
							lattice k ;
							k = r_lat ;
							k.numValue = 0 ;
							r_lat = lat_around ;
							r_lat.numValue = numValue * 2;
							changeLattice(r_lat);
							changeLattice(k);
							addScore(numValue*2);
							cheakWin(r_lat);
							latticesNum -- ;
					}else {
						break ;
					}
				}				
			}		
		}
	}
	
	public void scoreShow() {
		score_label.setText(""+maxNumber);
	}
	public void cheakWin(lattice lat) {
		if(lat.numValue == 2048) {
			checkTopScore(Integer.parseInt(score_label.getText()));
			Panel winPanel = new Panel();
			winPanel.setBackground(new Color(245,222,179));
			winPanel.setBounds(0,0,600,800);
			Button b_restart = new Button("PlayAgain");
			Button b_exit = new Button("Exit");
			b_exit.setFont(f_main);
			b_exit.setForeground(new Color(210,180,140));
			b_restart.setForeground(new Color(210,180,140));
			b_restart.setFont(f_main);
			b_restart.setBounds(200,200,200,100);
			b_exit.setBounds(200,400,200,100);
			b_restart.setBackground(new Color(255,255,224));
			b_exit.setBackground(new Color(255,255,224));
			winPanel.add(b_restart);
			winPanel.add(b_exit);
			panel.add(winPanel,1);
			b_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			b_restart.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for(int a=0;a<4;a++) {
						for(int b=0;b<4;b++) {
							lattice lat = latticebox[a][b];
							lat.numValue = 0 ;
							changeLattice(lat);
						}
					}
					launchLattice(2);
					winPanel.setVisible(false);
				}
			});
		}
	}
	public void cheakDead() {
		int aroundNum = 0;
		for(int a=0;a<4;a++) {
			for(int b=0;b<4;b++) {
				if(latticebox[a][b].numValue!=0) {
					lattice midLat = latticebox[a][b] ;
					int midValue = midLat.numValue ;
					try {
					lattice lattice_up = latticebox[a][b-1];
					lattice lattice_down = latticebox[a][b+1];
					lattice lattice_left = latticebox[a+1][b];
					lattice lattice_right = latticebox[a-1][b];
					if(lattice_up.numValue==midValue) {
						aroundNum++;
					}
					if(lattice_down.numValue==midValue) {
						aroundNum++;
					}
					if(lattice_left.numValue==midValue) {
						aroundNum++;
					}
					if(lattice_right.numValue==midValue) {
						aroundNum++;
					}
					}catch(Exception e) {
						//不可避免的。
					}
				}
			}
		}
		if(aroundNum==0) {
			checkTopScore(Integer.parseInt(score_label.getText()));
			//输了
			Panel losePanel = new Panel();
			losePanel.setBackground(new Color(245,222,179));
			losePanel.setBounds(0,0,600,800);
			Button b_restart = new Button("ReTry");
			Button b_exit = new Button("Exit");
			b_exit.setFont(f_main);
			b_exit.setForeground(new Color(210,180,140));
			b_restart.setFont(f_main);
			b_restart.setForeground(new Color(210,180,140));
			b_restart.setBounds(200,200,200,100);
			b_exit.setBounds(200,400,200,100);
			b_restart.setBackground(new Color(255,255,224));
			b_exit.setBackground(new Color(255,255,224));
			losePanel.add(b_restart);
			losePanel.add(b_exit);
			panel.add(losePanel,1);
			b_exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			b_restart.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					for(int a=0;a<4;a++) {
						for(int b=0;b<4;b++) {
							lattice lat = latticebox[a][b];
							lat.numValue = 0 ;
							changeLattice(lat);
						}
					}
					launchLattice(2);
					losePanel.setVisible(false);
				}
			});
		}
	}
	public String showTopScore() {
		p = new Properties();
		reader = null;
		try {
			reader = new FileReader("src//TopScore");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			p.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String score = p.getProperty("TOP");
		top_score = Integer.parseInt(score);
		System.out.println(score);
		return score ;
	}
	public void addScore(int addNum) {
		int nowScore = Integer.parseInt(score_label.getText());
		int afterScore = nowScore + addNum ;
		String add = "+"+addNum ;
		addScore_label.setText(add);
		try {
			Thread.sleep(500);
			addScore_label.setText("");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		score_label.setText(""+afterScore);
	}
	public void checkTopScore(int score) {
		if(top_score<score) {
			name_topScore.setText(""+score);
			String top = ""+score;
			Properties p_in = new Properties();
			OutputStream fos = null ;
			try {
				fos = new FileOutputStream("src//TopScore");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}   
			try {
				p_in.load(new FileInputStream("src//TopScore"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			p_in.setProperty("TOP", top);
			try {
				p_in.store(fos,"updata new top-score");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}	
	}
}
