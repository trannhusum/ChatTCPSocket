package DemoChatTCP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientChatter extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtStaff;
    private JTextField txtServerIP;
    private JTextField txtServerPort;

    private Socket mngSocket = null;
    private String mngIP = "";
    private int mngPort = 0;
    private String staffName = "";
    private BufferedReader bf = null;
    private DataOutputStream os = null;
    private OutputThread t = null;
    private String username;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientChatter frame = new ClientChatter();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ClientChatter(String username) {
        this.username = username;
        initialize();
    }

    public ClientChatter() {
        initialize();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 622, 403);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Staff and Server info.", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(1, 7, 0, 0));

        JLabel lblNewLabel = new JLabel("Staff :");
        lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblNewLabel);

        txtStaff = new JTextField();
        txtStaff.setText("Sum Tran");
        panel.add(txtStaff);
        txtStaff.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Manager IP :");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblNewLabel_1);

        txtServerIP = new JTextField();
        txtServerIP.setText("10.50.100.204");
        panel.add(txtServerIP);
        txtServerIP.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Port :");
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblNewLabel_2);

        txtServerPort = new JTextField();
        txtServerPort.setText("12345");
        panel.add(txtServerPort);

        JFrame thisFrame = this;
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mngIP = txtServerIP.getText();
                mngPort = Integer.parseInt(txtServerPort.getText());
                staffName = txtStaff.getText();
                try {
                    mngSocket = new Socket(mngIP, mngPort);
                    if (mngSocket != null) {
                        ChatPanel p = new ChatPanel(mngSocket, staffName, "Manager");
                        thisFrame.getContentPane().add(p);
                        p.getTxtMessages().append("Manager is running");
                        p.updateUI();

                        bf = new BufferedReader(new InputStreamReader(mngSocket.getInputStream()));
                        os = new DataOutputStream(mngSocket.getOutputStream());
                        os.writeBytes("Staff : " + staffName);
                        os.write(13);
                        os.write(10);
                        os.flush();
                        t = new OutputThread(mngSocket, bf, p.getTxtMessages());
                        t.start();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        panel.add(btnConnect);
        this.setSize(600, 400);
    }
}
