package com.xinlan.crystal;

public class GameInstance {
	public static final int SCREEN_WIDTH=480;
	public static final int SCREEN_HEIGHT=800;
	
	private static GameInstance instance;
	
	public static GameInstance getInstance() {
		if (instance == null) {
			instance = new GameInstance();
		}
		return instance;
	}
	
}//end class
