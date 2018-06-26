```bash
gfsh

gfsh> start locator --name=loc1 --bind-address=127.0.0.1
gfsh> configure pdx --read-serialized=true --disk-store=DEFAULT
gfsh> start server --name=server1 --server-bind-address=127.0.0.1 --bind-address=127.0.0.1
gfsh> start server --name=server2 --server-bind-address=127.0.0.1 --bind-address=127.0.0.1 --server-port=40405
gfsh> create region --name=teachers --type=PARTITION_PERSISTENT
gfsh> create region --name=courses --type=PARTITION_PERSISTENT
```


### Start the application
```bash
./gradlew bootRun
```

### Getting a list of Teachers

```bash
http http://localhost:8080/teachers
```

### Create a teacher
```bash
http http://localhost:8080/teachers name="test teacher 1" department="mathematics"
```

### Get a teacher
```bash
http http://localhost:8080/teachers/id-1
```


### Update a teacher
```bash
http PUT http://localhost:8080/teachers/id-1 name="teacher updated - 1" department="new department"
```

### Delete a teacher
```bash
http DELETE http://localhost:8080/teachers/id-1
```
