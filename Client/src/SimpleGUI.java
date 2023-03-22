import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class SimpleGUI extends JFrame {

    private JTextField textField;
    private JComboBox<String> comboBox;
    private JTextArea textArea;

    Socket socket = null;
    private int hearingPort = 1234;

    public SimpleGUI() {

        super("Simple translator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Insert word to translate");
        panel.add(label);
        textField = new JTextField(20);
        panel.add(textField);

        JLabel optionLabel = new JLabel("Choose language:");
        panel.add(optionLabel);
        String[] options = {"English EN", "German DE", "France FR"};
        comboBox = new JComboBox<>(options);
        panel.add(comboBox);

        JLabel resultLabel = new JLabel();
        panel.add(resultLabel);
        textArea = new JTextArea(3, 20);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEtchedBorder());
        panel.add(textArea);

        JButton refreshButton;
        refreshButton = new JButton("Translate");

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    System.out.println(textField.getText() + " " + comboBox.getSelectedItem());
                    socket = new Socket("localhost", 1234);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(textField.getText()+","+hearingPort+","+comboBox.getSelectedIndex());

                    BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = in2.readLine();
                    System.out.println("Server response: " + response);
                    //textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    textArea.setText("");
                    textArea.setText(response);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.add(refreshButton);

        add(panel);
        setResizable(false);
        setVisible(true);
    }
}