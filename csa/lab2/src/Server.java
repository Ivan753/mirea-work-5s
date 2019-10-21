import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// для примера посетите:
// http://localhost:8080/какой-то%20текст

public class Server {
    private static ServerSocket server;
    private static final int port = 8080;

    public static void main(String[] args) {
        try {
            try {
                // создаем сервер
                server = new ServerSocket(port);
                System.out.println("Сервер запущен!");

                while (true) {
                    // слушаем порт бесконечно
                    // при получении запроса создаем поток для работы с клиентом
                    Socket clientSocket = server.accept();
                    new Thread(
                            new Worker(clientSocket)
                    ).start();
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
