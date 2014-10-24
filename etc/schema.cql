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
  name varchar,
  url varchar,
  description varchar,
  retired boolean,
  primary key (id, name)
);

CREATE TABLE player_badges
(
  player_id int,
  badge_id UUID,
  unlocked timestamp,
  primary key (player_id, badge_id)
);