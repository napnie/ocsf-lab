package ocsf;

/**
 * Main class to run Client.
 * @author Nitith Chayakul
 *
 */
public class Main {
	
	/**
	 * Run ClientUI.
	 * @param arg
	 */
	public static void main(String[] arg) {
		MyClient client = new MyClient("", 0);
		ClientUI ui = new ClientUI(client);
		client.addObserver(ui);
		ui.run();
	}

}
