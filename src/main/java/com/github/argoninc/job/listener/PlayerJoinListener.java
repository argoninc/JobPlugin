package com.github.argoninc.job.listener;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.argoninc.job.user.UserJob;

public class PlayerJoinListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		setupNewUser(e.getPlayer());
	}
	
	public static void setupNewUser(Player p) {

		Player player = p;
		String uuid = player.getUniqueId().toString();

		JSONArray jsonArray = (JSONArray) UserJob.userDB.get("users");
		Iterator<Object> i = jsonArray.iterator();
		
		boolean tem = false;
		
		while(i.hasNext()) {
			JSONObject json = (JSONObject) i.next();
			
			if(json.getString("uuid").equals(uuid)) {
				tem=true;
			}
		}
		
		if(!tem) {
			UserJob u = new UserJob(player.getDisplayName(), uuid, null, 0);
			jsonArray.put(u.toJSON());
			UserJob.userDB.set("users", jsonArray);
			UserJob.refreshFromDB();
		}
	}
}
