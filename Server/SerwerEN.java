import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class SerwerEN {

    HashMap<String, String> engMap = new HashMap<>();
    private ServerSocket serverSocket;
    public void insertDictionary(HashMap engMap){
        try (BufferedReader br = new BufferedReader(new FileReader("Server/dicEN.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String polishWord = parts[0];
                String englishWord = parts[1];
                engMap.put(polishWord, englishWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  SerwerEN(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void start() throws IOException {
        System.out.println("SerwerEN uruchomiony.");
        insertDictionary(engMap);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            //System.out.println("KlientEN podłączony: " + clientSocket.getInetAddress().getHostName());
            obslugaKlienta(clientSocket);
        }
    }
    private void obslugaKlienta(Socket clientSocketEN) throws IOException {
        Scanner in = new Scanner(clientSocketEN.getInputStream());
        String line=in.nextLine();
        //System.out.println(line);
        String[] parts = line.split(",");
        String text = parts[0];
        int port = Integer.parseInt(parts[1]);
        String translatedText = engMap.get(text);
            if(translatedText !=null){
                System.out.println(":)");
            }
            else{
                translatedText = "Nie mam tego slowa w slowniku !";
            }

        System.out.println("Tłumaczę: " + text + " na EN: " + translatedText);

        PrintWriter out = new PrintWriter(clientSocketEN.getOutputStream(), true);
        out.println(translatedText);
    }
    public static void main(String[] args) {
        try {
            SerwerEN server = new SerwerEN(5555);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

