# LTC Sponsor Rally

A tool for managing sponsor rallys.

## recommended Postgres setup

```bash
docker run --name rallydb -p 5432:5432 -e POSTGRES_PASSWORD=rally -e POSTGRES_USER=rally -d postgres:11
```

## manual postgres setup

```sql
create user rally with password 'rally';
create database rally;
grant all privileges on database rally to rally;
```
