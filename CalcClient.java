import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClient {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        String SerIpAddress=null;
        String SerPortNum=null;
        String defaultIpAdd="localhost";
        String defaultPortNum="9999";
        String fileName = "server_Info.dat";
        File file = new File(fileName);
        try{       
            //if file server_Info.dat exists, read file data
            if(file.exists())
            {
            
                BufferedReader bufferReader = null;
                bufferReader = new BufferedReader(new FileReader(fileName));

                String wholeLine = bufferReader.readLine();
                String[] array=wholeLine.split(", port ");
                SerIpAddress = array[0];
                SerPortNum = array[1];
                bufferReader.close();

            }
            else
            {
            //else use default ip, portNum
                SerIpAddress = defaultIpAdd;
                SerPortNum = defaultPortNum;            
            } 
        } catch (IOException e)
        {
            System.out.println("[error] setting server ip, portnum");
        }





         try {
            socket = new Socket(SerIpAddress, Integer.parseInt(SerPortNum));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                System.out.print("Calculation formula (enter in blank spaces, e.g. 24 + 42)>>"); 
                String outputMessage = scanner.nextLine(); // input from user's keyboard
                
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage + "\n"); // "bye" string send
                    out.flush();
                    break; 
                }

                //protocal part
                //spilt msg from user by blank
                String[] splitMsg=outputMessage.split(" ");
                int leng=splitMsg.length;
                String proOut=null; 

                //protocol that switch operation when leng>2
                if(leng>=2 && !isInteger(splitMsg[1]))
                {
                    
                    String opr;
                    
                    switch (splitMsg[1]) {
                        case "+":
                        opr="PLU";
                        break;
                    case "-":
                        opr="MIN";
                        break;
                    case "*":
                        opr="MUP";
                        break;
                    case "/":
                        opr="DIV";
                        break;
                    default:
                        opr="OPR";
                    }
                    proOut=opr;

                }

                //merge splited message as protocol rule
                for(int i=0;i<leng;i++)
                {
                    if(i==1)
                        continue;
                    proOut=proOut+ " " + splitMsg[i];
                }
                proOut=proOut+"\n";
                
                //send data to server
                out.write(proOut); 
                out.flush();

                //get data from server
                String inputMessage = in.readLine(); // get data from sever
                System.out.println("result: " + inputMessage);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null)
                    socket.close(); // close the client socket
            } catch (IOException e) {
                System.out.println("Error chatting with server.");
            }
        }
    }

    //function to check integer
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
