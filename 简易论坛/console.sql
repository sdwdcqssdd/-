create table author(
  id varchar not null unique primary key,
  name varchar,
  registration_time timestamp not null,
  phone varchar
);

create table post (
    id int unique primary key,
    author varchar,
    foreign key (author) references author(name),
    title varchar not null,
    content varchar not null,
    post_time timestamp,
    post_city varchar
);

create table author_followed (
    id serial primary key,
    author_id varchar not null,
    foreign key (author_id) references author(id),
    followed_id varchar not null
);

create table author_favour_post (
    id serial primary key,
    post_id int not null ,
    foreign key (post_id) references post(id),
    author_id varchar not null
);

create table author_like_post (
    id serial primary key,
    post_id int not null ,
    foreign key (post_id) references post(id),
    author_id varchar not null
);

create table post_category(
    id serial primary key,
    post_id int not null,
    foreign key (post_id) references post(id),
    category varchar not null
);

create table author_shared(
    id serial primary key,
    post_id int not null,
    foreign key (post_id) references post(id),
    author_id varchar not null
);

create table reply(
    id serial primary key,
    post_id int not null,
    foreign key (post_id) references post(id),
    content varchar not null,
    star int,
    author_id varchar
);

create table second_reply(
    id serial primary key,
    reply_id int not null,
    foreign key (reply_id) references reply(id),
    content varchar not null,
    star int,
    author_id varchar
);



