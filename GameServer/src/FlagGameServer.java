import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;
import java.io.*;
import org.json.JSONObject;

public class FlagGameServer extends Thread																	// Server is inherited from the Thread object
{
   private ServerSocket serverSocket;																				// Declaration of the socket that the server will run on
   private HashMap<String, String> usernameStatus = new HashMap<String, String>();// Declaration of userNameStatus hashMap to store the users and their status (offline/online)
   private Map<String, JSONObject> players = new HashMap<>();								// Declaration of players hashMap to store the players and its attributes
   																																	//if the user is in the players list it is not an expectator
   private String[] states = {"n", "s"}; 																				//Keep track of game states 
   private String gameState = states[0];																			// Array to store the state of the game
   private Map<String, Integer> score = new HashMap<String, Integer>(Map.of("Team1",0, "Team2",0));	// Declaration of score hashMap to store the score for each team
   private ArrayList<Point> posBenchPlayers = new ArrayList<Point>(); 							// Array to store the position of the bench for players without lives
   private int iniPosX = 35;																								// Declaration and initialization of int variable to store the initial position in X for team 1  
   private int iniPosX2 = 865;																							// Declaration and initialization of int variable to store the initial position in Y for team 2  
   private int iniPosY = 40;																								// Declaration and initialization of int variable to store the initial position in X for team 1  
   private int iniPosY2 = 40;																							// Declaration and initialization of int variable to store the initial position in Y for team 2  
   
   public FlagGameServer(int port) throws IOException
   {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method						:	FlagGameServer
	//
	// Method parameters	:	port 176.196.222
	//
	// Method return			:	void
	//
	// Synopsis					:   This method instantiates the server socket and sets a timeout for it. 
	//
	// References				:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications			:
	//											Date			Developers				Notes
	//											----			---------				-----
	//										2024-02-20		V. Arias,               
	//																J. Silva,
	//																R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      serverSocket = new ServerSocket(port);			// Instantiates the server socket
      serverSocket.setSoTimeout(0);							// Sets the timeout for the socket. A timeout of 0 establishes an infinite timeout
   }																		

