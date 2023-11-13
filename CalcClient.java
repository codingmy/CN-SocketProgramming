import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClient {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        String SerIpAddress;
        String SerPortNum;
        Config configData = new Config();
        String defaultIpAdd="localhost";
        String defaultPortNum="9999";
        try {
            
            //get server's ip, portnum
            //check whether preset of server data
            if(configData.getIpAddress()==null || configData.getPortNum()==null)
            {
                //setting default data
                configData.setIpAddress(defaultIpAdd);
                configData.setPortNum(defaultPortNum);
            }    
            //get server's ip, portnum from configuration data
            SerIpAddress=configData.getIpAddress();
            SerPortNum=configData.getPortNum();


            socket = new Socket(SerIpAddress, Integer.parseInt(SerPortNum));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                System.out.print("Calculation formula (enter in blank spaces, e.g. 24 + 42)>>"); 
                String outputMessage = scanner.nextLine(); // input from user's keyboard
                
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage + "\n"); // "bye" 문자열 전송
                    out.flush();
                    break; // 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
                }

                //protocal part
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
                    socket.close(); // 클라이언트 소켓 닫기
            } catch (IOException e) {
                System.out.println("서버와 채팅 중 오류가 발생했습니다.");
            }
        }
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
}
//public void getConfig(String ip, String portNum)
//{
    //ip=config.getIpAddress();
//} 