//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Student name: Lance Baker 
// Course: SENG3400 (Network & Distributed Computing)
// Student number: c3128034
// Assignment title: SENG3400 Assignment 3 
// File name: DeferredSynchronous.java
// Created: 20-10-2010
// Last Change: 23-10-2010
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

import sync.Sync;

/**
* The DeferredSynchronous class extends Thread, and receives the Sync service via the contructor on instantiation.
* It enables the ability for the service call to be invoked, with the service response temporarily stored, 
* until the client requests for the response. Once the client requests for the response, it is unknown whether
* the response has actually been returned, therefore it enters a delay period (until the temporary variable 
* has changed) and then returns the response value.
*/
public class DeferredSynchronous extends Thread {
	private static final char STARTING_VALUE = '*';
	private static final int DELAY = 25;
	private char letter; // A temporary letter to hold the response.
	private Sync sync; // A reference to the Sync service.
	
	/**
	* The DeferredSynchronous constructor, which receives the Sync service reference.
	* @param Sync - The service object.
	*/
	public DeferredSynchronous(Sync sync) {
		this.sync = sync; // Assigns the service to a instance reference.
		this.letter = STARTING_VALUE; // Sets the starting value of the temporary variable.
	}
	
	/**
	* The run method is invoked once the Thread is started.
	*/
	public void run() {
		// Assigns the response from the character service to the temporary variable.
		this.letter = this.sync.getCharacter();
	}
	
	/**
	* The getResult method enables for polling to be achieved. It enters an loop
	* which delays further execution until the temporary variable has been changed.
	* @return char - The random character generated from the service.
	*/ 
	public char getResult() {
		// Waits until the temporary value receives the service response.
		while(this.letter == STARTING_VALUE) {
			try {
				// Delays each iteration slightly.
				Thread.sleep(DELAY); 
			} catch (Exception e) {}
		}
		// Once the letter has been changed, it will return the value.
		return this.letter;
	}
}