# tradeStoreApp

Trade requests are coming from various sources like Kafka topics, RESTful API's, JMS Queues etc
A TradeStore application uses Event Sourcing and CQRS pattern – Commands (writes) to MySQL; Queries (reads) from MongoDB.
A CommandService handles all the concurrent requests from various sources.
Thread Pool (ExecutorService) – For concurrent trade processing.
Store Trade entries in Redis-cache for faster retrieval, use it to validate the TRADE.
    If lower version TRADE is received, then TradeStore application rejects this TRADE.
    If trade's maturity-date is less than Today's date, then TradeStore application rejects this TRADE.
If TRADE with same version is received, then TradeStore application updates the details & updates the cache.
If NEW TRADE is received, then TradeStore application stores the new TRADE details & updates the cache.
Event-Driven Design – Trade updates will publish events to Kafka for Query Service.
Query Service will consume the message and

Each TRADE & its state is then maintained in MongoDB and will be used by further auditing, aggregation, reporting purpose..
Desing Links: 
Flow Design diagram: https://github.com/sarikaogale/tradeStoreApp/blob/main/src/main/resources/TradeStoreFlowDiagram.png
Sequence Diagram: https://github.com/sarikaogale/tradeStoreApp/blob/main/src/main/resources/tradeStoreSequenceDiagram.png
