import javax.swing.JFrame;

public class ServerTest {
	public static void main(String[] args) {
		server sally = new server();
		sally.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sally.startRunning();
	}
}
