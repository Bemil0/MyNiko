import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class myNiko implements MouseMotionListener, MouseListener{
	JFrame frame, menu;
	JPanel panel, menuPanel;
	JButton button1, button2, button3;
	JTextField byeName;
	DisplayMode screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	ImageIcon nikoIdle = new ImageIcon(getClass().getClassLoader().getResource("nikoIdle.png")),
			nikoIdleLegLeft = new ImageIcon(getClass().getClassLoader().getResource("nikoIdleLegLeft.png")),
			nikoIdleLegRight = new ImageIcon(getClass().getClassLoader().getResource("nikoIdleLegRight.png")),
			menuImg = new ImageIcon(getClass().getClassLoader().getResource("menu.png")),
			bye = new ImageIcon(getClass().getClassLoader().getResource("blank.png")),
			credits = new ImageIcon(getClass().getClassLoader().getResource("credits.png")),
			currentMenu = menuImg,
			current = nikoIdle;
	ImageIcon[] nikoWalkRight = new ImageIcon[4],
			nikoWalkLeft = new ImageIcon[4],
			nikoWalkUp = new ImageIcon[4],
			nikoWalkDown = new ImageIcon[4];
	Timer down, up, right, left, wait;
	boolean wander = false;
	int xPos, yPos;
	static int ce = 0, i;
	static String direc;
	
	public myNiko() {
		for(int i=0; i<4; i++) {
			nikoWalkRight[i] = new ImageIcon(getClass().getClassLoader().getResource("nikoWalkRight"+i+".png"));
			nikoWalkLeft[i] = new ImageIcon(getClass().getClassLoader().getResource("nikoWalkLeft"+i+".png"));
			nikoWalkUp[i] = new ImageIcon(getClass().getClassLoader().getResource("nikoWalkUp"+i+".png"));
			nikoWalkDown[i] = new ImageIcon(getClass().getClassLoader().getResource("nikoWalkDown"+i+".png"));
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.addMouseMotionListener(this);
		frame.addMouseListener(this);
		frame.setBackground(new Color(0,0,0,0));
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				frame.setSize(current.getIconWidth(), current.getIconHeight());
				current.paintIcon(this,  g,  0, 0);
			}
		};
		panel.setOpaque(false);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		
		menu = new JFrame();
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setUndecorated(true);
		menu.setAlwaysOnTop(true);
		menu.setBackground(new Color(0,0,0,0));
		menu.setLocation(frame.getX()-304+frame.getWidth()/2, frame.getY()-frame.getHeight());
		menuPanel = new JPanel(null) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				menu.setSize(currentMenu.getIconWidth(), currentMenu.getIconHeight());
				currentMenu.paintIcon(this,  g,  0, 0);
			}
		};
		menuPanel.setOpaque(false);
		button1 = new JButton();
		button1.setBorderPainted(false);
		button1.setContentAreaFilled(false);
		button1.setBounds(100, 77, 79, 18);
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				wander = true;
				move("right");
			}
		});
		button2 = new JButton();
		button2.setBorderPainted(false);
		button2.setContentAreaFilled(false);
		button2.setBounds(247, 77, 146, 22);
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				leave();
			}
		});
		button3 = new JButton();
		button3.setBorderPainted(false);
		button3.setContentAreaFilled(false);
		button3.setBounds(496, 16, 96, 96);
		button3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(currentMenu == menuImg) {currentMenu = credits;
				menuPanel.remove(button1); menuPanel.remove(button2);
				}
				else if(currentMenu == credits) {currentMenu = menuImg;
				menuPanel.add(button1); menuPanel.add(button2);
				}
				menuPanel.repaint();
			}
		});
		byeName = new JTextField("Bye "+System.getProperty("user.name")+"!");
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("terminus.ttf"));
			Font terminus = f.deriveFont(27f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(terminus);
			byeName.setFont(terminus);
		} catch (Exception e1) {}
		byeName.setForeground(new Color(255, 255, 255));
		byeName.setBackground(new Color(0,0,0,0));
		byeName.setBorder(null);
		byeName.setBounds(20, 16, 469, 27);
		byeName.setHighlighter(null);
		byeName.setEditable(false);
		byeName.setPreferredSize(new Dimension(469, 27));
		menuPanel.add(button1);
		menuPanel.add(button2);
		menuPanel.add(button3);
		menu.add(menuPanel);
		menu.pack();
	}
	
	public void move(String direction) {
		ce=0;
		i=0;
		int rand = (int)(Math.random()*4);
		 direc = new String();
		 if(rand == 0) {
			 direc = "right";
		 }else if(rand == 1) {
			 direc = "left";
		 }else if(rand == 2) {
			 direc = "up";
		 }else if(rand == 3) {
			 direc = "down";
		 }
		if(direction.equals("right")) {
			ActionListener moveRight = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!wander) {
						right.stop();
						return;
					}
					if(frame.getX()+3 > screen.getWidth()-frame.getWidth() || i == 43){
						right.stop();
						move(direc);
						return;
					}
					if(i%11 == 0) {
						current = nikoWalkRight[ce];
						panel.repaint();
						ce++;
					}
					frame.setLocation(frame.getX()+3, frame.getY());
					i++;
				}
			};
			right = new Timer(16, moveRight);
			right.start();
		}else if(direction.equals("left")){
			ActionListener moveLeft = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!wander) {
						left.stop();
						return;
					}
					if(frame.getX()-3 < 0 || i==43) {
						left.stop();
						move(direc);
						return;
					}
					if(i%11 == 0) {
						current = nikoWalkLeft[ce];
						panel.repaint();
						ce++;
					}
					frame.setLocation(frame.getX()-3, frame.getY());
					i++;
				}
			};
			left = new Timer(16, moveLeft);
			left.start();
		}else if(direction.equals("up")) {
			ActionListener moveUp = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!wander) {
						up.stop();
						return;
					}
					if(frame.getY()-3 < 0 || i==43) {
						up.stop();
						move(direc);
						return;
					}
					if(i%11 == 0) {
						current = nikoWalkUp[ce];
						panel.repaint();
						ce++;
					}
					frame.setLocation(frame.getX(), frame.getY()-3);
					i++;
				}
			};
			up = new Timer(16, moveUp);
			up.start();
		}else if(direction.equals("down")) {
			ActionListener moveDown = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(!wander) {
						down.stop();
						return;
					}
					if(frame.getY()+3 > screen.getHeight()-frame.getHeight() || i==43) {
						down.stop();
						move(direc);
						return;
					}
					if(i%11 == 0) {
						current = nikoWalkDown[ce];
						panel.repaint();
						ce++;
					}
					frame.setLocation(frame.getX(), frame.getY()+3);
					i++;
				}
			};
			down = new Timer(16, moveDown);
			down.start();
		}
	}
	
	public void leave() {
		frame.removeMouseMotionListener(this);
		frame.removeMouseListener(this);
		menuPanel.removeAll();
		currentMenu = bye;
		menuPanel.add(byeName);
		menuPanel.repaint();
		ActionListener waits = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				menu.dispose();
				wait.stop();
			}
		};
		wait = new Timer(999999999, waits);
		wait.setInitialDelay(1500);
		i=0;
		ActionListener moveDown = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				frame.setLocation(frame.getX(), frame.getY()+3);
				menu.setLocation(menu.getX(), menu.getY()+3);
				if(i%11 == 0) {
					if(ce == nikoWalkDown.length) {
						ce = 0;
					}
					current = nikoWalkDown[ce];
					panel.repaint();
					ce++;
				}
				if(frame.getY() > screen.getHeight()) {
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				i++;
			}
		};
		Timer down = new Timer(16, moveDown);
		down.setInitialDelay(1500);
		wait.start();
		down.start();
	}
	
	public static void main(String[] args) {
		new myNiko();
	}

	public void mouseDragged(MouseEvent e) {
		int newX = e.getXOnScreen()-xPos;
		int newY = e.getYOnScreen()-yPos;
		int lastX = frame.getX();
		frame.setLocation(newX, newY);
		menu.setLocation(frame.getX()-304+frame.getWidth()/2, frame.getY()-frame.getHeight());
		if(newX > lastX) {
			current = nikoIdleLegLeft;
			panel.repaint();
		}
		if(newX < lastX) {
			current = nikoIdleLegRight;
			panel.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		xPos = e.getX();
		yPos = e.getY();
	}
	
	public void mouseClicked(MouseEvent e) {
		menu.setLocation(frame.getX()-304+frame.getWidth()/2, frame.getY()-frame.getHeight());
		menu.setVisible(!menu.isVisible());
	}
	
	public void mousePressed(MouseEvent e) {
		if(wander) {
			wander = false;
			current = nikoIdle;
			panel.repaint();
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if(current.equals(nikoIdleLegLeft) || current.equals(nikoIdleLegRight)) {
			current = nikoIdle;
			panel.repaint();
		}
	}
	
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) {	}

}
