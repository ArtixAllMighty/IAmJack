package iamjack.player;

import java.util.ArrayList;

import framework.save.DataList;
import framework.save.DataTag;
import iamjack.player.achievements.Achievement;

public class PlayerData {

	public static ArrayList<String> videoOfTheDay = new ArrayList<String>();
	public static ArrayList<Achievement> achievements = new ArrayList<Achievement>();
	public static ArrayList<String> soundsPlayed = new ArrayList<String>();
	public static ArrayList<String> itemsBought = new ArrayList<String>();

	public static String currentlySaying = "";

	/**boolean triggered when credits roll. used to not loop the same song twice in a row*/
	public static boolean dontDoubleLoop;
	public static boolean hasWorkedOut = false;

	public static int money = 0;
	public static int fans = 0;
	public static int daysPlayed = 1;
	public static int exercised = 0;

	
	
	public static void writeToSave(DataTag tag){

		tag.writeInt("money", money);
		tag.writeInt("fans", fans);
		tag.writeInt("daysPlayed", daysPlayed);
		tag.writeInt("exercised", exercised);
		tag.writeBoolean("hasWorkedOut", hasWorkedOut);

		DataList list = new DataList();
		for(Achievement a : achievements){
			DataTag t = new DataTag();
			a.writeData(t);
			list.write(t);
		}
		tag.writeList("achievements", list);

		DataList list2 = new DataList();
		for(int i = 0; i < itemsBought.size();i++){
			String s = itemsBought.get(i);
			DataTag t = new DataTag();
			t.writeString("name"+i, s);
			list2.write(t);
		}
		tag.writeList("itemsBought", list2);
	}

	public static void readData(DataTag tag){
		money = tag.readInt("money");
		fans = tag.readInt("fans");
		daysPlayed = tag.readInt("daysPlayed");
		exercised = tag.readInt("exercised");
		hasWorkedOut = tag.readBoolean("hasWorkedOut");

		DataList list = tag.readList("achievements");
		for(int i = 0; i < list.data().length(); i++){
			DataTag dt = list.readArray(i);
			achievements.add(Achievement.readData(dt));
		}

		DataList list2 = tag.readList("itemsBought");
		for(int i = 0; i < list2.data().length(); i++){
			DataTag dt = list2.readArray(i);
			itemsBought.add(dt.readString("name"+i));
		}
	}
}
