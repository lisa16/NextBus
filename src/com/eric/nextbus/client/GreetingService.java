package com.eric.nextbus.client;

import java.util.List;

import com.eric.nextbus.shared.BusData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	List<BusData> greetServer(String name) throws IllegalArgumentException;
	String SendMail(String from, String to, String replyTo, String subject, String message) throws IllegalArgumentException;
}
