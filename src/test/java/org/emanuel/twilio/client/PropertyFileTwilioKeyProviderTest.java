package org.emanuel.twilio.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.emanuel.twilio.client.api.TwilioApiKeys;
import org.emanuel.twilio.client.impl.PropertyFileTwilioKeyProvider;

import junit.framework.TestCase;

public class PropertyFileTwilioKeyProviderTest extends TestCase {

	private Path propertyFile = null;

	// Doesn't matter what these are
	private final String fakeServiceSid = "fakeServiceSid";
	private final String fakeAccountSid = "fakeAccountSid";
	private final String fakeApiKey = "fakeApiKey";
	private final String fakeApiSecret = "fakeApiSecret";
	
	private final String lb = System.lineSeparator();
	
	private final String propertyFileContents =
			"TWILIO_IPM_SERVICE_SID=" + fakeServiceSid + lb +
			"TWILIO_ACCOUNT_SID=" + fakeAccountSid + lb +
			"TWILIO_API_KEY=" + fakeApiKey + lb +
			"TWILIO_API_SECRET=" + fakeApiSecret + lb;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		propertyFile = Files.createTempFile("properties", ".env");
				try ( BufferedWriter writer = new BufferedWriter(new FileWriter(propertyFile.toFile())) ) {
			writer.write(propertyFileContents);
		}		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Files.deleteIfExists(propertyFile);
	}
	
    public void testApp() throws IOException {
    	PropertyFileTwilioKeyProvider provider = new PropertyFileTwilioKeyProvider(propertyFile);
    	TwilioApiKeys keys = provider.getApiKeys();
    	assertEquals(fakeAccountSid, keys.getAccountSID());
    	assertEquals(fakeApiKey, keys.getApiKey());
    	assertEquals(fakeServiceSid, keys.getServiceSID());
    	assertEquals(fakeApiSecret, keys.getApiSecret());
    }
}
