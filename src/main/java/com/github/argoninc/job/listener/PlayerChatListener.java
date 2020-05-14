package com.github.argoninc.job.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.argoninc.job.user.UserJob;

import net.md_5.bungee.api.ChatColor;

public class PlayerChatListener implements Listener{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		String tag = ChatColor.DARK_GRAY+"[Desempregado] "+ChatColor.GRAY+player.getDisplayName()+": ";
		
		UserJob u = UserJob.getUser(uuid);
		if(u.hasJob()) {
			tag = u.getJob().getColor()+"["+u.getJob().getName()+"] "+ChatColor.GRAY+player.getDisplayName()+": ";
		}
		
		e.setFormat(e.getFormat().replace("<%1$s> ", tag));
		e.setMessage(ChatColor.WHITE+beautify(e.getMessage()));
	}
	
	private String beautify(String str) {
		str = str.trim();
		if(str.substring(str.length() - 1).matches("[a-zA-Z0-9]*")) {
			str+=".";
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
