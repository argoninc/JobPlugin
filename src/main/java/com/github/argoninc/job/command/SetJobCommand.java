package com.github.argoninc.job.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.argoninc.job.user.Job;
import com.github.argoninc.job.user.UserJob;

public class SetJobCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName();
		
		if(cmd.equals("setjob")) {
			if(args.length!=2) {
				sender.sendMessage(ChatColor.RED+"Uso incorreto: /setjob <player> <key>");
				return true;
			}
			
			if(sender instanceof Player) {
				if(!((Player) sender).isOp()) {
					sender.sendMessage(ChatColor.RED+"Voce nao tem permissao para isso.");
					return true;
				}
			}
			
			String name = args[0];
			String key = args[1];
			
			Player player = null;
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if(p.getDisplayName().equalsIgnoreCase(name)) {
					player = p;
				}
			}
			
			if(player==null) {
				sender.sendMessage(ChatColor.RED+"Player nao encontrado.");
				return true;
			}
			
			String uuid = player.getUniqueId().toString();
			
			if(args[1].equalsIgnoreCase("desempregado") || args[1].equalsIgnoreCase("0")) {
				UserJob.getUser(uuid).removeJob();
				sender.sendMessage(ChatColor.GREEN+name+" perdeu o emprego");
				return true;
			}
			
			Job job = null;
			
			for (Job j : Job.jobs) {
				if(j.getKey().equalsIgnoreCase(key) || j.getName().equalsIgnoreCase(key)) {
					job = j;
				}
			}
			
			if(job==null) {
				sender.sendMessage(ChatColor.RED+"Job nao encontrado.");
				return true;
			}
			
			UserJob.getUser(uuid).setJob(job);
			sender.sendMessage(ChatColor.GREEN+name+" virou "+job.getColor()+"["+job.getName()+"]");
			return true;
		}
		return false;
	}

}
