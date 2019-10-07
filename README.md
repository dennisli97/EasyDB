# EasyDB
A Simple Database System
This Porject is an implementation of a database system that applies colum store, left deep join order and hash join.
---
The speed of query processing is optimized with following steps
1. Left deep join tree to allow pipeline processing
2. Column Stored data to exclude unessesary columns while joining
3. Simple cardinanity prediction to arrange join orders and ensure minimal data passing to next level
4. In memory hash join to ensure fast look up for matching colums

