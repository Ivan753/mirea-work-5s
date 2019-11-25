import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(12345);
                System.out.println("Сервер запущен!");

                clientSocket = server.accept();

                try {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    String words = in.readLine();
                    WordCounter counter = new WordCounter(words);

                    out.write("Привет, это Сервер! Результат подсчета одинаковых слов: " + counter.getResult() + "\n");
                    out.flush();
                } finally {
                    System.out.println("Соединение закрыто!");
                    clientSocket.close();

                    in.close();
                    out.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
