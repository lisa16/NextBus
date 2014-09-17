package com.eric.nextbus.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.eric.nextbus.client.GreetingService;
import com.eric.nextbus.shared.FieldVerifier;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		
//		String queryLink = "http://api.translink.ca/rttiapi/v1/stops/"
//				+ input
//				+ "/estimates?apikey=YWxAZPnYPOJsiX0wFG3l";
		
		StringBuilder uriBuilder = new StringBuilder(
				"//api.translink.ca/rttiapi/v1/stops/");
		uriBuilder.append(input);
		uriBuilder.append("/estimates?apikey=YWxAZPnYPOJsiX0wFG3l");

		String busStopJsonQuery = makeJSONQuery(uriBuilder);
		
		String result = "";
		try 
		{
			JSONArray arr = new JSONArray(busStopJsonQuery);

			for(int i=0; i< arr.length(); i++)
			{
				JSONObject obj = arr.getJSONObject(i);
				String routeName = obj.getString("RouteNo");
				result += "RouteNo: " + routeName + "\n";
				//			BusRoute route = new BusRoute(routeName);
				JSONArray schedules = obj.getJSONArray("Schedules");
//				int[] estimatesArray = new int[schedules.length()];
				for (int s=0; s< schedules.length(); s++) 
				{
					JSONObject jobj = schedules.getJSONObject(s);
					int estimate = jobj.getInt("ExpectedCountdown");
//					estimatesArray[s] = estimate;
					
					result += "(" + s + "): " + estimate + "mins ";
					//			boolean cancelledTrip = jobj.getBoolean("CancelledTrip");
					//			boolean cancelledStop = jobj.getBoolean("CancelledStop");
//					BusWaitTime bwt = new BusWaitTime(route, estimate, cancelledTrip || cancelledStop);
//					stop.addWaitTime(bwt);
				}
				result += "\n";
			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return result;
		
//		return "Hello, " + input + "!<br><br>I am running " + serverInfo
//				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}
	
	private String makeJSONQuery(StringBuilder urlBuilder) {
		HttpURLConnection client = null;
		try {
			URL url = new URL("http:" + urlBuilder.toString());
			client = (HttpURLConnection) url.openConnection();
			client.setRequestProperty("accept", "application/json");
			client.setConnectTimeout(3000);
			client.setReadTimeout(3000);
			client.connect();
			BufferedReader br;
			InputStream err = client.getErrorStream();
			if( err != null )
				br = new BufferedReader(new InputStreamReader(err));
			else {
				InputStream in = client.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));
			}
			String returnString = br.readLine();
			return returnString;
		}
	      catch (Exception e) {
			return "ERROR";
		} finally {
			if(client != null)
				client.disconnect();
		}
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
