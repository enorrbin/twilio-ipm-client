package org.emanuel.twilio.client.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.api.TwilioKeyProvider;

/**
 * Looks in the user's configuration file (~/.twilio/keys) for twilio keys.
 * 
 * @author emanuel
 *)
 */
public class UserPropertyFileTwilioKeyProvider implements TwilioKeyProvider {
	
	public UserPropertyFileTwilioKeyProvider() {
	}
	
	private static Path getConfigFileLocation() throws IOException {
		String home = System.getenv("HOME");
		if ( home == null || home.isEmpty() ) {
			throw new IOException("Failed to find user's home directory.");
		}
		Path twilioDir = Paths.get(home, ".twilio").normalize();
		
		Path propertyFilePath = Paths.get(twilioDir.toString(), "keys");
		if ( !Files.isRegularFile(propertyFilePath) ) {
			throw new IOException("Property configuration file does not exist: " + propertyFilePath.toString());
		}
		return propertyFilePath;
	}
	
	public TwilioApiKeys getApiKeys() throws IOException {
		Path propertyFile = getConfigFileLocation();
		PropertyFileTwilioKeyProvider provider = new PropertyFileTwilioKeyProvider(propertyFile);
		return provider.getApiKeys();
	}

	public static void main(String[] args) {
		// Not a good candidate for Unit test due to user specific setup.
		// Test it manually here.

		TwilioKeyProvider test = new UserPropertyFileTwilioKeyProvider();
		try {
			test.getApiKeys();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
