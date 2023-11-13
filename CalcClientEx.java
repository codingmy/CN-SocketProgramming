import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClientEx {
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
                System.out.print("계산식(빈칸으로 띄어 입력,예:24 + 42)>>"); // 프롬프트
                String outputMessage = scanner.nextLine(); // 키보드에서 수식 읽기
                
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage + "\n"); // "bye" 문자열 전송
                    out.flush();
                    break; // 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
                }

                //protocal part
                String[] splitMsg=outputMessage.split(" ");
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

                //send data to server
                out.write(opr +" "+ splitMsg[0] + " "+ splitMsg[2] + "\n"); // 키보드에서 읽은 수식 문자열 전송
                out.flush();

                //get data from server
                String inputMessage = in.readLine(); // 서버로부터 계산 결과 수신
                System.out.println("계산 결과: " + inputMessage);
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
}


//public void getConfig(String ip, String portNum)
//{
    //ip=config.getIpAddress();
//} 