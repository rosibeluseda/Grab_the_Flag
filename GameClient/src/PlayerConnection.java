import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

	public class PlayerConnection {
	private int port = 6066;															// establish the number of the port
	private Socket client;																// declare the socket for the client
	private DataOutputStream out;														// declare the data output stream object
	private DataInputStream in;															// declare the data input stream object
	private String incomingData;														// declare a string for the incoming data
	private DefaultListModel<String> chatStrings = new DefaultListModel<String>();		// Declare list model to store the chat history  
	private HashMap<String, String> usernameStatus = new HashMap<String, String>();		// Declare the hashMap to store the users and their status
	private ArrayList<Player> players = new ArrayList<Player>();
	private Map<String, Integer> score = new HashMap<String, Integer>(Map.of("Team1",0, "Team2", 0));	      //https://www.baeldung.com/java-initialize-hashmap
	private boolean teamsValidationResult;
	private String gameState;

	
	public PlayerConnection(String serverName) throws IOException {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	ClientConnection(String serverName)
	//
	// Method parameters	:	String serverName
	//
	// Method return		:	void
	//
	// Synopsis				:   This method is the constructor of the client. 
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,               
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		try
	      {
	        client = new Socket(serverName, port);										// instantiate the socket to connect to server
	        OutputStream outToServer = client.getOutputStream();						// initialize the output stream
	        out = new DataOutputStream(outToServer);									// instantiate the data output stream
	        InputStream inFromServer = client.getInputStream(); 						// initialize the input stream
	        in = new DataInputStream(inFromServer);   									// instantiate the data input stream
	      }catch(IOException e)
	      {
	    	 JOptionPane.showMessageDialog(null,"No connection, try again.");			// if there is an error show an error dialog 
		  }
	}


	public String getGameState() {
		return gameState;
	}
	
	public void sendInformationToServer(String information) 
	{
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	sendInformationToServer(String information)
	//
	// Method parameters	:	String information
	//
	// Method return		:	void
	//
	// Synopsis				:   This method send information to server from client. 
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
		String substring;													// Declare substring variable to store a portion of the information 		
		String modifiedString;												// Declare a modifiedString variable
		String [] commaSeparated;											// Declare a comma separated string array 
		String [] parts;													// Declare a parts string array
		
		try
	      {
			
	         out.writeUTF(information); 									// send information out to the server 
	         incomingData = new String(in.readUTF());						// read the incoming drawing sequence from the server
	         
	         
	         if(incomingData.length() > 1) 									// Check if the incoming data has more than 1 character 			
	         {
	        	 substring = incomingData.substring(0, 2);					// Get the first 3 characters of the incomig data
	        	
	        	 if(incomingData.substring(0, 4).equals("{\"po"))
	        	 {
	        		 //System.out.println("Incoming data " + incomingData);
	        		 updatePlayerList();
	        			 
	        	 }
	        	 else  if(incomingData.substring(0, 3).equals("{Te"))
	        	 {
	        		 modifiedString = incomingData.substring(1, incomingData.length() - 1);	 // Store a substring of the modified string 
	      	         commaSeparated = modifiedString.split(", ");		// Separated the modified string
	      	       
		      	       for (String item : commaSeparated) 				// Iterate through   
		      	       {
		      	            parts = item.split("=");					// Split each item by "="	
		      	          
		      	            if (parts.length == 2) 						// Check if the split result has 2 parts: a key and a value
		      	            {
		      	            	
		      	            	score.put(parts[0], Integer.valueOf(parts[1]));	// parts[0] is the key, parts[1] is the value  ////***************************
		      	            	
		      	            } 
		        	   }
		      	       //System.out.println(score.toString());
	        	 }
	        	 else if(incomingData.substring(0, 1).equals("{"))			// In other case check if it is a JSON
	        	 {
		        		 modifiedString = incomingData.substring(1, incomingData.length() - 1);	 // Store a substring of the modified string 
		      	         commaSeparated = modifiedString.split(", ");		// Separated the modified string
		        		 
			      	       for (String item : commaSeparated) 				// Iterate through   
			      	       {
			      	            
			      	            parts = item.split("=");					// Split each item by "="	
		
			      	            if (parts.length == 2) 						// Check if the split result has 2 parts: a key and a value
			      	            {
			      	            	usernameStatus.put(parts[0], parts[1]);	// parts[0] is the key, parts[1] is the value
			      	            } 
			        	   }
	        	 }
	        	 else if(substring.equals("*{"))
	        	 {
	        		 //System.out.println("Incoming data " + incomingData);
	        		 updatePlayerList();	 
	        	 }
	        	 else if(substring.equals("del"))
	        	 {
	        		 gameState = "n";
	        		 players.clear();
	        	 }
	        	 client.close();  													// close the server connection
	         }
	         else {
	        	 //validate the teams and receive f if not valid
	        	 if (incomingData.equals("f")) {
	        		 teamsValidationResult = false;
	        	 }
	        	 else {
	        		 //receive the game state
	        		 gameState = incomingData;
	        		 teamsValidationResult = true;	
	        		 }
	         }
	      }
	      catch(Exception e)
	      {
	    	 JOptionPane.showMessageDialog(null, "No Server Connection.");		// display an error message if an exception occurs 
	         e.printStackTrace();												// and print the stack trace
	      }
	}
	
	public boolean getTeamsValidity() {
		if(teamsValidationResult) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updatePlayerList()
	{
		Pattern pattern = Pattern.compile("\\{[^}]*\\}");
	     Matcher matchPattern = pattern.matcher(incomingData);

	     while (matchPattern.find()) 
	     {
	            // Extract the JSON object string
	            String playerJsonStr = matchPattern.group();

	            // Parse the JSON object
	            JSONObject playerJson = new JSONObject(playerJsonStr);

	            // Extract player data
	           // boolean attacking = playerJson.getBoolean("attacking");
	            String playerStatus = playerJson.getString("status");
	            short lives = (short) playerJson.getInt("lives");
	            boolean isFlag = playerJson.getBoolean("isFlag");
	            short team = (short) playerJson.getInt("team");
	            Point position_point = new Point(0, 0);
              int x = playerJson.getInt("positionX");
              int y = playerJson.getInt("positionY");
               position_point = new Point(x, y);
	            short radius = (short) playerJson.getInt("radius");
	            String username = playerJson.getString("username");

	            Player player = new Player();
	            player = player.createPlayer(username, isFlag, lives, position_point, team, radius, playerStatus); 	// Create a new Player object and add it to the list
	            players.add(player);
	      }

	}
	
	
	public byte Login(String username) {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	Login(String username, String ipAddress)
	//
	// Method parameters	:	String username, String ipAddress
	//
	// Method return		:	byte
	//
	// Synopsis				:   This method allow to login the user to the server. 
	//
	// References			:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications		:
	//							Date			Developers				Notes
	//							----			---------				-----
	//							2024-02-20		V. Arias,              
	//											J. Silva,
	//											R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		String incomingData;											// Declare incoming data variable
		String substring;												// Declare substring data variable
		
		try {		
			
			out.writeUTF(username);										// Use the write utf to send the instruction the server

			incomingData = in.readUTF();								// Store the data in the sequence string variable
			client.close();												// Close the client instance		

			if(incomingData.equals("G"))								// Check if the length is greater than 1 
			{
				return 0;												// Return 0 in this case, it is a new username/player
			}
			else  if(incomingData.equals("E"))							//In case of receive E means user is offline
				return 1;												//Return 1
			else
				return 2;												//Return 2 in case the user is used
			
																		//close the connection instance
		} catch (IOException e) {														//catch block
			JOptionPane.showMessageDialog(null,"Connection not established");			//show a dialog message if the client couldn't connect with server
		}
		return 2;														//Return in other case
	}
		
	public ArrayList<String>  getUserStatus() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	getUserStatus()
	//
	// Method parameters	:	none
	//
	// Method return		:	ArrayList<String>
	//
	// Synopsis				:   This method allows to get a list with the user status . 
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
		ArrayList<String> usersSession = new ArrayList<String>();									// Define a local  arraylist variable of users
		String tempString;																			// Declarate a temp String
		
		for (Map.Entry<String, String> usersList : usernameStatus.entrySet()) {						// Loop through the hashMap				
            tempString = usersList.getKey() + " - " + usersList.getValue()+"\n";					// Store the user information in the temp string
            usersSession.add(tempString);															// Add the tempString to the list				
        }
		return usersSession;																		// Return the arraylist
	}
	
	public DefaultListModel<String> getChatStrings() {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method				:	getChatStrings()
	//
	// Method parameters	:	none
	//
	// Method return		:	DefaultListModel<String>
	//
	// Synopsis				:   This method allows to get the list with the chat history. 
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
		return chatStrings;																			//Return the chat history list
	}

	public ArrayList<Player> getPlayers()
	{
		return players;
	}
		
	public int getScore(int team)
	{
		String Team = "Team";
		if(team == 1)
			Team += "1";
		else
			Team += "2";
		//System.out.println("Score **** " + score.get(Team) + "  -  " + Team);
		return score.get(Team);
	}
}