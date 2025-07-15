# event-driven-spring-cloud-stream

- Domain Driven Design (DDD)
- Test Driven Design (TDD)
- Event Driven with Spring Cloud Stream
- Apache Kafka Binder
- Functional Interfaces
- Retries & DLQs
- Message Router Function Routing
- Test Containers
- Spring Cloud Contract
- Transaction Outbox Pattern
- Kafka Connect
- Debezium
- Idempotency
- Change Data Capture (CDC)

---

# Debezium

 Check if Debezium is running at `http://localhost:8083/`

 Check installed Debezium connectors at `http://localhost:8083/connectors`

 Check available Debezium connector plugins at `http://localhost:8083/connector-plugins`
 
### Configure MySQL connector 

`https://debezium.io/documentation/reference/3.2/connectors/mysql.html`

https://debezium.io/documentation/reference/3.2/connectors/mysql.html#mysql-creating-user

```bash
docker exec -it mysql bin/bash

mysql -u root -p
```


### Creating a user
A Debezium MySQL connector requires a MySQL user account. This MySQL user must have appropriate permissions on all databases for which the Debezium MySQL connector captures changes.

### Prerequisites
- A MySQL server.
- Basic knowledge of SQL commands.

### Procedure

1. Create the MySQL user:

    `mysql> CREATE USER 'user'@'%' IDENTIFIED BY 'password';`

2. Grant the required permissions to the user:

    `mysql> GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'user'@'%';`

3. Finalize the user’s permissions:

    `mysql> FLUSH PRIVILEGES;`

---

## Registering a connector to monitor a database

https://debezium.io/documentation/reference/3.2/tutorial.html#registering-connector-monitor-inventory-database

Send a POST request to `localhost:8083/connectors` with the following body:

```json
{
   "name": "outbox-connector",
   "config": {
      "connector.class": "io.debezium.connector.mysql.MySqlConnector",
      "tasks.max": "1",
      "database.hostname": "mysql",
      "database.port": "3306",
      "database.user": "root",
      "database.password": "password",
      "database.server.id": "1",
      "topic.prefix": "kafka.connect",
      "database.include.list": "customer",
      "table.include.list": "customer.outbox_message",
      "schema.history.internal.kafka.bootstrap.servers": "broker:29092",
      "schema.history.internal.kafka.topic": "schema-history.customer",
      "transforms": "dropTopicPrefix,wrap",
      "transforms.dropTopicPrefix.type": "org.apache.kafka.connect.transforms.RegexRouter",
      "transforms.wrap.type": "io.debezium.transforms.ExtractNewRecordState",
      "transforms.dropTopicPrefix.regex": "kafka.connect.customer.(.*)",
      "transforms.dropTopicPrefix.replacement": "$1",
      "key.converter": "org.apache.kafka.connect.json.JsonConverter",
      "key.converter.schemas.enable": false,
      "value.converter": "org.apache.kafka.connect.json.JsonConverter",
      "value.converter.schemas.enable": false,
      "key.converter.schema.registry.url": "http://schema-registry:8081",
      "value.converter.schema.registry.url": "http://schema-registry:8081"

   }
}
```


Delete connector `localhost:8083/connectors/outbox-connector`