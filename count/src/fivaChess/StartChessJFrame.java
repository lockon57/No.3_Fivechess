package fivaChess;

 
import java.awt.event.*;
import java.io.IOException;
import java.awt.*;  
  
import javax.swing.*;  
/* 
 五子棋主框架类，程序启动类 
 */  


public class StartChessJFrame extends JFrame {  
  private ChessBoard chessBoard;  
  private JPanel toolbar;  
  private JButton startButton,backButton,exitButton,outButton;  
  Image image=Toolkit.getDefaultToolkit().createImage("3.jpg"); 
  private JMenuBar menuBar;  
  private JMenu sysMenu;  
  private JMenuItem startMenuItem,exitMenuItem,backMenuItem,ptpModeMenuItem,ptcMenuItem,viewMenuItem;  
  //重新开始，退出，和悔棋菜单项  
  public StartChessJFrame(){  
      setTitle("单机版五子棋");//设置标题  
      chessBoard=new ChessBoard();  
      
      setIconImage(image); 
      Container contentPane=getContentPane();  
      contentPane.add(chessBoard);  
      chessBoard.setOpaque(true);  
        
        
      //创建和添加菜单  
      menuBar =new JMenuBar();//初始化菜单栏  
      sysMenu=new JMenu("选项");//初始化菜单  
      //初始化菜单项  
      startMenuItem=new JMenuItem("重新开始");  
      exitMenuItem =new JMenuItem("退出");  
      backMenuItem =new JMenuItem("悔棋");  
      ptpModeMenuItem=new JMenuItem("人人对战");
      ptcMenuItem=new JMenuItem("人机对战");
      viewMenuItem=new JMenuItem("保存棋谱");
      //将三个菜单项添加到菜单上  
      sysMenu.add(startMenuItem);  
      sysMenu.add(exitMenuItem);  
      sysMenu.add(backMenuItem);
      sysMenu.add(ptpModeMenuItem);
      sysMenu.add(ptcMenuItem);
      sysMenu.add(viewMenuItem);
      //初始化按钮事件监听器内部类  
      MyItemListener lis=new MyItemListener();  
      //将三个菜单注册到事件监听器上  
      this.startMenuItem.addActionListener(lis);  
      backMenuItem.addActionListener(lis);  
      exitMenuItem.addActionListener(lis); 
      ptpModeMenuItem.addActionListener(lis);
      ptcMenuItem.addActionListener(lis);
      viewMenuItem.addActionListener(lis);
      menuBar.add(sysMenu);//将系统菜单添加到菜单栏上  
      setJMenuBar(menuBar);//将menuBar设置为菜单栏  
        
      toolbar=new JPanel();//工具面板实例化  
      //三个按钮初始化  
      startButton=new JButton("重新开始");  
      exitButton=new JButton("退出");  
      backButton=new JButton("悔棋");
      outButton=new JButton("导出棋谱");  
      //将工具面板按钮用FlowLayout布局  
      toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));  
      //将三个按钮添加到工具面板  
      toolbar.add(startButton);  
      toolbar.add(exitButton);  
      toolbar.add(backButton);
      toolbar.add(outButton);  
      //将三个按钮注册监听事件  
      startButton.addActionListener(lis);  
      exitButton.addActionListener(lis);  
      backButton.addActionListener(lis);
      outButton.addActionListener(lis);  
      //将工具面板布局到界面”南方“也就是下方  
      add(toolbar,BorderLayout.SOUTH);  
      add(chessBoard);//将面板对象添加到窗体上  
      //设置界面关闭事件  
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
      //setSize(800,800);  
      pack();//自适应大小  
        
  } 
 
    
  private class MyItemListener implements ActionListener{  
      public void actionPerformed(ActionEvent e){  
          Object obj=e.getSource();//获得事件源  
          if(obj==StartChessJFrame.this.startMenuItem||obj==startButton){  
              //重新开始  
              //JFiveFrame.this内部类引用外部类  
              //System.out.println("重新开始");  
              chessBoard.restartGame(); 
              if(!chessBoard.isptp){
            	  chessBoard.isptp=false;
              }
          }  
          else if (obj==exitMenuItem||obj==exitButton)  
              System.exit(0);  
          else if (obj==backMenuItem||obj==backButton){  
              //System.out.println("悔棋...");  
              chessBoard.goback();  
          }  
          else if(obj==ptpModeMenuItem){
        	  chessBoard.restartGame();
          }
          else if(obj==viewMenuItem){
        	  chessBoard.Save();
          }
          else if (obj==outButton){   
              try {
				chessBoard.out();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
          } 
          else if(obj==ptcMenuItem){
        	  chessBoard.restartGame(); 
        	  chessBoard.isptp=false;
        	  
          }
      }  
  }   
  public ChessBoard getChessBoard(){
	  return chessBoard;
  }
}  
