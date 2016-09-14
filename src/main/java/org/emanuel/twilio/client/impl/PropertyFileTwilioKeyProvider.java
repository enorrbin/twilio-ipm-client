package org.emanuel.twilio.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.api.TwilioKeyProvider;

public class PropertyFileTwilioKeyProvider implements TwilioKeyProvider {
	
	private final Path propertyFilePath;
	
	public PropertyFileTwilioKeyProvider(Path propertyFilePath) {
		this.propertyFilePath = propertyFilePath;
	}

	private static Properties loadProperties(File file) throws IOException {
		Properties props = new Properties();
		try ( FileInputStream in = new FileInputStream(file) ) {
			props.load(in);			
		}
		return props;
	}
	
	public TwilioApiKeys getApiKeys() throws IOException {
		if ( !Files.isRegularFile(propertyFilePath) ) {
			throw new IOException("Key file does not exist: " + propertyFilePath.toString());
		}
		
		Properties twilioProperties = loadProperties(propertyFilePath.toFile());
		
		String serviceSID = twilioProperties.getProperty("TWILIO_IPM_SERVICE_SID");
		String accountSID = twilioProperties.getProperty("TWILIO_ACCOUNT_SID");
		String apiKey = twilioProperties.getProperty("TWILIO_API_KEY");
		String apiSecret = twilioProperties.getProperty("TWILIO_API_SECRET");
		return new TwilioApiKeys(serviceSID, accountSID, apiKey, apiSecret);
	}

}
