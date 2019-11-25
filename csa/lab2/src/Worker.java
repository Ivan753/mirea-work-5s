import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker implements Runnable {

    private Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    // атрибуты ответа
    private int code = 200;
    private String status = "OK";
    private String response_text = "";

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                // начинаем обработку запроса
                String line = in.readLine();
                Pattern pattern = Pattern.compile("GET \\/(.*) HTTP\\/1.1");
                Matcher matcher = pattern.matcher(line);

                // проверяем корректность запроса
                if (matcher.find()){
                    this.response_text = matcher.group(1);
                }else{
                    this.code = 400;
                    this.status = "BAD REQUEST";
                }

                this.createResponse();

                out.flush();

            } finally {
                System.out.println("Соединение закрыто!");
                clientSocket.close();

                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    private void createResponse(){
        try {
            response_text = "<h1>"+URLDecoder.decode(response_text, "UTF-8")+"</h1>";
            final byte[] utf8Bytes = this.response_text.getBytes("UTF-8");

            out.write("HTTP/1.1 "+this.code+" "+this.status+"\n");
            out.write("Content-Length: "+utf8Bytes.length+"\n");
            out.write("Content-Type: text/html; charset=utf-8\n");
            out.write("\n");
            out.write(response_text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
