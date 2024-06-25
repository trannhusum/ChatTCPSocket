package DemoChatTCP;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	ServerSocket srvSocket = null;
	BufferedReader bf = null;
	Thread t;
	private JTabbedPane tabbedPane;
	private JButton OK;
	int serverPort;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerChatter frame = new ServerChatter();
					frame.setVisible(true);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
//					e1.printStackTrace();
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
			while (true)
			{
				try {
					Socket aStaffSocket = srvSocket.accept();
					if (aStaffSocket != null)
					{
						bf = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream()));
						String S = bf.readLine();
						int pos = S.indexOf(":");
						String staffName = S.substring(pos+1);
						ChatPanel p = new ChatPanel(aStaffSocket, "Manager", staffName);
						tabbedPane.add(staffName,p);
						p.updateUI();
					}
					Thread.sleep(100);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
	}

}
