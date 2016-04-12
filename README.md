spring-redis-caching-with-protobuf
==================================
Java built-in serialization/deserialization mechanism is not only verbose but also platform dependent, which makes it not a good choice for RPC.  
This project will work as a showcase for spring redis caching in json and protobuf format.

# Getting Started

## Prerequisites

The project requires at least JDK 7, protobuf, redis and Maven 3.0.

## Installation and start the server

```
mvn clean jetty:run
```

## Json format to/from redis

1.  Save data to redis
	
	http://localhost:8080/users/to-redis/3

    Server response body

	{"id":3,"userName":"中文测试-UserName","userGroup":{"id":3,"userGroupName":"中文测试-GroupName"}}

2.  Fetch data from redis
	
	http://localhost:8080/users/from-redis/3

    Server response body

	{"id":3,"userName":"中文测试-UserName","userGroup":{"id":3,"userGroupName":"中文测试-GroupName"}}

## Protobuf format to/from redis

1.  Save data to redis
	
	http://localhost:8080/users/to-redis-protobuf/4

    Server response body

	{"id":4,"userName":"中文测试-UserName-protobuf","userGroup":{"id":4,"userGroupName":"中文测试-GroupName-protobuf"}}

2.  Fetch data to redis(default Content-Type:  application/xml)
	
	http://localhost:8080/users/from-redis-protobuf/4

    Server response body

	<User><id>4</id><userName>\344\270\255\346\226\207\346\265\213\350\257\225-UserName-protobuf</userName><userGroup><id>4</id><userGroupName>\344\270\255\346\226\207\346\265\213\350\257\225-GroupName-protobuf</userGroupName></userGroup></User>

3.  Fetch data from redis as json
	
	curl -H "Accept: application/json" http://localhost:8080/users/from-redis-protobuf/4

    Server response body

	{"id": 4,"userName": "中文测试-UserName-protobuf","userGroup": {"id": 4,"userGroupName": "中文测试-GroupName-protobuf"}}

	
# License

'spring-redis-caching-with-protobuf' is distributed under the terms of the [Apache Software Foundation license, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
