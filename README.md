-----------------------------------------------------------------------------------------
########### Installing #################################################################
-----------------------------------------------------------------------------------------
* Please place the five files (Asynchronous.java, DeferredSynchronous.java, SyncClient.java, 
SyncServer.java, and sync.idl) within the same directory.

idlj -fclient sync.idl
idlj -fserver sync.idl
javac sync\*.java
javac SyncServer.java
javac SyncClient.java

-----------------------------------------------------------------------------------------
########### Running ####################################################################
-----------------------------------------------------------------------------------------

start orbd -ORBInitialPort <port_number>
start java SyncServer -ORBInitialHost <hostname> -ORBInitialPort <port_number>
java SyncClient -ORBInitialHost <hostname> -ORBInitialPort <port_number>

-----------------------------------------------------------------------------------------
########### Output Example #############################################################
-----------------------------------------------------------------------------------------
The following output of the client application demonstrates two synchronous types
(deferred synchronous and asynchronous) by displaying the iteration number, and the
letter character stored within the client for each iteration. Both types invoke the 
service to receive the random letter character at iteration number 05. 

•	The deferred synchronous type sends the request to the service through a separate 
	thread, the thread is then started, and the response from the service invocation 
	is temporarily stored until the client requests for it.

	The process of requesting for the response involves in entering a delay loop, 
	which delays further code execution until the service responds, the response is then 
	returned back to the client. The deferred synchronous type waits for 5 iterations 
	until it requests for the response, and once the response has been requested; 
	the client will synchronise the value. The synchronisation process involves in 
	assigning the returned value to the client instance variable and increasing the 
	iterations count by 5.
 
	The deferred synchronous type will always perform in the same fashion, and will wait 
	until a response is received before continuing, therefore it will always show the 
	changed value from iteration 11 to 15.


• 	The asynchronous type uses another thread to make the service call. The client is 
	passed as a reference, and once a response is received from the service it will 
	invoke the synchronisation on the client without the client issuing for the request. 
	The loop will continue for an indeterminate number of iterations until synchronisation 
	takes place; the synchronisation will assign the response value to the letter instance, 
	and cause the loop to only iterate for another 5 iterations (after which the loop 
	terminates).


---------------------
Deferred Synchronous
---------------------
01  - *
02  - *
03  - *
04  - *
05  - *
06  - *
07  - *
08  - *
09  - *
10  - *
11  - Q
12  - Q
13  - Q
14  - Q
15  - Q
---------------------
Asynchronous
---------------------
01  - *
02  - *
03  - *
04  - *
05  - *
06  - *
07  - *
08  - *
09  - *
10  - *
11  - *
12  - *
13  - *
14  - *
15  - *
16  - *
17  - *
18  - *
19  - *
20  - *
21  - *
22  - C
23  - C
24  - C
25  - C
26  - C
---------------------