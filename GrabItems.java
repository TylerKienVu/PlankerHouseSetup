package scripts;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import mainFiles.Activity;

public class GrabItems implements Activity{
	private Script script;
	private String id = "GrabItems";
	private Area castleWarsArea = new Area(new Position(2443,3082,0),new Position(2438,3097,0));
	private int randomVariable;
	
	public GrabItems(Script script){
		this.script = script;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validate() throws InterruptedException {
		return script.getBank().isOpen() &&
				castleWarsArea.contains(script.myPosition());
	}

	@Override
	public void run() throws InterruptedException {
		if(script.getBank().isOpen()){
			grabBankCoins();
			grabBankTabs();
			grabBankRings();
			grabBankCloth();
			grabBankHammer();
			grabBankSaw();
			grabBankRope();
			checkBankOakPlank();
			script.getBank().close();
		}
	}
	private void rSleep(int x,int y) throws InterruptedException{
		Script.sleep(Script.random(x,y));
	}
	private void moveOffScreen() throws InterruptedException{
		randomVariable = Script.random(1,20);
		if(randomVariable==1){
			script.log("Moving mouse off screen");
			script.getMouse().moveOutsideScreen();
			rSleep(3000,9000);
		}
		else{
			rSleep(300,700);
		}
	}
	private void grabBankCoins() throws InterruptedException{
		if(script.getBank().isOpen() && !script.getInventory().contains("Coins")){
			script.log("Grabbing Coins");
			moveOffScreen();
			if(script.getBank().contains("Coins")){
				script.getBank().withdrawAll("Coins");
			}
		}
	}
	private void grabBankTabs() throws InterruptedException{
		if(script.getBank().isOpen() && !script.getInventory().contains("Teleport to house")){
			script.log("Grabbing house tabs");
			moveOffScreen();
			if(script.getBank().contains("Teleport to house")){
				script.getBank().withdrawAll("Teleport to house");
			}
		}
	}
	private void grabBankRings() throws InterruptedException{
		if(script.getBank().isOpen() &&
			script.getInventory().getSlotForNameThatContains("Ring of dueling") == -1){
			script.log("Grabbing rings of dueling");
			moveOffScreen();
			if(script.getBank().contains("Ring of dueling(8)")){
				script.getBank().withdraw("Ring of dueling(8)",1);
			}
		}
	}
	private void grabBankCloth() throws InterruptedException{
		if(script.getBank().isOpen() &&
			!script.getInventory().contains("Bolt of cloth")){
			script.log("Grabbing bolts of cloth");
			moveOffScreen();
			if(script.getBank().contains("Bolt of cloth")){
				script.getBank().withdraw("Bolt of cloth",4);
			}
		}
	}
	private void grabBankHammer() throws InterruptedException{
		if(script.getBank().isOpen() &&
			!script.getInventory().contains("Hammer")){
			script.log("Grabbing Hammer");
			moveOffScreen();
			if(script.getBank().contains("Hammer")){
				script.getBank().withdraw("Hammer",1);
			}
		}
	}
	private void grabBankSaw() throws InterruptedException{
		if(script.getBank().isOpen() &&
			!script.getInventory().contains("Saw")){
			script.log("Grabbing Saw");
			moveOffScreen();
			if(script.getBank().contains("Saw")){
				script.getBank().withdraw("Saw",1);
			}
		}
	}
	private void grabBankRope() throws InterruptedException{
		if(script.getBank().isOpen() &&
			!script.getInventory().contains("Rope")){
			script.log("Grabbing Rope");
			moveOffScreen();
			if(script.getBank().contains("Rope")){
				script.getBank().withdraw("Rope",1);
			}
		}
	}
	private void checkBankOakPlank() throws InterruptedException{
		if(script.getBank().isOpen() &&
			!script.getInventory().contains(8778)){
			script.log("Grabbing oak planks");
			moveOffScreen();
			if(script.getBank().contains(8778)){
				script.getBank().withdraw(8778,7);
			}
			else{
				script.log("Need to run to GE");
				teleportToGE();
			}
		}
	}
	private void teleportToGE() throws InterruptedException{
		if(script.getBank().isOpen()){
			script.getBank().depositAll();
			int rowSlot = script.getBank().getSlotForNameThatContains("Ring of wealth");
			script.getBank().interact(rowSlot, "Withdraw-1");
			rSleep(1500,3000);
			script.getBank().close();
			rSleep(1500,3000);
		rowSlot = script.getInventory().getSlotForNameThatContains("Ring of wealth");
		if(script.getInventory().interact(rowSlot, "Rub")){
			new ConditionalSleep(5000){
				public boolean condition(){
					return script.getDialogues().inDialogue();
				}
			}.sleep();
		}
		rSleep(1500,3000);
		if(script.getDialogues().inDialogue())
			script.getDialogues().selectOption(2);
		}
	}
}
