
import java.io.*;
import java.net.*;

public class ChatClientEx {

	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedReader stin = null;
		BufferedWriter out = null;

		Socket socket = null;

		try {
			socket = new Socket("localhost", 8888);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new BufferedReader(new InputStreamReader(System.in));

			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String outputMessage;
			while (true) {
				outputMessage = stin.readLine();
				if (outputMessage.equalsIgnoreCase("bye")) {
					out.write(outputMessage);
					out.flush();
					break;
				}
				out.write("Client> " + outputMessage + "\n");
				out.flush();

				String inputMessage = in.readLine();
				System.out.println(inputMessage);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Disconnected");
			}
		}
	}
}
