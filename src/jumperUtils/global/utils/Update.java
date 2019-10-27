package jumperutils.global.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.rylinaux.plugman.util.PluginUtil;

import jline.internal.Nullable;
import jumperutils.JumperUtils;

public class Update {
	public static final String STARTED_MSG = "is updating...";
	public static final String FAILED_MSG = "could not be updated.";
	public static final String SUCCESS_MSG = "was updated successfully.";
	public static final String RELOAD_MSG = "is reloading.";
	private static String pluginName;
	
	static {
		pluginName = JumperUtils.class.getSimpleName();
	}
	
	public static void updatePlugin(@Nullable final Player p) {
		boolean usePlugMan = (JumperUtils.getPluginManager().getPlugin("PlugMan") != null && JumperUtils.getPluginManager().isPluginEnabled(JumperUtils.getPluginManager().getPlugin("PlugMan")));
		new BukkitRunnable() {
			@Override
			public void run() {
				updatePlugin(p, usePlugMan);
			}
		}.runTaskAsynchronously(JumperUtils.getInstance());
	}
	
	//reload will be forced for the time being
	private static void updatePlugin(@Nullable final Player p, boolean usePlugMan) {
		messageRecipient(p, pluginName, STARTED_MSG, true);
		
		try {
			File file = new File(JumperUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			writeToFile(file, new BufferedInputStream(new URL("https://github.com/BlazingTwist/JumperUtils-1.14.4/releases/latest/download/JumperUtils.jar").openStream()));
			messageRecipient(p, pluginName, SUCCESS_MSG, true);
			
			//TODO add autoreload property (low priority)
			doReload(p, usePlugMan);
		}catch(URISyntaxException e) {
			e.printStackTrace();
			messageRecipient(p, pluginName, FAILED_MSG, false);
			return;
		}catch(IOException e) {
			e.printStackTrace();
			messageRecipient(p, pluginName, FAILED_MSG, false);
			return;
		}
	}
	
	public static void messageRecipient(@Nullable final Player p, String part1, String part2, boolean isPositive) {
		if(p != null) {
			String colorPrefix = isPositive ? " §a" : " §c";
			p.sendMessage("§b" + part1 + colorPrefix + part2);
		}else {
			System.out.println(part1 + " " + part2);
		}
	}
	
	public synchronized static void writeToFile(final File file, final BufferedInputStream inputStream) throws IOException {
		try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			if(!file.exists()) {
				Files.copy(inputStream, file.toPath());
				return;
			}else {
				final byte[] data = new byte[8192];
				for(int count; (count = inputStream.read(data, 0, 8192)) != -1; ) {
					outputStream.write(data, 0, count);
				}
			}
		}
	}
	
	public static void doReload(@Nullable final Player p, boolean usePlugMan) {
		if(usePlugMan) {
			messageRecipient(p, pluginName, RELOAD_MSG, true);
			PluginUtil.reload(JumperUtils.getInstance());
		}else {
			new BukkitRunnable() {
				@Override
				public void run() {
					messageRecipient(p, "Server", RELOAD_MSG, true);
					Bukkit.getServer().reload();
				}
			}.runTask(JumperUtils.getInstance());
		}
	}
}
