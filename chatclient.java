// ChatClient.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ChatClient(Socket socket, String username) {
        try {
            this.socket = socket;
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    public void sendMessage() {
        try {
            writer.println(username);
            
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                writer.println(message);
                
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            scanner.close();
        } catch (Exception e) {
            closeEverything(socket, reader, writer);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    try {
                        message = reader.readLine();
                        if (message == null) {
                            break; // Server closed connection
                        }
                        System.out.println(message);
                    } catch (IOException e) {
                        closeEverything(socket, reader, writer);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader reader, PrintWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        
        Socket socket = new Socket("localhost", 12345);
        ChatClient client = new ChatClient(socket, username);
        client.listenForMessage();
        client.sendMessage();
        
        scanner.close();
    }
}