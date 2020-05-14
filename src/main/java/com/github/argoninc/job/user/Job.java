package com.github.argoninc.job.user;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Villager.Profession;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.rillis.dao.DB;

public class Job{
	private String key;
	private String name;
	private char color;
	private Profession p;
	
	public static Job[] jobs = null;
	private static DB jobsDB = null;
	
	public static void init() {
		jobsDB = new DB("argoninc/jobs/jobs.json");
		if(!jobsDB.has("jobs")) {
			jobsDB.set("jobs", new JSONArray());
		}
		
		ArrayList<Job> arr = new ArrayList<Job>();
		
		JSONArray jsonArray = (JSONArray) jobsDB.get("jobs");
		
		Iterator<Object> i = jsonArray.iterator();
		
		while(i.hasNext()) {
			JSONObject json = (JSONObject) i.next();
			
			arr.add(new Job(json.getString("key"), json.getString("name"), json.getString("color").charAt(0)));
		}
		
		jobs = arr.toArray(new Job[0]);
	}
	
	public static Job getJobFromKey(String key) {
		for (Job job : jobs) {
			if(job.getKey().equalsIgnoreCase(key)) {
				return job;
			}
		}
		return null;
	}
	
	public static Job getJobFromName(String name) {
		for (Job job : jobs) {
			if(job.getName().equalsIgnoreCase(name)) {
				return job;
			}
		}
		return null;
	}
	
	public Job(String key, String name, char color) {
		this.key = key;
		this.name = name;
		this.color = color;
		
		try {
			this.p = Profession.valueOf(key);
		}catch(Exception e) {
			this.p = null;
		}
		
	}
	
	public String getTag() {
		return getColor()+"["+getName()+"]";
	}

	public Profession getProfession() {
		return p;
	}
	
	public String getKey() {
		return key;
	}
	
	public ChatColor getColor() {
		return ChatColor.getByChar(color);
	}
	
	public char getColorChar() {
		return color;
	}
	
	public String getColorString() {
		return Character.toString(color);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Job [key=" + key + ", name=" + name + ", color=" + color + ", p=" + p.toString() + "]";
	}
	
	
}
