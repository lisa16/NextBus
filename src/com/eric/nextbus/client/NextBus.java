package com.eric.nextbus.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.eric.nextbus.shared.BusData;
import com.eric.nextbus.shared.BusData.Status;
import com.eric.nextbus.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NextBus implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private String _stopNum;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public AsyncCallback<List<BusData>> nextBusService;

	private final TextBox _stopNumInput = new TextBox();

	private final Map<String, HTML> _busEstimatesMap = new TreeMap<String, HTML>();
	private final List<Button> _busRouteNumButtons = new ArrayList<Button>();
	
	private final Button refreshBtn = Button.wrap(Document.get().getElementById("refreshBtn"));
	
	private final String LOADING_GIF_DIV_ID = "loadingGif";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		ChangeLoadingGifVisibility(false);
		_stopNum = Window.Location.getParameter("stopnum");

		final HTML busRouteNumField = new HTML();
		final HTML busStopNumField = new HTML();

		// We can add style names to widgets
		//		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//		RootPanel.get("nameFieldContainer").add(nameField);
		//		RootPanel.get("sendButtonContainer").add(sendButton);
		//		RootPanel.get("errorLabelContainer").add(errorLabel);
		//		RootPanel.get("nextBusLabelContainer").add(nextBusLabel);
		RootPanel.get("busRouteNumField").add(busRouteNumField);
		RootPanel.get("busStopNumField").add(busStopNumField);

		/*		busRouteNumField.setHTML("<div class=\"row\">\r\n" + 
				"				<button type=\"button\" class=\"btn btn-lg btn-primary active\">099</button>\r\n" + 
				"				<button type=\"button\" class=\"btn btn-lg btn-primary\">041</button>\r\n" + 
				"				<button type=\"button\" class=\"btn btn-lg btn-primary\">009</button>\r\n" + 
				"				<button type=\"button\" class=\"btn btn-lg btn-primary\">049</button>\r\n" + 
				"				<button type=\"button\" class=\"btn btn-lg btn-primary\">084</button>\r\n" + 
				"			</div>");*/

		/*		busStopNumField.setHTML("<h3>Bus Stop Num: 59270</h3>");*/

		/*busEstimatesField.setHTML("<table class=\"table table-striped\">\r\n" + 
				"				<thead>\r\n" + 
				"					<th>Estimates</th>\r\n" + 
				"					<th>Status</th>\r\n" + 
				"				</thead>\r\n" + 
				"				<tbody>\r\n" + 
				"					<tr>\r\n" + 
				"						<td>0 min</td>\r\n" + 
				"						<td>On Time</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td>5 min</td>\r\n" + 
				"						<td>Delayed</td>\r\n" + 
				"					</tr>\r\n" + 
				"					<tr>\r\n" + 
				"						<td>10 min</td>\r\n" + 
				"						<td>Ahead</td>\r\n" + 
				"					</tr>\r\n" + 
				"				</tbody>\r\n" + 
				"			</table>");*/

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		nextBusService = new AsyncCallback<List<BusData>>(){
			public void onFailure(Throwable caught) {
				try
				{
					// Show the RPC error message to the user
					dialogBox.setText("Remote Procedure Call - Failure");
					serverResponseLabel
					.addStyleName("serverResponseLabelError");
					serverResponseLabel.setHTML(SERVER_ERROR);
					dialogBox.center();
					closeButton.setFocus(true);
				}
				catch(Exception e)
				{
					ChangeLoadingGifVisibility(false);
					throw(e);
				}
				
			}

			public void onSuccess(List<BusData> result) {
				try
				{
					if(result.size()==0)
					{
						busRouteNumField.setHTML(FieldAdder.NoBusMessage());
						return;
					}
	
					AddBusRouteNumField(result, RootPanel.get("busRouteNumField"));
					AddBusStopNumField(result.get(0).GetStopNum(), RootPanel.get("busStopNumField"));
	
					InitBusEstimatesMap(result);
	
					final RootPanel busEstimatesFieldPanel = RootPanel.get("busEstimatesField");
					busEstimatesFieldPanel.clear();
					busEstimatesFieldPanel.add((Widget) _busEstimatesMap.values().toArray()[0]);
					
					ChangeLoadingGifVisibility(false);
	
					System.out.println("succeed");
				}
				catch(Exception e)
				{
					ChangeLoadingGifVisibility(false);
					throw(e);
				}
			}
		};
		System.out.println(_stopNum);
		if(_stopNum != null && FieldVerifier.tryParseInt(_stopNum))
		{
			SearchStopNum(_stopNum);
		}
		else
		{
			AddBusStopNumField(0, RootPanel.get("busStopNumField"));
		}
		
		refreshBtn.addClickHandler(new RefreshClickHandler());
		
		
		
	}

	private void InitBusEstimatesMap(List<BusData> dataList)
	{
		_busEstimatesMap.clear();
		
		for(BusData data : dataList)
		{
			String head = "<table class=\"table table-striped\">\r\n" + 
					"				<thead>\r\n" + 
					"					<th>Estimates</th>\r\n" + 
					"					<th>Status</th>\r\n" + 
					"				</thead>\r\n" + 
					"				<tbody>\r\n";

			String content = "";

			SortedMap<Integer, Status> estimates = data.GetEstimates();
			for(int key : estimates.keySet())
			{
				BusData.Status status = estimates.get(key);
				content += "<tr>\r\n" + 
						"						<td>" + 
						key +
						" min</td>\r\n" + 
						FormatStatusTableData(status) + 
						"					</tr>\r\n";
			}

			String foot = "			</tbody>\r\n" + 
					"			</table>\r\n";

			final HTML busRouteTable = new HTML(head+content+foot);

			String busRouteNum = data.GetRouteNum();

			_busEstimatesMap.put(busRouteNum, busRouteTable);
		}
	}
	
	private String FormatStatusTableData(BusData.Status status)
	{
		String tdStatus = "<td";
		switch(status)
		{
			case NONE:
				tdStatus += ">";
				break;
			case AHEAD:
				tdStatus += " class=\"orangeColor\">";
				break;
			case DELAYED:
				tdStatus += " class=\"redColor\">";
				break;
			case ONTIME:
				tdStatus += " class=\"greenColor\">";
				break;
		}
		tdStatus +=status.toString() + "</td>\r\n";
		
		System.out.println(tdStatus);
		return tdStatus;
	}

	private void AddBusStopNumField(int stopNum, RootPanel busStopNumFieldPanel)
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
		busStopNumFieldPanel.clear();

		final HTML stopNumLabel = new HTML("<h3>Stop Num: </h3>");
		busStopNumFieldPanel.add(stopNumLabel);		

		_stopNumInput.getElement().setAttribute("placeHolder","Stop Number");
		_stopNumInput.getElement().setClassName("form-control");
		_stopNumInput.getElement().setAttribute("type", "number");
		if(stopNum==0)
		{
			_stopNumInput.setText("");
		}
		else
		{
			_stopNumInput.setText(stopNum + "");
		}

		_stopNumInput.addKeyUpHandler(new BusStopNumberEventHandler());

		busStopNumFieldPanel.add(_stopNumInput);		

		final Button searchButton = new Button("Search");
		searchButton.getElement().setClassName("btn btn-info btn-lg");

		searchButton.addClickHandler(new BusStopNumberEventHandler());

		busStopNumFieldPanel.add(searchButton);		
	}

	public void AddBusRouteNumField(List<BusData> dataList, RootPanel routeNumFieldPanel)
	{
		routeNumFieldPanel.clear();
		_busRouteNumButtons.clear();

		boolean isFirstButtonActive = false;

		//		NumberFormat fmt = NumberFormat.getFormat("000");
		for(BusData data : dataList)
		{
			String routeNum = data.GetRouteNum();
			Button routeNumberButton = new Button (routeNum);
			_busRouteNumButtons.add(routeNumberButton);
			if(!isFirstButtonActive)
			{
				routeNumberButton.getElement().setClassName("btn btn-lg btn-primary active");
				isFirstButtonActive = true;
			}
			else
			{
				routeNumberButton.getElement().setClassName("btn btn-lg btn-primary");
			}

			routeNumberButton.addClickHandler(new BusRouteNumberClickHandler());

			routeNumFieldPanel.add(routeNumberButton);
		}
	}
	
	

	public void ChangeLoadingGifVisibility(boolean isVisible)
	{
		if(isVisible)
			DOM.getElementById(LOADING_GIF_DIV_ID).getStyle().setDisplay(Display.INLINE);
		else
			DOM.getElementById(LOADING_GIF_DIV_ID).getStyle().setDisplay(Display.NONE);
	}

	class BusRouteNumberClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) 
		{
			for(Button b : _busRouteNumButtons)
			{
				b.getElement().setClassName("btn btn-lg btn-primary");
			}

			Button button = (Button)event.getSource();

			final RootPanel busEstimatesFieldPanel = RootPanel.get("busEstimatesField");

			busEstimatesFieldPanel.clear();

			button.getElement().setClassName("btn btn-lg btn-primary active");
			busEstimatesFieldPanel.add(_busEstimatesMap.get(button.getText()));
		}
	}

	class BusStopNumberEventHandler implements ClickHandler, KeyUpHandler {

		public void onClick(ClickEvent event) 
		{
			queryStopNumToServer();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				queryStopNumToServer();
			}
		}

		protected void queryStopNumToServer()
		{
			_stopNum = _stopNumInput.getText();
			if(FieldVerifier.isValidStopNum(_stopNum))
			{
				SearchStopNum(_stopNum);
			}
		}

	}
	
	class RefreshClickHandler implements ClickHandler {

		public void onClick(ClickEvent event) 
		{
			queryStopNumToServer();
		}
		
		protected void queryStopNumToServer()
		{
			if(FieldVerifier.isValidStopNum(_stopNum))
			{
				SearchStopNum(_stopNum);
			}
		}
	}
	
	private void SearchStopNum(String stopNum)
	{
		ChangeLoadingGifVisibility(true);
		greetingService.greetServer(stopNum, nextBusService);
	}
}
