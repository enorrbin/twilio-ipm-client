package org.emanuel.twilio.client.api;

/**
 * Provide Twilio SIDs/Keys.
 * @author emanuel
 *
 */
public interface TwilioKeyProvider {
	
	public TwilioApiKeys getApiKeys() throws Exception;
}
