
import java.io.*;
import java.net.*;

public class SendDataPackets {

	public static void main(String[] args) throws InterruptedException
	{
		
	final String SERVER_IP_M = "192.168.2.92";
	final String SERVER_IP_P = "192.168.2.95";
	final String SERVER_IP_S = "192.168.2.97";
	String fileName="/home/pi/Desktop/Parameters/data.txt";
	String line=null;
	String str[]= {"","","","","","","","",""};
	int i=0;
	String id = "unknown", id_ip;
    
	while(true)
	{
	i=0;
	    	try 
	    	{
	    		
	    		FileInputStream fileInputStream = new FileInputStream (new File(fileName));
	            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            
	            while ( (line = bufferedReader.readLine()) != null)
	            {
	            	str[i]=line;
	            	i++;
	            	
	            }
//	            System.out.println(str[0]);
//	            System.out.println(str[1]);
//	            System.out.println(str[2]);
	            fileInputStream.close();
	            bufferedReader.close();
	            
	            id_ip = SERVER_IP_P;
	            System.out.println(id_ip);
	        	if(id_ip.equals(SERVER_IP_M))
	        	{
	        		id = "M";
	        	}
	        	else if (id_ip.equals(SERVER_IP_P))
	        	{
	        		id = "P";
	        	}
		        else if (id_ip.equals(SERVER_IP_S))
		        {
		        	id = "S";
		        }
		        else 
		        {
		        	id = "unknown";
		        }
		} 
		catch (Exception e1) 
		{
			e1.printStackTrace();
		}
	    	if(id == "P" || id == "S")
	    	{
		        SendDataPackets sdp_M = new SendDataPackets();
		        // change the IP address to your server's ip address
		        sendData(sdp_M, SERVER_IP_M, id, str[0], str[1], str[2]);	    
		}
		if(id == "S" || id == "M")
		{
			SendDataPackets sdp_P = new SendDataPackets();
		        // change the IP address to your server's ip address
		        sendData(sdp_P, SERVER_IP_P, id, str[0], str[1], str[2]);
		}
		if(id == "M" || id == "P")
		{
		        SendDataPackets sdp_S = new SendDataPackets();
		        // change the IP address to your server's ip address
		        sendData(sdp_S, SERVER_IP_S, id, str[0], str[1], str[2]);
		}
	Thread.sleep(10);
	}
		
}
		
	void write(PrintWriter output, String message) 
	{
	        System.out.println("Sending: " +message);
	        output.println(message);
    	}
	
	private static void sendData(SendDataPackets sdp, String ip, String id, String x, String y, String theta)
	{
		Socket socket;
	    	PrintWriter output;
	    	BufferedReader input;
		try
        	{ 	
        		socket = new Socket(InetAddress.getByName(ip), 4141); 
            		output = new PrintWriter(socket.getOutputStream(), true);
            		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            
        		sdp.write(output, "SYN"); // Synchronize
       	    		String in = "";
	        	while ((in = input.readLine()) != null) 
            		{
                		System.out.println("Received: " + in);
                		if (in.equals("SYN-ACK"))
                		{ // Synchronize-Acknowledge
                			sdp.write(output, "ACK");
                			sdp.write(output, "id = " + id);
                			sdp.write(output, "x = " + x);
                			sdp.write(output, "y = " + y);
                			sdp.write(output, "theta = " + theta);
                			sdp.write(output, "FIN");
                		}
                		if (in.equals("FIN-ACK"))
                		{ // Final-Acknowledge
                    			break;
                		}
            		}
            		System.out.print("Closing socket.");
        	} 
        	catch (IOException e) 
        	{
            		e.printStackTrace();
        	}
	}
	
}
