package org.emanuel.twilio.client.api;

import java.util.Objects;

/**
 * Immutable class that holds the information needed to connect to twilio APIs.

 * @author emanuel
 *
 */
public class TwilioApiKeys {
	private final String serviceSID;
	private final String accountSID;
	private final String apiKey;
	private final String apiSecret;
	
	public TwilioApiKeys(String serviceSID,
			String accountSID,
			String apiKey,
			String apiSecret) 
	{
		Objects.requireNonNull(serviceSID, "Service SID can not be null.");
		Objects.requireNonNull(accountSID, "Account SID can not be null.");
		Objects.requireNonNull(apiKey, "API Key can not be null.");
		Objects.requireNonNull(apiSecret, "API Secret can not be null.");
		this.serviceSID = serviceSID;
		this.accountSID = accountSID;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public String getServiceSID() {
		return serviceSID;
	}

	public String getAccountSID() {
		return accountSID;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}
	
}
