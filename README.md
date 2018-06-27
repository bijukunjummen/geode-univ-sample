```bash
gfsh

# Run the following commands on the gfsh command line
 
start locator --name=loc1 --bind-address=127.0.0.1
configure pdx --read-serialized=true --disk-store=DEFAULT
start server --name=server1 --server-bind-address=127.0.0.1 --bind-address=127.0.0.1
start server --name=server2 --server-bind-address=127.0.0.1 --bind-address=127.0.0.1 --server-port=40405
create region --name=teachers --type=PARTITION_REDUNDANT_PERSISTENT
create region --name=courses --type=PARTITION_REDUNDANT_PERSISTENT
```


### Start the application
```bash
./gradlew bootRun
```

### Get all Teachers

```bash
http http://localhost:8080/teachers
```

### Create a teacher
```bash
http http://localhost:8080/teachers teacher-id=new-id-1 name="test teacher 1" department="mathematics"
```

### Get a teacher
```bash
http http://localhost:8080/teachers/new-id-1
```


### Update a teacher
```bash
http PUT http://localhost:8080/teachers/new-id-1 name="teacher updated - 1" department="new department"
```

### Delete a teacher
```bash
http DELETE http://localhost:8080/teachers/new-id-1
```

### Getting all Courses

```bash
http http://localhost:8080/courses
```

### Create a Course
```bash
http http://localhost:8080/courses course-code="some-code" name="some awesome course" description="some description" teacher-id="id-1"
```

### Get a Course
```bash
http http://localhost:8080/course/some-code
```


### Update a Course
```bash
http PUT http://localhost:8080/course/some-code name="some updated awesome course" description="some description"
```

### Delete a Course
```bash
http DELETE http://localhost:8080/course/some-code
```