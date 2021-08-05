package CoinEat;
/*
제한시간 코드 설명
1.맨처음 179번째에서 시간을 5초부터 시작되어 0초에 시간제한이 끝나도록 설정한다.
2.이후 run메소드에서 183번째 부분을 통해 계속해서 시간이 감소하고 Time변수에 저장되어  250번째에서 계속해서 감소되는 시간을 출력한다

3.시간이 0초를 지나 -1초가 되는것을 방지하기 위해 if문을 189번째 부분에 만들어준다.

4.시간이 모두 끝났음을 의미하는 189번째로 진입해 192번째에서 습득한 score를 팝업창으로 출력해준다.

5. 팝업을 닫게되면 다시 score와 시간이 1번처럼 초기화된다.

6. 2번처럼 계속해서 시간이 감소하고 250번째에서 감소되는 시간을 출력한다.(단 197번째에서 우리는 while을 사용했기에 시간이 -1초가 되는순간 반복이 종료된다)

7. 207번째의 true는 200,203번째의 파트가 정상적으로 실행된뒤, while문의 종료조건(countdownStarter<1)이 실행되었다는것을 의미하며, 이후 208번째의 scheduler.shutdown(); 을 실행하게 된다.

8. 이미 시간은 -1초라는 조건에 걸려있으므로 189번째 부분으로 이동한다.

9. 이후 197번째에서부터 시간이 흐르는동안 획득한 score가 192번째 줄에서 팝업메세지로 출력된다

10. 이하 반복

in Eng
1. In the first 179th, set the time to start at 5 seconds and end at 0 seconds.(actually in code you have to set 6 because the screen show from 4 if you set the time from 5)
2. After that, the time continues to decrease via the 183rd section in the 'run' method and is stored in the 'time' variable to output the continuously decreasing time at the 250th section.

3. Create an if statement at the 189th section to prevent the time from becoming -1 second after passing 0 seconds.

4. From the 189th, which means the time is over, and the score acquired from the 192nd section will be displayed in a pop-up window.

5. When the pop up is closed, the score and time are initialized again as in No.1 above.

6. As in No. 2, the time continues to decrease and the time to decrease at the 250th is output.

7. 'true' at the beginning of the 207th section means that after the 200th and 203rd sections are normally executed respectively, the termination condition of while statement(countdownStarter<1) was executed, and shortly after the 208th 'scheduler.shutdown();' will be run

8. Since the time is already on the condition of -1 second, move to the 189th part.

9. After that, the score obtained during the passage of time from the 197th is displayed as a pop-up message in the 192th line.

10. Repeat Below
 */
