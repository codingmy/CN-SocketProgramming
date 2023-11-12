import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServerEx {

	public static void main(String[] args) {
		BufferedReader in = null;
		Scanner stin = null;
		BufferedWriter out = null;

		ServerSocket listener = null;
		Socket socket = null;

		try {
			listener = new ServerSocket(8888);

			System.out.println("Start Server...");
			System.out.println("Waiting for clients");

			socket = listener.accept();

			System.out.println("A new connection has been established!");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			stin = new Scanner(System.in);
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String inputMessage;

			while (true) {
				inputMessage = in.readLine();

				if (inputMessage.equalsIgnoreCase("bye")) {
					System.out.println("Bye");
					break;
				}

				System.out.println(inputMessage);
				String outputMessage = stin.nextLine();
				out.write("Server> " + outputMessage + "\n");
				out.flush();

			}

		} catch (IOException e) {

			System.out.println(e.getMessage());

		} finally {

			try {
				socket.close();
				listener.close();
			} catch (IOException e) {
				System.out.println("Disconnected.");
			}
		}
	}
}
