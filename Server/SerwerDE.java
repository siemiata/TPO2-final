import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class SerwerDE {
    private ServerSocket serverSocket;
    HashMap<String, String> deMap = new HashMap<>();
    public void insertDictionary(HashMap deMap){
        try (BufferedReader br = new BufferedReader(new FileReader("dicDE.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String polishWord = parts[0];
                String germanWord = parts[1];
                deMap.put(polishWord, germanWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  SerwerDE(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void start() throws IOException {
        System.out.println("SerwerDE uruchomiony.");
        insertDictionary(deMap);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("KlientDE podłączony: " + clientSocket.getInetAddress().getHostName());
            obslugaKlienta(clientSocket);
        }
    }
    private void obslugaKlienta(Socket clientSocketDE) throws IOException {
        Scanner in = new Scanner(clientSocketDE.getInputStream());
        String line=in.nextLine();
        System.out.println(line);

        String[] parts = line.split(",");
        String text = parts[0];
        int port = Integer.parseInt(parts[1]);
        String translatedText = deMap.get(text);
        if(translatedText !=null){
            System.out.println(":)");
        }
        else{
            translatedText = "Nie mam tego slowa w slowniku !";
        }
        System.out.println("Tłumaczę: " + text + " na DE: " + translatedText);
        PrintWriter out = new PrintWriter(clientSocketDE.getOutputStream(), true);
        out.println(translatedText);
    }
    public static void main(String[] args) {
        try {
            SerwerDE server = new SerwerDE(7777);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

