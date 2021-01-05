# Postgres local set up

Create a new database called `minanamanila`.

```
CREATE DATABASE minanamanila;
```

# Useful postgres commands

List databases

```
\l
```

Connect to a database

```
\c <database name>
```

Quit

```
\q
```

Insert new row with sequence

```
insert into biz_term(
  biz_term_id, 
  biz_term_name, 
) 
values(
 nextval('idsequence'),
 'temp'
);
```

Docker cheatsheet
[https://www.docker.com/sites/default/files/d8/2019-09/docker-cheat-sheet.pdf](here)