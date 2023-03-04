package hr.fer.zemiris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * JComponent for drawing board used for sampling and testing gestures.
 * @author Antonio
 *
 */
public class DrawingBoard extends JComponent {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Image image;
	private Graphics2D graphics2D;
	int currentX, currentY, oldX, oldY;

	private List<Vector> vektori;
	/**
	 * Creates {@link DrawingBoard} {@link JComponent}.
	 */
	public DrawingBoard() {

		this.vektori = new ArrayList<>();

		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				oldX = e.getX();
				oldY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				vektori.add(new Vector(currentX, currentY));
				if (graphics2D != null)
					graphics2D.drawLine(oldX, oldY, currentX, currentY);
				repaint();
				oldX = currentX;
				oldY = currentY;
			}
		});
	}

	public void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D) image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Clears drawing board and all data.
	 */
	public void clear() {
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
		vektori=new ArrayList<>();
	}
	
	/**
	 * Prepares gesture for using it.
	 */
	private void pripremiGestu() {

		double sumX = 0;
		double sumY = 0;

		
		
		for (Vector vect : vektori) {
			sumX += vect.getX();
			sumY += vect.getY();
		}

		Vector Tc = new Vector(sumX / vektori.size(), sumY / vektori.size());
		List<Vector> noviVektori = new ArrayList<>();

		for (Vector vect : vektori) {
			noviVektori.add(new Vector(vect.getX() - Tc.getX(), vect.getY() - Tc.getY()));
		}

		double maxX = 0;
		double maxY = 0;

		vektori = noviVektori;
		noviVektori = new ArrayList<>();
		
		
		
		for (Vector vect : vektori) {
			if (Math.abs(vect.getX()) > maxX) {
				maxX = Math.abs(vect.getX());
			}

			if (Math.abs(vect.getY()) > maxY) {
				maxY = Math.abs(vect.getY());
			}
		}

		double m = Math.max(maxX, maxY);

		for (Vector vect : vektori) {
			noviVektori.add(new Vector(vect.getX() / m, vect.getY() / m));
		}
		
		
		vektori = noviVektori;

		double D = vektori.size()-1;

		int M = 20;

		double korak = (D-1) / (M - 1);

		Vector prvaTocka = vektori.get(0);
		
		noviVektori = new ArrayList<>();
		noviVektori.add(prvaTocka);
		
		for (int k = 1; k < M; k++) {
			double tocka=k*korak;
			
			int prviIndex= (int)Math.floor(tocka);
			double koficijent=tocka-prviIndex;
			
			Vector interpoliran=new Vector(vektori.get(prviIndex).getX()*(1-koficijent)+vektori.get(prviIndex).getX()*koficijent,
					vektori.get(prviIndex+1).getY()*(1-koficijent)+vektori.get(prviIndex+1).getY()*koficijent);
			
			noviVektori.add(interpoliran);
		}

		vektori = noviVektori;	
		
		
	}

	/**
	 * Prepares gesture for testing it.
	 * @return
	 */
	public Double[] pripremiX() {
		pripremiGestu();
		
		
		List<Double> list=new ArrayList<>();
		
		for(Vector vect: vektori) {
			list.add(vect.getX());
			list.add(vect.getY());
		}
		
		return list.toArray(new Double[0]);
	}
	
	/**
	 * Saves gesture vector.
	 * @param file
	 */
	public void spremiVektor(String file) {
		pripremiGestu();
		try {
			File myObj = new File(file);
			myObj.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file,StandardCharsets.UTF_8, true))) {
			StringBuilder sb= new StringBuilder();
			
			for(Vector vect: vektori) {
				sb.append(vect.getX()+", "+ vect.getY()+", ");
			}
			String str=sb.toString();
			str=str.substring(0, str.length()-2);
			str=str+"#1, 0, 0, 0, 0";

			writer.append(str+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}