package net.aesircraft.SlowSet;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class UndoTask
{
	public List rems=new ArrayList();
	public List blocks=new ArrayList();	
	public List undoblocks=new ArrayList();
	int count;
	int counter;
	Player player;
	World world;
}
