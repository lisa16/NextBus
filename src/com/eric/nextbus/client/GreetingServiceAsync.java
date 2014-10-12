package com.eric.nextbus.client;

import java.util.List;

import com.eric.nextbus.shared.BusData;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<List<BusData>> callback)throws IllegalArgumentException;
	void SendMail(String from, String to, String replyTo, String subject, String message, AsyncCallback<String> callback) throws IllegalArgumentException;
}
