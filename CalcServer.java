import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
public class CalcServer {

    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");

        //when blank (nothing or spaces) has been sent from client
        if (st.countTokens()<=2 )
            return "ERR LESS";
        if (st.countTokens()>3)
            return "ERR MUCH";
        
        //protocal(decoding(client msg for server)) part
        String splitMsg=st.nextToken().toString();
        String opr;
        switch (splitMsg) {
            case "PLU":
                opr="+";
                break;
            case "MIN":
                opr="-";
                break;
            case "MUP":
                opr="*";
                break;
            case "DIV":
                opr="/";
                break;
            case "null":
                opr="ERR OPR";
                break;
            default:
                opr="ERR OPR";
        }

            
        String res = "";
        String in1=st.nextToken();
        String in2=st.nextToken();
        
        //check format is right, 
        //if not, don't translate the value at integer position into integer
        //to prevent error 
        int op1=0, op2=0;
        if(isInteger(in1) && isInteger(in2))
        {
            op1 = Integer.parseInt(in1);
            op2 = Integer.parseInt(in2);
        }
        else opr="ERR FORM";

        //calculate & encoding(server value for sending client) part
        switch (opr) {
            case "+":
                res = "ANS "+Integer.toString(op1 + op2);
                break;
            case "-":
                res = "ANS "+Integer.toString(op1 - op2);
                break;
            case "*":
                res = "ANS "+Integer.toString(op1 * op2);
                break;
            case "/":
                //check whether divide to 0, return error msg
                if(op2 == 0)
                    return "ERR 0_DIV";
                res = "ANS "+Integer.toString(op1 / op2);
                break;
            case "ERR OPR":
                //wrong operation
                res = opr; 
                break;
            case "ERR FORM":
                //integer is not at right position
                res = opr;
                break;
            default:
                res = "ERR FORM";
        }
        return res;
    }

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
    
    public static void main(String[] args) throws Exception{
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;
                
        //initilize thread pool
        ExecutorService threadPool = new ThreadPoolExecutor(3,         //the number of core thread
        100,       //the maximum of threads
        180,        //the maximum time of life time
        TimeUnit.SECONDS,         //unit of time
        new SynchronousQueue<>());  //queue

        listener = new ServerSocket(9999); // make server socket
        System.out.println("waiting for connection.....");

            while(true)
            {
                 //Socket
                socket = listener.accept(); // Waiting for connection requests from clients
                System.out.println("connected.");

                // socket get from client as runnable 
                Runnable taskCli= new ServerSk(socket);
                //threadpool
                threadPool.execute(taskCli);
            }

    }

    public static class ServerSk implements Runnable{
        private Socket socket;
        public ServerSk(Socket inSocket)
        {
            this.socket=inSocket;
        }

        public void run(){
            try
            {
                BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));


                while (true) {
                    String inputMessage = in.readLine();
                    System.out.println(inputMessage+" inputtttttttt");
                    if(inputMessage ==null)
                       continue;
                    if (inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("Client terminated the connection");
                        break; // Shut down when get "bye"
                    }
                    System.out.println(inputMessage); // Outputs received messages to the screen
                    String res = calc(inputMessage); //Calculation. Calculation results are res
                    out.write(res + "\n"); // Send calculation result string
                    out.flush();
                }
            } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (socket != null)
                    socket.close(); //Close the socket for communication
            } catch (IOException e) {
                System.out.println("Error chatting with client.");
            }
        }
        }
    }

}
