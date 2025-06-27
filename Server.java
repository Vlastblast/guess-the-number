import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.*;
import static sun.management.jmxremote.ConnectorBootstrap.PropertyNames.PORT;

public class Server {
    private static final int MAX_PLAYERS = 10;
    private static final int PORT = 5555;
    private static int secretNumber;
    private static boolean gameRunning = false;
    private static List<ClientHandler> client = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Сервак запущен. Ожидание игроков" + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (client.size() >= MAX_PLAYERS) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("SERVER_FULL");
                    clientSocket.close();
                    continue;
                }
                ClientHandler clientThread = new ClientHandler(clientSocket);
                client.add(clientThread);
                clientThread.start();

            }
        } catch (IOException e){
            System.out.println("Ошибка сервера"+e.getMessage());
        }
    }
    public static synchronized void broadcast(String message){
        for(ClientHandler client : client){
            client.sendMessage(message);
        }
    }
    public static synchronized void removeClient(ClientHandler clients){
        client.remove(clients);
        System.out.println("Игрок отключился. Осталось игроков"+client.size());
        broadcast("Игрок "+clients.playerName+" вышел из игры, игроков онлайн: "+client.size());
    }
    public static synchronized void startGame(int number){
        secretNumber = number;
        gameRunning = true;
        broadcast("Игра началась, число уже загадано! Угадывай!");
        System.out.println("number "+number);
    }
    public static synchronized void processGuess(ClientHandler clients, int guess){
        if (!gameRunning){
            clients.sendMessage("Игра ещё не началась, подождите...");
            return;
        }
        System.out.println(clients.playerName + "предложил "+guess);
        broadcast("Игрок_гадает "+clients.playerName+":"+guess);
        if(guess < secretNumber){
            clients.sendMessage("Результат: больше!");
        }else if(guess < secretNumber){
            clients.sendMessage("Результат: больше!");
        }else{
            gameRunning = false;
            broadcast("Игрок "+clients.playerName+" угадал число"+secretNumber+"!");
            System.out.println(clients.playerName+" winner, number is "+secretNumber);
        }
    }
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName;
        public ClientHandler(Socket socket){
            this.socket = socket;
        }
        public void run(){
            try{
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                playerName = in.readLine();
                System.out.println(playerName+" подключился ("+socket.getInetAddress()+")");
                broadcast("Игрок "+playerName+" присоединился к игре. Игроков онлайн:"+client.size());
                out.println("Добро пожаловать в игру, "+playerName+",вы"+(client.size() == 1 ? "ведущий":"участник"));
            }
        }
        public void sendMessage(String message){
            out.println(message);
        }
    }
}

