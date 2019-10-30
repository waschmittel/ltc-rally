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

## generate list for runners

```sql
select name, number_of_laps_run, bonus_laps, fastest
from runner, (select min(duration) fastest, runner_id from lap group by runner_id order by fastest asc) as lap
where lap.runner_id = runner.id;
```

## generate list for sponsor letters

```sql
select sponsor.name, street, city, country, per_lap_donation, one_time_donation, runner.name, coalesce(runner.number_of_laps_run,0)+coalesce(runner.bonus_laps,0), total_donation, runner.room_number
from sponsor, runner
where sponsor.runner_id = runner.id
order by runner.room_number asc, runner.name asc;
```
