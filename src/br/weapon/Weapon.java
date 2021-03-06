package br.weapon;

import br.Placeable;
import br.Vector;

public class Weapon extends Placeable {
	
	private WeaponType type;
	
	public int getAttackBonus() {
		return type.getAttackBonus();
	}
	
	public WeaponType getType() {
		return type;
	}
	
	public Weapon(WeaponType type, Vector position) {
		super(position);
		this.type = type;
	}
	
}
