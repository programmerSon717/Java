package CoinEat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class GameOverFrame extends JFrame implements ActionListener{
	private JButton bt_OK;
	private JTextField tf;
	
	private String namePlayer;
	
	static int SCORE;
	static int SCORE_REPAINT;
	
	GameOverFrame(){
		setTitle("Eat Coin - Game Over");
		setUndecorated(true);
		setResizable(false);
		setLayout(null);
		setBounds(100, 100, CoinEat.FRAME_WIDTH, CoinEat.FRAME_HEIGHT);
		
		tf=new JTextField(20);
		tf.setBounds(CoinEat.FRAME_WIDTH/2-180/2, CoinEat.FRAME_HEIGHT/2-25/2,180,25);
		tf.addActionListener(this);
		
		
		bt_OK=new JButton("»Æ¿Œ");
		bt_OK.setBounds(390, CoinEat.FRAME_HEIGHT/2-25/2,30,25);
		bt_OK.addActionListener(this);
		
		add(tf);
		add(bt_OK);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e ){
		namePlayer= tf.getText();
		try(
				FileWriter fw=new FileWriter("test.txt",true);
				BufferedWriter bw=new BufferedWriter(fw);
		) {
			bw.write(namePlayer);
			bw.newLine();
			bw.write(Integer.toString(SCORE));
			bw.newLine();
			bw.write(Integer.toString(SCORE_REPAINT));
			bw.newLine();
			bw.flush();
		} catch(IOException ie) {
			System.out.println(ie);
		}
		new Ranking();
	}
}
