package scripts;


import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import mainFiles.Activity;

public class BuildRooms implements Activity{
	private Script script;
	private String id = "BuildRooms";
	private Area castleWarsArea = new Area(new Position(2443,3082,0),new Position(2438,3097,0));
	private RS2Widget settings;
	private RS2Widget house;
	private RS2Widget inventory;
	private RS2Widget inventoryIcon;
	private RS2Widget servant;
	private RS2Widget bedroom;
	private int randomVariable;
	private RS2Object larder = null;
	private RS2Object door = null;
	private RS2Object bed = null;
	private RS2Object portal = null;
	private RS2Object bell = null;
	
	public BuildRooms(Script script){
		this.script = script;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validate() throws InterruptedException {
		return !castleWarsArea.contains(script.myPosition()) && script.getMap().isInHouse();
	}

	@Override
	public void run() throws InterruptedException {
		//grab widgets
		cacheWidgets();	
		buildingMode();
		
		//makeRooms
		makeRooms();
			
		//teleport back
		//if(!script.getDialogues().inDialogue() && !script.getInventory().contains("Oak logs")){
			//teleportBack();
		//}
	}
	private void rSleep(int x,int y) throws InterruptedException{
		Script.sleep(Script.random(x,y));
	}
	private void scrollInterval(int intervals){
		for(int i = 0; i < intervals; i++){
			script.getMouse().scrollDown();
		}
	}
	private void makeRooms() throws InterruptedException{
		buildFirstRoom();
		buildSecondRoom();
		buildBell();

	}
	private void buildBell() throws InterruptedException{
		if(script.getInventory().contains("Oak plank") &&
				script.getInventory().getAmount("Oak plank") == 1){
			if(portal == null){
				portal = script.getObjects().closest("Portal");
			}
			if(portal != null){
				script.getWalking().walk(new Position(portal.getX()-2,portal.getY(),portal.getZ()));
			}
			if(bell == null){
				bell = script.getObjects().closest("Bell pull space");
			}
			if(bell != null){
				script.log("Trying to build bell");
				bell.interact("Build");
				rSleep(4000,5000);
				script.getKeyboard().typeKey('1');
				rSleep(3000,5000);
				teleportBack();
				
			}
		}
	}
	private void buildSecondRoom() throws InterruptedException{
		if(larder != null &&
				script.getObjects().closest("Bed") != null &&
				script.getInventory().contains("Oak plank") &&
				script.getInventory().getAmount("Oak plank") == 4) {
			script.getWalking().walk(new Position(larder.getX(),larder.getY()-4,larder.getZ()));
			buyRoom();
			buildBed();
		}
	}
	private void buildFirstRoom() throws InterruptedException{
		if(larder == null){
			larder = script.getObjects().closest("Larder");
		}
		if(door != null && script.getInventory().contains("Oak plank") &&
				script.getInventory().getAmount("Oak plank") == 7 &&
				script.getObjects().closest("Bed space") == null &&
				larder != null){
			script.getWalking().walk(new Position(larder.getX()-1,larder.getY(),larder.getZ()));
			buyRoom();
			buildBed();
		}
	}
	private void buildBed() throws InterruptedException{
		bed = script.getObjects().closest("Bed space");
		if(bed != null){
			bed.interact("Build");
			rSleep(8000,11000);
			script.getKeyboard().typeKey('2');
			rSleep(3000,5000);
		}
	}
	private void buyRoom() throws InterruptedException{
		door = script.getObjects().closest("Door hotspot");
		door.interact("Build");
		rSleep(1500,3000);
		script.getMouse().move(379,115);
		rSleep(1500,3000);
		scrollInterval(5);
		rSleep(1500,3000);
		bedroom = script.getWidgets().get(212, 13);
		rSleep(1500,3000);
		if(bedroom != null){
			bedroom.hover();
			rSleep(500,700);
			script.getMouse().click(false);
			rSleep(1500,3000);
			script.getDialogues().selectOption(1);
			rSleep(1500,3000);
			script.getDialogues().selectOption(3);
			rSleep(5000,7000);
		}
	}
	private void makeServantVisible() throws InterruptedException{
		if(house.isVisible()){
			house.interact("View House Options");
			rSleep(700,1500);
		}
		else if(inventory.isVisible()){
			settings.interact("Options");
			rSleep(500,1000);
			house.interact("View House Options");
			rSleep(700,1500);
		}
	}
	private void cacheWidgets() throws InterruptedException{
		if(settings == null){
			script.log("Caching settings");
			settings = script.getWidgets().get(548, 39);
		}
		if(house == null){
			script.log("Caching house");
			house = script.getWidgets().get(261, 76);
		}
		if(inventory == null){
			script.log("Caching inventory");
			inventory = script.getWidgets().get(548, 66);
		}
		if(inventoryIcon == null){
			script.log("Caching inventory icon");
			inventoryIcon = script.getWidgets().get(548, 55);
		}
	 }
	 private void buildingMode() throws InterruptedException{
		if(script.getObjects().closest("Door hotspot") == null){
			makeServantVisible();
			servant = script.getWidgets().get(370,5);
			if(servant != null && servant.hover()){
				script.getMouse().click(false);
				script.log("mouse clicked");
				rSleep(2000,3000);
			}
		}
	 }
	private void teleportBack() throws InterruptedException{
		script.log("Teleporting back");
		if(inventory.isHidden()){
			inventoryIcon.interact("Inventory");
			rSleep(300,1000);
		}
		int rodSlot = script.getInventory().getSlotForNameThatContains("Ring of dueling");
		randomVariable = Script.random(1,5);
		if(randomVariable == 1){
			script.getInventory().hover(rodSlot);
		}
		rSleep(300,1000);
		if(script.getInventory().interact(rodSlot, "Rub")){
			new ConditionalSleep(Script.random(3000,7000)){
				@Override
				public boolean condition() throws InterruptedException{
					return script.getDialogues().inDialogue();
				}
			}.sleep();
		}
		rSleep(300,1000);
		if(script.getDialogues().completeDialogue("Castle Wars Arena.")){
			rSleep(200,300);
			if(Script.random(1,3) == 1){
				script.log("Simulating 2 overclick");
				randomVariable = Script.random(1,3);
				for(int i = 0; i < randomVariable; i++){
					script.getKeyboard().typeString("2",false);
					rSleep(100,300);
				}
			}
			new ConditionalSleep(Script.random(3000,7000)){
				@Override
				public boolean condition() throws InterruptedException{
					return castleWarsArea.contains(script.myPosition());
				}
			}.sleep();
			script.log("Done setting up house, stopping script...");
			rSleep(5000,7000);
			script.stop();
		}
	 }
}