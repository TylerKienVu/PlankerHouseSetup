package mainFiles;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import scripts.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

//Current goals: finish room building

@ScriptManifest(author = "Tylersobored", info = "Sets up house for planker", name = "SetupHouse", version = 0, logo = "")

public class MainScript extends Script {
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private TaskManager taskmanager;
	
	@Override
	public void onStart() {
		Collections.addAll(activities,new TeleportToBank(this),new OpenBank(this),
				new GrabItems(this),new ItemGECheck(this),new TeleportToHouse(this)
				,new BuildRooms(this),new Wait(this));
		this.taskmanager = new TaskManager(activities,this);
	}
	
	@Override
	public int onLoop() throws InterruptedException {
		taskmanager.getActive().run();
		return Script.random(500, 700);
	}
	
	@Override
	public void onExit() {
	}
	
	@Override
	public void onPaint(Graphics2D g) {
		
	}
}