This is a readme file for the IOT Final Project by team Faraday.
Authors: Mukul Anil Gosavi, Praveen Ganapathy Narasimhan, Supraja Mahadevan

Hardware and E/E components:
3 Raspberry Pi's
3 Android mobile devices
3 USB cords
Wifi connection(just the Router, internet connection not required)


Basic Idea:
3 Raspberry Pi's represent the ECU's(Electronic Control Unit) inside each of the cars. 
3 Android deviec act as a remote control for the car operation
Android devices are connected to their respective RPi via USB and to other RPi using wifi socket network.


About the program:
	This program consists of 2 parts:

	1. 	Android Application(.apk)
			Install the android apk provided in the mobile
	
	2. 	Java code for Raspberry Pi's
			This code needs 4 terminals to execute completely

			a) Starting the Receiver : execute the following command
				javac ReceiveDataPackets.java
				java ReceiveDataPackets
 
			b) Starting the Sender
				javac SendDataPackets.java
				java SendDataPackets

			c) Push Files in Recv folder
				watch -n 0 adb push /home/pi/Desktop/Parameters/Recv/ /sdcard/
				
			d) Pull Data.txt
				watch -n 0 adb pull /sdcard/data.txt /home/pi/Desktop/Parameters/
				

About different files:
1. Faraday.apk is the name of the apk file for the android app 'Faraday'
2. ReceiveDataPackets.java is a java class responsible for receiving different data packets from other two RPi's using wifi.
3. SendDataPackets.java is a java class responsible for sending data packets to other two RPi's using wifi.
4. For sending the data that is calculated in this RPi and received from other RPi's back to the android devices, 
   Push command is used and the data goes in Recv Folder in 3 files corresponding to each RPi.
5. For receiving the control signals from mobile, Pull command is used and data goes into the data.txt file.

Paths:
1. Main folder of this project is : IOT_Final_Project_Team_Faraday and both ReceiveDataPackets.java and SendDataPackets.java are in the Java/RPiProj
2. Both Recv folder and data.txt files are in the SD card of the respective mobile.
3. Report_Faraday is the final project report

	