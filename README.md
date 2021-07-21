# Azure Event Hub Producer Consumer

This is just an example about how to use Azure Event Hub. A string path of file on your local is sent through Postman. 
Then, the file is split into chunks of 250kb, to send chunks in order a partition id is required in producer 
and consumer as well. Then, chunks are gotten by consumer and file is assembly again.

## Docs

* https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-java-get-started-send
* https://docs.microsoft.com/en-us/java/api/overview/azure/messaging-eventhubs-readme?view=azure-java-stable
* https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-availability-and-consistency?tabs=dotnet