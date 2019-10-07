# EasyDB
A Simple Database System
---
This project is an implementation of a database system that applies colum store, left deep join order and hash join.

The speed of query processing is optimized with the following steps:
1. Left deep join tree to allow pipeline processing
2. Column stored data to exclude unnecessary columns while processing 
3. Simple cardinality prediction to arrange join orders and ensure minimal data passing to next level
4. In memory hash join to provide efficient lookup for matching columns

