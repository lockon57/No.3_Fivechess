package fivaChess;

import java.awt.Color;  
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.Image;  
import java.awt.RadialGradientPaint;  
import java.awt.RenderingHints;  
import java.awt.Toolkit;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.MouseMotionListener;  
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;  
/** 
 * 五子棋--棋盘类  
 */  
  
public class ChessBoard extends JPanel implements MouseListener {  
   public static final int MARGIN=30;//边距  
   public static final int GRID_SPAN=35;//网格间距  
   public static final int ROWS=17;//棋盘行数  
   public static final int COLS=17;//棋盘列数  
   boolean isptp=true;//是否为人人模式
   Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始每个数组元素为null  
   boolean isBlack=true;//默认开始是黑棋先  
   boolean gameOver=false;//游戏是否结束  
   int chessCount;//当前棋盘棋子的个数  
   int xIndex,yIndex;//当前刚下棋子的索引  
   int [][]location=new int[17][17];//棋盘中每个位置的状态  1黑子  0空  -1白子
   int [][]score=new int [17][17];//人机状态下存储每个落子点得到的分数
   int [][]review=new int[17][17];//复盘数组
   StringBuffer sb = new StringBuffer(); 
   
  Image img=Toolkit.getDefaultToolkit().createImage("3.jpg");  
  // Image shadows;  
   Color colortemp;  
   public ChessBoard(){  
      
      
      for(int i=0;i<ROWS;i++){
    	  for(int j=0;j<COLS;j++){
    		  location[i][j]=0;
    	  }
      }
       setBackground(Color.PINK);//设置背景色为橘黄色   
      // shadows=Toolkit.getDefaultToolkit().getImage("shadows.jpg");  
       addMouseListener(this);  
       addMouseMotionListener(new MouseMotionListener(){  
           public void mouseDragged(MouseEvent e){  
                 
           }  
             
           public void mouseMoved(MouseEvent e){  
             int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
             //将鼠标点击的坐标位置转成网格索引  
             int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
             //游戏已经结束不能下  
             //落在棋盘外不能下  
             //x，y位置已经有棋子存在，不能下  
             if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))  
                 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
             //设置成默认状态  
             else setCursor(new Cursor(Cursor.HAND_CURSOR));  
               
           }  
       });  
   }   
     
    
  
