package com.github.argoninc.job.sign;

import com.github.rillis.dao.DB;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.github.argoninc.job.user.Job;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Sign {
	private Location location;
	private Job job;

	private static Sign[] signs = null;
	private static DB signDB = null;
	public static boolean SHOW_SIGN_LOCATIONS = false;

	public static void init(){
		signDB = new DB("argoninc/jobs/jobs_sign.json");
		ArrayList<Sign> signArr = new ArrayList<>();

		if(!signDB.has("list")){
			signDB.set("list", new JSONArray());
		}
		if(!signDB.has("showSignLocations")){
			signDB.set("showSignLocations", false);
		}

		SHOW_SIGN_LOCATIONS = (boolean) signDB.get("showSignLocations");

		JSONArray jsonArray = (JSONArray) signDB.get("list");

		for (Object o : jsonArray) {
			JSONObject obj = (JSONObject) o;

			String locationString = obj.getString("location");

			Location locTemp = toLocation(locationString);
			Job jobTemp = Job.getJobFromKey(obj.getString("job"));

			signArr.add(new Sign(locTemp, jobTemp));

		}
		signs = signArr.toArray(new Sign[0]);
	}

	public Sign(Location location, Job job) {
		this.location = location;
		this.job = job;
	}
	
	public Location getLocation() {
		return location;
	}
	public Job getJob() {
		return job;
	}

	public static Sign getSign(Location l) {
		for (Sign s : signs){
			if(s.getLocation().equals(l)){
				return s;
			}
		}
		return null;
	}

	public static Sign getSign(Job j) {
		for (Sign s : signs){
			if(s.getJob().getKey().equals(j.getKey())){
				return s;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Sign [location=" + location + ", job=" + job + "]";
	}

	private static Location toLocation(String str) {
		String[] temp = str.split(",");
		return new Location(Bukkit.getWorld(temp[0]), Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), Double.parseDouble(temp[3]));
	}
}
