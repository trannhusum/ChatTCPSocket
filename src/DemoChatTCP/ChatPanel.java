package DemoChatTCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    Socket socket = null;
    BufferedReader bf = null;
    DataOutputStream os = null;
    OuputThread t = null;
    String sender;
    String receiver;
    JTextArea txtMessages;

    /**
     * Create the panel.
     */
    public ChatPanel(Socket s, String sender, String receiver) {
        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Message"));
        add(panel, BorderLayout.SOUTH);
        panel.setLayout(new GridLayout(1, 2, 0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        JTextArea txtMessage = new JTextArea();
        scrollPane.setViewportView(txtMessage);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtMessage.getText().trim().length() == 0) return;
                try {
                    os.writeBytes(txtMessage.getText());
                    os.write(13);
                    os.write(10);
                    os.flush();
                    txtMessages.append("\n" + sender + ": " + txtMessage.getText());

                    // Save message to database
                    MessageDAO messageDAO = new MessageDAO();
                    messageDAO.saveMessage(sender, receiver, txtMessage.getText());

                    txtMessage.setText("");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        panel.add(btnSend);

        JScrollPane scrollPane_1 = new JScrollPane();
        add(scrollPane_1, BorderLayout.CENTER);

        txtMessages = new JTextArea();
        txtMessages.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
        scrollPane_1.setViewportView(txtMessages);
        txtMessages.setBackground(Color.CYAN);

        socket = s;
        this.sender = sender;
        this.receiver = receiver;
        try {
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new DataOutputStream(socket.getOutputStream());
            t = new OuputThread(s, txtMessages, sender, receiver);
            t.start();

        } catch (Exception e) {
//          e.printStackTrace();
        }
    }

    public JTextArea getTxtMessages() {
        return this.txtMessages;
    }
}