//绘制  
   public void paintComponent(Graphics g){  
       
       super.paintComponent(g);//画棋盘  
       
       int imgWidth= img.getWidth(this);  
       int imgHeight=img.getHeight(this);//获得图片的宽度与高度  
       int FWidth=getWidth();  
       int FHeight=getHeight();//获得窗口的宽度与高度  
       int x=(FWidth-imgWidth)/2;  
       int y=(FHeight-imgHeight)/2;  
      // g.drawImage(img, 0, 0, null);  
      
         
       for(int i=0;i<ROWS;i++){//画横线  
           g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+(COLS-1)*GRID_SPAN, MARGIN+i*GRID_SPAN);  
       }  
       for(int i=0;i<COLS;i++){//画竖线  
           g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+(ROWS-1)*GRID_SPAN);  
             
       }  
         
       //画棋子  
       for(int i=0;i<chessCount;i++){  
           //网格交叉点x，y坐标  
           int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;  
           int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;  
           g.setColor(chessList[i].getColor());//设置颜色  
          // g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,  
                           //Point.DIAMETER, Point.DIAMETER);  
           //g.drawImage(shadows, xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER, null);  
           colortemp=chessList[i].getColor();  
           if(colortemp==Color.black){  
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 20, new float[]{0f, 1f}  
               , new Color[]{Color.WHITE, Color.BLACK});  
               ((Graphics2D) g).setPaint(paint);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);  
  
           }  
           else if(colortemp==Color.white){  
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 70, new float[]{0f, 1f}  
               , new Color[]{Color.WHITE, Color.BLACK});  
               ((Graphics2D) g).setPaint(paint);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);  
  
           }  
           
           Ellipse2D e = new Ellipse2D.Float(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, 34, 35);  
           ((Graphics2D) g).fill(e);  
           //标记最后一个棋子的红矩形框  
             
           if(i==chessCount-1){//如果是最后一个棋子  
               g.setColor(Color.red);  
               g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,  
                           34, 35);  
           }  
       }  
   }  
     
   public void mousePressed(MouseEvent e){//鼠标在组件上按下时调用  
	 //游戏结束时，不再能下  
       if(gameOver) return;  
         
       String colorName=isBlack?"黑棋":"白棋";  
         
       //将鼠标点击的坐标位置转换成网格索引  
       xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
       yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;  
        
     
       //落在棋盘外不能下  
       if(xIndex<0||xIndex>ROWS+1||yIndex<0||yIndex>COLS+1)  
           return;  
         
       //如果x，y位置已经有棋子存在，不能下  
       if(findChess(xIndex,yIndex))return; 
       
     //设置棋盘上该位置的值
       if(isBlack){
    	   location[xIndex][yIndex]=1;
    	   sb.append("黑子:"+ "X:"+xIndex + " "+"Y:"+yIndex+"\r\n");  
       }else
    	   sb.append("白子:"+ "X:"+xIndex + " "+"Y:"+yIndex+"\r\n");  
         
       //可以进行时的处理  
       Point ch=new Point(xIndex,yIndex,isBlack?Color.black:Color.white);  
       chessList[chessCount++]=ch;  
        repaint();//通知系统重新绘制  
         
        if(isWin()){ 
        	if(isptp){
        		String msg=String.format("恭喜，%s赢了！", colorName);  
                JOptionPane.showMessageDialog(this, msg); 
        	}
        	else{
        		String msg="恭喜，你赢了";
        		JOptionPane.showMessageDialog(this, msg); 		
        	}
            gameOver=true;
            return;
        } 
        isBlack=!isBlack;
        if(!isptp){//如果不是人人模式，则进入AI过程，电脑持白子
        	
     	   int []ccount=new int[4];
     	   int []pcount=new int[4];
     	   boolean []cisDie=new boolean[4];
     	   boolean []pisDie=new boolean[4];
     	   for (int i = 0; i < cisDie.length; i++) {
     		  ccount[i]=0;
     		  pcount[i]=0;
     		  cisDie[i]=false;
     		  pisDie[i]=false;
		   }
     	   for(int i=0;i<ROWS;i++){
     		   for(int j=0;j<COLS;j++){
     			   score[i][j]=0;
     		   }
     	   }
     	   for(int i=0;i<ROWS;i++){
     		   for(int j=0;j<COLS;j++){
     			   if(location[i][j]==0){
     				   
     				   //对电脑
     				   int jtemp=j-1;
     				   for(;jtemp>=0;jtemp--){                //向左查找
     					   if(location[i][jtemp]==-1){//电脑向左边进行查找，如果左边是白子(自己)则数组值加一，如果左边是黑子，也就是敌方，则把布尔数组值设置为true
     						  ccount[0]++;
     					   }
     					   else{
     						   if(location[i][jtemp]==1)
     							  cisDie[0]=true;
     						   break;
     					   }
     				   } 
     				   for(jtemp=j+1;jtemp<COLS;jtemp++){        //向右查找
     					  if(location[i][jtemp]==-1){
     						 ccount[0]++;
    					   }
    					   else{
    						   if(location[i][jtemp]==1)
    							   cisDie[0]=true;
    						   break;
    					   }
     				   }
     				   
     				   
     				   
     				   int itemp=i-1;
     				   for(;itemp>=0;itemp--){       //向上查找
     					   if(location[itemp][j]==-1){
     						  ccount[1]++;
     					   }
     					  else{
    						   if(location[itemp][j]==1)
    							   cisDie[1]=true;
    						   break;
    					   }
     				   }
     				   itemp=i+1;
     				   for(;itemp<ROWS;itemp++){               //向下查找
     					   if(location[itemp][j]==-1){
     						  ccount[1]++;
     					   }
     					   else{
     						  if(location[itemp][j]==1)
     							 cisDie[1]=true;
   						      break;
     					   }
     				   }
     				   
     				   
     				   
     				   itemp=i-1;jtemp=j-1;           //向左上方查找
     				   for(;itemp>=0&&jtemp>=0;itemp--,jtemp--){
     					  if(location[itemp][jtemp]==-1){
     						 ccount[2]++;
    					   }
    					   else{
    						  if(location[itemp][jtemp]==1)
    							  cisDie[2]=true;
  						      break;
    					   }
     				   }
     				  itemp=i+1;jtemp=j+1;           //向右下方查找
    				   for(;itemp<ROWS&&jtemp<COLS;itemp++,jtemp++){
    					  if(location[itemp][jtemp]==-1){
    						  ccount[2]++;
   					      }
   					      else{
   						      if(location[itemp][jtemp]==1)
   						    	cisDie[2]=true;
 						      break;
   					      }
    				   }
    				   
    				   
    				   
    				   itemp=i-1;jtemp=j+1;    //向右上方查找
    				   for (; itemp>=0&&jtemp<COLS; itemp--,jtemp++) {
    					   if(location[itemp][jtemp]==-1){
    						   ccount[3]++;
    					      }
    					      else{
    						      if(location[itemp][jtemp]==1)
    						    	  cisDie[3]=true;
  						      break;
    					      }
					   }
    				   itemp=i+1;jtemp=j-1;      //向左下方查找
    				   for(;itemp<ROWS&&jtemp>=0;itemp++,jtemp--){
    					   if(location[itemp][jtemp]==-1){
    						   ccount[3]++;
 					      }
 					      else{
 						      if(location[itemp][jtemp]==1)
 						    	 cisDie[3]=true;
						      break;
 					      }
    				   }
    				   
    				   
    				   //对玩家
    				   jtemp=j-1;
     				   for(;jtemp>=0;jtemp--){                //向左查找
     					   if(location[i][jtemp]==1){//玩家向左边进行查找，如果左边是黑子(自己)则数组值加一，如果左边是白子，也就是敌方，则把布尔数组值设置为true
     						  pcount[0]++;
     					   }
     					   else{
     						   if(location[i][jtemp]==-1)
     							  pisDie[0]=true;
     						   break;
     					   }
     				   } 
     				   for(jtemp=j+1;jtemp<COLS;jtemp++){        //向右查找
     					  if(location[i][jtemp]==1){
     						 pcount[0]++;
    					   }
    					   else{
    						   if(location[i][jtemp]==-1)
    							   pisDie[0]=true;
    						   break;
    					   }
     				   }
     				   
     				   
     				   
     				   itemp=i-1;
     				   for(;itemp>=0;itemp--){       //向上查找
     					   if(location[itemp][j]==1){
     						  pcount[1]++;
     					   }
     					  else{
    						   if(location[itemp][j]==-1)
    							   pisDie[1]=true;
    						   break;
    					   }
     				   }
     				   itemp=i+1;
     				   for(;itemp<ROWS;itemp++){               //向下查找
     					   if(location[itemp][j]==1){
     						  pcount[1]++;
     					   }
     					   else{
     						  if(location[itemp][j]==-1)
     							 pisDie[1]=true;
   						      break;
     					   }
     				   }
     				   
     				   
     				   
     				   itemp=i-1;jtemp=j-1;           //向左上方查找
     				   for(;itemp>=0&&jtemp>=0;itemp--,jtemp--){
     					  if(location[itemp][jtemp]==1){
     						 pcount[2]++;
    					   }
    					   else{
    						  if(location[itemp][jtemp]==-1)
    							  pisDie[2]=true;
  						      break;
    					   }
     				   }
     				  itemp=i+1;jtemp=j+1;           //向右下方查找
    				   for(;itemp<ROWS&&jtemp<COLS;itemp++,jtemp++){
    					  if(location[itemp][jtemp]==1){
    						  pcount[2]++;
   					      }
   					      else{
   						      if(location[itemp][jtemp]==-1)
   						    	pisDie[2]=true;
 						      break;
   					      }
    				   }
    				   
    				   
    				   
    				   itemp=i-1;jtemp=j+1;    //向右上方查找
    				   for (; itemp>=0&&jtemp<COLS; itemp--,jtemp++) {
    					   if(location[itemp][jtemp]==1){
    						   pcount[3]++;
    					      }
    					      else{
    						      if(location[itemp][jtemp]==-1)
    						    	  pisDie[3]=true;
  						          break;
    					      }
					   }
    				   itemp=i+1;jtemp=j-1;      //向左下方查找
    				   for(;itemp<ROWS&&jtemp>=0;itemp++,jtemp--){
    					   if(location[itemp][jtemp]==1){
    						   pcount[3]++;
 					      }
 					      else{
 						      if(location[itemp][jtemp]==-1)
 						    	 pisDie[3]=true;
						      break;
 					      }
    				   }
    				   
    				   score[i][j]=getScore(ccount,cisDie,pcount,pisDie);
    				   for (int k = 0; k < cisDie.length; k++) {
    			     		  ccount[k]=0;
    			     		  pcount[k]=0;
    			     		  cisDie[k]=false;
    			     		  pisDie[k]=false;
    				 }
     			   }
     		   }
     	   }
     	   
     	   int iindex=0,jindex=0;
     	   for(int i=0;i<ROWS;i++){//获得所有落子处的得分最大值，电脑落白子
     		   for(int j=0;j<COLS;j++){
     			   if(score[i][j]>score[iindex][jindex]){
     				   iindex=i;
     				   jindex=j;
     			   }
     		   }
     		
     	   }
     
           location[iindex][jindex]=-1; 
           sb.append("白子:"+ "X:"+iindex + " "+"Y:"+jindex+"\r\n");  
         //  System.out.println(""+iindex+" "+jindex);

          // System.out.println(score[iindex][yIndex]);
     	  ch=new Point(iindex,jindex,Color.white);  
          chessList[chessCount++]=ch;  
           repaint();//通知系统重新绘制  
           xIndex=iindex;
           yIndex=jindex;
           colorName=isBlack?"黑棋":"白棋"; 
           
         //如果胜出则给出提示信息，不能继续下棋  
           if(isWin()){ 
        	   
               String msg="你输了，继续努力吧！";  
               JOptionPane.showMessageDialog(this, msg);  
               gameOver=true; 
               return;
           } 
           isBlack=!isBlack;
     	   
        }
         
       
         
        
       
    }  
   //覆盖mouseListener的方法  
   public void mouseClicked(MouseEvent e){  
       //鼠标按键在组件上单击时调用  
   }  
     
   public void mouseEntered(MouseEvent e){  
       //鼠标进入到组件上时调用  
   }  
   public void mouseExited(MouseEvent e){  
       //鼠标离开组件时调用  
   }  
   public void mouseReleased(MouseEvent e){  
       //鼠标按钮在组件上释放时调用  
   }  
   //在棋子数组中查找是否有索引为x，y的棋子存在  
   private boolean findChess(int x,int y){  
       for(Point c:chessList){  
           if(c!=null&&c.getX()==x&&c.getY()==y)  
               return true;  
       }  
       return false;  
   }  
     
   public int getScore(int []ccount,boolean []cisDie,int []pcount,boolean []pisDie){
	   int cmaxIndex1=0;//定义四个中间变量
	   int cmaxIndex2=0;
	   int pmaxIndex1=0;
	   int pmaxIndex2=0;
	   int score=0;
	   for(int i=0;i<ccount.length;i++){//求出最有优势的方向
		   if(ccount[cmaxIndex1]<ccount[i]){
			   cmaxIndex1=i;
		   }
	   }
	   for(int i=0;i<ccount.length;i++){//求出第二有优势的方向
		   if(ccount[cmaxIndex2]<ccount[i]&&cmaxIndex2!=cmaxIndex1){
			   cmaxIndex2=i;
		   }
	   }
	   for(int i=0;i<pcount.length;i++){
		   if(pcount[pmaxIndex1]<pcount[i]){
			   pmaxIndex1=i;
		   }
	   }
	   for(int i=0;i<pcount.length;i++){
		   if(pcount[pmaxIndex2]<pcount[i]&&pmaxIndex2!=pmaxIndex1){
			   pmaxIndex2=i;
		   }
	   }
	   //进攻权值
	   if(ccount[cmaxIndex1]==4)
		   score=100000;
	   else if(ccount[cmaxIndex1]==3&&cisDie[cmaxIndex1]==false){
		   score=8000;
	   }
	   else if(ccount[cmaxIndex1]==3&&cisDie[cmaxIndex1]==true&&ccount[cmaxIndex2]==3&&cisDie[cmaxIndex2]==true)
		   score=8000;
	   else if(ccount[cmaxIndex1]==3&&cisDie[cmaxIndex1]==true&&ccount[cmaxIndex2]==2&&cisDie[cmaxIndex2]==false)
		   score=8000;
	   else if(ccount[cmaxIndex1]==2&&cisDie[cmaxIndex1]==false&&ccount[cmaxIndex2]==2&&cisDie[cmaxIndex2]==false)
		   score=5000;
	   else if(ccount[cmaxIndex1]==2&&cisDie[cmaxIndex1]==true&&ccount[cmaxIndex2]==2&&cisDie[cmaxIndex2]==false||
			   ccount[cmaxIndex1]==2&&cisDie[cmaxIndex1]==false&&ccount[cmaxIndex2]==2&&cisDie[cmaxIndex2]==true )
		   score=1000;
	   else if(ccount[cmaxIndex1]==3&&cisDie[cmaxIndex1]==true){
		   score=500;
	   }
	   else if(ccount[cmaxIndex1]==2&&cisDie[cmaxIndex1]==false){
		   score=200;
	   }
	   else if(ccount[cmaxIndex1]==1&&cisDie[cmaxIndex1]==false&&ccount[cmaxIndex2]==1&&cisDie[cmaxIndex2]==false)
		   score=100;
	   else if(ccount[cmaxIndex1]==2&&cisDie[cmaxIndex1]==true)
		   score=50;
	   else if(ccount[cmaxIndex1]==1&&cisDie[cmaxIndex1]==true&&ccount[cmaxIndex2]==1&&cisDie[cmaxIndex2]==true)
		   score=10;
	   else if(ccount[cmaxIndex1]==1&&cisDie[cmaxIndex1]==false){
		   score=5;
	   }
	   else if(ccount[cmaxIndex1]==1&&cisDie[cmaxIndex1]==true){
		   score=3;
	   }
	   else {
		score=0;
	   }
	   
	   //防守权值
	   
	   if(pcount[pmaxIndex1]==4)
		   score+=90000;
	   else if(pcount[pmaxIndex1]==3&&pisDie[pmaxIndex1]==false){
		   score+=7000;
	   }
	   else if(pcount[pmaxIndex1]==3&&pisDie[pmaxIndex1]==true&&pcount[pmaxIndex2]==3&&pisDie[pmaxIndex2]==true)
		   score+=7000;
	   else if(pcount[pmaxIndex1]==3&&pisDie[pmaxIndex1]==true&&pcount[pmaxIndex2]==2&&pisDie[pmaxIndex2]==false)
		   score+=7000;
	   else if(pcount[pmaxIndex1]==2&&pisDie[pmaxIndex1]==false&&pcount[pmaxIndex2]==2&&pisDie[pmaxIndex2]==false)
		   score+=4000;
	   else if(pcount[pmaxIndex1]==2&&pisDie[pmaxIndex1]==true&&pcount[pmaxIndex2]==2&&pisDie[pmaxIndex2]==false||
			   pcount[pmaxIndex1]==2&&pisDie[pmaxIndex1]==false&&pcount[pmaxIndex2]==2&&pisDie[pmaxIndex2]==true )
		   score+=800;
	   else if(pcount[pmaxIndex1]==3&&pisDie[pmaxIndex1]==true){
		   score+=400;
	   }
	   else if(pcount[pmaxIndex1]==2&&pisDie[pmaxIndex1]==false){
		   score+=150;
	   }
	   else if(pcount[pmaxIndex1]==1&&pisDie[pmaxIndex1]==false&&pcount[pmaxIndex2]==1&&pisDie[pmaxIndex2]==false)
		   score+=80;
	   else if(pcount[pmaxIndex1]==2&&pisDie[pmaxIndex1]==true)
		   score+=40;
	   else if(pcount[pmaxIndex1]==1&&pisDie[pmaxIndex1]==true&&pcount[pmaxIndex2]==1&&pisDie[pmaxIndex2]==true)
		   score+=8;
	   else if(pcount[pmaxIndex1]==1&&pisDie[pmaxIndex1]==false){
		   score+=4;
	   }
	   else if(pcount[pmaxIndex1]==1&&pisDie[pmaxIndex1]==true){
		   score+=2;
	   }
	   else {
		score+=0;
	   }
	   return score;
	   
   }
   private boolean isWin(){  
       int continueCount=1;//连续棋子的个数  
        
       //横向向西寻找  
       for(int x=xIndex-1;x>=0;x--){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,yIndex,c)!=null){  
               continueCount++;  
           }else  
               break;  
       }  
      //横向向东寻找  
       for(int x=xIndex+1;x<=COLS;x++){  
          Color c=isBlack?Color.black:Color.white;  
          if(getChess(x,yIndex,c)!=null){  
             continueCount++;  
          }else  
             break;  
       }  
       if(continueCount>=5){  
             return true;  
       }else   
       continueCount=1;  
         
       //继续另一种搜索纵向  
       //向上搜索  
       for(int y=yIndex-1;y>=0;y--){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(xIndex,y,c)!=null){  
               continueCount++;  
           }else  
               break;  
       }  
       //纵向向下寻找  
       for(int y=yIndex+1;y<=ROWS;y++){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(xIndex,y,c)!=null)  
               continueCount++;  
           else  
              break;  
         
       }  
       if(continueCount>=5)  
           return true;  
       else  
           continueCount=1;  
         
         
       //继续另一种情况的搜索：斜向  
       //东北寻找  
       for(int x=xIndex+1,y=yIndex-1;y>=0&&x<=COLS;x++,y--){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,y,c)!=null){  
               continueCount++;  
           }  
           else break;  
       }  
       //西南寻找  
       for(int x=xIndex-1,y=yIndex+1;x>=0&&y<=ROWS;x--,y++){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,y,c)!=null){  
               continueCount++;  
           }  
           else break;  
       }  
       if(continueCount>=5)  
           return true;  
       else continueCount=1;  
         
         
       //继续另一种情况的搜索：斜向  
       //西北寻找  
       for(int x=xIndex-1,y=yIndex-1;x>=0&&y>=0;x--,y--){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,y,c)!=null)  
               continueCount++;  
           else break;  
       }  
       //东南寻找  
       for(int x=xIndex+1,y=yIndex+1;x<=COLS&&y<=ROWS;x++,y++){  
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,y,c)!=null)  
               continueCount++;  
           else break;  
       }  
       if(continueCount>=5)  
           return true;  
       else continueCount=1;  
         
       return false;  
     }  
     
     
   private Point getChess(int xIndex,int yIndex,Color color){  
       for(Point p:chessList){  
           if(p!=null&&p.getX()==xIndex&&p.getY()==yIndex  
                   &&p.getColor()==color)  
               return p;  
       }  
       return null;  
   }  
     
     
   public void restartGame(){  
       //清除棋子  
       for(int i=0;i<chessList.length;i++){  
           chessList[i]=null;  
       }  
       //恢复游戏相关的变量值  
       isBlack=true;  
       gameOver=false; //游戏是否结束  
       chessCount =0; //当前棋盘棋子个数  
       isptp=true;
       for(int i=0;i<ROWS;i++){
    	   for(int j=0;j<COLS;j++){
    		   score[i][j]=0;
    		   location[i][j]=0;
    	   }
       }
       sb.delete(0,sb.length()-1);
       repaint();  
   }  
     
   //悔棋  
   public void goback(){  
       if(chessCount==0)  
           return ; 
       if(!isptp){
    	   chessList[chessCount-1]=null;
    	   chessCount--;
    	   isBlack=!isBlack;
       }
       chessList[chessCount-1]=null;  
       chessCount--;  
       if(chessCount>0){  
           xIndex=chessList[chessCount-1].getX();  
           yIndex=chessList[chessCount-1].getY();  
       }  
       isBlack=!isBlack;  
       repaint();  
   }
   
   //棋谱管理 保存
   public void Save(){
	   try {

		   String str = sb.toString();
		   
		   File file = new File("src/qipu.txt");
		   if (!file.exists()) {
		    file.createNewFile();
		   }

		   FileWriter fw = new FileWriter(file.getAbsoluteFile());
		   BufferedWriter bw = new BufferedWriter(fw);
		   bw.write(str);
		   bw.close();

		   System.out.println("Done");

		  } catch (IOException e) {
		   e.printStackTrace();
		  }

   }
   
   public void out() throws IOException{
	   
	   this.Save();
	   Desktop.getDesktop().open(new File("src/qipu.txt"));
	   
   }
   
     
   //矩形Dimension  
  
   public Dimension getPreferredSize(){  
       return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2  
                            +GRID_SPAN*ROWS);  
   }  
//   public void ptcGameModel(){
//	   restartGame();
//	   
//   }
     
     
}  
