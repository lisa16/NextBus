package com.eric.nextbus.client;

import java.util.List;
import java.util.SortedMap;

import com.eric.nextbus.shared.BusData;
import com.eric.nextbus.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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

	private String _stopNo;
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	
	public AsyncCallback<List<BusData>> nextBusService;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
//		final Button sendButton = new Button("Query");
//		final TextBox nameField = new TextBox();
//		nameField.getElement().setAttribute("placeholder", "Enter Bus Stop Number");
		_stopNo = Window.Location.getParameter("stopnum");

//		final Label errorLabel = new Label();
//		final HTML nextBusLabel = new HTML();
		final HTML busRouteNumField = new HTML();
		final HTML busStopNumField = new HTML();
		final HTML busEstimatesField = new HTML();
		
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
		RootPanel.get("busEstimatesField").add(busEstimatesField);
		
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

		// Focus the cursor on the name field when the app loads
//		nameField.setFocus(true);
//		nameField.selectAll();

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
//				sendButton.setEnabled(true);
//				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}
			
			

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			protected void sendNameToServer() {
				// First, we validate the input.
//				errorLabel.setText("");
//				nextBusLabel.setText("");
//				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidBusNo(_stopNo)) {
//					errorLabel.setText("Please enter 5 digit bus stop number");
					return;
				}

				// Then, we send the input to the server.
//				sendButton.setEnabled(false);
//				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(_stopNo, nextBusService);
			}
		}
		nextBusService = new AsyncCallback<List<BusData>>(){
			public void onFailure(Throwable caught) {
				// Show the RPC error message to the user
				dialogBox
						.setText("Remote Procedure Call - Failure");
				serverResponseLabel
						.addStyleName("serverResponseLabelError");
				serverResponseLabel.setHTML(SERVER_ERROR);
				dialogBox.center();
				closeButton.setFocus(true);
//				sendButton.setEnabled(true);
			}

			public void onSuccess(List<BusData> result) {
//				dialogBox.setText("Remote Procedure Call");
//				serverResponseLabel
//						.removeStyleName("serverResponseLabelError");
//				serverResponseLabel.setHTML(result);
//				dialogBox.center();
//				closeButton.setFocus(true);
//				System.out.println(result);
//				String resultHTML = StringEscapeUtils.escapeHtml4(result);
//				nextBusLabel.setHTML(result);
				//nextBusLabel;
				if(result.size()==0)
				{
					busRouteNumField.setHTML(FieldAdder.NoBusMessage());
					return;
				}

				String content = "";
				content += "Stop No: " + result.get(0).GetStopNum() + "<br>";
				for(BusData busData : result)
				{
					String routeNo = busData.GetRouteNum();
//					int stopNo = busData.GetStopNum();

					SortedMap<Integer, BusData.Status> estimates = busData.GetEstimates();
					
					content += "Route No: " + routeNo;
					content += "<br>";
					
					for(int key : estimates.keySet())
					{
						BusData.Status status = estimates.get(key);
						content += key + " : " + status.toString();
						content += "<br>";
					}
					
				}
				System.out.println("content");
//				nextBusLabel.setHTML(content);
				
				FieldAdder.AddBusRouteNumField(result, RootPanel.get("busRouteNumField"));
				FieldAdder.AddBusStopNumField(result.get(0).GetStopNum(), RootPanel.get("busStopNumField"));
				busEstimatesField.setHTML(FieldAdder.AddBusEstimatesField(result));
				System.out.println("succed done");
//				sendButton.setEnabled(true);
			}
		};
		System.out.println(_stopNo);
		if(_stopNo != null && FieldVerifier.tryParseInt(_stopNo))
		{
//			nameField.setText(routeNo);
			greetingService.greetServer(_stopNo, nextBusService);
		}

		// Add a handler to send the name to the server
//		MyHandler handler = new MyHandler();
//		sendButton.addClickHandler(handler);
//		nameField.addKeyUpHandler(handler);
	}
}
