import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.JSONObject;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;


public class GameLobby extends JFrame implements ActionListener {

	private JPanel contentPane;
	private GameRules rules;
	private DefaultListModel<String> users;										//Declare Default list model to store the chat content
	private JLabel lblPlayerId = new JLabel("PLAYER ID");
	private String status;
	private String[] teams = { "Select Team", "Team 1", "Team 2"};
    private boolean isFlag = false;
    private short lives = 10;
    private short team = 0;
    private short radius = 15;
	private Point position;
	//private GameScreen gameScreenObj = new GameScreen();
	private short positionX1 = 120;
	private short positionX2 = 780;
	private short positionY1 = 140; // add 35 per player
	
	private static final Integer FRAMETIME = 16;                              //Define the frame time that use in the timer
	private Timer tickTock = new Timer(FRAMETIME,this);	
	private StyledDocument docLobby;													//Define StyleDocument to manage the style on the textPane
	private StyledDocument docTeam1;
	private StyledDocument docTeam2;
	private javax.swing.text.Style styleLobby;
	private javax.swing.text.Style styleTeam1;
	private javax.swing.text.Style styleTeam2;
	private PlayerConnection player;
	private JTextPane textPaneLobby;
	private JTextPane textPaneTeam1;
	private JTextPane textPaneTeam2;
	private JButton btnStartGame;
	
	private ArrayList<String> usersSession = new ArrayList<String>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private boolean gameInProgress = false;
	private String role;
	public boolean screenOpen = false;
	private String username;													//Declare string variable to store the username
	private String ipAddress;

	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public void setUsername(String username) {
		this.username = username;
		lblPlayerId.setText(username);
	}

