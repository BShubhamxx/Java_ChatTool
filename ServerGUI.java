import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerGUI extends JFrame {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton exitButton;
    private FileWriter logWriter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ServerGUI() {
        setTitle("Server Chat");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
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
        messageField.addActionListener(_ -> sendMessage()); // send on Enter key
        exitButton.addActionListener(_ -> sendExitMessage());

        // Start the server
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            logWriter = new FileWriter("chat_history.txt", true);
            LocalDateTime startTime = LocalDateTime.now();
            logWriter.write("\n\n===== New Chat Session (Server Started) =====\n");
            logWriter.write("Session Start: " + startTime.format(formatter) + "\n");
            logWriter.flush();

            serverSocket = new ServerSocket(3333);
            messageArea.append("Server started. Waiting for client...\n");
            socket = serverSocket.accept();
            messageArea.append("Client connected.\n");

            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            // Listen for incoming messages
            String msgIn;
            while (true) {
                msgIn = din.readUTF();
                messageArea.append("Client: " + msgIn + "\n");
                logWriter.write("Client: " + msgIn + "\n");
                logWriter.flush();

                if (msgIn.equalsIgnoreCase("byee") || msgIn.equalsIgnoreCase("close")) {
                    closeConnection();
                    break;
                }
            }
        } catch (IOException e) {
            messageArea.append("Error starting server: " + e.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String msgOut = messageField.getText().trim();
        if (!msgOut.isEmpty()) {
            try {
                dout.writeUTF(msgOut);
                dout.flush();
                messageArea.append("Server: " + msgOut + "\n");
                logWriter.write("Server: " + msgOut + "\n");
                logWriter.flush();
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
            messageArea.append("Server: close\n");
            logWriter.write("Server: close\n");
            logWriter.flush();
            closeConnection();
        } catch (IOException e) {
            messageArea.append("Error sending exit message.\n");
        }
    }

    private void closeConnection() {
        try {
            LocalDateTime endTime = LocalDateTime.now();
            logWriter.write("Session End: " + endTime.format(formatter) + "\n");
            logWriter.write("===== Chat Ended =====\n");
            logWriter.flush();
            logWriter.close();

            if (socket != null) socket.close();
            if (din != null) din.close();
            if (dout != null) dout.close();
            if (serverSocket != null) serverSocket.close();

            messageArea.append("Connection closed.\n");
        } catch (IOException e) {
            messageArea.append("Error closing connection.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI server = new ServerGUI();
            server.setVisible(true);
        });
    }
}
