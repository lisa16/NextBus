package com.eric.nextbus.client;

import java.util.List;
import java.util.SortedMap;

import com.eric.nextbus.shared.BusData;
import com.eric.nextbus.shared.BusData.Status;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class FieldAdder {

	
	public static String NoBusMessage()
	{
		String noBusMessage = "<h2>No bus is coming to this stop in the next 24 hours</h2>";
		return noBusMessage;
	}

	public static String AddBusStopNumField(int stopNum, RootPanel busStopNumFieldPanel)
	{
/*		String busStopNumFormat = "<div class=\"row\">\r\n" + 
								  "<h3>Stop Num: " +
								  "<input width=\"75px\" type=\"number\" placeholder=\"Stop Number\" value=\"" +
								  stopNum + 
								  "\">\r\n" +
								  "<button type=\"button\" class=\"btn btn-info\">Refresh</button>\r\n" +
								  "</h3>\r\n" +
								  "</div>\r\n";
*/
		
//		busStopNumFieldPanel.getElement().setClassName("row");
		
//		FlowPanel flowPanel = new FlowPanel();
//		busStopNumFieldPanel.add(flowPanel);
		
//		flowPanel.getElement().setClassName("row");
		
		HTML stopNumLabel = new HTML("<h3>Stop Num: </h3>");
		busStopNumFieldPanel.add(stopNumLabel);		
		
		TextBox stopNumInput = new TextBox();
		stopNumInput.getElement().setAttribute("placeHolder","Stop Number");
		stopNumInput.getElement().setClassName("form-control");
		stopNumInput.getElement().setAttribute("type", "number");
		stopNumInput.setText(stopNum + "");
		
		busStopNumFieldPanel.add(stopNumInput);		
		
		Button searchButton = new Button("Search");
		searchButton.getElement().setClassName("btn btn-info btn-lg");
		
		busStopNumFieldPanel.add(searchButton);		
		
		return "";
	}
	
	public static String AddBusRouteNumField(List<BusData> dataList, RootPanel routeNumFieldPanel)
	{
		routeNumFieldPanel.clear();
		
		String head = "<div class=\"row\">\r\n";
		String content = "";

//		NumberFormat fmt = NumberFormat.getFormat("000");
		for(BusData data : dataList)
		{
			String routeNum = data.GetRouteNum();
			Button stopButton = new Button (routeNum);
			stopButton.getElement().setClassName("btn btn-lg btn-primary");
			
			
			
			routeNumFieldPanel.add(stopButton);
//			content += "<button type=\"button\" class=\"btn btn-lg btn-primary\">" +
//					routeNum +
//					"</button>\r\n";
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
