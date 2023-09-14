# dijkstra
simple impl of dijkstra pathfinder alg on jdk 17+

command line includes 
<source_vertex_name> <target_vertex name>_<edge triplet>
where <edge_triplet> ::= <from_vertex_name> "," <to_vertex_name> "," <weight>

```bash
java  -jar build/libs/app.jar V1 V4 V1,V2,3 V1,V3,2 V2,V3,1 V3,V4,4
```
