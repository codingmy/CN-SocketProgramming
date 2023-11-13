import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
public class CalcServer {

    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");

        //when blank (nothing or spaces) has been sent from client
        if (st.countTokens()<=2 )
            return "[error] Insufficient input values";
        if (st.countTokens()>3)
            return "[error] too many input values";
        
        //protocal part
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
                opr="OPR";
                break;
            default:
                opr="OPR";
        }

            
        String res = "";
        int op1 = Integer.parseInt(st.nextToken());
        int op2 = Integer.parseInt(st.nextToken());

        //calculate part
        switch (opr) {
            case "+":
                res = Integer.toString(op1 + op2);
                break;
            case "-":
                res = Integer.toString(op1 - op2);
                break;
            case "*":
                res = Integer.toString(op1 * op2);
                break;
            case "/":
                res = Integer.toString(op1 / op2);
                break;
            case "OPR":
                res="[error] wrong input format, format: integer operation integer";
                break;
            default:
                res = "[error] wrong input format, format: integer operation integer";
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

    //    try {
            listener = new ServerSocket(9999); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.....");

            while(true)
            {
            //Socket
             socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
            System.out.println("연결되었습니다.");



            // socket get from client as runnable 
            Runnable taskCli= new ServerSk(socket);
            //threadpool
            threadPool.execute(taskCli);
            }

        
      //  } 
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
                    if (inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("클라이언트에서 연결을 종료하였음");
                        break; // "bye"를 받으면 연결 종료
                    }
                    System.out.println(inputMessage); // 받은 메시지를 화면에 출력
                    String res = calc(inputMessage); // 계산. 계산 결과는 res
                    out.write(res + "\n"); // 계산 결과 문자열 전송
                    out.flush();
                }
            } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (socket != null)
                    socket.close(); // 통신용 소켓 닫기
    //            if (listener != null)
     //               listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
            }
        }
           // break;
        }
    }

}
