package net.aesircraft.SlowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class Scheduler
{
	public static List Tasks=new ArrayList();
	public static List Undos=new ArrayList();
	
	
	
	public static void executeUndo(UndoTask task, int tid){
		if (!task.player.isOnline()){
			Undos.remove(tid);
		}
		ItemStack item;
		boolean refund=true;
		if (!(task.rems.get(task.counter) instanceof String)){
		item=(ItemStack)task.rems.get(task.counter);
		}
		else{
			item=new ItemStack(0);
			refund=false;
		}
		
		Block block=task.world.getBlockAt(((Block) task.blocks.get(task.counter)).getLocation());
		UndoBlock undoblock=(UndoBlock) task.undoblocks.get(task.counter);
		if (!block.getChunk().isLoaded()){
			task.player.sendMessage("§4Stopped building, must be in area!");
			Undos.remove(tid);
			Util.undos.remove(task.player);
			return;
		}
		if (!block.isEmpty()){
		BlockBreakEvent breake=new BlockBreakEvent(block,task.player);
		SlowSet.getStatic().getServer().getPluginManager().callEvent(breake);
		if (breake.isCancelled()){
			task.player.sendMessage("§4Stopped building, protected area!");
			Undos.remove(tid);
			Util.undos.remove(task.player);
			return;
		}
		}
		if (undoblock.id!=0){
			BlockPlaceEvent blockpe=new BlockPlaceEvent(block,block.getState(),block,item,task.player,true);
			SlowSet.getStatic().getServer().getPluginManager().callEvent(blockpe);
			if (blockpe.isCancelled()){
			task.player.sendMessage("§4Stopped building, protected area!");
			Undos.remove(tid);
			Util.undos.remove(task.player);
			return;
		}
		}	
		block.setTypeId(undoblock.id);
		block.setData(undoblock.dat);
		if (refund){
			if (isSpace(task.player,1))
				InventoryWorkaround.addItem(task.player.getInventory(), false, item);
			else{
				if (task.counter==0){
					task.player.sendMessage("§4You need a free slot in your inventory for refunds!");
				}
			}
		}
		if (task.counter==task.count-1){
			task.player.sendMessage("§2Completed building, changed §b"+task.count+"§2 blocks!");
			Undos.remove(tid);
			Util.undos.remove(task.player);
			return;
		}
		task.counter++;		
	}
	
	
	
	
	public static boolean isSpace(Player player, int amount){
			ItemStack[] stacks;
			stacks=player.getInventory().getContents();
			int space=0,stackCount=0;
			for (int ctr = 0;ctr<36;ctr++){
				if (stacks[ctr]==null)
					space=space+1;
			}
			if (amount>64){
				BigDecimal stacky=new BigDecimal(amount/64).setScale(0,BigDecimal.ROUND_UP);
				stackCount=Integer.parseInt(stacky.toString());
			}
			else
				stackCount=1;
			if (stackCount<=space)
				return true;
		return false;
	}
	
	
	
	
	public static void execute(Task task, int tid){
		if (!task.player.isOnline()){
			Tasks.remove(tid);
		}	
		if (task.delete && task.locs[task.counter].getBlock().isEmpty()){
			if (task.counter==task.count-1){
			task.player.sendMessage("§2Completed building, changed §b"+task.count+"§2 blocks!");
			Tasks.remove(tid);
			return;
		}
		task.counter++;	
		}
		ItemStack item=new ItemStack(task.id,1,task.dat.getData());
		if (!InventoryWorkaround.containsItem(task.player.getInventory(),false,item) && !task.delete && !task.admin){
			task.player.sendMessage("§4Stopped building, no longer have enough items!");
			Tasks.remove(tid);
			return;
		}
		
		Block block=task.world.getBlockAt(task.locs[task.counter]);
		if (!block.getChunk().isLoaded()){
			task.player.sendMessage("§4Stopped building, must be in area!");
			Tasks.remove(tid);
			return;
		}
		if (!block.isEmpty()){
		BlockBreakEvent breake=new BlockBreakEvent(block,task.player);
		SlowSet.getStatic().getServer().getPluginManager().callEvent(breake);
		if (breake.isCancelled()){
			task.player.sendMessage("§4Stopped building, protected area!");
			Tasks.remove(tid);
			return;
		}
		}
		if (!task.delete){
			BlockPlaceEvent blockpe=new BlockPlaceEvent(block,block.getState(),block,item,task.player,true);
			SlowSet.getStatic().getServer().getPluginManager().callEvent(blockpe);
			if (blockpe.isCancelled()){
			task.player.sendMessage("§4Stopped building, protected area!");
			Tasks.remove(tid);
			return;
		}
		}	
		Undo undo;
		if (task.counter==0){
			if(Util.undos.containsKey(task.player)){
				Util.undos.remove(task.player);
			}
			undo=new Undo();
			undo.count=0;
		}
		else{
			undo=(Undo) Util.undos.get(task.player);
		}
		if (task.delete){
			UndoBlock undoblock=new UndoBlock();
			undoblock.dat=block.getData();
		    undoblock.id=block.getTypeId();
			undo.undoblocks.add(undoblock);
			undo.blocks.add(block);
			undo.rems.add("NULL");
			undo.count++;
			if(Util.undos.containsKey(task.player)){
				Util.undos.remove(task.player);
			}
			Util.undos.put(task.player, undo);
			block.setTypeId(0);
		}
		else{
		UndoBlock undoblock=new UndoBlock();
		undoblock.dat=block.getData();
		undoblock.id=block.getTypeId();
		undo.undoblocks.add(undoblock);
		undo.blocks.add(block);
			undo.count++;
			
			
		block.setTypeId(task.id);
		block.setData(task.dat.getData());
		if (!task.admin)
		InventoryWorkaround.removeItem(task.player.getInventory(), false, item);
		undo.rems.add(item);
		}
		if(Util.undos.containsKey(task.player)){
				Util.undos.remove(task.player);
			}
		Util.undos.put(task.player, undo);
		if (task.counter==task.count-1){
			task.player.sendMessage("§2Completed building, changed §b"+task.count+"§2 blocks!");
			Tasks.remove(tid);
			return;
		}
		task.counter++;		
	}
	public static void run(){
		if (Tasks.isEmpty()){
		}
		else{
		int ctr=Tasks.size();
		Task task;
		for (int ctr2=0;ctr2<ctr;ctr2++){
			task=(Task) Tasks.get(ctr2);
			execute(task,ctr2);
		}
		}
		if (Undos.isEmpty()){
			return;
		}
		int ctr3=Undos.size();
		UndoTask utask;
		for (int ctr4=0;ctr4<ctr3;ctr4++){
			utask=(UndoTask) Undos.get(ctr4);
			executeUndo(utask,ctr4);
		}
	}
}
