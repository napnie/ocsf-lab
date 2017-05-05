package ocsf;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.lloseng.ocsf.client.ObservableClient;

/**
 * GUI for Client.
 * @author Nitith Chayakul
 *
 */
@SuppressWarnings("serial")
public class ClientUI extends JFrame implements Observer {
	/** Client to connect to server. */
	private ObservableClient client;
	/** Text Area to show server respond. */
	private JTextArea text;
	/** Text field for prompt to send to server. */
	private JTextField prompt;
	
	/**
	 * Initialize ClientUI.
	 * @param client - client to connect to server.
	 */
	public ClientUI(ObservableClient client) {
		this.client = client;
	}
	
	/** Open windows. */
	public void run() {
		initComponents();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/** Connect to server. */
	private void connect() {
		try {
			client.openConnection();
		} catch (IOException e) {
			notify("Client", "Connection failed! "+e.getMessage());
		}
	}
	
	/** Initialize a component in ClientUI. */
	private void initComponents() {
		setTitle("Client");
		
		JPanel connectPanel = new JPanel();
		JLabel serverText = new JLabel("Server: ");
		JTextField serverInput = new JTextField(16);
		serverInput.setText( client.getHost() );
		JLabel portText = new JLabel("Port: ");
		JTextField portInput = new JTextField(5);
		portInput.setText( String.valueOf( client.getPort() ) );
		JButton connect = new JButton("Connect");
		connect.addActionListener( (event) -> {
			client.setHost( serverInput.getText() );
			client.setPort( Integer.parseInt( portInput.getText() ) );
			connect();
		});
		JButton quit = new JButton("Quit");
		quit.addActionListener( (event) -> {
			try {
				client.closeConnection();
			} catch (IOException e) {
				notify("Client", "Unable to disconnect.");
			}
		});
		connectPanel.add(serverText);
		connectPanel.add(serverInput);
		connectPanel.add(portText);
		connectPanel.add(portInput);
		connectPanel.add(connect);
		connectPanel.add(quit);
		
		final int column = 50;
		text = new JTextArea(20, column);
		text.setEditable(false);
		text.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		prompt = new JTextField(column);
		prompt.addActionListener( (event) -> sendPrompt() );
		
		this.setLayout(new BorderLayout());
		add(connectPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(prompt, BorderLayout.SOUTH);
		
		pack();
	}
	
	/** Send message to server. */
	private void sendPrompt() {
		String msg = prompt.getText();
		if( client.isConnected() ) {
			try {
				client.sendToServer(msg);
				notify("Console", msg);
			} catch (IOException e) {
				notify("Client", "error!: "+ e.getMessage());
			}
		} else {
			notify("Client", "Didn't connect.");
		}
		prompt.setText("");
	}
	
	/** Notify user. */
	private void notify(String announcer ,Object report) {
		text.append("["+announcer+"] >> " + report +"\n");
	}
	
	/** Update GUI. */
	@Override
	public void update(Observable subject, Object info) {
		notify("Server", info );
	}

}
