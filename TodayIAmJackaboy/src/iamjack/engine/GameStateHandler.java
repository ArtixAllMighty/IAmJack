package iamjack.engine;


import java.awt.Graphics2D;

import iamjack.engine.input.MouseHandler;
import iamjack.gamestates.GameStateEndDay;
import iamjack.gamestates.GameStateMenu;
import iamjack.gamestates.GameStateRoom;
import iamjack.gamestates.GameStateRoomDone;
import iamjack.gamestates.GameStateRoomPlay;

public class GameStateHandler {

	public int currentGameState;

	public static final int MENU = 0;
	public static final int GAME_ENTRY = 1;
	public static final int GAME_GAMING = 2;
	public static final int GAME_DONEGAMING = 3;
	public static final int GAME_ENDDAY = 4;

	public static final GameState[] states = new GameState[20];

	public GameStateHandler() {
		currentGameState = MENU;
		loadState(MENU);
	}

	public int getCurrentGameState() {
		return currentGameState;
	}


	public void draw(Graphics2D g) {
		states[currentGameState].draw(g);
	}

	public void update() {
		states[currentGameState].update();
	}

	public void changeGameState(int state) {
		MouseHandler.clicked = null;
		
		unloadState(currentGameState);
		currentGameState = state;
		loadState(state);
	}

	private void unloadState(int state){
		states[state] = null;
	}

	private void loadState(int state){
		switch(state){
		case MENU:
			states[state] = new GameStateMenu(this);
			break;
		case GAME_ENTRY:
			states[state] = new GameStateRoom(this);
			break;
		case GAME_GAMING:
			states[state] = new GameStateRoomPlay(this);
			break;
		case GAME_DONEGAMING:
			states[state] = new GameStateRoomDone(this);
			break;
		case GAME_ENDDAY:
			states[state] = new GameStateEndDay(this);
			break;
		}
	}
}
