package fivaChess;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainUI extends JFrame{
	  private StartChessJFrame f=new StartChessJFrame();
	  Image image=Toolkit.getDefaultToolkit().createImage("3.jpg");
	  public MainUI(){
		  this.setSize(200, 250); 
		  setIconImage(image);  
		  Container container=getContentPane();
		  mainPanel panel2=new mainPanel();
		  panel2.setSize(200,250);
		  container.add(panel2);
		 
	  }
	  public class mainPanel extends JPanel implements MouseListener{
		  private int OEDGEDISTANCE=50;
		  private int VEDGEDISTANCE=50;
		  private int INTERVAL=20;
		  private int width=80;
		  private int height=50;
		  public mainPanel(){
			  addMouseListener(this);
			  repaint();		  
		  }
		  public void paintComponent(Graphics g){
			  super.paintComponent(g);
			  g.drawImage(image, 0,0, 200,250,null);
			  g.setColor(Color.DARK_GRAY);
			  g.fillRoundRect(OEDGEDISTANCE, VEDGEDISTANCE, width, height, 20, 15); //绘制圆角矩形
			  g.fillRoundRect(OEDGEDISTANCE, VEDGEDISTANCE+height+INTERVAL, width, height,20,15);
			  g.setColor(Color.ORANGE);
			  g.drawString("人机对战",OEDGEDISTANCE+15,VEDGEDISTANCE+height/2+5);
			  g.drawString("人人对战",OEDGEDISTANCE+15,VEDGEDISTANCE+height+INTERVAL+height/2+5);
		  }
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			int x=e.getX();
			int y=e.getY();
			if(x>OEDGEDISTANCE&&x<OEDGEDISTANCE+width&&y>VEDGEDISTANCE&&y<VEDGEDISTANCE+height){//按钮在人机按钮范围内，选则人机模式
				f.setVisible(true);
				f.getChessBoard().isptp=false;
			}
			if(x>OEDGEDISTANCE&&x<OEDGEDISTANCE+width&&y>VEDGEDISTANCE+height+INTERVAL&&y<VEDGEDISTANCE+2*height+INTERVAL){
				f.setVisible(true);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainUI mainUI=new MainUI();
		mainUI.setTitle("五子棋游戏");
		mainUI.setVisible(true);
		mainUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainUI.setLocation(600, 400);
	}

}
