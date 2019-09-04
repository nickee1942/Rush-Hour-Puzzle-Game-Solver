# Rush-Hour-Puzzle-Game-Solver
##  Instructions
### ![](https://github.com/nickee1942/Rush-Hour-Puzzle-Game-Solver/blob/master/Image/map.png)  ![](https://github.com/nickee1942/Rush-Hour-Puzzle-Game-Solver/blob/master/Image/string.jpg)
#### ---Translate map to string to get shortest path solution---
* 'XX' = your car
#### * '22' = a car horizontally
#### * 'BB' = a car of the same size vertically
#### * 'CCC' = a truck vertically
#### * '333' = a truck of the same size horizontally
#### * '.' = an empty space
### Move a car from one spot to another count as only 1 move
## Algorithm Analysis
#### * Each Map is a unique graph, the number of vertices and edges may vary
#### * Use branch and bound to reduce the redundant moves from each steps, all the maps could cut some branches
#### * The shortest path (steps) is not related to how complicated the map is
### ![](https://github.com/nickee1942/Rush-Hour-Puzzle-Game-Solver/blob/master/Image/RelationReduced%26shortest.png) 
![](https://github.com/nickee1942/Rush-Hour-Puzzle-Game-Solver/blob/master/Image/WholeVSreduced.png)