import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class CoinEat extends JFrame {
	static final int FRAME_WIDTH=500;
	static final int FRAME_HEIGHT=500;
	
	private static int Time;
	
	//creating bufferimage object and graphic object that is for taking screenimage.
	private Image bufferImage;
	private Graphics screenGraphic;
	
	//Declare Clip value for starting of music in this game
	private Clip clip;
	
	//ImageIcon show via Absolute path
	private Image backgroundImage = new ImageIcon("src/images/mainScreen.jpg").getImage();
	private Image player = new ImageIcon("src/images/player.png").getImage();
	private Image coin = new ImageIcon("src/images/coin.png").getImage(); 
	private int playerX, playerY;
	private int playerWidth= player.getWidth(null);
	private int playerHeight=player.getHeight(null);
	
	private int coinX, coinY;
	private int coinWidth= coin.getWidth(null);
	private int coinHeight=coin.getHeight(null);
	
	public static int score;
	public static int saveScore;
	
	
	//show activity of objects in mainscreen
	private boolean up, down, left, right;
	public CoinEat() {
		setTitle("Game of Eating coins");
		setVisible(true);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					up=true;
					break;
				case KeyEvent.VK_S:
					down=true;
					break;
				case KeyEvent.VK_A:
					left=true;
					break;
				case KeyEvent.VK_D:
					right=true;
					break;
				}
						
			}
			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					up=false;
					break;
				case KeyEvent.VK_S:
					down=false;
					break;
				case KeyEvent.VK_A:
					left=false;
					break;
				case KeyEvent.VK_D:
					right=false;
					break;
				}			
			}
		});
		
		//this is custom of initialization function 
		Init();
		
		//repeat method that I made in below
		while (true) {
			
			//Give some waiting times for preventing overload
			try {
				Thread.sleep(20);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			keyProcess();
			crashCheck();
		}
	}
	
	public void Init() {
		//Excute Initialization 
		score= 0;
		
		//set player location in the middle of screen when you start game. 
		playerX=(500-playerWidth)/2;
		playerY=(500-playerHeight)/2;
		
		
		//show coin location randomly
		//Math.random: take the decimal number from 0 to 1 randomly
		//(int): type change decimal number to integer
		//(int)(Math.random()*screen size+1 - image length);
		coinX=(int)(Math.random()*(501-playerWidth));
		//Same way like coinX but add up 30 by considering the length of the frame
		coinY=(int)(Math.random()*(501-playerHeight+30))+30;
		
		//playSound("path", repeat=true, or false);
		playSound("src/audio/backgroundMusic.wav", true);
		
	}
	
	//the number of 30 is minimum boundary from the screen side 
	public void keyProcess() {
		if (up && playerY-10 > 30) playerY-=10;
		if (down && playerY + playerHeight +10 < 500) playerY+=10;
		if (left && playerX - 10 > 0) playerX-=10;
		if (right && playerX + playerWidth +10 < 500) playerX+=10;
	}
	//Implementation of scoring when the player and the coin crash
	public void crashCheck() {
		//set the range of crash
		if(playerX+playerWidth>coinX && coinX+coinWidth>playerX && playerY+playerHeight > coinY && coinY+coinHeight >playerY) {
			score+=100;
			//get coin sound is not repeated as back ground music so, set false
			playSound("src/audio/getCoin.wav", false);
			
			//re-move the coin to random place after crashing with player
			coinX=(int)(Math.random()*(501-playerWidth));
			
			//Prevent coin path-course out of screen 
			coinY=(int)(Math.random()*(501-playerHeight))+30;
			
		}
		
		
		
	}
	//creating playSound method for play music
	//in this method, we will set whether to repeat audio
	public void playSound(String pathName, boolean isLoop) {
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File(pathName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
			clip.start();
			if (isLoop)
				
				//repeat infinitely
				clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException e) {
			e.printStackTrace();	
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
	

	public static void main(String[] args) {
		
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final Runnable runnable = new Runnable() {
			
			//5,4,3,2,1,0 (if u set =5; then time will start from 4~
			int countdownStarter=6;
			public void run() {
				
					//literally timer will be downed
					countdownStarter--;
					
					//you must set the time value in static because this value will be initialized by countdownStarter value.
					Time=countdownStarter;
					
				//5,4,3,2,1,0  (if u set <0 then time will be "Time: -1"
				if (countdownStarter<1) {
					
					//if you use null like (null, message), the pop up will be displayed in the middle of the screen 
					JOptionPane.showMessageDialog(null, "Time Over! \n Your Score: "+score);	
					
					//score and time-limit reset when you press the button of 'ok'
					score=0;
					countdownStarter=6;
					while(countdownStarter<1) {
						
						//literally timer will be downed
						countdownStarter--;
						
						//you must set the time value in static because this value will be initialized by countdownStarter value.
						Time=countdownStarter;
						
						//if you use 'if' then the time will go toward minus infinitely like -1~-999999 
						//so use while(true) 
						while(true) {
							scheduler.shutdown();
						}	
					}
					
				}
			}
		};
		//implement the task specified by the period from the first time
		//scheduleAtFixedRate(TImerTask task,Date firstTime, longPeriod);
		scheduler.scheduleAtFixedRate(runnable,0,1,SECONDS);
		new CoinEat();
		
	};
	

	//using Double Buffer for minimizing many of twinkling 
	public void paint(Graphics g) {
		bufferImage=createImage(500,500);
		
		//creating bufferImage of the screensize and receiving graphic via getGraphics() 
		screenGraphic = bufferImage.getGraphics();
		
		//call screenDraw again and repeat drawing this bufferImage on the screen.
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}
	public void screenDraw(Graphics g) {
		//graphic draw method : g.drawImage(img,x,y,ImageObserver);
		g.drawImage(backgroundImage, 0, 0, null);
		g.drawImage(coin, coinX, coinY, null);
		g.drawImage(player, playerX, playerY, null);
		
		//print score on the screen
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD, 30));
		g.drawString("Score : "+score, 280, 80);
		
		//print left time on the screen
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD, 30));
		g.drawString("Timer : "+Time, 280, 120);
		//if something is changed like re-sizing at 'component' in 'awt', literally it is a method for re-paint.
		this.repaint();
		
	}
	
}

