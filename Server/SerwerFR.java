import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class SerwerFR {

    private ServerSocket serverSocket;
    HashMap<String, String> frMap = new HashMap<>();
    public void insertDictionary(HashMap frMap){
        try (BufferedReader br = new BufferedReader(new FileReader("dicFR.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String polishWord = parts[0];
                String franceWord = parts[1];
                frMap.put(polishWord, franceWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  SerwerFR(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void start() throws IOException {
        System.out.println("SerwerFR uruchomiony.");
        insertDictionary(frMap);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("KlientFR podłączony: " + clientSocket.getInetAddress().getHostName());
            obslugaKlienta(clientSocket);
        }
    }
    private void obslugaKlienta(Socket clientSocketFR) throws IOException {
        Scanner in = new Scanner(clientSocketFR.getInputStream());
        String line=in.nextLine();
        System.out.println(line);

        String[] parts = line.split(",");
        String text = parts[0];
        int port = Integer.parseInt(parts[1]);
        String translatedText = frMap.get(text);
        if(translatedText !=null){
            System.out.println(":)");
        }
        else{
            translatedText = "Nie mam tego slowa w slowniku !";
        }
        System.out.println("Tłumaczę: " + text + " na FR: " + translatedText);
        PrintWriter out = new PrintWriter(clientSocketFR.getOutputStream(), true);
        out.println(translatedText);
    }
    public static void main(String[] args) {
        try {
            SerwerFR server = new SerwerFR(6666);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

