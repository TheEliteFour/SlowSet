package net.aesircraft.SlowSet;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SlowSet extends JavaPlugin
{
	private static SlowSet instance = null;
	private static WorldEditPlugin weinstance = null;
	public static final Logger logger = Logger.getLogger("Minecraft");
	public PermissionHandler permissionHandler;
	public static SlowSet getStatic()
    
			
	{
		return instance;
	}
	public static WorldEditPlugin getWE()
    
			
	{
		return weinstance;
	}
	private void setStatic()
	{
		instance = this;
	}	
	private void setupPermissions()
	{
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

		if (this.permissionHandler == null)
		{
			if (permissionsPlugin != null)
			{
				this.permissionHandler = ((Permissions)permissionsPlugin).getHandler();
			}
			else
			{
				logger.log(Level.SEVERE, "AesirRegistrar - Permission system not detected!");
			}
		}
	}

	@Override
	public void onDisable()
	{
		logger.info("AesirRegistrar is INACTIVE!");
	}

	@Override
	public void onEnable()
	{
		setStatic();
		setupPermissions();
		PluginManager pm = getServer().getPluginManager();
		weinstance=(WorldEditPlugin) pm.getPlugin("WorldEdit");
		getCommand("xset").setExecutor(new xset(this));
		getCommand("xundo").setExecutor(new xundo(this));
		getCommand("aset").setExecutor(new aset(this));
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			 public void run() {
		     Scheduler.run();
		 }
		}, 0L, 5L);
		logger.info("[SlowSet] Loaded " + this.getDescription().getName() + " build " + this.getDescription().getVersion() + "!");
	}
	
}
