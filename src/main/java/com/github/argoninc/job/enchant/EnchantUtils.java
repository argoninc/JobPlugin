package com.github.argoninc.job.enchant;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class EnchantUtils {
	public static Enchant[] getPossibleEnchanments(ItemStack i) {
		ArrayList<Enchant> arr = new ArrayList<>();
		
		Enchant[] all = Enchant.getEnchants();
		
		if(i.getType().equals(Material.ENCHANTED_BOOK)) {
			return all;
		}
		
		for (Enchant enchant : all) {
			if(enchant.getEnchantment().canEnchantItem(i) && !enchant.getEnchantment().isTreasure() && !i.containsEnchantment(enchant.getEnchantment())) {
				arr.add(enchant);
			}
		}
		
		if(arr.size()>0) {
			return arr.toArray(new Enchant[0]);
		}
		return null;
	}
	
	public static void enchant(ItemStack i) {
		int numberEnchants = rand(1,3);
		
		for (int j = 0; j < numberEnchants; j++) {
			Enchant[] possible = getPossibleEnchanments(i);
			
			Enchantment[] listEnchants = weightArray(possible);
			
			if(listEnchants.length==0) {
				System.out.println("cabou");
				break;
			}
			
			int nEnc = rand(0,(listEnchants.length-1));
			
			Enchantment enchant = listEnchants[nEnc];
			
			//System.out.println("enchant: "+enchant.getKey());
			int level = 1;
			if(enchant.getMaxLevel()>1) {
				level = rand(1, enchant.getMaxLevel());
			}
			//System.out.println("level: "+level);
			if(i.getType().equals(Material.ENCHANTED_BOOK)) {
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta)i.getItemMeta();
				meta.addStoredEnchant(enchant, level, false);
				i.setItemMeta(meta);
			}else {
				i.addEnchantment(enchant, level);
			}
		}
	}
	
	private static Enchantment[] weightArray(Enchant[] e) {
		ArrayList<Enchantment> list = new ArrayList<>();
		
		if(e !=null) {
			for (Enchant enc : e) {
				for (int i = 0; i < enc.getWeight(); i++) {
					list.add(enc.getEnchantment());
				}
			}
		}
		return list.toArray(new Enchantment[0]);
	}
	
	private static int rand(int min, int max)
	{
		double rand = Math.random();
		return (int)(rand * ((max - min) + 1)) + min;
	}
}
