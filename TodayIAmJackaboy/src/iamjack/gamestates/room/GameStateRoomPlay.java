package iamjack.gamestates.room;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import framework.GameStateHandler;
import framework.input.KeyHandler;
import framework.input.MouseHandler;
import framework.resourceLoaders.Music;
import framework.resourceLoaders.StreamMusic;
import framework.window.Window;
import iamjack.buttons.ButtonGamePlay;
import iamjack.gamestates.GameStateDrawHelper;
import iamjack.gamestates.shop.ShopItems;
import iamjack.main.GameStateHandlerJack;
import iamjack.main.GameStateJack;
import iamjack.player.Jack;
import iamjack.player.PlayerData;
import iamjack.resourceManager.Images;
import iamjack.resourceManager.Sounds;

public class GameStateRoomPlay extends GameStateJack {

	private int sitX ;
	private int sitY ;

	private Jack jack;

	private int stage = 0;

	private boolean buttonClicked = false;

	private double speakTimer = 0;

	private int[][] choices = new int[][]{
		{0},
		{1,2},
		{3,5,2},
		{7,2,8},
		{2,6,5},
		{4,3,2},
		{5,2,7},
		{2,6,8},
		{4,3,2},
		{9}
	};

	private String[] text = new String[]{
			"Play Game",//0
			"Intro",//1
			"Yell",//2
			"Funny",//3
			"Jack TM",//4
			"Laugh",//5
			"Rage",//6
			"Energy",//7
			"Scared",//8
			"Outro"//9
	};

	private ButtonGamePlay[][] buttons = new ButtonGamePlay[10][3];

	public GameStateRoomPlay(GameStateHandler gsh) {
		super(gsh);

		StreamMusic.loopStream(Sounds.STREAM_METAL);
		
		jack = new Jack();
		sitX = Window.getGameScale(852);
		sitY = Window.getGameScale(240);

		jack.setPosX(sitX);
		jack.setPosY(sitY);

		jack.setSitting(true);
		jack.setAnimated(true);

		//if new set of choices bought
		if(PlayerData.itemsBought.contains(ShopItems.voices))
			choices = new int[][]{
			{0},
			{1,6},
			{4,7,5},
			{8,2,6},
			{7,5,3},
			{4,6,2},
			{5,8,7},
			{6,2,3},
			{4,8,5},
			{9}
		};
		else
			choices = new int[][]{
			{0},
			{1,2},
			{3,5,2},
			{7,2,8},
			{2,6,5},
			{4,3,2},
			{5,2,7},
			{2,6,8},
			{4,3,2},
			{9}
		};

		for(int j = 0; j < choices.length; j++)
			for(int i = 0; i < choices[j].length; i++){
				buttons[j][i] = new ButtonGamePlay(
						text[choices[j][i]],
						Window.getGameScale(550),
						Window.getGameScale(150) + (Window.getGameScale(75) * i));
			}
	}

	@Override
	public void draw(Graphics2D g) {

		GameStateDrawHelper.drawRoom(g);

		jack.draw(g);

		g.drawImage(Images.chairLow, Window.getGameScale(824), Window.getGameScale(272), (int)(64f*GameStateDrawHelper.scale), (int)(64f*GameStateDrawHelper.scale), null);

		g.setComposite(AlphaComposite.SrcOver);

		g.drawImage(Images.roomShade, 
				Window.getWidth()/2 - (int)(GameStateDrawHelper.sizeX/2f),
				Window.getHeight()/2 - (int)(GameStateDrawHelper.sizeY/2f), 
				(int)GameStateDrawHelper.sizeX, (int)GameStateDrawHelper.sizeY, null);

		if(stage < buttons.length)
			for(int i = 0; i < buttons[stage].length; i++){

				ButtonGamePlay b = buttons[stage][i];

				if(b != null)
					b.draw(g);

			}

		if(KeyHandler.isHeld(KeyHandler.ESCAPE))
			GameStateDrawHelper.drawMenu(g);
		
		super.draw(g);
	}

	@Override
	public void update() {
		super.update();

		jack.update();

		//check for click before button clicked
		if(MouseHandler.click && stage >= choices.length ){
			Music.stop(PlayerData.currentlySaying);
			StreamMusic.endStream(Sounds.STREAM_METAL);
			gsh.changeGameState(GameStateHandlerJack.GAME_ROOM_VIDEO_DONE);
		}

		if(speakTimer <= 0 && stage < buttons.length)
			for(ButtonGamePlay b : buttons[stage]){
				if(b!=null){
					b.update(gsh);
					if(MouseHandler.click && b.isLit()){
						buttonClicked = true;
						stage++;
						//sets jack mashing his keyboard after first button is clicked
						if(!jack.isPlaying())
							jack.setPlaying(true);
					}
				}
			}	

		if(buttonClicked){
			buttonClicked = false;
			if(PlayerData.currentlySaying.length() > 0){
				int songlength = Music.getFrameLength(PlayerData.currentlySaying);
				float secs = (float)songlength/Music.getFrameRate(PlayerData.currentlySaying);
				double frames = ((double)secs) * 60d;
				speakTimer = Math.floor(frames); 
				jack.setTalking(true);
			}
		}

		if(speakTimer > 0)
			speakTimer --;
		else
			jack.setTalking(false);

		if(stage ==	choices.length && speakTimer <= 120d)
			StreamMusic.endStream(Sounds.STREAM_METAL);
		if(stage >=	choices.length && speakTimer <= 0d)
			gsh.changeGameState(GameStateHandlerJack.GAME_ROOM_VIDEO_DONE);
	}
}
