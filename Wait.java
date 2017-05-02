package scripts;

import org.osbot.rs07.script.Script;

import mainFiles.Activity;

public class Wait implements Activity{
	private Script script;
	private String id = "Wait";
	
	public Wait(Script script){
		this.script = script;
	}
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validate() throws InterruptedException {
		return true;
	}

	@Override
	public void run() throws InterruptedException {
		script.getMouse().moveRandomly();
		moveOffScreen();
	}
	private void moveOffScreen() throws InterruptedException{
		if(Script.random(1,10) == 1){
			script.log("Moving mouse off screen");
			script.getMouse().moveOutsideScreen();
			rSleep(7000,20000);
		}
		else{
			rSleep(5000,7000);
		}
	}
	
	private void rSleep(int x,int y) throws InterruptedException{
		Script.sleep(Script.random(x,y));
	}

}
