package CoinEat;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Ranking extends JFrame implements ActionListener{
	private final int X_Rank = 150;
	private final int X_Name = 250;
	private final int X_Score = 350;
	private final int X_Score_R = 450;
	private final int Y=75;
	private final int WIDTH_LABEL=100;
	private final int HEIGHT_LABEL=50;
	
	private JButton bt_AnotherGame ,bt_Quit;
	private JLabel labelRank, labelName, labelScore, labelScoreR;
	
	private ArrayList Is=new ArrayList();
	
	Ranking(){
		setTitle("Eat Coin - Game Over");
		setUndecorated(true);
		setResizable(false);
		setLayout(null);
		setBounds(100, 100, CoinEat.FRAME_WIDTH, CoinEat.FRAME_HEIGHT);
		
		bt_AnotherGame = new JButton("한판더!!!");
		bt_AnotherGame.setFont(new Font(null, 0,20));
		bt_AnotherGame.setBounds(0,500,100,100);
		bt_AnotherGame.addActionListener(this);
		
		bt_Quit=new JButton("게임종료");
		bt_Quit.setFont(new Font(null, 0, 20));
		bt_Quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		printRanking();
		add(bt_AnotherGame);
		add(bt_Quit);
		setVisible(true);
	}
	private void printRanking() {
		printRankingTitle();
		printActualRanking();
	}
	
	private void printRankingTitle() {
		labelRank = new JLabel("순위");
		labelRank.setBounds(X_Rank, Y, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelRank);
		
		labelName = new JLabel("이름");
		labelName.setBounds(X_Name, Y, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelName);
		
		labelScore=new JLabel("점수");
		labelScore.setBounds(X_Score, Y, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelScore);
		
		labelScoreR = new JLabel("Repaint()");
		labelScoreR.setBounds(X_Score_R, Y, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelScoreR);
	}
	
	private void printActualRanking() {
		try (
				FileReader fr = new FileReader("test.txt");
				BufferedReader br = new BufferedReader(fr);
				
		) {
			String readLine=null;
			while ((readLine = br.readLine())!=null) {
				Is.add(readLine);
			}
		} catch (IOException e) {
		}
		
		ArrayList<Integer> IsScore = new ArrayList<Integer>();
		for (int i=1; i<=Is.size()/3; i++) {
			IsScore.add(Integer.valueOf((String) Is.get(3*i-2)));
			
		}
		Collections.sort(IsScore);
		
		ArrayList<String> IsScore2 = new ArrayList<String>();
		for (int i=0; i<IsScore.size(); i++) {
			IsScore2.add(String.valueOf(IsScore.get(i)));
		}
		
		int rank=0;
		for(int i= IsScore2.size(); i>=1; i--) {
			int x = Is.indexOf(IsScore2.get(i-1));
			rank ++;
			callAllGen(x, rank);
		}
	}
	
	private void callAllGen(int x, int rank) {
		genName(x-1,rank);
		genScore(x,rank);
		genScoreR(x+1,rank);
		genRank(Integer.toString(rank),rank);
	}
	
	private void genRank(String number, int rank) {
		labelRank=new JLabel(number);
		labelRank.setBounds(X_Rank, Y+25*rank, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelRank);
	}
	private void genName(int index, int rank) {
		labelName=new JLabel((String) Is.get(index));
		labelName.setBounds(X_Name, Y+25*rank, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelName);
	}
	private void genScore(int index, int rank) {
		labelScore=new JLabel((String) Is.get(index));
		labelScore.setBounds(X_Score, Y+25*rank, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelScore);
	}
	private void genScoreR(int index, int rank) {
		labelScoreR=new JLabel((String) Is.get(index));
		labelScoreR.setBounds(X_Score_R, Y+25*rank, WIDTH_LABEL, HEIGHT_LABEL);
		add(labelScoreR);
		
	}
	
	private void reset() {
		this.setVisible(false);
		this.dispose();
		new CoinEat();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		reset();
	}

}
