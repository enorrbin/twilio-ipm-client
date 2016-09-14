package org.emanuel.twilio.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.api.TwilioKeyProvider;

/**
 * Looks in the user's configuration file (~/.twilio/keys by default) for twilio keys.
 * 
 * @author emanuel
 *
 */
public class UserConfigFileTwilioKeyProvider implements TwilioKeyProvider {
	
	UserConfigFileTwilioKeyProvider() {
	}
	
	private static Path getConfigFileLocation() throws IOException {
		String home = System.getenv("HOME");
		if ( home == null || home.isEmpty() ) {
			throw new IOException("Failed to find user's home directory.");
		}
		Path twilioDir = Paths.get(home, ".twilio").normalize();
		if ( !Files.exists(twilioDir) ) {
			Files.createDirectory(twilioDir);
			// TODO: Set user permission to 700.
		}
		
		return Paths.get(twilioDir.toString(), "keys");
	}
	
	private static Properties loadProperties(File file) throws IOException {
		Properties props = new Properties();
		try ( FileInputStream in = new FileInputStream(file) ) {
			props.load(in);			
		}
		return props;
	}

	public TwilioApiKeys getApiKeys() throws IOException {
		Path configFile = getConfigFileLocation();
		if ( !Files.isRegularFile(configFile) ) {
			throw new IOException("Key file does not exist: " + configFile.toString());
		}
		
		Properties twilioProperties = loadProperties(configFile.toFile());
		
		String serviceSID = twilioProperties.getProperty("TWILIO_IPM_SERVICE_SID");
		String accountSID = twilioProperties.getProperty("TWILIO_ACCOUNT_SID");
		String apiKey = twilioProperties.getProperty("TWILIO_API_KEY");
		String apiSecret = twilioProperties.getProperty("TWILIO_API_SECRET");
		return new TwilioApiKeys(serviceSID, accountSID, apiKey, apiSecret);
	}

	public static void main(String[] args) {
		TwilioKeyProvider test = new UserConfigFileTwilioKeyProvider();
		try {
			test.getApiKeys();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
