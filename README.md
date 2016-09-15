# Sample Java cli for IP Messaging using the twilio API

A simple example of a Java cli for sending and receiving text messages using the twilio API. This is a toy project.

Expects your API keys in the standard Java property file format in `~/.twilio/keys`:

    TWILIO_IPM_SERVICE_SID=<your IPM SID>
    TWILIO_ACCOUNT_SID=<your Account SID>
    TWILIO_API_KEY=<your API key>
    TWILIO_API_SECRET=<your API secret>

## Usage

    mvn package
    chmod 755 client
    ./client

## License

No license
