package hr.fer.zemiris;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * JFrame with drawing board for testing {@link NeuralNetwork}.
 * @author Antonio
 *
 */
public class Tester extends JFrame {

	private static final long serialVersionUID = 1L;
	private NeuralNetwork nn;
	
	/**
	 *Creates {@link Tester} {@link JFrame}.
	 */
	public Tester(int algUcenja, int num_epochs, double learning_rate, int M, int... slojeviMreze) throws Exception {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Tester");

		nn= new NeuralNetwork("data.txt", 20, algUcenja, num_epochs, learning_rate, M, slojeviMreze);
		
		initGUI();
		setSize(800, 500);

	}

	private void initGUI() {
		this.setLayout(new BorderLayout());

		DrawingBoard db = new DrawingBoard();
		this.add(db, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		JButton clear = new JButton("Clear");

		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				db.clear();

			}
		});

		JButton test = new JButton("Test");
		
		test.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Double[] x=db.pripremiX();
				db.clear();
				
				Double[] y = null;
				try {
					y = nn.predict(x);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for(int i=0; i<y.length;i++) {
					System.out.println(y[i]);
				}
					
				
				
				double highest=y[0];
				String slovo="alfa";
				
				if(y[1]>highest) {
					highest=y[1];
					slovo="beta";
				}
				if(y[2]>highest) {
					highest=y[2];
					slovo="gamma";
				}
				if(y[3]>highest) {
					highest=y[3];
					slovo="delta";
				}
				if(y[4]>highest) {
					highest=y[4];
					slovo="epsilon";
				}
				System.out.println("Ovo slovo je: "+ slovo);
			}
		});
		
		panel.add(clear);
		panel.add(test);
		this.add(panel, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				new Tester(2, 30000, 0.05, 20, 40,5).setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
