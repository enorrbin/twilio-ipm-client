package org.emanuel.twilio.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.api.TwilioKeyProvider;

/**
 * Provides twilio keys from a Java property file like this (fake but realistic example):
 * TWILIO_IPM_SERVICE_SID=IS4aaea52aa770457290b74d50c92ad50e
 * TWILIO_ACCOUNT_SID=AC0cf13fbb8371ad25814f901e5188529f
 * TWILIO_API_KEY=SKede9d2d34e9f170e875c2536cb7833e3
 * TWILIO_API_SECRET=NRg9NMz81xBtIgngsKtWJCU1u4KqVxOu
 * 
 * @author emanuel
 *
 */
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
