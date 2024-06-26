package DemoChatTCP;

import java.awt.EventQueue;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerChatter extends JFrame implements Runnable {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtServerPort;
    private ServerSocket srvSocket = null;
    private Thread t;
    private JTabbedPane tabbedPane;
    private JButton OK;
    private int serverPort;
    private Map<String, ClientHandler> clientHandlers = new HashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServerChatter frame = new ServerChatter();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ServerChatter() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(1, 2, 0, 0));

        JLabel lblNewLabel = new JLabel("Manager Port :");
        lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblNewLabel);

        txtServerPort = new JTextField();
        txtServerPort.setText("");
        panel.add(txtServerPort); 
        txtServerPort.setColumns(10);

        OK = new JButton("OK");
        OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverPort = Integer.parseInt(txtServerPort.getText());
                try {
                    srvSocket = new ServerSocket(serverPort);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(OK);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        this.setSize(600,363);

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = srvSocket.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String initialMessage = bf.readLine();
                int pos = initialMessage.indexOf(":");
                String clientName = initialMessage.substring(pos + 1);

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                clientHandlers.put(clientName, clientHandler);

                ChatPanel p = new ChatPanel(clientSocket, "Manager", clientName);
                tabbedPane.add(clientName, p);
                p.updateUI();

                new Thread(clientHandler).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private String clientName;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket, String clientName) {
            this.socket = socket;
            this.clientName = clientName;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String[] parts = message.split(":", 3);
                    if (parts.length == 3) {
                        String sender = parts[0];
                        String receiver = parts[1];
                        String content = parts[2];
                        if (clientHandlers.containsKey(receiver)) {
                            ClientHandler receiverHandler = clientHandlers.get(receiver);
                            receiverHandler.sendMessage(sender + ": " + content);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
