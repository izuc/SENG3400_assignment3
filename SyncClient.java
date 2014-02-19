//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Student name: Lance Baker 
// Course: SENG3400 (Network & Distributed Computing)
// Student number: c3128034
// Assignment title: SENG3400 Assignment 3 
// File name: SyncClient.java
// Created: 15-10-2010
// Last Change: 23-10-2010
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

import sync.*;
import org.omg.CosNaming.*; 
import org.omg.CosNaming.NamingContextPackage.*; 
import org.omg.CORBA.*;

public class SyncClient {
	// Various constants used throughout this class.
	private static final String NAME_SERVICE = "NameService";
	private static final String SERVER_BIND_NAME = "Sync";
	private static final String SEPARATOR = " - ";
	private static final String LINE_GAP = "---------------------";
	private static final String TITLE_DEFERRED_SYNCHRONOUS = "Deferred Synchronous";
	private static final String TITLE_ASYNCHRONOUS = "Asynchronous";
	private static final String LINE_SEPARATOR = "line.separator";
	private static final String NUMERIC_PRINT_FORMAT = "%02d ";
	private static final String RUNTIME_ERROR = "Please recheck your arguments.";
	
	private static final int DEFAULT_ITERATIONS = 5;
	private static final char STARTING_VALUE = '*';
	
	// The enumerated type which is used to determine the synchronous method mode.
	private enum SyncType {DEFERRED_SYNCHRONOUS, ASYNCHRONOUS};
	private Thread thread; 
	private char letter;
	private int iterations;
	private Sync sync;
	
	/**
	* The constructor for the SyncClient
	* @param Sync - Receives the reference to the service.
	*/
	public SyncClient(Sync sync) {
		this.sync = sync; // Assigns the received SyncServer stub object reference to an instance variable.
	}
	
	/**
	* Performs the synchronous interation based on the received Enumerated type.
	* @param SyncType - An enumerated type representing the mode, either being deferred synchronous or asynchronous.
	*/
	public void synchronous(SyncType type) {
		this.letter = STARTING_VALUE; // Initialises the starting values. 
		this.iterations = DEFAULT_ITERATIONS; // Sets the max interation count to 5.
		
		for (int i = 1; i <= this.iterations; i++) {
			// Displays the iteration counter and the content of the letter instance variable.
			System.out.printf(NUMERIC_PRINT_FORMAT + SEPARATOR + this.letter + System.getProperty(LINE_SEPARATOR), i);
			
			// Only after 5 iterations it will proceed.
			if (i >= DEFAULT_ITERATIONS) {
				// Determines the synchronous mode.
				switch(type) {
					// In the case of deferred synchronous interaction.
					case DEFERRED_SYNCHRONOUS:
						// Once 5 iterations have been reached.
						if (i == DEFAULT_ITERATIONS) {
							// Instantiates the DeferredSynchronous Thread, passing the service object into the constructor.
							this.thread = new DeferredSynchronous(this.sync);
							// Starts the Thread
							this.thread.start();
							// Continues for a further 5 iterations after the service call.
							this.iterations += DEFAULT_ITERATIONS; 
						// Once 10 iterations have been reached.
						} else if (i == (DEFAULT_ITERATIONS * 2)) {
							if (this.thread != null) { // Ensures that the Thread was created.
								// The value retrieved from the service call will need to be syncronized with the letter
								// instance variable. This is achieved through the use of polling, and once the Thread has
								// the result, it will return the value. The value returned is passed to the synchroniseLetter method. 
								this.synchroniseLetter(((DeferredSynchronous)this.thread).getResult());
							}
						}
						break;
					// In the case of asynchronous interaction.
					case ASYNCHRONOUS:
						// Once 5 iterations have been reached.
						if (i == DEFAULT_ITERATIONS) {
							// Instantiates the Asynchronous Thread, passing the client (this) and the Sync service 
							// into the constructor. The thread is then started, and it will synchronise the response with the client
							// once the service invocation has returned a value.
							this.thread = new Asynchronous(this, this.sync);
							this.thread.start();
						}
						// If the iteration is at the end of the specified count and the letter is still
						// at the starting value, then it increments the count variable to enable the 
						// loop to iterate for an inderminate number of iterations.
						this.iterations = (((i == this.iterations) && (this.letter == STARTING_VALUE))? this.iterations += 1: this.iterations);
						break;
				}
			}
		}
	}
	
	/**
	* The synchroniseLetter method is used by both synchronous types, which assigns the response letter to the 
	* instance letter variable and increases the loop iterations by another five.
	* @param char - the letter response from the service call.
	*/
	public void synchroniseLetter(char letter) {
		this.letter = letter; // Assigns the response letter to the instance variable.
		this.iterations += DEFAULT_ITERATIONS;  // Increases the iterations by another 5.
	}
	
	public static void main(String[] args) {
		try {
			// Initiates the ORB service based on the received arguments.
			ORB orb = ORB.init(args, null);
			// Fetches the naming service from the ORB.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references(NAME_SERVICE));
			// Using the defined constant, it grabs the binded SynServer object based on the value.
			// Narrows the object to a usable DataType that represents the SyncServer object (which is really only a stub object).
			// Passes the SyncServer stub object to the SyncClient constructor, thereby instantiating a new client object instance 
			// assigned to a local variable.
			SyncClient client = new SyncClient(SyncHelper.narrow(ncRef.resolve_str(SERVER_BIND_NAME)));
			
			// Demonstrates the Deferred Synchronous interation.
			System.out.println(LINE_GAP); 
			System.out.println(TITLE_DEFERRED_SYNCHRONOUS);
			System.out.println(LINE_GAP);
			// Invokes the synchronous method on the client object,
			// passing the DEFERRED_SYNCHRONOUS enumerated type as an argument.
			client.synchronous(SyncType.DEFERRED_SYNCHRONOUS);
			System.out.println(LINE_GAP);
			
			// The following demonstrates the Asynchronous interation.
			System.out.println(TITLE_ASYNCHRONOUS); 
			System.out.println(LINE_GAP);
			// Invokes the synchronous method on the client object,
			// passing the ASYNCHRONOUS enumerated type as an argument.
			client.synchronous(SyncType.ASYNCHRONOUS);
			System.out.println(LINE_GAP);
			
		} catch(Exception e) { // Catches any exceptions (most probably caused by invalid arguments).
			System.out.println(RUNTIME_ERROR);
		} 
	}
}