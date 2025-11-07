package com.ust.bean;

public class RouteBean {
	@Override
	public String toString() {
		return "routeID=" + routeID + ", source=" + source + ", destination=" + destination + ", distance="
				+ distance + ", travelDuration=" + travelDuration;
	}
	private String routeID;
    private String source;
    private String destination;
    private int distance;
    private int travelDuration;
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTravelDuration() {
		return travelDuration;
	}
	public void setTravelDuration(int travelDuration) {
		this.travelDuration = travelDuration;
	}
    
}
