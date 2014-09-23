package com.eric.nextbus.shared;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

public class BusData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Status {
		NONE("-"), 
		ONTIME("On Time"), 
		DELAYED("Delayed"), 
		AHEAD("Ahead");

		private final String name;       

		private Status(String s) {
			name = s;
		}

		public boolean equalsName(String otherName){
			return (otherName == null)? false:name.equals(otherName);
		}

		public String toString(){
			return name;
		}
	};
	
	private int _stopNum;
	private String _routeNum;
	private SortedMap<Integer, Status> _estimates = new TreeMap<Integer, Status>();
	
	public BusData()
	{
	}
	
	public BusData(int stopNum, String routeNum)
	{
		_stopNum = stopNum;
		_routeNum = routeNum;
	}
	
	public int GetStopNum()
	{
		return _stopNum;
	}
	
	public String GetRouteNum()
	{
		return _routeNum;
	}
	
	public void AddEstimate(int min, Status status)
	{
		_estimates.put(min, status);
	}
	
	public SortedMap<Integer, Status> GetEstimates()
	{
		return _estimates;
	}
}
