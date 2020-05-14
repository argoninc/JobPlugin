package com.github.argoninc.job.user;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.argoninc.job.listener.JobConfig;
import com.github.rillis.dao.DB;

public class UserJob {
	private String username;
	private String uuid;
	private Job job;
	private long lastChange;
	
	public static UserJob[] users = null;
	public static DB userDB = null;
	
	public static void init() {
		userDB = new DB("argoninc/job_users.json");
		if(!userDB.has("users")) {
			userDB.set("users", new JSONArray());
		}
		
		ArrayList<UserJob> arr = new ArrayList<UserJob>();
		
		JSONArray jsonArray = (JSONArray) userDB.get("users");
		
		Iterator<Object> i = jsonArray.iterator();
		
		while(i.hasNext()) {
			JSONObject json = (JSONObject) i.next();
			
			Job j = null;
			
			if(json.has("key")) {
				j = new Job(json.getString("key"), json.getString("name"), json.getString("color").charAt(0));
			}
			
			UserJob u = new UserJob(json.getString("username"),json.getString("uuid"), j, json.getLong("lastChange"));
			
			arr.add(u);
		}
		
		users = arr.toArray(new UserJob[0]);
		
	}
	
	public static void refreshFromDB() {
		init();
	}
	
	public static UserJob getUser(String uuid) {
		for (UserJob userJob : users) {
			if(userJob.getUuid().equals(uuid)) {
				return userJob;
			}
		}
		return null;
	}
	
	public UserJob(String username, String uuid, Job job, long lastChange) {
		this.username = username;
		this.uuid = uuid;
		this.job = job;
		this.lastChange = lastChange;
	}

	public String getUuid() {
		return uuid;
	}
	public String getUsername() {
		return username;
	}
	public Job getJob() {
		return job;
	}
	public long getLastChange() {
		return lastChange;
	}
	public void setLastChange(long time) {
		lastChange=time;
		save();
	}
	public boolean isInCooldown() {
		long cooldown = (long) JobConfig.jobConfigDB.get("jobCooldownSec") * 1000;
		if(System.currentTimeMillis()-lastChange < cooldown) {
			return true;
		}
		return false;
	}
	public boolean hasJob() {
		if(job==null) {
			return false;
		}
		return true;
	}
	public void setJob(Job job) {
		this.job = job;
		save();
	}
	
	public void removeJob() {
		this.job = null;
		save();
	}
	
	public JSONObject toJSON() {
		JSONObject j = new JSONObject();
		
		j.put("username", username);
		j.put("uuid", uuid);
		if(this.hasJob()) {
			j.put("key", job.getKey());
			j.put("name", job.getName());
			j.put("color", job.getColorString());
		}
		j.put("lastChange", lastChange);
		
		return j;
	}
	
	private void save() {		
		//java
		for (int i = 0; i < users.length; i++) {
			if(users[i].getUuid().equals(this.uuid)) {
				users[i] = this;
			}
		}
		
		//file
		JSONArray jsonArray = (JSONArray) userDB.get("users");
		Iterator<Object> i = jsonArray.iterator();
		
		int in = 0;
		while(i.hasNext()) {
			JSONObject json = (JSONObject) i.next();
			
			if(json.getString("uuid").equals(uuid)) {
				jsonArray.put(in, this.toJSON());
				userDB.set("users", jsonArray);
			}
			
			in++;
		}
	}

	@Override
	public String toString() {
		return "UserJob [username=" + username + ", uuid=" + uuid + ", job=" + job.toString() + ", lastChange=" + lastChange + "]";
	}

	
	
}
