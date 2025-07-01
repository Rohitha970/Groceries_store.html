// ChatServer.java
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server is running...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            }
        }
        
        public static void broadcast(String message, ClientHandler excludeUser) {
            for (ClientHandler client : clientHandlers) {
                if (client != excludeUser) {
                    client.sendMessage(message);
                }
            }
        }
        
        public static void removeClient(ClientHandler client) {
            clientHandlers.remove(client);
            System.out.println("User disconnected");
        }
    }
    
    class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private String username;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                
                // Get username
                writer.println("Enter your username:");
                username = reader.readLine();
                ChatServer.broadcast(username + " has joined the chat!", this);
                
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    ChatServer.broadcast(username + ": " + message, this);
                }
            } catch (IOException e) {
                System.out.println("Error in ClientHandler: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket: " + e.getMessage());
                }
                ChatServer.removeClient(this);
                ChatServer.broadcast(username + " has left the chat.", null);
            }
        }
        
        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}