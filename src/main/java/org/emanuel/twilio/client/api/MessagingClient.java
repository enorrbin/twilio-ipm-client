package org.emanuel.twilio.client.api;

public interface MessagingClient {

	public void sendMessage(String msg) throws Exception;
	
	public void setMessageHandler(MessageHandler mh);
	
	void start() throws Exception;
	
	void stop();
}
