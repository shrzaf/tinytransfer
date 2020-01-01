# TinyTransfer
A minimal backend with REST API for creating bank accounts and transferring money between accounts.

Built with JDK 8 and [Dropwizard](https://www.dropwizard.io).


How to start the application
---

1. Run `mvn clean install` to build the application.
1. Start the application with `java -jar target/tinytransfer-1.0-SNAPSHOT.jar server config.yml`
1. The application should be reachable at `http://localhost:8080`.

REST API
---
**GET /accounts/{accountNo}**

Returns the account information for a single account.

Example:

```
GET http://localhost:8080/accounts/foo
```
Response body:
```
{
    accountNo: "foo",
    balance: 100
}
```

**POST /accounts**

Creates a new account.

Example:

POST http://localhost:8080/accounts

Request body:

```
{
    accountNo: "foo",
    balance: 0
}
```

**POST /accounts/reset**

Resets the internal data store by deleting all stored accounts.

**POST /transfer**

Transfers an amount between two accounts.

Example:

POST http://localhost:8080/transfer

Request body:

```
{
    senderAccNo: "foo",
    receiverAccNo: "bar",
    amount: 100.42
}
```

Health Check
---

To see the application's health enter `http://localhost:8081/healthcheck`
