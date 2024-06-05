import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import org.json.JSONObject;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

	private JPanel contentPane;
	private JLabel lblTeam;
	private static final Integer FRAMETIME = 16;                              //Define the frame time that use in the timer
	private Timer tickTock = new Timer(FRAMETIME,this);					//Define a timer variable and instantiate the timer class
	private JPanel panel;
	private List<Player> players = new ArrayList<>();							//to store the players positions 
    private Player currentPlayer;
	private String ipAddress;
	private String username;
	private short radiusAttack = 20;
	private short counterTicks = 0;
	private PlayerConnection playerConnection;
	private String status;														//Declare string variable to store the status
	private boolean firstSetup = true;
	private short playerLives;
	private int scoreTeam1;
	private int scoreTeam2;
	private boolean endRound = false;
	private int tempTeam = 0;
	JLabel lblAnnouncement = new JLabel("ROUND 1!");
	JLabel lblScoreTeam1;
	JLabel lblScoreTeam2;
	
	
	public GameScreen(String ipAddress_t, String username_t) {
		setResizable(false);
		username = username_t;
		ipAddress = ipAddress_t;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				status = "Offline";
				try {
					playerConnection = new PlayerConnection(ipAddress);
					String infoUpdate = username+","+status;								//Change status to offline
					playerConnection.sendInformationToServer(infoUpdate);
					JOptionPane.showMessageDialog(contentPane, "Closing the game.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		tickTock.start();
		setTitle("GET THE FLAG!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);		
		
		//PLACEHOLDER for round announcements, like ( ROUD 1, START!, TEAM X WINS ROUND X, TEAM X WINS THE GAME!, etc...)
		
		lblAnnouncement.setVisible(false);
		//lblAnnouncement.setOpaque(true);
		//lblAnnouncement.setBackground(Color.DARK_GRAY);
		lblAnnouncement.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnnouncement.setForeground(Color.WHITE);
		lblAnnouncement.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblAnnouncement.setBounds(225, 200, 520, 162);
		contentPane.add(lblAnnouncement);
		
		
		
		// References			:   Oracle (2022) Lesson: Performing Custom Painting.
		//							https://docs.oracle.com/javase/tutorial/uiswing/painting/index.html
		//							GeeksForGeeks (2023) Super Keyword in Java. https://www.geeksforgeeks.org/super-keyword/
		panel = new JPanel()
		 { 
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        drawPlayers((Graphics2D) g);
		    }
		};
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(new Color(176, 196, 222)));
		panel.setBounds(38, 98, 900, 400);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panelScore = new JPanel() { 
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        drawLives((Graphics2D) g, playerLives);
		    }
		};
		panelScore.setOpaque(false);
		panelScore.setBounds(381, 518, 286, 49);
		panelScore.setLayout(null);
		contentPane.add(panelScore);
		
		
		JLabel lblGameField = new JLabel("");
		lblGameField.setIcon(new ImageIcon(GameScreen.class.getResource("/Media/GameField.png")));
		lblGameField.setBounds(38, 98, 900, 400);
		contentPane.add(lblGameField);
		
		lblScoreTeam1 = new JLabel("0");
		lblScoreTeam1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblScoreTeam1.setHorizontalAlignment(SwingConstants.CENTER);
		lblScoreTeam1.setForeground(Color.WHITE);
		lblScoreTeam1.setBorder(new LineBorder(Color.CYAN, 3));
		lblScoreTeam1.setBounds(444, 57, 45, 45);
		contentPane.add(lblScoreTeam1);
		
		lblScoreTeam2 = new JLabel("0");
		lblScoreTeam2.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblScoreTeam2.setHorizontalAlignment(SwingConstants.CENTER);
		lblScoreTeam2.setForeground(Color.WHITE);
		lblScoreTeam2.setBorder(new LineBorder(Color.CYAN, 3));
		lblScoreTeam2.setBounds(487, 57, 45, 45);
		contentPane.add(lblScoreTeam2);
		
		JLabel lblScore = new JLabel("SCORE");
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblScore.setForeground(new Color(102, 153, 255));
		lblScore.setBounds(443, 22, 89, 24);
		contentPane.add(lblScore);
		
		JLabel lblPlayer = new JLabel("PLAYER ID:");
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer.setForeground(new Color(102, 153, 255));
		lblPlayer.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPlayer.setBounds(38, 28, 89, 24);
		contentPane.add(lblPlayer);
		
		JLabel lblPlayerID = new JLabel(username);
		lblPlayerID.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerID.setForeground(new Color(176, 196, 222));
		lblPlayerID.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPlayerID.setBounds(137, 28, 89, 24);
		contentPane.add(lblPlayerID);
		
		JLabel lblLivesOrSpectator = new JLabel("LIVES");
		lblLivesOrSpectator.setOpaque(true);
		lblLivesOrSpectator.setBackground(Color.BLACK);
		lblLivesOrSpectator.setHorizontalAlignment(SwingConstants.CENTER);
		lblLivesOrSpectator.setForeground(new Color(102, 153, 255));
		lblLivesOrSpectator.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblLivesOrSpectator.setBounds(381, 566, 212, 40);
		contentPane.add(lblLivesOrSpectator);
		
		JLabel lblLife1 = new JLabel("");
		lblLife1.setOpaque(true);
		lblLife1.setBackground(Color.GRAY);
		lblLife1.setBounds(383, 519, 12, 35);
		contentPane.add(lblLife1);
		
		JLabel lblLife2 = new JLabel("");
		lblLife2.setOpaque(true);
		lblLife2.setBackground(Color.GRAY);
		lblLife2.setBounds(405, 519, 12, 35);
		contentPane.add(lblLife2);
		
		JLabel lblLife3 = new JLabel("");
		lblLife3.setOpaque(true);
		lblLife3.setBackground(Color.GRAY);
		lblLife3.setBounds(427, 519, 12, 35);
		contentPane.add(lblLife3);
		
		JLabel lblLife4 = new JLabel("");
		lblLife4.setOpaque(true);
		lblLife4.setBackground(Color.GRAY);
		lblLife4.setBounds(449, 519, 12, 35);
		contentPane.add(lblLife4);
		
		JLabel lblLife5 = new JLabel("");
		lblLife5.setOpaque(true);
		lblLife5.setBackground(Color.GRAY);
		lblLife5.setBounds(471, 519, 12, 35);
		contentPane.add(lblLife5);
		
		JLabel lblLife6 = new JLabel("");
		lblLife6.setOpaque(true);
		lblLife6.setBackground(Color.GRAY);
		lblLife6.setBounds(493, 519, 12, 35);
		contentPane.add(lblLife6);
		
		JLabel lblLife7 = new JLabel("");
		lblLife7.setOpaque(true);
		lblLife7.setBackground(Color.GRAY);
		lblLife7.setBounds(515, 519, 12, 35);
		contentPane.add(lblLife7);
		
		JLabel lblLife8 = new JLabel("");
		lblLife8.setOpaque(true);
		lblLife8.setBackground(Color.GRAY);
		lblLife8.setBounds(537, 519, 12, 35);
		contentPane.add(lblLife8);
		
		JLabel lblLife9 = new JLabel("");
		lblLife9.setOpaque(true);
		lblLife9.setBackground(Color.GRAY);
		lblLife9.setBounds(559, 519, 12, 35);
		contentPane.add(lblLife9);
		
		JLabel lblLife10 = new JLabel("");
		lblLife10.setOpaque(true);
		lblLife10.setBackground(Color.GRAY);
		lblLife10.setBounds(581, 519, 12, 35);
		contentPane.add(lblLife10);
		
	
		lblTeam = new JLabel("Team ");
		lblTeam.setHorizontalAlignment(SwingConstants.CENTER);
		lblTeam.setForeground(new Color(176, 196, 222));
		lblTeam.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTeam.setBounds(849, 22, 89, 24);
		contentPane.add(lblTeam);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(GameScreen.class.getResource("/Media/Background.png")));
		lblBackground.setBounds(0, 0, 984, 611);
		contentPane.add(lblBackground);
		
		setFocusable(true);
	    addKeyListener(this);
	    setVisible(true);
	    requestFocusInWindow();
	    
	}
	
	public void drawPlayers(Graphics2D g) {  					//Draw() method
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	drawAreaMarkers
		//
		// Method parameters	:	Graphics2D g
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is used to draw the graphics on the game screen.
		//
		// References			:   Oracle (2022) Lesson: Performing Custom Painting.
		//							https://docs.oracle.com/javase/tutorial/uiswing/painting/index.html
		//							https://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
		// Modifications		:
		//							Date			Developer				Notes
		//							----			---------				-----
		//							2024-01-29		V. Arias				Draw Area Markers
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		
		//pull players' to draw from the server and update the list players
		
		g.setStroke(new BasicStroke(5)); 							// set the stroke thickness of the area delimiters to 5
		
		for(Player player : players)
		{
			// Set the circle's characteristics
	       int radius = 0; 
	       if(player.getStatus() == Player.PlayerStatus.ATTACKING)
	        	radius = radiusAttack;
	       else
	    	   radius = 15;
	        int diameter = radius * 2;
	        Point currentPosition = player.getPosition();
			int x = currentPosition.x - radius;
	        int y = currentPosition.y - radius;
	        
	       
	        
	        //if(!player.getAttacking())
	        if(player.getStatus() != Player.PlayerStatus.ATTACKING && player.getStatus() != Player.PlayerStatus.DAMAGE)
	        {
	        	if(player.getTeam() == 1)
	 	        	g.setColor(Color.RED); 										// set the color of the area delimiters to red
	 	        else 
	 	        	g.setColor(Color.GREEN); 
	        }
	        else  if(player.getStatus() == Player.PlayerStatus.ATTACKING || player.getStatus() == Player.PlayerStatus.DAMAGE)
	        	g.setColor(Color.white); 
	        
	     
	        g.fillOval(x, y, diameter, diameter);
	        if(player.isFlag())
	        {
	        	g.setColor(Color.YELLOW);
		        g.drawOval(x, y, diameter-1, diameter-1);
	        }
	        
	        if(player.getUsername().equals(username)) {

	        	int smallDiameter = diameter / 4;
	            int centerX = x + radius; // Center X of the larger circle
	            int centerY = y + radius; // Center Y of the larger circle

	            int smallX = centerX - smallDiameter / 2; // Adjusted X for the small circle
	            int smallY = centerY - smallDiameter / 2; // Adjusted Y for the small circle

	            // Display visual aid or mark in the center
	            g.setColor(Color.BLUE);
	            g.fillOval(smallX, smallY, smallDiameter, smallDiameter);
	       	  	            
        	}
	     
		}//end for 
		
		panel.repaint();
    } 
	
		@SuppressWarnings({ "serial", "serial" })
		public void actionPerformed(ActionEvent e) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	actionPerformed
		//
		// Method parameters	:	ActionEvent e
		//
		// Method return		:	void
		//
		// Synopsis				:   This method is managed by the timer event. All the automatic updated with the server are done here.
		//
		// References			:   
		//							
		//
		// Modifications		:
		//							Date			Developers				Notes
		//							----			---------				-----
		//							2024-02-20		V. Arias,               
		//											J. Silva,
		//											R. Useda
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=		

			if(counterTicks > 0)
        		counterTicks--;
			
			currentPlayer = getPlayer(username);
			
			if(currentPlayer != null && !endRound)
			{
				if(counterTicks < 12 && counterTicks > 9 && currentPlayer.getStatus() == Player.PlayerStatus.DAMAGE)
				{
					currentPlayer.setStatus(Player.PlayerStatus.DEFENSE);
				}
				else if(counterTicks <= 0 )
				{
	       		 	currentPlayer.setStatus(Player.PlayerStatus.DEFENSE);
	       		 	updateServer(currentPlayer, 1);
				}
				playerLives = currentPlayer.getLives();
			}
			
			if(lblAnnouncement.isVisible() && counterTicks > 0) {
	        		counterTicks--;
	        		if(counterTicks <= 0) {
	        			lblAnnouncement.setVisible(false);
	        			
	        			if(scoreTeam1 < 2 && scoreTeam2 <2 ) {
	        				startNewRound();
	        			}
	        			else if(scoreTeam1 == 2) {
	        				lblAnnouncement.setText("Team 1 Won the game");
	        				lblAnnouncement.setVisible(true);
	        				goToLobby();
	        			}
	        			else if(scoreTeam2 == 2) {
	        				lblAnnouncement.setText("Team 2 Won the game");
	        				lblAnnouncement.setVisible(true);
	        				goToLobby();
	        			}
	        		}
			}
			checkRoundWinner();
			paintScreen();		
		}
		
		public void setIpAddress(String ipAddress_t)
		{
			this.ipAddress = ipAddress_t;
		}
		public void setUsername(String username_t)
		{
			this.username = username_t;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(!lblAnnouncement.isVisible())
			{
				int key = e.getKeyCode();
				
				currentPlayer = getPlayer(username);
				
				if(currentPlayer !=null && currentPlayer.getStatus()!=Player.PlayerStatus.DEAD)
				{
					int radius = currentPlayer.getRadius();
		            Point currentPosition = currentPlayer.getPosition();
		            
		            switch(key) 
		            { 
		            	case KeyEvent.VK_A:
		                	//if (isFlagNearby(currentPlayer)) 		
		            		if(checkSurroundings(currentPlayer, 3))
		            		{ 
		                		endRound = true;
		                		checkRoundWinner();
		                    }
		                	break;
		                case KeyEvent.VK_UP: 																				// Arrow Up
		                	if(currentPosition.y >=45-radius )
		                		currentPosition.y -= 10; 																		// Move up
		                    break;
		                case KeyEvent.VK_DOWN: 																		// Arrow Down
		                	if(currentPosition.y <=390-radius )
		                		currentPosition.y += 10; 																		// Move down
		                    break;
		                case KeyEvent.VK_LEFT: 																			// Arrow Left
		                	if(currentPosition.x >=105-radius )
		                		currentPosition.x -= 10; 																		// Move left
		                    break;
		                case KeyEvent.VK_RIGHT: 																			// Arrow Right
		                	if(currentPosition.x <=830-radius )
		                		currentPosition.x += 10; 																		// Move right
		                    break;
		                case KeyEvent.VK_SPACE:
		                	
		                	if (currentPlayer.getStatus() == Player.PlayerStatus.DEFENSE && checkSurroundings(currentPlayer, 1)) 					
	                		{ 	
		                		counterTicks = 20;
	                			currentPlayer.setStatus(Player.PlayerStatus.ATTACKING);
	                			updateServer(currentPlayer, 1);
	                        }
		            }//end switch
		            
		            if(!checkSurroundings(currentPlayer, 2))						
		            	updateServer(currentPlayer, 1); // Update player position on server side
		            
				}//end if
				 
			    paintScreen();
			}
			
		    
		}
		
		public void updateServer(Player playerToUpdate, int option)   
		{
			if(playerToUpdate != null)
			{
				//System.out.println("update");
				String playerString = null;
				if(option == 1)
				{
					String playerStatus = playerToUpdate.getStatus().name();
					JSONObject playerJson = 									//Create JSon object to send to the server
							new JSONObject("{ \"username\" : \"" + playerToUpdate.getUsername() + "\" , \"team\" : \"" + playerToUpdate.getTeam() +"\"  ,  \"positionX\" : \"" + playerToUpdate.getPosition().x+ "\" ,  \"positionY\" : \"" + playerToUpdate.getPosition().y + "\"  , "
									+ "						\"lives\" : \"" + playerToUpdate.getLives() +  "\" ,  \"isFlag\" : \"" + playerToUpdate.isFlag() + "\" , \"radius\" : \"" + playerToUpdate.getRadius() +"\" , \"status\":\"" + playerStatus+ "\"}");
					playerString = String.valueOf(playerJson);			//JSon object stored in a string variable
					PlayerConnection playerConnectionObj;
					try {
						playerConnectionObj = new PlayerConnection(ipAddress);
						playerConnectionObj.sendInformationToServer(playerString);
						players = playerConnectionObj.getPlayers();
						for(Player playersList: players)
						{
							if(playersList.equals(currentPlayer))
							{
								currentPlayer.setLives(playersList.getLives());
								playerLives = currentPlayer.getLives();
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
				
		public void updateLives(Player playerAttacked)
		{
			if(playerAttacked != null)
			{
	            JSONObject playerJson = 									//CReate JSon object to send to the server
						new JSONObject("{ \"username\" : \"" + playerAttacked.getUsername() + "\" , \"lives\" : \"" + playerAttacked.getLives() + "\"}");
				String playerString = String.valueOf(playerJson);			//JSon object stored in a string variable
				PlayerConnection playerConnectionObj;
				
				try {
					playerConnectionObj = new PlayerConnection(ipAddress);
					playerConnectionObj.sendInformationToServer(playerString);
					players = playerConnectionObj.getPlayers();
		
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		public Player getPlayer(String username)
		{
			for(Player playerInGame : players)
			{
				if(playerInGame.getUsername().equals(username)) 
					return playerInGame;
			}
			return null;
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			//DO nothing
		}
		
		public boolean checkSurroundings(Player player, int option) {   //option 2 =collision option=1 enemies option 3 flag nearby
		    Point playerPos = player.getPosition();
		    short team = player.getTeam();
		    int diameter = player.getRadius() * 2;
		    int enemyDiameter = diameter + 3;
		    int collisionDiameter = diameter - 2;
		    
		    for (Player otherPlayer : players) {
		        if (player != otherPlayer) {
			        int xDistance = playerPos.x - otherPlayer.getPosition().x;
			        int yDistance = playerPos.y - otherPlayer.getPosition().y;
			        int distanceSquared = xDistance * xDistance + yDistance * yDistance;
			   
			        if (option == 1) {
			            if (!otherPlayer.isFlag() && team != otherPlayer.getTeam() && distanceSquared <= enemyDiameter * enemyDiameter) {
			                otherPlayer.setLives((short) (otherPlayer.getLives() - 1));
			                otherPlayer.setStatus(Player.PlayerStatus.DAMAGE);																												//*******************************************
			                updateServer(otherPlayer, 1);
			                return true;
			            }
			        } 
			        else if (option == 2)
			        {
			        	if (distanceSquared <= collisionDiameter * collisionDiameter) 
				            return true;
			        }
			        else if (option == 3)
		        	{
		        		if(otherPlayer.isFlag() && team != otherPlayer.getTeam() && distanceSquared <= enemyDiameter * enemyDiameter)
		        		{
		        			//playerPosition = players.indexOf(currentPlayer);
		        			currentPlayer.setStatus(Player.PlayerStatus.WIN);
		        			otherPlayer.setFlag(false);
		        			updateServer(otherPlayer, 1); 
		        			contentPane.repaint();
	                		updateServer(currentPlayer, 1); 
		        			return true;
		        		}
		        	}
		        }//end if to avoid self check
		    }//end for
		    
		    return false;
		}
		
		public void checkRoundWinner() {
			for(int i=0;i<players.size() ;i++)
			//for (Player player : players) 
		    {
				//System.out.println(players.get(i).getUsername()+" STATUS "+ players.get(i).getStatus()+" POSITION "+i);
				
				if(players.get(i).getStatus().equals(Player.PlayerStatus.WIN) && !lblAnnouncement.isVisible()) {
					if(players.get(i).getTeam() == 1) {
						scoreTeam1 += 1;
						endRound = false;
						lblScoreTeam1.setText(String.valueOf(scoreTeam1));
					}	
					if(players.get(i).getTeam() == 2) {
						scoreTeam2 += 1;
						endRound = false;
						lblScoreTeam2.setText(String.valueOf(scoreTeam2));
					}
					lblAnnouncement.setText("Team " +players.get(i).getTeam() + " Won the round");
					lblAnnouncement.setVisible(true);
					counterTicks = 120;
					}	
			}
		}
		
		
		public void startNewRound() {
			PlayerConnection playerConnectionObj;
			try {
				playerConnectionObj = new PlayerConnection(ipAddress);
				playerConnectionObj.sendInformationToServer("res");
				players = playerConnectionObj.getPlayers();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			panel.repaint();
		}
		
		public void goToLobby() {
			deletePlayers();
			dispose();
			tickTock.stop();
			GameLobby gameLobby = new GameLobby();
			gameLobby.SetPlayerID(username, ipAddress, "Online");
			gameLobby.setGameInProgress(false);
			gameLobby.setScreenOpen(false);
			gameLobby.setVisible(true);
		}
		
		public void deletePlayers() {
			PlayerConnection playerConnectionObj;
			try {
				playerConnectionObj = new PlayerConnection(ipAddress);
				playerConnectionObj.sendInformationToServer("del");
				players = playerConnectionObj.getPlayers();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		
		public void drawLives(Graphics2D g, int lives) {
			int startX = 0; // starting x position for the first label
	        int labelWidth = 12; // width of each label
	        int distanceX = 22; // distance between each label
	        int labelHeight = 35; // height of each label

	        g.setColor(Color.YELLOW); // Set the color
	        
	        if(lives > 0) {
	        for (int i = 0; i < lives; i++) {
	            int x = startX + i * distanceX;
	            int y = 0;
	            g.fillRect(x, y, labelWidth, labelHeight); // Draw a rectangle for each life
	        }
	        }
		}
		
		public void paintScreen()
		{
			PlayerConnection playerConnectionObj;
			try {
				playerConnectionObj = new PlayerConnection(ipAddress);
				playerConnectionObj.sendInformationToServer("getPos");
				players = playerConnectionObj.getPlayers();
				
				if(firstSetup) {
					
					for(Player player : players)
					{
						if(player.getUsername().equals(username)) {
							lblTeam.setText(lblTeam.getText()+player.getTeam());
							playerLives = player.getLives();
						}
						
					}
					firstSetup = false;
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			contentPane.repaint();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
}