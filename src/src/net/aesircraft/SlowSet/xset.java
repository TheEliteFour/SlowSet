package net.aesircraft.SlowSet;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;



public class xset implements CommandExecutor
{
	public static SlowSet plugin;
	 public xset(SlowSet instance){
		 plugin=instance;
	 }
	 public static boolean perms(Player player){
		 if (SlowSet.getStatic().permissionHandler.has(player, "slowset.xset") || player.isOp()){
			 return true;
		 }
		 return false;
	 }
	 private WorldEditPlugin pluginwe=SlowSet.getWE();
	  private BukkitPlayer wrapPlayer(Player player) {
        return new BukkitPlayer(pluginwe, pluginwe.getServerInterface(), player);
    }
	@Override
	public boolean onCommand(CommandSender player, Command command, String cmd,String[] comA) {
		Player user=(Player) player;
		if (!perms(user)){
			player.sendMessage("§4No.");	
			return true;
		}
		List checkt=Scheduler.Tasks;
		for (int chectr=0;chectr<checkt.size();chectr++){
			if (((Task) checkt.get(chectr)).player==player){
			player.sendMessage("§4You already have a task set!");
			return true;
		}
		}
		int id=user.getItemInHand().getTypeId();
		
		if (!Util.useable(id)){
			player.sendMessage("§4Cannot use that type of item!");
			return true;
		}		
		
		WorldEdit we=pluginwe.getWorldEdit();
		LocalPlayer weplayer=wrapPlayer(user);
		LocalSession session=we.getSession(weplayer);
		Region region;
		try
		{
			region=session.getSelection(weplayer.getWorld());
		}
		catch (IncompleteRegionException ex)
		{
			player.sendMessage("§4Finish selecting your region!");
			return true;
		}
		int count=region.getArea();
		if (id!=0){
		ItemStack item=new ItemStack(id,count,user.getItemInHand().getData().getData());
		if (!InventoryWorkaround.containsItem(user.getInventory(), false, item)){
			player.sendMessage("§4You do not have enough of that item for this, it requires §b"+count+"§4!");
			return true;
		}
		}
		Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();

            int minX = min.getBlockX();
            int minY = min.getBlockY();
            int minZ = min.getBlockZ();
            int maxX = max.getBlockX();
            int maxY = max.getBlockY();
            int maxZ = max.getBlockZ();
			int ctr=0;
			Location[] locations=new Location[count];
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        locations[ctr] = new Location(user.getWorld(),x, y, z);
						ctr++;                        
                   }
                }
            }
			
			Task task=new Task();
			if (id!=0){
				task.id=id;
				task.dat=user.getItemInHand().getData();
			}
			else{
				task.id=0;
				task.dat=new MaterialData(0);
			}
			task.count=count;
			task.locs=locations;
			task.player=user;
			if (id==0){
				task.delete=true;
			}
			task.world=user.getWorld();
			task.counter=0;
			task.admin=false;
			player.sendMessage("§2Starting Task. Do not leave, logout or change your inventory!");
			player.sendMessage("§2If you do the proccess will be cancled!");
			player.sendMessage("§bUsing /xundo will refund items ONLY if you have space!");
			Scheduler.Tasks.add(task);
		return true;
	}
}
