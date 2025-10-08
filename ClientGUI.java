import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton exitButton;

    public ClientGUI() {
        setTitle("Client Chat");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        exitButton = new JButton("Exit");

        inputPanel.add(exitButton, BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Action listeners
        sendButton.addActionListener(_ -> sendMessage());
        messageField.addActionListener(_ -> sendMessage()); // Send on Enter
        exitButton.addActionListener(_ -> sendExitMessage());

        connectToServer();
        new Thread(this::listenForMessages).start();
    }

    private void connectToServer() {
        try {
            // socket = new Socket("192.168.224.135", 3333); // replace with your server IP if needed
            socket = new Socket("localhost", 3333);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            messageArea.append("Connected to server.\n");
        } catch (IOException e) {
            messageArea.append("Error: Unable to connect to server.\n");
        }
    }

    private void sendMessage() {
        String msgOut = messageField.getText().trim();
        if (!msgOut.isEmpty()) {
            try {
                dout.writeUTF(msgOut);
                dout.flush();
                messageArea.append("You: " + msgOut + "\n");
                messageField.setText("");
            } catch (IOException e) {
                messageArea.append("Error sending message.\n");
            }
        }
    }

    private void sendExitMessage() {
        try {
            dout.writeUTF("close");
            dout.flush();
            messageArea.append("You: close\n");
            closeConnection();
        } catch (IOException e) {
            messageArea.append("Error sending exit message.\n");
        }
    }

    private void listenForMessages() {
        try {
            String msgIn;
            while (true) {
                msgIn = din.readUTF();
                messageArea.append("Server: " + msgIn + "\n");
                if (msgIn.equalsIgnoreCase("byee") || msgIn.equalsIgnoreCase("close")) {
                    closeConnection();
                    break;
                }
            }
        } catch (IOException e) {
            messageArea.append("Connection closed.\n");
        }
    }

    private void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (din != null) din.close();
            if (dout != null) dout.close();
            messageArea.append("Disconnected from server.\n");
        } catch (IOException e) {
            messageArea.append("Error closing connection.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI client = new ClientGUI();
            client.setVisible(true);
        });
    }
}
