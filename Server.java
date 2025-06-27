import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.*;
import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.PORT;

public class Server {
    private static final int MAX_PLAYERS = 10;
    private static final int PORT = 5555;
    private static int secretNumber;
    private static boolean GameRunning = false;
    private static List<ClientHandler> client = new ArrayList<Object>();

    public static void main(String[] args) {
        System.out.println("Сервак запущен. Ожидание игроков" + PORT + "...");
        try (ServerSocket serverSoccet = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = ServerSocket.accept();
                if (client.size() >= MAX_PLAYERS) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("SERVER_FULL");
                    clientSocket.close();
                    continue;
                }
                ClientHandler clientHandler = new ClientHandler(clientSocket);
            }
        }
    }
    private static class ClientHandler extends Thread {

    }
}