   public void run()
   {
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: run()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method to run the server to receive and send information to the client.
		//				
		// References				:  Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
		//
  		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-02-21		Julian Silva			
		//															Valentina Arias
  		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
      while(true)
      {
         try
         {
            Socket server = serverSocket.accept();																	// Wait for a connection from the client
            DataInputStream in =  new DataInputStream(server.getInputStream()); 					// Get the data from the client          
            DataOutputStream out =  new DataOutputStream(server.getOutputStream()); 		// Prepare the object for returning data to the client
            
            																															//RECEIVE USERNAME OR CHAT INPUT
            String clientInput = String.valueOf(in.readUTF());													//Read the instruction of the client									
            String substring = clientInput.substring(0, 3);															// Get a substring of the first 3 characters of the chain
            
            if (substring.equals("{\"p"))																						// Update player received												
            {
            	
            	JSONObject receivedPlayer = new JSONObject(clientInput);								// Get the JSon object from the input received
            	String username = receivedPlayer.getString("username"); 									// Get username to find the player to update info
           	
            	if(players.containsKey(username))																		
            	{
            		JSONObject currentPlayer = players.get(username);  									 //get the current player
            		int currentLives = currentPlayer.getInt("lives");												// Get current lives
            		int receivedLives = receivedPlayer.getInt("lives");											// Get the lives from the data received
            		
            		if(receivedLives > currentLives)							
            			receivedPlayer.put("lives", currentLives);														// Update lives only if they're lower
            		
            		if(receivedPlayer.getInt("positionX") < 0 && currentPlayer.getInt("positionX") > 0)
            		{		
            			receivedPlayer.put("positionX", currentPlayer.getInt("positionX"));				// Update the position on the data received with the current data  
            			receivedPlayer.put("positionY", currentPlayer.getInt("positionY"));
            		}
            		
            		if(!receivedPlayer.get("status").equals("DEAD") && receivedPlayer.getInt("lives") == 0) // Check if the player has no lives but the status is not DEAD
            		{
            			receivedPlayer.put("lives", -1);																	// Set the lives to -1 to control state
            			receivedPlayer.put("status", "DEAD");
            			if(receivedPlayer.getInt("team") == 1)
            			{
            				posBenchPlayers.add(new Point(iniPosX,iniPosY));									// Set the position on the bench area
            				iniPosY += 40;
            			}
            			else
            			{
            				posBenchPlayers.add(new Point(iniPosX2,iniPosY2));
            				iniPosY2 += 40;
            			}
            			receivedPlayer.put("positionX", posBenchPlayers.get(posBenchPlayers.size()-1).x);	//Update position on the received data
            			receivedPlayer.put("positionY", posBenchPlayers.get(posBenchPlayers.size()-1).y);
            		} // if lives ==0
            		else if(receivedPlayer.getInt("lives") < 0)
            		{
            			receivedPlayer.put("status", "DEAD");															// If lives are negative, change state to Dead and update position to bench
            			receivedPlayer.put("positionX", currentPlayer.get("positionX"));
            			receivedPlayer.put("positionY", currentPlayer.get("positionY"));
            		}
            		else if(!receivedPlayer.get("status").equals("WIN") && currentPlayer.get("status").equals("WIN"))
            			receivedPlayer.put("status", "WIN");															// If the status is WIN (a team already won) do not overwrite the data
            			
            	}
            	
            	players.put(username, receivedPlayer);  															//update player information in the hashmap
        		
        		String tempString = "*" + getPlayersInSession();												// Get the players information to return it to the client
        		
            	out.writeUTF(tempString);																					// Sent the players hashMap converted to JSON to the client	
            }
            else if (substring.equals("{\"l"))																				// Update lives
            {
            	JSONObject receivedPlayer = new JSONObject(clientInput);								// Get the information received converted to a JSON
            	String username = receivedPlayer.getString("username");									// Get the username to find the player
            	
            	if(players.containsKey(username))
            	{
            		JSONObject currentPlayer = players.get(username);   									//get the current player
            		int currentLives = currentPlayer.getInt("lives");
            		int receivedLives = receivedPlayer.getInt("lives");
            		
            		if(receivedLives < currentLives)																		// Update lives only if they're lower to the current value in the server
            		{
            			currentPlayer.put("lives", receivedLives);														// Update lives of player
            			players.put(username,  currentPlayer);														// Update players list
            		}
            	}
            	
            	String tempString = "*" + getPlayersInSession();												// Get the players information to return it to the client
        		
            	out.writeUTF(tempString);																					// Sent the players hashMap converted to JSON to the client	
            }
            else if (clientInput.substring(0, 3).equals("{\"s"))														// Update score 
            {
            	JSONObject receivedData = new JSONObject(clientInput);								// Get the information received converted to a JSON
            	String team = receivedData.getString("team");													// Get the team and score
            	int scoreTeam = receivedData.getInt("score");
            	
            	score.put(team, scoreTeam);																				// Update score in the score list
            	out.writeUTF(score.toString());																			// Return the score update to the client
            }
            else if (substring.equals("get"))																				// Received the petition to update the players list on the client side
            {
            	String tempString = "*" + getPlayersInSession();												// Get the information received converted to a JSON
            	out.writeUTF(tempString);																					// Return players list to the client
            }
            else if (substring.equals("vts"))																				// VALIDATE TEAMS
            {
            	calculatePositions();																							// Calculate the position of the players at the start of the game
            	String validation = validateTeams();																	// Validate if the teams are valid or not
            	out.writeUTF(validation);																					// Return if the players are valid or not
           	
            }
            else if(substring.equals("res")) {																				// Receive the petition to reset the game screen. Including players' positions and lives. 
            	resetGame();						
            	out.writeUTF("rese");																							// Return confirmation of petition
            }
            else if(substring.equals("del")) {																				// Receive the petition to finish current game to start a new one. 
            	cleanListPlayers();																								// Remove items from the players list																					
            	gameState = states[0];																						// Update state to not valid for game			
            	out.writeUTF("dele");																							// Return confirmation of petition
            }
            else if (substring.equals("ste"))																				// GET UPDATE ON GAME STATE
            {
            	out.writeUTF(gameState);																					// Retuen the current game state
            }
            else 
            {																															// VALIDATE LOGIN
            	String[] dataReceived = clientInput.split(",", 2);													// Store the client input in a String array 
            	if(dataReceived.length > 1)																				// Check if a user is trying to login
            	{
            		usernameStatus.put(dataReceived[0].toString(), dataReceived[1].toString()); 	// Add to the hash map the username and their status 

                	out.writeUTF(usernameStatus.toString());														// Sent the usernameStatus hashMap to the client	
            	}
            	else if(!clientInput.equals("U$%")) { 																	// Check if the client sent an update
            										
            		char validation = validateUsername(clientInput);											// Validate the user depends of the status	
                    if(validation == 'G') {
                    	out.writeUTF("G");																						// Send G to the client => GOOD
            		}
                    else if(validation == 'E') 																				// If validation is equal E means that the user has been logged but can re enter to the chat	
                    	out.writeUTF("E"); 																						// Sent E to the client  => ERROR
                    else																												// In other case the user is currently online 
                    	out.writeUTF("O"); 																					// Sent O to the client => ONLINE
            	}
            	else {
            		String userList = usernameStatus.toString();													// Get the users' list to return it
            		out.writeUTF(userList);																					// Return the users's status to update the client
            	}	
            }
            server.close();																										// Shut down the connection server/client
         
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public void calculatePositions()
   {
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: calculatePositions()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method calculates the position of the players before the start of the game.
		//				
		// References				:  
		//
		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-03-21		Julian Silva			
		//															Valentina Arias
		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   
	    String tempString;																													// Declare String variable to store individual items retrieve from the players' list
	    short posX1 = 120;																												// Declare short variable to store the initial X positions of the players (team 1)
		short posX2 = 780;																												// Declare short variable to store the initial X positions of the players (team 2)
		short posY = 140; // add 40 per player																					// Declare short variable to store the initial Y positions of the players 
		int counter1=0, counter2 = 0;																								// Counter variables to control the position asignations to the player 
		
		
	   for (Entry<String, JSONObject> playersList : players.entrySet()) 												// Loop to go through the players' list
		{	
			tempString = playersList.getValue()+" "; 																			// Store the user information in the temp string
			JSONObject tempJson = new JSONObject(tempString);														// Get the information received converted to a JSON 
			short team = (short) tempJson.getInt("team");																	// Get the team of the player
																																					// increase counters for necessary variables
			if(team == 1)																													// Set the positions to players
			{
				tempJson.put("positionX", posX1);
				tempJson.put("positionY", (posY + (counter1 * 40)));
				counter1++;
			}
			else if(team == 2)
			{
				tempJson.put("positionX", posX2);
				tempJson.put("positionY", (posY + (counter2 * 40)));
				counter2++;
			}
			players.put(tempJson.getString("username"), tempJson);													// Update the players' list with the positions 
			
		}
   }
   
   public void resetGame()
   {
	   
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: resetGame()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method reset the postions of the players to start a new game..
		//				
		// References				:  
		//
		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-03-21		Julian Silva			
		//															Valentina Arias
		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   
	    String tempString;																													// Declare String variable to store individual items retrieve from the players' list
	    short posX1 = 120;																												// Declare short variable to store the initial X positions of the players (team 1)
		short posX2 = 780;																												// Declare short variable to store the initial X positions of the players (team 2)
		short posY = 140; // add 40 per player																					// Declare short variable to store the initial Y positions of the players 
		int counter1=0, counter2 = 0;																								// Counter variables to control the position asignations to the player 
		
		
	   for (Entry<String, JSONObject> playersList : players.entrySet()) 												// Loop to go through the players' list
		{	
			tempString = playersList.getValue()+" "; 																			// Store the user information in the temp string
			JSONObject tempJson = new JSONObject(tempString);														// Get the information received converted to a JSON 
			short team = (short) tempJson.getInt("team");																	// Get the team of the player
																																					// increase counters for necessary variables
			if(team == 1)																													// Set the positions to players
			{
				tempJson.put("positionX", posX1);
				tempJson.put("positionY", (posY + (counter1 * 40)));
				counter1++;
			}
			else if(team == 2)
			{
				tempJson.put("positionX", posX2);
				tempJson.put("positionY", (posY + (counter2 * 40)));
				counter2++;
			}
				tempJson.put("lives", 10);																								// Reset lives to initial values
				tempJson.put("status", "DEFENSE");																				// Reset the status to defense 
				players.put(tempJson.getString("username"), tempJson);												// Update the players' list with the positions 
		}
   }
   
   public void cleanListPlayers() {
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: cleanListPlayers()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method removes all the items from the players' list.
		//				
		// References				:  
		//
		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-03-21		Julian Silva			
		//															Valentina Arias
		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   players.clear();
   }
   
   public String validateTeams() {
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: validateTeams()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method checks if the teams are valid, when the Start Game button is pressed. 
		//				
		// References				:  
		//
		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-03-21		Julian Silva			
		//															Valentina Arias
		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   //have counter for members of each team, make sure they are even and from 2-4 per team.
	   //make sure there is only one flag per team
	   //if it is not valid, return f 'false', else return t 'true'
	   short numOfPlayersTeam1 = 0;																					// Declare short variable to store the number of player of team1
	   short numOfPlayersTeam2 = 0;																					// Declare short variable to store the number of player of team2
	   short numOfFlagsTeam1 = 0;																						// Declare short variable to store the number of flags of team1
	   short numOfFlagsTeam2 = 0;																						// Declare short variable to store the number of flags of team2
	   short team;																												// Declare short variable to store the team value
	   boolean isFlag;																											// Declare boolean variable to control if is a flag
	   String tempString;																										// Declare String variable to store the player item retrieve from the list on the loop
	   
	   for (Entry<String, JSONObject> playersList : players.entrySet()) 
		{		
			tempString = playersList.getValue()+" ";  																// Store the user information in the temp string
			JSONObject tempJson = new JSONObject(tempString);											// Convert the data retrieve to JSON
			isFlag = tempJson.getBoolean("isFlag");																	// Get the falg value
			team = (short) tempJson.getInt("team");																// Get the team value
			
			switch(team) {																										//Swich to increment values to compare the composition of the teams
			case 1:
				numOfPlayersTeam1++;
				if (isFlag) 
					numOfFlagsTeam1++;
				break;
			case 2:
				numOfPlayersTeam2++;
				if (isFlag) 
					numOfFlagsTeam2++;
				break;
			}
		}
	   
	   	//validate
		if (numOfFlagsTeam1 != 1 || numOfFlagsTeam2 != 1) {
			return "f"; 																											//false -  not valid teams
		}
		else if (numOfPlayersTeam1 != numOfPlayersTeam2) {
			return "f"; 																											//false -  not valid teams
		}
		else if (numOfPlayersTeam1 < 2 || numOfPlayersTeam1 > 4) {
			return "f";  																											//false -  not valid teams
		}
		else  
		{
			gameState = states[1];																							// Set the game state to valid
			return "s"; 																											//true - valid teams
		}
	   
   }
   
   public String getPlayersInSession()
   {
	     // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						: getPlayersInSession()
		//
		// Method parameters	: none
		//
		// Method return			: none
		//
		// Synopsis					:  This method gets the users connected to the server.
		//				
		// References				:  
		//
		// Modifications			:
		//										Date			Developers				Notes
		//										----			---------				-----
		//									2024-03-21		Julian Silva			
		//															Valentina Arias
		//															Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   	ArrayList<String> playersInSession = new ArrayList<String>();							// Define a local  arraylist variable of users
		String tempString = "";																						// Declarate a temp String
		
		for(Entry<String, String> usersList : usernameStatus.entrySet()) {
			if (usersList.getValue().contains("Offline")) {
				players.remove(usersList.getKey());																//remove offline users from the players list
			}
		}
		
		for (Entry<String, JSONObject> playersList : players.entrySet()) 
		{				
			tempString = playersList.getValue()+" ";  														// Store the user information in the temp string
			playersInSession.add(tempString);																	// Add the tempString to the list						
		}		
		
		tempString = "";
		for(String players_t : playersInSession)
			tempString += players_t.toString();	
		
		return tempString;																							// Return list of users to the client
   }
   
   public char validateUsername(String username)
   {
	   	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
		// Method						:validateUsername(String username)
		//
		// Method parameters	: String username
		//
		// Method return			: char
		//
		// Synopsis					: This method check the user status and add to the list or return a character depends of the status.
		//				
		// References				:   
		//
	   	// Modifications			:
		//											Date			Developer				Notes
		//											----			---------				-----
		//										2024-02-21		Julian Silva			
		//																Valentina Arias
 		//																Rosibel Useda
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=   			
       if(! usernameStatus.containsKey(username)) {									// Check if the user is not registered on the hashMap
       	usernameStatus.put(username, "Online");										// Add the user and the status to the hashMap					
       	
       	return 'G';																						// Return G in case of the new user
       }
       else {																								// In other cases
    	 String status = usernameStatus.get(username);							// Get the status of the user
    	 if(status.equals("Offline")) {															// Check if the user is offline	
    		 return 'E';																					// Return E in case the user is offline	
    	 }
    	 else {																							// In other case
    		 return 'O';																				// Return O that means the user is online or idle		
    	 } 	 
       }     
   }
   
   public static void main(String [] args)
   {
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	// Method						:	GreetingServer
	//
	// Method parameters	:	none
	//
	// Method return			:	void
	//
	// Synopsis					:   This method instantiates the server socket and sets a timeout for it. 
	//
	// References				:   Johnson S.(2024) GreetingServer example. Interactive Media: Network Programming.
	//
	// Modifications			:
	//												Date			Developers				Notes
	//												----			---------				-----
	//											2024-03-08		V. Arias,               Greeting Server
	//																	J. Silva,
	//																	R. Useda
	//
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	   
      int port = 6066;														// Establish port 6066 as a hard-coded port override
      try
      {
         Thread t = new FlagGameServer(port);					// Instantiates a server on a separate thread
         t.start();															  	// Executes the server
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}