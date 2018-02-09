package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter;
	private List<GObject> gObjects;
	private GObject target;

	private int gridSize = 10;

	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}

	public void addGObject(GObject gObject) {
		gObjects.add(gObject);
		repaint();
	}

	public void groupAll() {
		CompositeGObject group = new CompositeGObject();
		for (GObject obs : gObjects)
			group.add(obs);
		group.recalculateRegion();
		this.clear();
		this.gObjects.add(group);
		repaint();
	}

	public void deleteSelected() {
		this.gObjects.remove(target);
		repaint();
	}

	public void clear() {
		this.gObjects.removeAll(gObjects);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {

		boolean clicked = false;
		int clickedX, clickedY;

		private void deselectAll() {
			for (GObject ob : gObjects)
				ob.deselected();
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			clicked = false;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			deselectAll();
			for (GObject ob : gObjects) {
				if (ob.pointerHit(e.getX(), e.getY())) {
					this.clickedX = e.getX();
					this.clickedY = e.getY();
					clicked = true;
					ob.selected();
					target = ob;
					break;
				}
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (target != null && clicked) {
				target.move(e.getX() - this.clickedX, e.getY() - this.clickedY);
				this.clickedX = e.getX();
				this.clickedY = e.getY();
				repaint();
			}
		}
	}

}