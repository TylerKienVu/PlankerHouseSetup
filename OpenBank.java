package scripts;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import mainFiles.Activity;

public class OpenBank implements Activity{
	private Script script;
	private String id = "OpenBank";
	private Area castleWarsArea = new Area(new Position(2443,3082,0),new Position(2438,3097,0));
	private RS2Object bank;	
	
	public OpenBank(Script script){
		this.script = script;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validate() throws InterruptedException {
		return !script.getBank().isOpen() && castleWarsArea.contains(script.myPosition()) &&
				!script.getMap().isInHouse() && !script.getInventory().contains("Bolt of cloth");
	}

	@Override
	public void run() throws InterruptedException {
		moveOffScreen();
		bank = script.getObjects().closest("Bank chest");
		if(!script.getBank().isOpen()){
			openBank();
		}
		script.getBank().depositAll();
	}
	private void rSleep(int x,int y) throws InterruptedException{
		Script.sleep(Script.random(x,y));
	}
	private void moveOffScreen() throws InterruptedException{
		if(Script.random(1,20)==1){
			script.log("Moving mouse off screen");
			script.getMouse().moveOutsideScreen();
			rSleep(3000,9000);
		}
		else{
			if(script.myPlayer().isMoving()){
				rSleep(1000,2000);
			}
			else{
				rSleep(300,500);
			}
		}
	}
	private void openBank() throws InterruptedException{
		if(!script.getBank().isOpen() && bank != null){
			if(bank.interact("Use")){
				script.log("Bank interact 2 executed");
				new ConditionalSleep(Script.random(3000,7000)){
					@Override
					public boolean condition() throws InterruptedException{
						return script.getBank().isOpen();
					}
				}.sleep();
			}
			moveOffScreen();
		}
	}
}