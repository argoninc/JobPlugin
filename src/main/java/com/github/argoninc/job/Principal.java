package com.github.argoninc.job;

import com.github.argoninc.job.sign.Sign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.argoninc.job.command.SetJobCommand;
import com.github.argoninc.job.enchant.Enchant;
import com.github.argoninc.job.listener.JobConfig;
import com.github.argoninc.job.listener.PlayerChatListener;
import com.github.argoninc.job.listener.PlayerInteractListener;
import com.github.argoninc.job.listener.PlayerJoinListener;
import com.github.argoninc.job.user.Job;
import com.github.argoninc.job.user.UserJob;

public class Principal extends JavaPlugin{
	@Override
	public void onEnable() {
		Job.init();
		UserJob.init();
		JobConfig.init();
		Enchant.init();
		Sign.init();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerJoinListener.setupNewUser(p);
		}
		
		this.getCommand("setjob").setExecutor(new SetJobCommand());
		
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
