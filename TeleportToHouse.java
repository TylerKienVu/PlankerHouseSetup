package scripts;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import mainFiles.Activity;

public class TeleportToHouse implements Activity{
	private Script script;
	private String id = "TeleportToHouse";
	private Area castleWarsArea = new Area(new Position(2443,3082,0),new Position(2438,3097,0));
	private RS2Widget settings;
	private RS2Widget house;
	private RS2Widget building;
	private RS2Object portal;
	
	public TeleportToHouse(Script script){
		this.script = script;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validate() throws InterruptedException {
		return castleWarsArea.contains(script.myPosition()) &&
				script.getInventory().contains("Bolt of cloth");
	}

	@Override
	public void run() throws InterruptedException {		
		if(script.getInventory().getAmount("Coins") < 20000){
			script.log("Not enough coins stopping script");
			script.stop();
		}
		script.getInventory().interact("Break","Teleport to house");
		if(settings == null){
			settings = script.getWidgets().get(548, 39);
		}
		if(house == null){
			house = script.getWidgets().get(261,76);
		}
		hoverMode();
	}
	private void rSleep(int x,int y) throws InterruptedException{
		Script.sleep(Script.random(x,y));
	}
	private void hoverMode() throws InterruptedException{
		rSleep(500,1500);
		settings.interact("Options");
		rSleep(500,1000);
		if(house.hover()){
			new ConditionalSleep(7000){
				public boolean condition() throws InterruptedException{
					portal = script.getObjects().closest(4525);
					return portal != null;
				}
			}.sleep();
		}
		rSleep(1000,1500);
		house.interact("View House Options");
		rSleep(3000,4000);
		if(building == null){
			building = script.getWidgets().get(370,5);
		}
		if(building != null && !building.hover()){
			rSleep(700,1500);
			script.getMouse().click(false);
			script.log("Building clicked");
		}
	}
}
