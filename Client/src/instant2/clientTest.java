package instant2;

import javax.swing.JFrame;

public class clientTest {
	
	public static void main(String[] args) {
		client charlie;
		charlie = new client("127.0.0.1");
		charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		charlie.startRunning();
	}
}
