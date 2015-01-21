package zuul.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import zuul.Game;
import zuul.io.Command;
import zuul.io.CommandWord;
import zuul.rooms.Room;


/**
 * 
 * @author Adrien Boucher
 * 
 * Manage the IA's
 *	-> Make them act like a player
 */
public class IAManager {
	private ArrayList<IA> listIA;
	private ArrayList<Room> rooms;
	private ArrayList<CommandWord> commands;
	
	private Game game;
	
	public IAManager(ArrayList<Room> rooms)
	{
		this.rooms 		= rooms;
		this.commands 	= new ArrayList<CommandWord>();
		this.listIA 	= new ArrayList<IA>();
		
		addIA(new IA("Dorian le violent"));
		addIA(new IA("Toto"));
		addIA(new IA("Jean Gui"));
	}
	
	public void addIA(IA npc){ listIA.add(npc); }
	
	public void initIA(Room r)
	{
		for(int i=0; i<listIA.size(); i++)
			listIA.get(i).enter(r);
	}
	
	
	public int getNbIA(){ return listIA.size(); }
	
	public IA getNextIA()
	{
		return listIA.get(new Random().nextInt(listIA.size()));
	}
	
	public HashMap<IA, Command> generateCommandForIAs()
	{		
		HashMap<IA, Command> result = new HashMap<IA, Command>();
		HashSet<Integer> iaNum = getRandomIANum();
		
		for(int i : iaNum)
		{
			IA ia = listIA.get(i);
			result.put(ia, getCommandForIA(ia));
		}
		
		return result;
	}
	
	public HashSet<Integer> getRandomIANum()
	{
		Random r = new Random();
		int nbIA = listIA.size(), nbAct = r.nextInt(nbIA)+1;
		HashSet<Integer> num = new HashSet<Integer>();
		
		for(int i=1; i<=nbAct; i++)
		{
			int n;
			do{ n = r.nextInt(nbIA);}while(num.contains(n));
			num.add(n);			
		}
		
		return num;
	}
	
	public Command getCommandForIA(IA ia)
	{
		Room currentRoom = ia.getCurrentRoom();
		boolean canDo = currentRoom.canUseDoCommand();
		//boolean canAnwser = currentRoom.canUseAnswerCommand();
		
		if(canDo && Math.random() < 0.3)
			return generateDoCommand(ia);
		
		return generateValidGoCommand(ia);		
	}
	
	public Command generateDoCommand(IA ia)
	{
		ArrayList<String> actions = ia.getCurrentRoom().getActions();
		String action = actions.get(new Random().nextInt(actions.size()));
		
		return new Command(CommandWord.DO, action);
	}
	
	public Command generateValidGoCommand(IA ia)
	{
		return new Command(CommandWord.GO, ia.getCurrentRoom().getRandomExitDirection());
	}
	
	public void initCommandWords()
	{
		commands.add(CommandWord.GO);
		commands.add(CommandWord.DO);
		commands.add(CommandWord.PICK);
		commands.add(CommandWord.DROP);
		commands.add(CommandWord.ANSWER);
	}
}
