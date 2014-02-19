//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Student name: Lance Baker 
// Course: SENG3400 (Network & Distributed Computing)
// Student number: c3128034
// Assignment title: SENG3400 Assignment 3 
// File name: SyncServer.java
// Created: 15-10-2010
// Last Change: 20-10-2010
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

import sync.*;
import java.util.Random;
import org.omg.CosNaming.*; 
import org.omg.CosNaming.NamingContextPackage.*; 
import org.omg.CORBA.*; 
import org.omg.PortableServer.*;

public class SyncServer extends SyncPOA {
	private static final String ROOT_POA = "RootPOA";
	private static final String NAME_SERVICE = "NameService";
	private static final String SERVER_BIND_NAME = "Sync";
	private static final String RUNTIME_ERROR = "Please recheck your arguments.";
	
	// Letters of the alphabet defined in a String constant.
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
	
	/**
	* A public method providing a random character service.
	* @return char - a randomly selected alphabetic character.
	*/
	public char getCharacter() {
		// Instantiates a new Random instance (imported from the Java util package).
		// Generates a random number based on the length of the alphabet (starting at zero), and using the random value
		// it returns the character at the index position within the Alphabet String Constant (which is converted to a char array).
		return ALPHABET.toCharArray()[new Random().nextInt(ALPHABET.length())];
	}
	
	public static void main(String[] args) {
		try {
			// Initiates the ORB service based on the received arguments.
			ORB orb = ORB.init(args, null);
			// Gets a reference to the root POA and activates the POA manager.
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references(ROOT_POA));
			rootpoa.the_POAManager().activate();
			// Gets the root Naming Service Context, and converts the object reference to a NamingContextExt DataType.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references(NAME_SERVICE));
			// Instantiates the SyncServer class, and gets a Corba Object Reference (which is then narrowed down to the appropriate
			// service 'Sync' datatype). It then binds the object to the Corba Naming Service, enabling for the object to be published 
			// on the ORB under the bindname.
			ncRef.rebind(ncRef.to_name(SERVER_BIND_NAME), SyncHelper.narrow(rootpoa.servant_to_reference(new SyncServer())));
			orb.run(); // Runs the ORB, which then will wait for client invocations.
		} catch (Exception ex) { // Catches any exceptions raised (most probably from invalid arguments).
			System.out.println(RUNTIME_ERROR);
		} 
	}
}