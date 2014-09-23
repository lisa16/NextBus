package com.eric.nextbus.client;

import java.util.List;
import java.util.SortedMap;

import com.eric.nextbus.shared.BusData;
import com.eric.nextbus.shared.BusData.Status;

public class FieldAdder {

	
	public static String NoBusMessage()
	{
		String noBusMessage = "<h2>No bus is coming to this stop in the next 24 hours</h2>";
		return noBusMessage;
	}

	public static String AddBusStopNumField(int stopNum)
	{
		String busStopNumFormat = "<div class=\"row\">\r\n" + 
								  "<h3>Stop Num: " +
								  "<input width=\"75px\" type=\"number\" placeholder=\"Stop Number\" value=\"" +
								  stopNum + 
								  "\">\r\n" +
								  "<button type=\"button\" class=\"btn btn-info\">Refresh</button>\r\n" +
								  "</h3>\r\n" +
								  "</div>\r\n";
		
		return busStopNumFormat;
	}
	
	public static String AddBusRouteNumField(List<BusData> dataList)
	{
		String head = "<div class=\"row\">\r\n";
		String content = "";

//		NumberFormat fmt = NumberFormat.getFormat("000");
		for(BusData data : dataList)
		{
			String routeNum = data.GetRouteNum();
			content += "<button type=\"button\" class=\"btn btn-lg btn-primary\">" +
					routeNum +
					"</button>\r\n";
		}

		String foot = "</div>\r\n";

		return head+content+foot;
	}

	public static String AddBusEstimatesField(List<BusData> dataList)
	{
		String head = "<table class=\"table table-striped\">\r\n" + 
				"				<thead>\r\n" + 
				"					<th>Estimates</th>\r\n" + 
				"					<th>Status</th>\r\n" + 
				"				</thead>\r\n" + 
				"				<tbody>\r\n";
		
		String content = "";
		for(BusData data : dataList)
		{
			SortedMap<Integer, Status> estimates = data.GetEstimates();
			for(int key : estimates.keySet())
			{
				BusData.Status status = estimates.get(key);
				content += "<tr>\r\n" + 
						"						<td>" + 
						key +
						" min</td>\r\n" + 
						"						<td>" +
						status.toString() +
						"</td>\r\n" + 
						"					</tr>\r\n";
			}
			
		}
		
		String foot = "			</tbody>\r\n" + 
				"			</table>\r\n";
		
		return head+content+foot;
	}
}
