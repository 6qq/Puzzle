package myPack;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Puzzle extends JPanel implements ActionListener{
	BufferedImage picture;
	JButton[][] pieces;
	JButton control = new JButton("control");
	boolean isClick = false;
	JButton clicked;
	int pieceCountX,pieceCountY,pieceWidth,pieceHeight;
	Puzzle(int width,int height,int x,int y){
		this.setPreferredSize(new Dimension(width,height));
		this.setLayout(null);
		pieceCountX = x;
		pieceCountY = y;
		pieceWidth = width/pieceCountX;
		pieceHeight = height/pieceCountY;
		pieces = new JButton[pieceCountX][pieceCountY];
		for(int i = 0;i < pieceCountX;i++) {
			for(int j = 0;j < pieceCountY;j++) {
				pieces[i][j] = new JButton();
				pieces[i][j].setBounds(i*pieceWidth, j*pieceHeight, pieceWidth, pieceHeight);
				pieces[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				pieces[i][j].addActionListener(this);
				this.add(pieces[i][j]);
			}
		}
	}
	
	public void setPicture(BufferedImage src) {
		picture = src;
		int picturePieceWidth = picture.getWidth()/pieceCountX;
		int picturePieceHeight = picture.getHeight()/pieceCountY;
		for(int i = 0;i < pieceCountX;i++) {
			for(int j = 0;j < pieceCountY;j++) {
				BufferedImage tmp = picture.getSubimage(i*picturePieceWidth, j*picturePieceHeight, picturePieceWidth, picturePieceHeight);
				pieces[i][j].setIcon(new ImageIcon(new ImageIcon(tmp).getImage().getScaledInstance(pieceWidth, pieceHeight, Image.SCALE_DEFAULT)));
			}
		}

	}
	
	public void move(int x1,int y1,int x2,int y2) {
		Point swap = pieces[x1][y1].getLocation();
		pieces[x1][y1].setLocation(pieces[x2][y2].getLocation());
		pieces[x2][y2].setLocation(swap);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "" :
			JButton click = (JButton) e.getSource();
			if(isClick) {
				Point swap = click.getLocation();
				click.setLocation(clicked.getLocation());
				clicked.setLocation(swap);
				isClick = false;
			}else {
				clicked = click;
				isClick = true;
			}
			break;
		case "finish" :
			boolean isWrong = false;
			start :
			for(int i = 0;i < pieceCountX;i++) {
				for(int j = 0;j < pieceCountY;j++) {
					if(pieces[i][j].getLocation().x != i*pieceWidth) {
						isWrong = true;
						break start;
					}
				}
			}
			if(isWrong) {
				JOptionPane.showMessageDialog(null, "wrong", "test", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "true", "test", JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case "reset" :
			for(int i = 0;i < pieceCountX;i++) {
				for(int j = 0;j < pieceCountY;j++) {
					pieces[i][j].setLocation(i*pieceWidth,j*pieceHeight);
				}
			}
			break;
		case "mix" :
			for(int i = 0;i < pieceCountX;i++) {
				for(int j = 0;j < pieceCountY;j++) {
					move((int)(Math.random()*pieceCountX),(int)(Math.random()*pieceCountY),(int)(Math.random()*pieceCountX),(int)(Math.random()*pieceCountY));
				}
			}
			break;
		case "open" :
			String inp = JOptionPane.showInputDialog(null, "Please enter the file path :", "File", JOptionPane.INFORMATION_MESSAGE);
			try {
				BufferedImage img = ImageIO.read(new File(inp));
				this.setPicture(img);
			}catch(IOException e2) {
				JOptionPane.showMessageDialog(null, "Please enter true path", "alert", JOptionPane.WARNING_MESSAGE);
			}
			break;
		case "save" :
			String inp2 = JOptionPane.showInputDialog(null, "Please enter the file path :", "File", JOptionPane.INFORMATION_MESSAGE);
			try {
				Robot robot = new Robot();
				Point p = this.getLocationOnScreen();
				Dimension d = this.getPreferredSize();
				BufferedImage ss = robot.createScreenCapture(new Rectangle(p.x,p.y,d.width,d.height));
				File file = new File(inp2);
				ImageIO.write(ss, "jpg", file);
			}catch (AWTException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
	}
	public static void main(String[]args) throws IOException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Puzzle game = new Puzzle(500,500,10,10);
		frame.setContentPane(game);
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("file");
		JMenu options = new JMenu("options");
		menuBar.add(file);
		menuBar.add(options);
		JMenuItem item1 = new JMenuItem("finish");
		JMenuItem item2 = new JMenuItem("reset");
		JMenuItem item3 = new JMenuItem("mix");
		JMenuItem item4 = new JMenuItem("open");
		JMenuItem item5 = new JMenuItem("save");
		options.add(item1);
		options.add(item2);
		options.add(item3);
		file.add(item4);
		file.add(item5);
		item1.addActionListener(game);
		item2.addActionListener(game);
		item3.addActionListener(game);
		item4.addActionListener(game);
		item5.addActionListener(game);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
	}
}