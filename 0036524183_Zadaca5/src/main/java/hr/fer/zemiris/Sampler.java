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
 * {@link JFrame} with drawing board for sampling data.
 * @author Antonio
 *
 */
public class Sampler extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates {@link Sampler} {@link JFrame}.
	 */
	public Sampler() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Sampler");

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

		JButton save = new JButton("Save");
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				db.spremiVektor("alfa.txt");
				db.clear();

			}
		});
		
		panel.add(clear);
		panel.add(save);
		this.add(panel, BorderLayout.SOUTH);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Sampler().setVisible(true);
		});
	}
}
