package com.github.argoninc.job.sign;

import org.bukkit.Location;

import com.github.argoninc.job.user.Job;

public class Sign {
	private Location location;
	private Job job;

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

	@Override
	public String toString() {
		return "Sign [location=" + location + ", job=" + job + "]";
	}	
	
}
