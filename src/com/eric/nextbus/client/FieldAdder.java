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

}
