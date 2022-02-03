import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class server extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//constructor
	public server() {
		super("Motlatsi instant Messenger");
		userText = new JTextField();
		userText.setEditable(false  );//wont allow to type if no connection
		userText.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText(""); 
					}
				});
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(400,200);
		setVisible(true);
	}
	//setup and run the server
	
	public void startRunning() {
		try { 
			server = new ServerSocket(6789,100);
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException) {
					showMessage("\n Server ended Connection");
				}finally {
					closeCrap();
				}
			}
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//wait for connection,then display connection information
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect...\n");
		connection = server.accept();
		showMessage("Now Connected to "+ connection.getInetAddress().getHostName());

		}
	
	//get stream to send and recieve data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = "You are now connected";
		sendMessage(message);
		ableToType(true);
		do {
			//have a conversation
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException) {
				showMessage("\n idk what  the user sent");
			}
		}while(!message.equals("CLIENT - END"));
	}
	//close streams and sockets after done chatting
	private void closeCrap() {
		showMessage("\n Closing Connections...\n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	//send a message to client
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\n Server  -"+ message);
		}catch(IOException ioException) {
			chatWindow.append("\n Error cant see msg");
		}
	}
	
	//updates chatWindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(text);
				}
			}
			);
	}
	//let the user type stuff into their box
	
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
					userText.setEditable(tof);
					}
				}
				);
	}
}
