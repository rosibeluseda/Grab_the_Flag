# Grab_the_Flag
This small game is designed for network play, allowing up to four players to connect to a live server and see each other's moves in real-time. The objective is to capture the opponent's flag, with each team having one flag. The first team to win two out of three rounds wins the match.
<p align="center">
     <img src="https://github.com/rosibeluseda/Grab_the_Flag/assets/145386489/cbc9fae2-f233-4575-8771-ed9e0615e1b7" alt="gif">
</p>

# Connection between the client and the server
Communication between the server and clients is established using sockets in Java. This allows for real-time data exchange and ensures a robust and reliable connection. The server listens for incoming connections, while clients initiate the communication. Once connected, they can send and receive messages, facilitating seamless interaction.
```java
 while(true)
      {
         try
         {
            Socket server = serverSocket.accept();					// Wait for a connection from the client
            DataInputStream in =  new DataInputStream(server.getInputStream()); 	// Get the data from the client          
            DataOutputStream out =  new DataOutputStream(server.getOutputStream());	// Prepare the object for returning data to the client

            String clientInput = String.valueOf(in.readUTF());				//Read the instruction of the client									
            ....
	
            }
            server.close();								// Shut down the server
         
         }
      }
```
```java
 client = new Socket(serverName, port);							// instantiate the socket to connect to server
	        OutputStream outToServer = client.getOutputStream();			// initialize the output stream
	        out = new DataOutputStream(outToServer);				// instantiate the data output stream
	        InputStream inFromServer = client.getInputStream(); 			// initialize the input stream
	        in = new DataInputStream(inFromServer);   				// instantiate the data input stream
```

# Login screen and rules
<p align="center">
     <img src="https://github.com/rosibeluseda/Grab_the_Flag/assets/145386489/0e1d96b2-0185-4107-9739-d42beb404204" alt="Login Screen">
</p>
<p align="center">
     <img src="https://github.com/rosibeluseda/Grab_the_Flag/assets/145386489/1554f9f8-b5e6-460a-8090-9da65dab9fcb" alt="Rules">
</p>


