import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*; 

public class Client {
	
	
	//we are gonna keep all our swing components statics bc all these components are gonna be same for all our chat clients 
	//also we wanna be able to access them without creating a object of JFrame type 
	
	static JFrame chatWindow = new JFrame("Chat Application"); 
	static JTextArea chatArea = new JTextArea(22,40);
	static JTextField textField = new JTextField (40);
	static JLabel blankLabel = new JLabel("               ") ;	
	static JButton sendButton = new JButton("Send");
	static BufferedReader in; 
	static PrintWriter out; 
	static JLabel nameLabel = new JLabel("      "); 
	
	
	
	Client(){
		
		chatWindow.add(nameLabel);
		chatWindow.setLayout(new FlowLayout());
		
		chatWindow.add(new JScrollPane(chatArea)); 
		chatWindow.add(blankLabel);
		chatWindow.add(textField);
		chatWindow.add(sendButton);
	
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		chatWindow.setSize(480, 500);
		chatWindow.setVisible(true);
		
		
	
		textField.setEditable(false);
		chatArea.setEditable(false);
		
		//when the user clicks on send the message will be send to the server with the help of actionListener 
		sendButton.addActionListener(new Listener()); 
		//when user click on enter 
		textField.addActionListener(new Listener()); 
		
		
	}
	
	void startChat() throws Exception{
		
		String ipAddress = JOptionPane.showInputDialog(chatArea, "Enter IP Address:" , "IP Address Required!" , JOptionPane.PLAIN_MESSAGE); 
		
		int  portNum = Integer.parseInt(JOptionPane.showInputDialog(chatArea, "Enter Port number of the machine you are trying to conncet:" , "Port Number Required!" , JOptionPane.PLAIN_MESSAGE));
		
	if (portNum != 61910) {
		
		
		  portNum = Integer.parseInt(JOptionPane.showInputDialog(chatArea, "Incorrect Port number enter again :" , "Port Number Required!" , JOptionPane.PLAIN_MESSAGE));
			
		} 
		
		Socket soc = new Socket(ipAddress , portNum) ; 
		
		in = new BufferedReader(new InputStreamReader(soc.getInputStream())) ; 
		
		out = new PrintWriter(soc.getOutputStream(),true); 
		
		
	
		
		while(true) {
			
			//to read messsage that server sends us 
			String str = in.readLine();
			
			if(str.equals("Name required!")) {
				
				String name = JOptionPane.showInputDialog(chatWindow, "Enter a unique name:" , "Name Required!" , JOptionPane.PLAIN_MESSAGE); 
				
				out.println(name);
				
				
			}
			else if (str.equals("Name already exists")) {
				
				String name = JOptionPane.showInputDialog(chatWindow, "Enter another name:" , "Name already exist!" , JOptionPane.WARNING_MESSAGE); 
				
				out.println(name);
				
				
			}
			//we are using starts with instead of .euqls cuz we have to read a concatenated siting 
			else if(str.startsWith("Name accepted")){
				
				textField.setEditable(true); 
				// substring ignores first 12 characters and only gives us client name 
				nameLabel.setText("You are logged in as: "+str.substring(13));
				
			}
			else {
				
				chatArea.append(str + "\n");
			}
			
		}
	
	}
	
	public static void main(String[] args) throws Exception{
		
		Client client = new Client(); 
		client.startChat();
		
		

	}
}

//invoves when an action occures
class Listener implements ActionListener {
	
	@Override
	
	public void actionPerformed(ActionEvent e) { 
		//reciving the message from the clients then sending it to the server 
		Client.out.println(Client.textField.getText());
		//clearing the text field 
		Client.textField.setText("");
		
		
	}
	
	
}
















