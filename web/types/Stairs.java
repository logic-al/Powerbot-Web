package web.types;

import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Locations;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Location;
import web.types.Base.WebAction;
import web.walking.RSWalking;

/**
 * Author: Tom
 * Date: 03/04/12
 * Time: 02:03
 */
public abstract class Stairs extends WebAction {

	private final int idA;
	private final int idB;

	private final String actionA;
	private final String actionB;

	private final int planeA;
	private final int planeB;

	private int attempts = 0;


	protected Stairs(final Tile a, final Tile b, final int idA, final int idB, final String actionA, final String actionB, final int planeA,
	                 final int planeB) {
		super(a, b);
		this.idA = idA;
		this.idB = idB;
		this.actionA = actionA;
		this.actionB = actionB;
		this.planeA = planeA;
		this.planeB = planeB;
	}

	@Override
	public boolean doActionAtoB() {
		Location shortcut = Locations.getNearest(new Filter<Location>() {

			@Override
			public boolean accept(final Location l) {
				return l.getId() == idA;
			}
		});
		if (shortcut != null) {
			attempts++;
			if (!shortcut.isOnScreen()) {
				RSWalking.walkToTile(super.getA());
			}
			if (shortcut.isOnScreen()) {
				shortcut.interact(actionA);
				for (int i = 0; i < 10; i++) {
					if (Players.getLocal().getPosition().getPlane() == planeB) {
						break;
					}
					Time.sleep(300);
				}

			}
			if (Players.getLocal().getPosition().getPlane() == planeB) {
				attempts = 0;
				return true;
			}
			if (attempts < 5) {
				return doActionAtoB();
			}
			attempts = 0;
		}
		return false;
	}

	@Override
	public boolean doActionBtoA() {
		Location shortcut = Locations.getNearest(new Filter<Location>() {

			@Override
			public boolean accept(final Location l) {
				return l.getId() == idB;
			}
		});
		if (shortcut != null) {
			attempts++;
			if (!shortcut.isOnScreen()) {
				RSWalking.walkToTile(super.getB());
			}
			if (shortcut.isOnScreen()) {
				shortcut.interact(actionB);
				for (int i = 0; i < 10; i++) {
					if (Players.getLocal().getPosition().getPlane() == planeA) {
						break;
					}
					Time.sleep(300);
				}

			}
			if (Players.getLocal().getPosition().getPlane() == planeA) {
				attempts = 0;
				return true;
			}
			if (attempts < 5) {
				return doActionBtoA();
			}
			attempts = 0;
		}
		return false;
	}

	@Override
	public double getWeight() {
		return 0;
	}


}
