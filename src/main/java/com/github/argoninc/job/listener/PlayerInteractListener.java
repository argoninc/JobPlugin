package com.github.argoninc.job.listener;

import java.util.Iterator;

import com.github.argoninc.job.sign.Sign;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.argoninc.job.enchant.EnchantUtils;
import com.github.argoninc.job.user.Job;
import com.github.argoninc.job.user.UserJob;

import net.md_5.bungee.api.ChatColor;

import javax.jws.soap.SOAPBinding;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {

        if (CheckIllegal.checkGrief(e)) {
            e.setUseInteractedBlock(Result.DENY);
            e.setCancelled(true);
        }

        JobConfig[] use = JobConfig.getByType("use");

        for (JobConfig j : use) {
            if (CheckIllegal.checkForIllegalUse(j.getMaterials(), j.getJobs(), e, j.getActions())) {
                e.setCancelled(true);
            }
        }

        JobConfig[] useBlock = (JobConfig[]) ArrayUtils.addAll(JobConfig.getByType("useBlock"),
                JobConfig.getByType("disabled"));

        for (JobConfig j : useBlock) {
            if (CheckIllegal.checkForIllegalUseBlock(j.getMaterials(), j.getJobs(), e, j.getActions())
                    || CheckIllegal.checkForIllegalUse(j.getMaterials(), j.getJobs(), e, j.getActions())) {
                e.setCancelled(true);
            }
        }

        if (e.getClickedBlock() != null) {
            Player player = e.getPlayer();
            Block b = e.getClickedBlock();
            if (b.getType().equals(Material.ENCHANTING_TABLE)
                    && player.getInventory().getItemInMainHand() != null) {
                enchantItem(e);
            } else if (b.getType().equals(Material.OAK_WALL_SIGN)) {
                Location l = b.getLocation();

                Sign s = Sign.getSign(l);
                if (s != null) {
                    String uuid = player.getUniqueId().toString();

                    UserJob user = UserJob.getUser(uuid);
                    if (!user.hasJob()) {
                        user.giveJob(s.getJob());
                        player.sendMessage(ChatColor.GREEN + "Você agora é um " + s.getJob().getTag());
                    } else if (!user.isInCooldown()) {
                        user.giveJob(s.getJob());
                        player.sendMessage(ChatColor.GREEN + "Você agora é um " + s.getJob().getTag());
                    } else {
                        player.sendMessage(ChatColor.RED + "Você deve esperar mais " + user.getCooldownText() + " para isso.");
                    }

                } else if (player.isOp() && Sign.SHOW_SIGN_LOCATIONS) {
                    player.sendMessage(ChatColor.GREEN + l.toString());
                }
            }else if (b.getType().equals(Material.OBSIDIAN)){
                ItemStack main = player.getInventory().getItemInMainHand();
                ItemStack off = player.getInventory().getItemInOffHand();

                boolean tentou = false;
                if(main!=null){
                    if(main.getType().equals(Material.FLINT_AND_STEEL)){
                        tentou = true;
                    }
                }
                if(off!=null){
                    if(off.getType().equals(Material.FLINT_AND_STEEL)){
                        tentou = true;
                    }
                }
                String uuid = player.getUniqueId().toString();
                if(tentou){
                    UserJob user = UserJob.getUser(uuid);
                    Job mason = Job.getJobFromKey("MASON");
                    if (user.hasJob()) {
                        if(user.getJob().getKey().equals(mason.getKey())){
                            int preco = (int) JobConfig.jobConfigDB.get("portalPrice");

                            if(taxar(player, preco)){
                                player.sendMessage(ChatColor.GREEN + "Portal criado.");
                            }else{
                                player.sendMessage(ChatColor.RED + "Voce não tem a quantidade de esmeraldas necessária. (" + preco + ").");
                                e.setCancelled(true);
                            }
                        }else{
                            e.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "Você deve ser " + mason.getTag() + ChatColor.RED+ " para isso.");
                        }
                    }else{
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Você deve ser " + mason.getTag() + ChatColor.RED+ " para isso.");
                    }
                }
            }
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        JobConfig[] breakvar = JobConfig.getByType("break");

        for (JobConfig j : breakvar) {
            if (e.getPlayer() != null) {
                if (CheckIllegal.checkForIllegalBreak(j.getMaterials(), j.getJobs(), e)) {
                    e.setDropItems(false);
                }
            }
        }

        JobConfig[] breakItem = JobConfig.getByType("breakItem");

        for (JobConfig j : breakItem) {
            if (e.getPlayer() != null) {
                if (CheckIllegal.checkForIllegalBreakItem(j.getMaterials(), j.getJobs(), e)) {
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        JobConfig[] craft = (JobConfig[]) ArrayUtils.addAll(JobConfig.getByType("craft"),
                JobConfig.getByType("disabled"));

        for (JobConfig j : craft) {
            if (CheckIllegal.checkForIllegalCraft(j.getMaterials(), j.getJobs(), e)) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        JobConfig[] smelt = JobConfig.getByType("smelt");

        for (JobConfig j : smelt) {
            if (CheckIllegal.checkIllegalSmelt(j.getMaterials(), j.getJobs(), e)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onXPChange(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        UserJob u = UserJob.getUser(p.getUniqueId().toString());
        Job librarian = Job.getJobFromKey("LIBRARIAN");

        if (u.hasJob()) {
            if (u.getJob().getKey().equals(librarian.getKey())) {
                p.setExp(0);
                p.setLevel(100);
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getType().equals(InventoryType.ANVIL)) {
            Player p = (Player) e.getPlayer();
            UserJob u = UserJob.getUser(p.getUniqueId().toString());
            Job librarian = Job.getJobFromKey("LIBRARIAN");

            if (u.hasJob()) {
                if (u.getJob().getKey().equals(librarian.getKey())) {
                    p.setExp(0);
                    p.setLevel(100);
                }
            }
        }
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent e) {
        System.out.println(e.toString());
    }

    /**
     * Metodos de apoio
     */

    private String enchantItem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        UserJob u = UserJob.getUser(uuid);
        e.setCancelled(true);

        Job librarian = Job.getJobFromKey("LIBRARIAN");
        if (!u.hasJob()) {
            player.sendMessage(
                    ChatColor.RED + "Você precisa ser " + librarian.getTag() + ChatColor.RED + " para encantar itens.");
            return null;
        }
        if (!u.getJob().getKey().equals(librarian.getKey())) {
            player.sendMessage(
                    ChatColor.RED + "Você precisa ser " + librarian.getTag() + ChatColor.RED + " para encantar itens.");
            return null;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getEnchantments().size() > 0 || item.getType().equals(Material.ENCHANTED_BOOK)) {
            player.sendMessage(ChatColor.RED + "Esse item já está encantado.");
            return null;
        }

        if (EnchantUtils.getPossibleEnchanments(item) == null && !item.getType().equals(Material.BOOK)) {
            player.sendMessage(ChatColor.RED + "Esse não tem encantamentos disponíveis.");
            return null;
        }

        int price = (int) JobConfig.jobConfigDB.get("enchantPrice");
        if(!taxar(player, price)){
            player.sendMessage(ChatColor.RED + "Voce não tem a quantidade de esmeraldas necessária. (" + price + ").");
            return null;
        }

        if (item.getType().equals(Material.BOOK)) {
            PlayerInventory inv = player.getInventory();
            if (inv.firstEmpty() == -1 && item.getAmount() > 1) {
                player.sendMessage(ChatColor.RED + "Voce não tem espaço para isso.");
                return null;
            }

            item.setAmount(item.getAmount() - 1);

            ItemStack i = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantUtils.enchant(i);

            inv.addItem(i);

            player.updateInventory();
        } else {
            EnchantUtils.enchant(item);
        }
        player.sendMessage(ChatColor.GREEN + "Item encantado!");

        return null;
    }

    public boolean taxar(Player player, int value){
        if (count(player, Material.EMERALD) < value) {
            return false;
        }
        remove(player, Material.EMERALD, value);
        return true;
    }

    private void remove(Player player, Material m, int quantity) {
        PlayerInventory inv = player.getInventory();

        Iterator<Integer> i = inv.all(Material.EMERALD).keySet().iterator();

        while (i.hasNext()) {
            if (quantity == 0) {
                break;
            }
            ItemStack item = inv.getItem(i.next());

            int q = item.getAmount();

            if (quantity > q) {
                item.setAmount(0);
                quantity -= q;
            } else if (quantity == q) {
                item.setAmount(0);
                quantity = 0;
            } else {
                item.setAmount(item.getAmount() - quantity);
                quantity = 0;
            }
        }
    }

    private int count(Player player, Material m) {
        ItemStack[] contents = player.getInventory().getContents();

        int i = 0;
        for (ItemStack itemStack : contents) {
            if (itemStack != null) {
                if (itemStack.getType().equals(m)) {
                    i += itemStack.getAmount();
                }
            }

        }
        return i;
    }
}
