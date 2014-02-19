//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Student name: Lance Baker 
// Course: SENG3400 (Network & Distributed Computing)
// Student number: c3128034
// Assignment title: SENG3400 Assignment 3 
// File name: Asynchronous.java
// Created: 20-10-2010
// Last Change: 23-10-2010
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

import sync.Sync;

/**
* The Asynchronous class extends Thread. It receives the SyncClient object and the Sync service via the 
* constructor on instantiation. The class enables the ability for a service request to be made, and the
* response from the request to be returned to the client without the client actually requesting for it.
*/
public class Asynchronous extends Thread {
	private SyncClient client; // A reference to the client object.
	private Sync sync; // A reference to the Sync service.
	
	/**
	* The constructor receives the SyncClient, and the Sync service. 
	* @param SyncClient - The client is required in order to synchronise the response.
	* @param Sync - Receives the reference to the service.
	*/
	public Asynchronous(SyncClient client, Sync sync) {
		this.client = client; // Assigns the client object reference to a instance variable.
		this.sync = sync; // Assigns the service stub reference to a instance variable.
	}
	
	/**
	* The run method is invoked once the Thread is started. It invokes the character service,
	* and passes the response to the client via the synchroniseLetter method.
	*/
	public void run() {
		this.client.synchroniseLetter(this.sync.getCharacter());
	}
}
