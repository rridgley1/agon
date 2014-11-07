CREATE KEYSPACE agon with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 3 };

CREATE TABLE actions
(
  player_id int,
  event varchar,
  event_type varchar,
  event_time timeuuid,
  value varchar,
  primary key (player_id, event_time, event)
);

CREATE TABLE player_event_counts
(
  counter_value counter,
  player_id int,
  event varchar,
  PRIMARY KEY (player_id, event)
);

CREATE TABLE badges
(
  id UUID,
  parent UUID,
  name varchar,
  url varchar,
  description varchar,
  retired boolean,
  primary key (id, name)
);

CREATE TABLE badge_events
(
  event varchar,
  count int,
  badge_id UUID,
  primary key (badge_id, event)
);

CREATE INDEX evt ON badge_events(event);

CREATE TABLE player_badges
(
  player_id int,
  badge_id UUID,
  unlocked timestamp,
  primary key (player_id, badge_id)
);

insert into badges (id, name, url, retired) values (uuid(), 'max bet badge', '/images/badge.jpg',false);

insert into badge_events (event, count, badge_id) values('MAX_BET', 10, 'ccf0270f-16a5-44b1-b98c-66add2ae55f2');
