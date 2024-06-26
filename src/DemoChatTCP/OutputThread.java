package DemoChatTCP;

import java.io.BufferedReader;
import java.net.Socket;
import javax.swing.JTextArea;

public class OutputThread extends Thread {
    private Socket socket;
    private BufferedReader bf;
    private JTextArea txt;

    public OutputThread(Socket socket, BufferedReader bf, JTextArea txt) {
        super();
        this.socket = socket;
        this.bf = bf;
        this.txt = txt;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (socket != null) {
                    String msg = bf.readLine();
                    if (msg != null && !msg.trim().equals("")) {
                        txt.append("\n" + msg);
                    }
                }
                sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
