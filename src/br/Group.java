package br;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.player.Player;
import br.player.TacticPlayer;

@SuppressWarnings("serial")
public class Group extends ArrayList<Player> {
	
	public void add(Group group) {
		for (int i = 0; i < group.size(); i++) {
			this.add(group.get(i));
		}
	}
	
	public double distanceTo(Group group) {
		double minimum = Double.MAX_VALUE;
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < group.size(); j++) {
				double distance = this.get(i).distanceTo(group.get(j));
			    minimum = Math.min(distance, minimum);
			}
		}
		return minimum;
	}
	
	public String getNames() {
		if (this.size() == 0)
			return null;
		if (this.size() == 1)
			return this.get(0).getName();
		Collections.shuffle(this);
		String names = this.get(0).getName() 
				+ " and " 
				+ this.get(1).getName();
		for (int i = 2; i < this.size(); i++) {
			names = this.get(i).getName() + ", " + names;
		}
		return names;
	}
	
	/**
	 * Checks if group is in vicinity of this.
	 */
	public boolean isInVicinity(Group group) {
		for (int i = 0; i < this.size(); i++) {
			for (int j = 0; j < group.size(); j++) {
				if (!this.get(i).isInVicinity(group.get(j)))
					return false;
			}
		}
		return true;
	}
	
	public void resolveFight() {
		if (this.size() <= 1)
			return;
		Global.messenger.messageTimeStamp();
		ArrayList<TacticPlayer> tacticals = new ArrayList<TacticPlayer>();
		for (Player player : this)
			tacticals.add(TacticPlayer.generate(player));
		int maximum = Integer.MIN_VALUE;
		for (TacticPlayer tactical : tacticals)
			maximum = Math.max(maximum, tactical.getAttack());
		Group winners = new Group();
		Group losers = new Group();
		for (TacticPlayer tactical : tacticals)
			if (tactical.getAttack() < maximum)
				losers.add(tactical.getPlayer());
			else 
				winners.add(tactical.getPlayer());
		for (int i = 0; i < losers.size(); i++) {
			losers.get(i).setDead(Global.gameTime);
			winners.get(Global.random.nextInt(winners.size())).getStatistic()
					.increaseKills();
		}
		Global.messenger.messageRunInto(this);
		if (losers.size() != 0) {
			ArrayList<Group> list = new ArrayList<Group>();
			for (int i = 0; i < winners.size(); i++)
				list.add(new Group());
			for (int i = 0; i < losers.size(); i++)
				list.get(Global.random.nextInt(list.size()))
						.add(losers.get(i));
			for (int i = 0; i < list.size(); i++) {
				Global.messenger.messageKill(new Group(winners.get(i)), list.get(i));
			}
			Global.messenger.messageEndOfLine();
		} else {
			Global.messenger.messageNotToFight()
					.messageEndOfLine();
		}
	}
	
	public void sortByTimeOfDeath() {
		this.sort(new Comparator<Player>() {
			@Override
			public int compare(Player arg0, Player arg1) {
				if (arg0.getDead()) {
					if (arg1.getDead()) {
						return arg0.getStatistic().getTimeOfDeath().getTicks()
								- arg1.getStatistic().getTimeOfDeath().getTicks();
					} else {
						return -1;
					}
				} else {
					if (arg1.getDead()) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		});
	}
	
	public Group() {}
	
	public Group(Player player) {
		this.add(player);
	}
	
	public Group(Group group) {
		for (int i = 0; i < group.size(); i++) {
			this.add(group.get(i));
		}
	}
	
}
