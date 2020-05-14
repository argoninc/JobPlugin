package com.github.argoninc.job.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.argoninc.job.user.Job;
import com.github.argoninc.job.user.UserJob;

import net.md_5.bungee.api.ChatColor;

public class CheckIllegal {
	public static boolean checkIllegalSmelt(Material[] ms, Job[] jobs, InventoryClickEvent e) {
		if (e.getInventory().getType().equals(InventoryType.FURNACE)) {
			Material clicked = e.getCurrentItem().getType();
			Player player = (Player) e.getWhoClicked();
			UserJob u = UserJob.getUser(player.getUniqueId().toString());
			if (!isJobIncluded(u, jobs) && isMaterialIncluded(clicked, ms)) {
				player.sendMessage(ChatColor.RED + "O item " + clicked.toString() + " só pode ser queimado por " + getJobString(jobs));
				return true;
			}
		}
		return false;
	}

	public static boolean checkGrief(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) {
			Block block = e.getClickedBlock();
			if (block == null)
				return false;
			if (block.getType() == Material.FARMLAND) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkForIllegalUseBlock(Material[] ms, Job[] jobs, PlayerInteractEvent e, Action[] actions) {
		Player player = e.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return false;
		}
		String uuid = player.getUniqueId().toString();
		UserJob user = UserJob.getUser(uuid);
		if (e.getClickedBlock() == null) {
			return false;
		}

		Material m = e.getClickedBlock().getType();
		
		if(checkDisabled(ms, jobs, player, m)) {
			return true;
		}
		
		if (!isJobIncluded(user, jobs) && isMaterialIncluded(m, ms) && isActionIncluded(e.getAction(), actions)) {
			player.sendMessage(
					ChatColor.RED + "O bloco " + m.toString() + " só pode ser usado por " + getJobString(jobs));
			return true;
		}
		return false;
	}

	public static boolean checkDisabled(Material[] ms, Job[] jobs, Player player, Material m) {
		if(jobs==null) {
			for (Material material : ms) {
				if (material.equals(m)) {
					player.sendMessage(ChatColor.RED + "O item " + m.toString() + " foi desabilitado no servidor.");
					
					player.getInventory().remove(m);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkForIllegalUse(Material[] ms, Job[] jobs, PlayerInteractEvent e, Action[] actions) {
		Player player = e.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return false;
		}
		String uuid = player.getUniqueId().toString();
		UserJob user = UserJob.getUser(uuid);
		Material mainHand = player.getInventory().getItemInMainHand().getType();
		Material offHand = player.getInventory().getItemInOffHand().getType();
		
		if(checkDisabled(ms, jobs, player, mainHand)) {
			return true;
		}
		if(checkDisabled(ms, jobs, player, offHand)) {
			return true;
		}
		
		if (!isJobIncluded(user, jobs) && isMaterialIncluded(mainHand, ms)
				&& isActionIncluded(e.getAction(), actions)) {
			player.sendMessage(
					ChatColor.RED + "O item " + mainHand.toString() + " só pode ser usado por " + getJobString(jobs));
			return true;
		}
		if (!isJobIncluded(user, jobs) && isMaterialIncluded(offHand, ms) && isActionIncluded(e.getAction(), actions)) {
			player.sendMessage(
					ChatColor.RED + "O item " + offHand.toString() + " só pode ser usado por " + getJobString(jobs));
			return true;
		}
		return false;
	}

	public static boolean checkForIllegalCraft(Material[] ms, Job[] jobs, CraftItemEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return false;
		}
		String uuid = player.getUniqueId().toString();
		UserJob user = UserJob.getUser(uuid);
		Material m = e.getRecipe().getResult().getType();
		
		if(checkDisabled(ms, jobs, player, m)) {
			return true;
		}
		
		if (!isJobIncluded(user, jobs) && isMaterialIncluded(m, ms)) {
			player.sendMessage(ChatColor.RED + "O item " + m.toString() + " só pode ser criado por " + getJobString(jobs));
			return true;
		}
		return false;
	}

	public static boolean checkForIllegalBreakItem(Material[] ms, Job[] jobs, BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return false;
		}
		String uuid = player.getUniqueId().toString();
		UserJob user = UserJob.getUser(uuid);
		Material mainHand = player.getInventory().getItemInMainHand().getType();
		Material offHand = player.getInventory().getItemInOffHand().getType();

		if (!isJobIncluded(user, jobs) && isMaterialIncluded(mainHand, ms)) {
			player.sendMessage(
					ChatColor.RED + "O item " + mainHand.toString() + " só pode ser usado por " + getJobString(jobs));
			return true;
		}
		if (!isJobIncluded(user, jobs) && isMaterialIncluded(offHand, ms)) {
			player.sendMessage(
					ChatColor.RED + "O item " + offHand.toString() + " só pode ser usado por " + getJobString(jobs));
			return true;
		}
		return false;
	}

	public static boolean checkForIllegalBreak(Material[] ms, Job[] jobs, BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			return false;
		}
		String uuid = player.getUniqueId().toString();
		UserJob user = UserJob.getUser(uuid);
		Block b = e.getBlock();
		Material material = b.getType();

		if (!isJobIncluded(user, jobs) && isMaterialIncluded(material, ms)) {
			player.sendMessage(ChatColor.RED + "O bloco " + material.toString() + " só pode ser COLETADO por "
					+ getJobString(jobs));
			return true;
		}
		return false;

	}
	
	private static String getJobString(Job[] jobs) {
		String base = "";
		String msg = base;

		for (Job job : jobs) {
			if (!msg.equals(base)) {
				msg += ", ";
			}

			msg += job.getColor() + "[" + job.getName() + "]";
		}

		msg += ChatColor.RED + ".";

		return msg;
	}

	private static boolean isActionIncluded(Action m, Action[] ms) {
		if (ms == null) {
			return true;
		}

		for (Action material : ms) {
			if (material.equals(m)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isMaterialIncluded(Material m, Material[] ms) {
		for (Material material : ms) {
			if (material.equals(m)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isJobIncluded(UserJob user, Job[] jobs) {
		if(jobs==null) {
			return true;
		}
		if (!user.hasJob()) {
			return false;
		}
		for (Job j : jobs) {
			if (j.getKey().equals(user.getJob().getKey())) {
				return true;
			}
		}

		return false;
	}
}
