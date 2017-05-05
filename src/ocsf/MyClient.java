package ocsf;

import com.lloseng.ocsf.client.ObservableClient;

/**
 * Client to connect to server.
 * @author Nitith Chayakul
 *
 */
public class MyClient extends ObservableClient {

	/**
	 * Initialize MyClient.
	 * @param host - IP of the server.
	 * @param port - port of the server.
	 */
	public MyClient(String host, int port) {
		super(host, port);
	}
	
	/**
	 * Give message from server to obverser.
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		setChanged();
		notifyObservers( msg );
	}
	
	/** Give message to observer when connection is closed. */
	@Override
	protected void connectionClosed() {
		String msg = "Connection to " + getHost() + ":" + getPort() + " has been closed.";
		setChanged();
		notifyObservers(msg);
	}
	
	/** Give message to observer when connection is established. */
	@Override
	protected void connectionEstablished() {
		String msg = "Connection to " + getHost() + ":" + getPort() + " has been established.";
		setChanged();
		notifyObservers( msg );
	}
}
