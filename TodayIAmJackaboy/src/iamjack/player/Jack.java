package iamjack.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import iamjack.engine.Window;
import iamjack.engine.input.KeyHandler;
import iamjack.engine.input.MouseHandler;
import iamjack.engine.resources.Music;
import iamjack.resourceManager.Images;

public class Jack {

	private double posX;
	private double posY;

	public boolean facingRight = true;
	private boolean animated = false;

	private int counter = 0;
	private int animIndexWalking = 0;
	private int animIndexKeyboard = 0;
	private int animTalking = 0;

	private boolean isSitting;
	private boolean isPlaying;
	private boolean isTalking;
	private boolean isBenchPressing;

	private BufferedImage[] animation ;
	private BufferedImage[] animationPlaying ;
	private BufferedImage[] animationTalking ;

	private double speed = Window.scale(5D);

	private Random rand = new Random();

	private final Font subTitle ;

	private float armPress = 0f;
	private boolean canPress = true;
	private int reps = 0;

	public Jack() {

		subTitle = new Font("SquareFont", Font.PLAIN, Window.scale(35));

		posX = Window.scale(50);
		posY = Window.getHeight() / 2;

		animation = new BufferedImage[]{
				Images.jack,
				Images.jackWalk[0],
				Images.jack,
				Images.jackWalk[1]
		};

		animationPlaying = new BufferedImage[]{
				Images.jackKeyBoard[0],
				Images.jackKeyBoard[1],
		};

		animationTalking = new BufferedImage[]{
				Images.jackTalk[0],
				Images.jackTalk[1],
				Images.jackTalk[2],
				Images.jackTalk[3],
				Images.jackTalk[4],
		};
	}

	public void draw(Graphics2D g){

		if(isSitting){
			if(isPlaying)
				g.drawImage(animationPlaying[animIndexKeyboard], (int)posX, (int)posY, Window.scale(128), Window.scale(128), null);
			else
				g.drawImage(Images.jackSit, (int)posX, (int)posY, Window.scale(128), Window.scale(128), null);

			if(isTalking)
				g.drawImage(animationTalking[animTalking], (int)posX, (int)posY, Window.scale(128), Window.scale(128), null);

		}else if(isBenchPressing){
			g.drawImage(Images.jackPress, (int)posX, (int)posY, Window.scale(128), Window.scale(128), null);
			if(armPress > 0 && canPress)
				g.drawImage(Images.jackPressFace, (int)posX, (int)posY, Window.scale(128), Window.scale(128), null);

			g.drawImage(Images.jackPressArms, (int)posX, (int)posY - (int)armPress, Window.scale(128), Window.scale(128), null);
			
		}else
			if(facingRight)
				if(animated)
					g.drawImage(animation[animIndexWalking], (int)posX - Window.scale(32), (int)posY, Window.scale(128), Window.scale(128), null);
				else
					g.drawImage(Images.jack, (int)posX- Window.scale(32), (int)posY, Window.scale(128), Window.scale(128), null);
			else
				if(animated)
					g.drawImage(animation[animIndexWalking], (int)posX+Window.scale(128)- Window.scale(32), (int)posY, -Window.scale(128), Window.scale(128), null);
				else
					g.drawImage(Images.jack, (int)posX+Window.scale(128)- Window.scale(32), (int)posY, -Window.scale(128), Window.scale(128), null);

	}

	public void say(String sentence, Graphics2D g){
		g.setColor(Color.green.darker().darker().darker());
		g.setFont(subTitle);

		int x = g.getFontMetrics().stringWidth(sentence);
		g.drawString(sentence, Window.getWidth()/2 - x/2, Window.getHeight()/2 + Window.getHeight()/3);

	}

	public void update(){

		if(!isSitting && !isBenchPressing)
			doMovement();

		if(animated){
			counter++;
		}else{
			counter = 0;
		}

		if(counter % 2 == 0){
			animIndexWalking++;

			if(animIndexWalking >= animation.length)
				animIndexWalking = 0;
		}

		if(counter % 5 == 0){
			animIndexKeyboard++;

			if(animIndexKeyboard >= animationPlaying.length)
				animIndexKeyboard = 0;
		}

		if(counter % 4 == 0)
			animTalking = rand.nextInt(5);

		if(armPress > 20f){
			if(canPress)
				reps++;
			canPress = false;
		}

		if(armPress <= 0)
			canPress = true;

		if(armPress > 0f)
			armPress -= canPress ? 0.05 : 0.2f;
	}

	private void doMovement(){

		if(MouseHandler.clicked != null){
			if(MouseHandler.clicked.getX() > Window.getWidth() - Window.scale(175))
				MouseHandler.clicked.x = Window.getWidth() - Window.scale(175)-1;

			if(MouseHandler.clicked.getX() < 50)
				MouseHandler.clicked.x = -50;

			if(posX == MouseHandler.clicked.getX()){
				MouseHandler.clicked = null;
				animated = false;
			}
		}

		if(KeyHandler.keyState[KeyHandler.RIGHT] && posX < Window.getWidth() - Window.scale(175)){
			posX += speed;
			facingRight = true;
			animated = true;

			if(counter % 5 == 0)
				Music.play("step"+rand.nextInt(4));

			MouseHandler.clicked = null;
		}

		if(KeyHandler.keyState[KeyHandler.LEFT] && posX > - 100 ){
			posX -= speed;
			facingRight = false;
			animated = true;

			if(counter % 5 == 0)
				Music.play("step"+rand.nextInt(4));

			MouseHandler.clicked = null;
		}

		if(MouseHandler.clicked != null && posX > MouseHandler.clicked.getX() && posX > -100){
			double speedMod = speed;
			if(posX - MouseHandler.clicked.getX() < speed){
				speedMod =  posX - MouseHandler.clicked.getX();
			}

			posX -= speedMod;
			facingRight = false;
			animated = true;

			if(counter % 5 == 0)
				Music.play("step"+rand.nextInt(4));

		}

		if(MouseHandler.clicked != null && posX < MouseHandler.clicked.getX() && posX < Window.getWidth() - Window.scale(175)){
			double speedMod = speed;
			if(MouseHandler.clicked.getX() - posX < speed){
				speedMod =  MouseHandler.clicked.getX() - posX;
			}
			posX += speedMod;
			facingRight = true;
			animated = true;

			if(counter % 5 == 0)
				Music.play("step"+rand.nextInt(4));
		}


		if(!KeyHandler.keyState[KeyHandler.RIGHT] && !KeyHandler.keyState[KeyHandler.LEFT] && MouseHandler.clicked == null){
			animated = false;
		}
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public void setSitting(boolean isSitting) {
		this.isSitting = isSitting;
	}

	public boolean isSitting() {
		return isSitting;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	public void setTalking(boolean isTalking) {
		this.isTalking = isTalking;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setBenchPressing(boolean isBenchPressing) {
		this.isBenchPressing = isBenchPressing;
	}

	public boolean isBenchPressing() {
		return isBenchPressing;
	}

	public void pressArms(){
		armPress +=1f;
	}

	public boolean canPress(){
		return canPress;
	}

	public int repsDone(){
		return reps;
	}
}
