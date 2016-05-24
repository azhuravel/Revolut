# Revolut

A very simple RESTful service. To run: java -jar ... [PORT].
For example: java -jar /home/azhuravel/WikiProjects/RevolutTest/facade/target/zhuravel-facade-1.0.jar 9090

Technologies:
+ Hibernate
+ H2 (in-memory)
+ Spark framework
- nothing from Spring :)

User has several Accounts. It is acceptable to create new users, add their accounts and send transfers.

Test case:

curl -H "Content-Type: application/json" -XPOST localhost:9090/users -d'{"name": "alex"}'
{"id":1,"name":"alex"}

curl -H "Content-Type: application/json" -XPOST localhost:9090/users -d'{"name": "serg"}'
{"id":2,"name":"serg"}

curl -H "Content-Type: application/json" -XPOST localhost:9090/users/1/accounts -d'{"ammount": 10}'
{"id":1,"ammount":10}

curl -H "Content-Type: application/json" -XPOST localhost:9090/users/2/accounts -d'{"ammount": 10}'
{"id":2,"ammount":10}

curl -H "Content-Type: application/json" -XPOST localhost:9090/transfers -d'{"fromAccountId": 2, "toAccountId": 1, "sum": 10}'
Successful transfer from account #3 to #1
