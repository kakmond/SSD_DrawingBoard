package objects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		this.gObjects.add(gObject);
	}

	public void remove(GObject gObject) {
		this.gObjects.remove(gObject);
	}

	@Override
	public void move(int dX, int dY) {
		for (GObject g : gObjects)
			g.move(dX, dY);
		this.x += dX;
		this.y += dY;
	}

	public void recalculateRegion() {
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int top = 0, right = 0, left = 0, buttom = 0;
		for (GObject gObject : gObjects) {
			if (ymax < gObject.y + gObject.height) {
				ymax = gObject.y + gObject.height;
				top = ymax;
			}
			if (xmax < gObject.x + gObject.width) {
				xmax = gObject.x + gObject.width;
				right = xmax;
			}
			if (xmin > gObject.x) {
				xmin = gObject.x;
				left = xmin;
			}
			if (ymin > gObject.y) {
				ymin = gObject.y;
				buttom = ymin;
			}
		}
		this.x = xmin;
		this.y = ymin;
		this.width = right - left;
		this.height = top - buttom;
	}

	@Override
	public void paintObject(Graphics g) {
		for (GObject gobs : gObjects)
			gobs.paintObject(g);
	}

	@Override
	public void paintLabel(Graphics g) {
		g.drawString("Composite", x, y);
	}

}
