package net.aesircraft.SlowSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;



public class xundo implements CommandExecutor
{
	public static SlowSet plugin;
	 public xundo(SlowSet instance){
		 plugin=instance;
	 }
	 public static boolean perms(Player player){
		 if (SlowSet.getStatic().permissionHandler.has(player, "slowset.xundo") || player.isOp()){
			 return true;
		 }
		 return false;
	 }

	@Override
	public boolean onCommand(CommandSender player, Command command, String cmd,String[] comA) {
		Player user=(Player) player;
		if (!Util.undos.containsKey(user)){
			player.sendMessage("§4Nothing to undo!");
			return true;
		}
		Undo undo = (Undo) Util.undos.get(user);
		UndoTask task=new UndoTask();
			task.count=undo.count;
			task.player=user;
			task.world=user.getWorld();
			task.counter=0;
			task.blocks=undo.blocks;
			task.undoblocks=undo.undoblocks;
			task.rems=undo.rems;
			player.sendMessage("§2Starting Undo. Do not leave or logout!");
			player.sendMessage("§2If you do the proccess will be cancled!");
			player.sendMessage("§bUndo will refund items ONLY if you have space!");
			Scheduler.Undos.add(task);
		return true;
	}
}
