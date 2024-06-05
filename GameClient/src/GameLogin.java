import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;

//https://medium.com/@ilakk2023/regex-to-validate-ip-address-java-d56d450d679c#:~:text=In%20the%20case%20of%20the,%5C%5C
//https://stackoverflow.com/questions/6782437/regular-expression-in-java-for-validating-username
public class GameLogin extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldIPAddress;
	private JTextField textFieldPlayerID;
	private PlayerConnection playerConnection;
	private GameLobby lobby;
	private PlayerConnection player;

	public static void main(String[] args) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameLogin frame = new GameLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameLogin() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 340);
		setTitle("Game Login");
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIPAddress = new JLabel("IP ADDRESS: ");
		lblIPAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblIPAddress.setForeground(new Color(102, 153, 255));
		lblIPAddress.setBounds(73, 121, 112, 14);
		contentPane.add(lblIPAddress);
		
		JLabel lblPlayerID = new JLabel("PLAYER ID");
		lblPlayerID.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPlayerID.setForeground(new Color(102, 153, 255));
		lblPlayerID.setBounds(73, 167, 106, 14);
		contentPane.add(lblPlayerID);
		
		JLabel lblGameTitle = new JLabel("Grab The Flag!");
		lblGameTitle.setFont(new Font("Magneto", Font.BOLD, 22));
		lblGameTitle.setForeground(Color.CYAN);
		lblGameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTitle.setBounds(151, 25, 223, 85);
		contentPane.add(lblGameTitle);
		
		JLabel lblInstructions = new JLabel("Please enter the server IP Address and a player ID with letters ");
		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstructions.setForeground(new Color(102, 153, 255));
		lblInstructions.setBounds(10, 258, 414, 14);
		contentPane.add(lblInstructions);
		
		JLabel lblInstructionsContinued = new JLabel("and/or numbers that contains from 3 to 6 characters.");
		lblInstructionsContinued.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstructionsContinued.setForeground(new Color(102, 153, 255));
		lblInstructionsContinued.setBounds(10, 276, 414, 14);
		contentPane.add(lblInstructionsContinued);
		
		JLabel lblGameLogo = new JLabel("");
		lblGameLogo.setIcon(new ImageIcon(GameLogin.class.getResource("/Media/Logo.png")));
		lblGameLogo.setBounds(78, 26, 70, 70);
		contentPane.add(lblGameLogo);
		
		textFieldIPAddress = new JTextField();
		textFieldIPAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		textFieldIPAddress.setBackground(Color.LIGHT_GRAY);
		textFieldIPAddress.setForeground(Color.BLACK);
		textFieldIPAddress.setBounds(189, 118, 169, 20);
		contentPane.add(textFieldIPAddress);
		textFieldIPAddress.setColumns(10);
		
		textFieldPlayerID = new JTextField();
		textFieldPlayerID.setFont(new Font("Tahoma", Font.BOLD, 12));
		textFieldPlayerID.setForeground(Color.BLACK);
		textFieldPlayerID.setBackground(Color.LIGHT_GRAY);
		textFieldPlayerID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					enterAction();
			}
		});
		textFieldPlayerID.setBounds(189, 164, 169, 20);
		contentPane.add(textFieldPlayerID);
		textFieldPlayerID.setColumns(10);
		
		JButton btnEnter = new JButton("ENTER LOBBY");
		btnEnter.setFocusPainted(false);
		btnEnter.setForeground(new Color(176, 196, 222));
		btnEnter.setBorder(new LineBorder(new Color(0, 0, 255), 3));
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnEnter.setBackground(Color.BLACK);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enterAction();
			}
		});
		btnEnter.setBounds(158, 213, 106, 23);
		contentPane.add(btnEnter);			
	}
	
	public boolean isValidIP(String input, String pattern) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
	    Pattern ipPattern = Pattern.compile(pattern);
	    Matcher matcher = ipPattern.matcher(input);
	    return matcher.matches();
	}
	
	public boolean isValidUsername(String input) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
		if(input.length() > 1 && input.length() < 11) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void enterAction(){
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	GreetingServer
	//
	// Method parameters	:	port
	//
	// Method return		:	void
	//
	// Synopsis				:   This method instantiates the server socket and sets a timeout for it. 
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
		String ipAddress = textFieldIPAddress.getText();
		String username = textFieldPlayerID.getText();
		String patternIp = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		String patternUser = "[a-zA-Z0-9\\._\\-]{3,6}";
		byte optionLogin;
		
		if( isValidIP(ipAddress,patternIp) && isValidIP(username,patternUser)) {
			try {
				playerConnection = new PlayerConnection(ipAddress);
				optionLogin = playerConnection.Login(username);
				if(optionLogin == 0) {
					triggerWindow(username, ipAddress);
				}
				else if(optionLogin == 1){
					player = new PlayerConnection(ipAddress);
					String infoUpdate = username+","+"Online";								//Change status to online for the returning user
					player.sendInformationToServer(infoUpdate);
					triggerWindow(username, ipAddress);
				}
				else{
					JOptionPane.showMessageDialog(null,"Username is already taken.");
				};
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Please enter a valid Username and IP Address.");
		}
	
	}
	
	public void triggerWindow(String username, String ipAddress)
	{
		lobby = new GameLobby();
		lobby.SetPlayerID(username, ipAddress, "Online");
		lobby.setVisible(true);
		dispose();
	}
	
}
