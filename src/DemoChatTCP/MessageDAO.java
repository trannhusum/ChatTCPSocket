package DemoChatTCP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ChatDB";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public MessageDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void saveMessage(String sender, String receiver, String message) {
        String sql = "INSERT INTO messages (sender, receiver, message) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getMessages() {
        String sql = "SELECT * FROM messages";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String sender = rs.getString("sender");
                String receiver = rs.getString("receiver");
                String message = rs.getString("message");
                String timestamp = rs.getString("timestamp");

                System.out.println("ID: " + id);
                System.out.println("Sender: " + sender);
                System.out.println("Receiver: " + receiver);
                System.out.println("Message: " + message);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("----------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
