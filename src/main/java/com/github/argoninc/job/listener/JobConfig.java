package com.github.argoninc.job.listener;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.argoninc.job.user.Job;
import com.github.rillis.dao.DB;

public class JobConfig {
	public static DB jobConfigDB = null;
	
	public static void init() {
		jobConfigDB = new DB("argoninc/jobs_config.json");
		
		if(!jobConfigDB.has("list")) {
			jobConfigDB.set("list", new JSONArray());
		}
		
		if(!jobConfigDB.has("enchantPrice")) {
			jobConfigDB.set("enchantPrice", 1);
		}
		
		if(!jobConfigDB.has("jobCooldownSec")) {
			jobConfigDB.set("jobCooldownSec", 86400);
		}
	}
	
	private String type;
	private Material[] materials;
	private Job[] jobs;
	private Action[] actions;

	private JobConfig(String type, Material[] materials, Job[] jobs, Action[] actions) {
		this.type = type;
		this.materials = materials;
		this.jobs = jobs;
		this.actions = actions;
	}
	
	public static JobConfig[] getByType(String type) {
		JobConfig[] base = getJobConfigs();
		
		ArrayList<JobConfig> jobs = new ArrayList<JobConfig>();
		
		for (JobConfig jobConfig : base) {
			if(jobConfig.getType().equalsIgnoreCase(type)) {
				jobs.add(jobConfig);
			}
		}
		
		return jobs.toArray(new JobConfig[0]);
	}

	public String getType() {
		return type;
	}

	public Material[] getMaterials() {
		return materials;
	}

	public Job[] getJobs() {
		return jobs;
	}

	public Action[] getActions() {
		return actions;
	}
	
	
	private static JobConfig[] getJobConfigs() {
		ArrayList<JobConfig> jobconfigs = new ArrayList<JobConfig>();
		
		JSONArray list = (JSONArray) jobConfigDB.get("list");
		
		Iterator<Object> iList = list.iterator();
		
		while(iList.hasNext()) {
			JSONObject json = (JSONObject) iList.next();
			
			JobConfig jobConfig = null;
			
			String type = (String) getIfHas(json, "type");
			
			Material[] materials = convertToMaterials(getIfHasArray(json, "materials"));
			Job[] jobs = convertToJobs(getIfHasArray(json, "jobs"));
			Action[] actions = convertToActions(getIfHasArray(json, "actions"));
			
			jobConfig = new JobConfig(type, materials, jobs, actions);
			
			jobconfigs.add(jobConfig);
		}
		
		
		return jobconfigs.toArray(new JobConfig[0]);
	}
	
	
	private static Object getIfHas(JSONObject json, String key) {
		if(json.has(key)) {
			return json.get(key);
		}
		return null;
	}
	
	private static String[] getIfHasArray(JSONObject json, String key) {
		ArrayList<String> result = new ArrayList<String>();
		if(json.has(key)) {
			JSONArray list = (JSONArray) json.get(key);
			
			Iterator<Object> iList = list.iterator();
			
			while(iList.hasNext()) {
				result.add((String) iList.next());
			}
		}
		return result.toArray(new String[0]);
	}
	
	public static Material[] convertToMaterials(String[] str) {
		if(str.length>0) {
			ArrayList<Material> obj = new ArrayList<Material>();
			for (String string : str) {
				obj.add(Material.valueOf(string));
			}
			return obj.toArray(new Material[0]);
		}else {
			return null;
		}
	}
	
	public static Action[] convertToActions(String[] str) {
		if(str.length>0) {
			ArrayList<Action> obj = new ArrayList<Action>();
			for (String string : str) {
				obj.add(Action.valueOf(string));
			}
			return obj.toArray(new Action[0]);
		}else {
			return null;
		}
	}
	
	public static Job[] convertToJobs(String[] str) {
		if(str.length>0) {
			ArrayList<Job> obj = new ArrayList<Job>();
			for (String string : str) {
				obj.add(Job.getJobFromKey(string));
			}
			return obj.toArray(new Job[0]);
		}else {
			return null;
		}
	}
}
