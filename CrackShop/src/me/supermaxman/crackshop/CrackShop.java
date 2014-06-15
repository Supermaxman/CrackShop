package me.supermaxman.crackshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



public class CrackShop extends JavaPlugin {
	public static CrackShop plugin;
	public static FileConfiguration conf;
	public static final Logger log = Logger.getLogger("Minecraft");
	public static Economy economy = null;

	static LinkedHashMap<String, Shop> signs = new LinkedHashMap<String, Shop>();

	public void onEnable() {
		plugin = this;
		if (!setupEconomy() ) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		saveDefaultConfig();
		conf = plugin.getConfig();
		loadFiles();
		getServer().getPluginManager().registerEvents(new CrackShopListener(), plugin);
		log.info(getName() + " has been enabled.");

	}

	public void onDisable() {
		saveFiles();

		log.info(getName() + " has been disabled.");
	}

	void saveFiles() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDataFolder() + File.separator + "signs.ser"));
			oos.writeObject(signs);
			oos.close();
		} catch (Exception e) {
			log.warning("[" + getName() + "] Files could not be saved!");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	void loadFiles() {
		try {
			signs = (LinkedHashMap<String, Shop>) new ObjectInputStream(new FileInputStream(getDataFolder() + File.separator + "signs.ser")).readObject();
			new ObjectInputStream(new FileInputStream(getDataFolder() + File.separator + "signs.ser")).close();
		} catch (Exception e) {
			log.warning("[" + getName() + "] Files could not be read! All files are now ignored.");
		}

	}

	public static String makeString(Location loc) {
		return loc.getWorld().getName() + "&&" + loc.getBlockX() + "&&" + loc.getBlockY() + "&&" + loc.getBlockZ(); 
	}

	public static Location makeLocation(String s) {
		String[] loc = s.split("&&");

		return new Location(CrackShop.plugin.getServer().getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])); 
	}

	private boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
}