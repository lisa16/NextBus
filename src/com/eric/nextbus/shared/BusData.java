package com.eric.nextbus.shared;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

public class BusData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Status {NONE, ONTIME, DELAYED, AHEAD};
	
	private int _stopNum;
	private int _routeNum;
	private SortedMap<Integer, Status> _estimates = new TreeMap<Integer, Status>();
	
	public BusData()
	{
	}
	
	public BusData(int stopNum, int routeNum)
	{
		_stopNum = stopNum;
		_routeNum = routeNum;
	}
	
	public int GetStopNum()
	{
		return _stopNum;
	}
	
	public int GetRouteNum()
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
