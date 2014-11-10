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

CREATE INDEX badge_name ON badges(name);

insert into badges (id, name, url, retired) values (uuid(), 'testBadge', '/images/badge.jpg',false);