package com.github.argoninc.job.enchant;

import java.util.ArrayList;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Enchant {
	private static Enchant[] enchants = null;

	private Enchantment enchantment;
	private int weight;

	private Enchant(Enchantment enchantment, int weight) {
		this.enchantment = enchantment;
		this.weight = weight;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getWeight() {
		return weight;
	}

	public static Enchant[] getEnchants() {
		return enchants;
	}

	public static void init() {
		ArrayList<Enchant> ench = new ArrayList<Enchant>();

		ench.add(new Enchant(getEnchantmentByKey("aqua_affinity"), 2));
		ench.add(new Enchant(getEnchantmentByKey("bane_of_arthropods"), 5));
		ench.add(new Enchant(getEnchantmentByKey("blast_protection"), 2));
		ench.add(new Enchant(getEnchantmentByKey("channeling"), 1));
		ench.add(new Enchant(getEnchantmentByKey("binding_curse"), 1));
		ench.add(new Enchant(getEnchantmentByKey("vanishing_curse"), 1));
		ench.add(new Enchant(getEnchantmentByKey("depth_strider"), 2));
		ench.add(new Enchant(getEnchantmentByKey("efficiency"), 10));
		ench.add(new Enchant(getEnchantmentByKey("feather_falling"), 5));
		ench.add(new Enchant(getEnchantmentByKey("fire_aspect"), 2));
		ench.add(new Enchant(getEnchantmentByKey("fire_protection"), 5));
		ench.add(new Enchant(getEnchantmentByKey("flame"), 2));
		ench.add(new Enchant(getEnchantmentByKey("fortune"), 2));
		ench.add(new Enchant(getEnchantmentByKey("frost_walker"), 2));
		ench.add(new Enchant(getEnchantmentByKey("impaling"), 2));
		ench.add(new Enchant(getEnchantmentByKey("infinity"), 1));
		ench.add(new Enchant(getEnchantmentByKey("knockback"), 5));
		ench.add(new Enchant(getEnchantmentByKey("looting"), 2));
		ench.add(new Enchant(getEnchantmentByKey("loyalty"), 5));
		ench.add(new Enchant(getEnchantmentByKey("luck_of_the_sea"), 2));
		ench.add(new Enchant(getEnchantmentByKey("lure"), 2));
		ench.add(new Enchant(getEnchantmentByKey("mending"), 2));
		ench.add(new Enchant(getEnchantmentByKey("multishot"), 2));
		ench.add(new Enchant(getEnchantmentByKey("power"), 10));
		ench.add(new Enchant(getEnchantmentByKey("projectile_protection"), 5));
		ench.add(new Enchant(getEnchantmentByKey("protection"), 10));
		ench.add(new Enchant(getEnchantmentByKey("quick_charge"), 5));
		ench.add(new Enchant(getEnchantmentByKey("respiration"), 2));
		ench.add(new Enchant(getEnchantmentByKey("sharpness"), 10));
		ench.add(new Enchant(getEnchantmentByKey("silk_touch"), 1));
		ench.add(new Enchant(getEnchantmentByKey("smite"), 5));
		ench.add(new Enchant(getEnchantmentByKey("sweeping"), 2));
		ench.add(new Enchant(getEnchantmentByKey("thorns"), 1));
		ench.add(new Enchant(getEnchantmentByKey("unbreaking"), 5));
		ench.add(new Enchant(getEnchantmentByKey("piercing"), 6));
		ench.add(new Enchant(getEnchantmentByKey("punch"), 7));
		ench.add(new Enchant(getEnchantmentByKey("riptide"), 8));

		enchants = ench.toArray(new Enchant[0]);
	}

	private static Enchantment getEnchantmentByKey(String key) {
		return Enchantment.getByKey(NamespacedKey.minecraft(key));
	}
}
