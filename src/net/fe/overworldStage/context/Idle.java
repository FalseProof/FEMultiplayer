package net.fe.overworldStage.context;

import net.fe.Player;
import net.fe.overworldStage.ClientOverworldStage;
import net.fe.overworldStage.CursorContext;
import net.fe.overworldStage.Zone;
import net.fe.unit.Unit;
import chu.engine.anim.AudioPlayer;

public class Idle extends CursorContext {
	private Player player;
	private Zone move,attack,heal;
	
	public Idle(ClientOverworldStage s, Player p) {
		super(s, null);
		player = p;
	}
	
	public void startContext(){
//		boolean movable = false;
//		for(Unit u: stage.getCurrentPlayer().getParty()){
//			if(!u.hasMoved()){
//				movable = true;
//			}
//		}
//		if(movable){
//			super.startContext();
//			cursorChanged();
//		} else {
//			stage.end();
//		}
		super.startContext();
		cursorChanged();
	}
	
	
	public void cleanUp(){
		removeZones();
	}

	@Override
	public void onSelect() {
		Unit u = getHoveredUnit();
		AudioPlayer.playAudio("select", 1, 1);
		if(u!=null && u.getParty() == player.getParty() && !u.hasMoved()){
			new UnitSelected(stage, this, u).startContext();
		}
		if(u == null){
			new EndMenu(stage, this).startContext();
		}

	}


	@Override
	public void onCancel() {
		//Nothing
	}
	
	public void cursorWillChange(){
		removeZones();
		Unit u = getHoveredUnit();
		if(u!=null && !u.hasMoved()){
			u.sprite.setAnimation("IDLE");
		}
		AudioPlayer.playAudio("cursor", 1, 1);
	}

	public void cursorChanged(){
		Unit u = getHoveredUnit();
		
		if(u!=null && !u.hasMoved()){
			addZones(u);
			if(u.getParty() == stage.getCurrentPlayer().getParty()){
				u.sprite.setAnimation("SELECTED");
			}
		}
	}
	
	public void addZones(Unit u){
		this.move = new Zone(stage.grid.getPossibleMoves(u), Zone.MOVE_LIGHT);
		this.attack = Zone.minus(
				new Zone(stage.grid.getAttackRange(u),Zone.ATTACK_LIGHT), move);
		this.heal = Zone.minus(Zone.minus(
				new Zone(stage.grid.getHealRange(u),Zone.HEAL_LIGHT), move), attack);
		stage.addEntity(move);
		stage.addEntity(attack);
		stage.addEntity(heal);
	}
	
	public void removeZones(){
		stage.removeEntity(move);
		stage.removeEntity(attack);
		stage.removeEntity(heal);
	}

}
