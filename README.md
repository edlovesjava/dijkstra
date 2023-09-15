# dijkstra
simple impl of dijkstra pathfinder alg on jdk 17+

command line 
```bnf
<cmd> ::= <source_node_id> <target_node_id> <edge_triplet>*
<edge_triplet> ::= <from_noe_id> "," <to_node_id> "," <weight>
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
