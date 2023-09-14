# dijkstra
simple impl of dijkstra pathfinder alg on jdk 17+

command line 
```bnf
<cmd> ::= <source_vertex_name> <target_vertex name> <edge_triplet>*
<edge_triplet> ::= <from_vertex_name> "," <to_vertex_name> "," <weight>
```

# build
```bash
./gradlew app:jar
```
# test
example graph

![example graph](./Drawing.png)

# run
```bash
java  -jar app/build/libs/app.jar V1 V4 V1,V2,3 V1,V3,2 V2,V3,1 V2,V4,2 V3,V4,4 
```