	public void setScreenOpen(boolean screenOpen) {
		this.screenOpen = screenOpen;
	}

	
	
	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}
	
	public void SetPlayerID(String username_t, String ipAddress_t, String status_t) {
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method				:	SetUsernameIP
		//
		// Method parameters	:	String username_t, String ipAddress_t, ClientConnection client_t, String status_t
		//
		// Method return		:	void
		//
		// Synopsis				:   This method initialise the public variables of the class.  
		//
		// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
		//
		// Modifications		:
		//							Date			Developers				Notes
		//							----			---------				-----
		//							2024-02-20		V. Arias,               Greeting Server
		//											J. Silva,
		//											R. Useda
		//
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
			username = username_t;							//Initialized public variables
			ipAddress = ipAddress_t;
			status = status_t;
			//lblServerIPAddress.setText(ipAddress_t);		//Update value of username and IpAddress labels
			lblPlayerId.setText(username_t);
		}
	
	/**
	 * Create the frame.
	 */
	public GameLobby() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				status = "Offline";
				try {
					player = new PlayerConnection(ipAddress);
					String infoUpdate = username+","+status;								//Change status to offline
					player.sendInformationToServer(infoUpdate);
					textPaneLobby.setText("");
					JOptionPane.showMessageDialog(contentPane, "Closing the game.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		tickTock.start(); //START THE TIMER
		lblPlayerId.setText("TESTID");
		setTitle("GRAB THE FLAG!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSeparator = new JLabel("");
		lblSeparator.setOpaque(true);
		lblSeparator.setBackground(Color.ORANGE);
		lblSeparator.setBounds(0, 72, 984, 3);
		contentPane.add(lblSeparator);
		
		JLabel lblTitle = new JLabel("Grab The Flag!");
		lblTitle.setForeground(Color.CYAN);
		lblTitle.setFont(new Font("Magneto", Font.PLAIN, 26));
		lblTitle.setBounds(77, 29, 220, 26);
		contentPane.add(lblTitle);
		
		JButton btnRules = new JButton("RULES");
		btnRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules = new GameRules();
				rules.setVisible(true);
			}
		});
		btnRules.setFocusPainted(false);
		btnRules.setBorder(new LineBorder(new Color(0, 0, 255), 3, true));
		btnRules.setBackground(Color.BLACK);
		btnRules.setForeground(new Color(102, 153, 255));
		btnRules.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnRules.setBounds(756, 30, 140, 21);
		contentPane.add(btnRules);
		
		textPaneLobby = new JTextPane();
		textPaneLobby.setBorder(new LineBorder(Color.CYAN, 3, true));
		textPaneLobby.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPaneLobby.setEditable(false);
		textPaneLobby.setRequestFocusEnabled(false);
		textPaneLobby.setFocusable(false);
		textPaneLobby.setBackground(new Color(40, 40, 40));
		textPaneLobby.setBounds(61, 120, 209, 400);
		contentPane.add(textPaneLobby);
		docLobby = textPaneLobby.getStyledDocument(); 								//Fetches the model associated with the editor.
		 // Style for red text
		styleLobby = (javax.swing.text.Style) textPaneLobby.addStyle("", null); //Adds a new style into the logical style hierarchy. Style attributes resolve from bottom up so an attribute specified in a child will override an attribute specified in the parent.
		StyleConstants.setForeground(styleLobby, Color.WHITE); 
		StyleConstants.setFontSize(styleLobby, 18);
       
       JScrollPane scrollPaneLobby = new JScrollPane(textPaneLobby);
       scrollPaneLobby.setBounds(77, 124, 209, 400);
       scrollPaneLobby.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
       contentPane.add(scrollPaneLobby);
		
		
		lblPlayerId.setForeground(new Color(176, 196, 222));
		lblPlayerId.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPlayerId.setBounds(455, 113, 84, 39);
		contentPane.add(lblPlayerId);
		
		JButton btnReadyToPlay = new JButton("READY TO PLAY");
		btnReadyToPlay.setEnabled(false);
		btnReadyToPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String playerString;											//Declare string to store the Json object text
				
				try {
					PlayerConnection playerConnectionObj;
					String playerStatus = Player.PlayerStatus.DEFENSE.name();
					
					//Convertir player information to JSON
					JSONObject playerJson = 									//CReate JSon object to send to the server
							new JSONObject("{ \"username\" : \"" + username + "\" , \"team\" : \"" + team +"\"  ,  \"positionX\" : \"" + -1 + "\" ,  \"positionY\" : \"" 
														+ -1 + "\"  , \"lives\" : \"" + lives +  "\" ,  \"isFlag\" : \"" + isFlag + "\" , \"radius\" : \"" + radius +"\" , \"status\": \""+ playerStatus+"\"}");																																																                    
					
					playerString = String.valueOf(playerJson);			//JSon object stored in a string variable

					playerConnectionObj = new PlayerConnection(ipAddress);
					playerConnectionObj.sendInformationToServer(playerString);
					System.out.println("ready");
					btnStartGame.setEnabled(true);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
			}

		});
		btnReadyToPlay.setForeground(Color.ORANGE);
		btnReadyToPlay.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnReadyToPlay.setFocusPainted(false);
		btnReadyToPlay.setBorder(new LineBorder(new Color(0, 0, 255), 3, true));
		btnReadyToPlay.setBackground(Color.BLACK);
		btnReadyToPlay.setBounds(353, 461, 159, 21);
		contentPane.add(btnReadyToPlay);
		
		JLabel lblTeam1 = new JLabel(" TEAM 1");
		lblTeam1.setBackground(Color.BLACK);
		lblTeam1.setOpaque(true);
		lblTeam1.setForeground(Color.WHITE);
		lblTeam1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTeam1.setBounds(586, 89, 310, 33);
		contentPane.add(lblTeam1);
		
		JLabel lblTeam2 = new JLabel(" TEAM 2");
		lblTeam2.setOpaque(true);
		lblTeam2.setBackground(Color.BLACK);
		lblTeam2.setForeground(Color.WHITE);
		lblTeam2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTeam2.setBounds(586, 306, 310, 26);
		contentPane.add(lblTeam2);
		
		textPaneTeam1 = new JTextPane();
		textPaneTeam1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPaneTeam1.setEditable(false);
		textPaneTeam1.setRequestFocusEnabled(false);
		textPaneTeam1.setFocusable(false);
		textPaneTeam1.setBorder(new LineBorder(Color.CYAN, 3, true));
		textPaneTeam1.setBackground(new Color(40, 40, 40));
		textPaneTeam1.setBounds(586, 125, 310, 160);
		contentPane.add(textPaneTeam1);
		docTeam1 = textPaneTeam1.getStyledDocument(); 								//Fetches the model associated with the editor.
		styleTeam1 = (javax.swing.text.Style) textPaneTeam1.addStyle("", null);
		StyleConstants.setForeground(styleTeam1, Color.CYAN); 
		StyleConstants.setFontSize(styleTeam1, 16);
		
		
		textPaneTeam2 = new JTextPane();
		textPaneTeam2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textPaneTeam2.setEditable(false);
		textPaneTeam2.setRequestFocusEnabled(false);
		textPaneTeam2.setFocusable(false);
		textPaneTeam2.setBorder(new LineBorder(Color.CYAN, 3, true));
		textPaneTeam2.setBackground(new Color(40, 40, 40));
		textPaneTeam2.setBounds(586, 343, 310, 160);
		contentPane.add(textPaneTeam2);
		docTeam2 = textPaneTeam2.getStyledDocument(); 								//Fetches the model associated with the editor.
		styleTeam2 = (javax.swing.text.Style) textPaneTeam2.addStyle("", null);
		StyleConstants.setForeground(styleTeam2, Color.CYAN); 
		StyleConstants.setFontSize(styleTeam2, 16);	
		
		JComboBox comboBoxTeams = new JComboBox(teams);
		comboBoxTeams.setBackground(new Color(0, 0, 0));
		comboBoxTeams.setForeground(new Color(102, 153, 255));
		comboBoxTeams.setFont(new Font("Tahoma", Font.BOLD, 15));
		comboBoxTeams.setBounds(353, 241, 159, 21);
		comboBoxTeams.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String selected = (String) comboBoxTeams.getSelectedItem();
		        if(selected == "Team 1") {
		        	team = 1;
		        	btnReadyToPlay.setEnabled(true);
		        }
		        else if(selected == "Team 2") {
		        	team = 2;
		        	btnReadyToPlay.setEnabled(true);
		        }
		        else {
					team = 0;
					btnReadyToPlay.setEnabled(false);
				}     
		    }
		});
		contentPane.add(comboBoxTeams);
		
		JLabel lblSelectTeam = new JLabel("SELECT TEAM");
		lblSelectTeam.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectTeam.setForeground(new Color(102, 153, 255));
		lblSelectTeam.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSelectTeam.setBounds(353, 204, 159, 26);
		contentPane.add(lblSelectTeam);
				
		JLabel lblLobby = new JLabel("LOBBY");
		lblLobby.setBackground(new Color(0, 0, 0));
		lblLobby.setOpaque(true);
		lblLobby.setForeground(Color.WHITE);
		lblLobby.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblLobby.setBounds(77, 89, 209, 26);
		contentPane.add(lblLobby);
		
		JLabel lblPlayer = new JLabel("PLAYER");
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer.setForeground(new Color(102, 153, 255));
		lblPlayer.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPlayer.setBounds(344, 119, 101, 26);
		contentPane.add(lblPlayer);
		
		JToggleButton tglbtnBeTheFlag = new JToggleButton("BE THE FLAG");
		tglbtnBeTheFlag.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	            if (tglbtnBeTheFlag.isSelected()) {
	               isFlag = true;
	            } else {
	               isFlag = false;
	            }
	         }
	      });
		tglbtnBeTheFlag.setBackground(Color.BLACK);
		tglbtnBeTheFlag.setFont(new Font("Tahoma", Font.BOLD, 15));
		tglbtnBeTheFlag.setForeground(new Color(102, 153, 255));
		tglbtnBeTheFlag.setBorder(new LineBorder(new Color(0, 0, 255), 3));
		tglbtnBeTheFlag.setFocusable(false);
		tglbtnBeTheFlag.setBounds(353, 354, 159, 21);
		contentPane.add(tglbtnBeTheFlag);
		
		btnStartGame = new JButton("START GAME");								///**********************************************************************************************************-------------------
		btnStartGame.setEnabled(false);
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					player = new PlayerConnection(ipAddress);
					player.sendInformationToServer("vts");							//send update to the server to validate the teams
					player = new PlayerConnection(ipAddress);
					player.sendInformationToServer("getPos");
					players = player.getPlayers();
					
					for(Player play:players)
					{
						System.out.println(play.getUsername() + " pos " + play.getPosition());
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			
				if (!player.getTeamsValidity()) {
					btnStartGame.setEnabled(false);
					//error message
					JOptionPane.showMessageDialog(contentPane, "Your teams are not valid. Please refer to the rules for future reference.");  
			
				}
			}
		});
		btnStartGame.setForeground(Color.ORANGE);
		btnStartGame.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnStartGame.setFocusPainted(false);
		btnStartGame.setBorder(new LineBorder(new Color(0, 0, 255), 3, true));
		btnStartGame.setBackground(Color.BLACK);
		btnStartGame.setBounds(756, 545, 140, 21);
		contentPane.add(btnStartGame);
		
		JLabel lblRibbon = new JLabel("");
		lblRibbon.setOpaque(true);
		lblRibbon.setBackground(new Color(0, 18, 55));
		lblRibbon.setBounds(0, 0, 984, 72);
		contentPane.add(lblRibbon);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBackground(new Color(0, 0, 0));
		lblBackground.setIcon(new ImageIcon(GameLobby.class.getResource("/Media/Background.png")));
		lblBackground.setBounds(0, 0, 984, 611);
		contentPane.add(lblBackground);
	}
	
	//Action performance that is managed by the timer event
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
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	// 							Oracle (2024). Class JTextPane. 		https://docs.oracle.com/javase/8/docs/api/javax/swing/JTextPane.html#JTextPane-javax.swing.text.StyledDocument-
	//							Oracle (2024). Java JTextPane. 			https://www.geeksforgeeks.org/java-jtextpane/
	//							
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               Greeting Server
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=		
		try {

				String user = username;												//Declare string to store the name of the user with the status

				player = new PlayerConnection(ipAddress);						
				player.sendInformationToServer("U$%");							//send update to the server to confirm that the client is connected
				
				if(status != "Offline")
				{
					usersSession = player.getUserStatus();		//get the users on session to update the list of the users
					//String panelStatusData = "";
					//clear text panel 
					textPaneLobby.setText("");
					
					for(short counter = 0; counter < usersSession.size(); counter++)	//set colors to each user based on the status
					{
						String[] statusArray = usersSession.get(counter).split("-", 2);
						if(statusArray[1].trim().equals("Online"))						//online is green
							docLobby.insertString(docLobby.getLength(), "\n    " + statusArray[0] + "\n", styleLobby);
					}	
				}
				
				//TEAMS
				player = new PlayerConnection(ipAddress);						
				player.sendInformationToServer("get");							//send update to the server to get the current list of players
				players = player.getPlayers();
				
				if(!gameInProgress && !players.isEmpty()) {
					textPaneTeam1.setText("");
					textPaneTeam2.setText("");

					for (Player player:players) 
					{
						if(player.getTeam() == 1) {
							if (player.isFlag())
								role = "FLAG";
							else
								role = "DEFENSE";
							docTeam1.insertString(docTeam1.getLength(), "\n  " + player.getUsername() + " -- " + role , styleTeam1);
						}
						else {
							if (player.isFlag())
								role = "FLAG";
							else
								role = "DEFENSE";
							docTeam2.insertString(docTeam2.getLength(), "\n  " + player.getUsername() + " -- " + role , styleTeam2);
						}
					}
				}
				
				//GAME STATE
				//player.getGameState();
				
				//GAME STATE		
				player = new PlayerConnection(ipAddress);						
				player.sendInformationToServer("ste");							//send update to the server to get the current list of players
				//System.out.println("screenOpen " + screenOpen);
				if (player != null && player.getGameState().equals("s") && !screenOpen) {
					//a game is in progress
					gameInProgress = true;
					screenOpen = true;
					tickTock.stop();
					dispose();
					//JOptionPane.showMessageDialog(btnStartGame, "disposed");
					//System.out.println("disposed ");
					GameScreen gameScreenObj = new GameScreen(ipAddress, username);
					gameScreenObj.setVisible(true);
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	
}