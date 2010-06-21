package org.onesocialweb.gwt.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;

public class OSWUrlBuilder extends UrlBuilder {

	public OSWUrlBuilder() {
		
		// default stuff
		setProtocol(Window.Location.getProtocol()); 
		setHost(Window.Location.getHost()); 
		String path = Window.Location.getPath(); 
		if (path != null && path.length() > 0) { 
			setPath(path); 
		} 
		String hash = Window.Location.getHash(); 
		if (hash != null && hash.length() > 0) {
			setHash(hash);
		} 
		String port = Window.Location.getPort(); 
		if (port != null && port.length() > 0) { 
			setPort(Integer.parseInt(port)); 
		} 
		
		// Add query parameters.
		Map<String, List<String>> params = Window.Location.getParameterMap(); 
		for (Map.Entry<String, List<String>> entry : params.entrySet()) { 
			List<String> values = new ArrayList<String>(entry.getValue()); 
			setParameter(entry.getKey(), values.toArray(new String[values.size()])); 
		} 

	}
	
}
