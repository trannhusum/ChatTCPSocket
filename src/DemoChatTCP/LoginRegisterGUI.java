package DemoChatTCP;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegisterGUI extends JFrame {
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginRegisterGUI frame = new LoginRegisterGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginRegisterGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(3, 2, 0, 0));

        JLabel lblUsername = new JLabel("Username:");
        panel.add(lblUsername);

        txtUsername = new JTextField();
        panel.add(txtUsername);
        txtUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String encryptedPassword = EncryptionUtil.encrypt(password); // Encrypt password

                UserDAO userDAO = new UserDAO();
                if (userDAO.register(username, encryptedPassword)) {
                    JOptionPane.showMessageDialog(null, "Registration Successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Failed");
                }
            }
        });
        panel.add(btnRegister);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String encryptedPassword = EncryptionUtil.encrypt(password); // Encrypt password

                UserDAO userDAO = new UserDAO();
                if (userDAO.login(username, encryptedPassword)) {
                    JOptionPane.showMessageDialog(null, "Login Successful");
                    new ClientChatter(username).setVisible(true); // Open main application window
                    dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed");
                }
            }
        });
        panel.add(btnLogin);
    }
}
