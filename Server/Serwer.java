import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serwer {

    private ServerSocket serverSocket;
    Socket socket = null;
    public Serwer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void start() throws IOException {
        System.out.println("Serwer uruchomiony.");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Klient podłączony: " + clientSocket.getInetAddress().getHostName());
            obslugaKlienta(clientSocket);
        }
    }

    public void languageServer(int langCode, String toTranslate, int port) throws IOException {
        int toConnect=0;
        switch(langCode){
            case 0: toConnect = 5555;
            break;
            case 1: toConnect = 7777;
                break;
            case 2: toConnect = 6666;
                break;
        }
        socket = new Socket("localhost", toConnect);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(toTranslate + "," + port);

    }

    private void obslugaKlienta(Socket clientSocket) throws IOException {
        Scanner in = new Scanner(clientSocket.getInputStream());
        String line=in.nextLine();
        String[] parts = line.split(",");
        String text = parts[0];
        int port = Integer.parseInt(parts[1]);
        int langCode = Integer.parseInt(parts[2]);
        System.out.println("{ ["+text+"] "+"["+port+"] "+"["+langCode+"] }" );
        languageServer(langCode,text,port);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in2.readLine();
        System.out.println("Server response: " + response);
        socket.setSoTimeout(5000);
        clientSocket.setSoTimeout(5000);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println(response);
    }
    public static void main(String[] args) {
        try {
            Serwer server = new Serwer(1234);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
