 CREATE DATABASE chatty;
USE chatty;
CREATE TABLE users(
	user_id INT primary key auto_increment,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null DEFAULT 'user@mail.com',
    gender int not null,
    country varchar(30) not null default 'Egypt',
    password varchar(120) not null,
	phone varchar(15) unique not null,
	image_uri varchar(100),
	bio varchar(250) DEFAULT 'Bio.....',
	birthDate varchar(60) DEFAULT "1993-10-10",
    isServer int not null DEFAULT 0
);

CREATE TABLE friend_requests(
	sender_id INT not null,
    receiver_id INT not null,
    status int,
    primary key (sender_id, receiver_id),
    foreign key (sender_id) references users(user_id) ON DELETE CASCADE,
    foreign key (receiver_id) references users(user_id) ON DELETE CASCADE
);

CREATE TABLE chat_rooms(
	chat_room_id int primary key auto_increment
);

CREATE TABLE users_chat_rooms(
	chat_room_id int,
    user_id int,
    primary key (chat_room_id, user_id),
    foreign key (chat_room_id) references chat_rooms(chat_room_id) ON DELETE CASCADE,
    foreign key (user_id) references users(user_id) ON DELETE CASCADE
);

CREATE TABLE messages(
	message_id int primary key auto_increment,
	message_type int not null,
    sender_id int not null,
    chat_room_id int not null,
    content varchar(500) not null,
    foreign key (sender_id) references users(user_id) ON DELETE CASCADE,
    foreign key (chat_room_id) references chat_rooms(chat_room_id) ON DELETE CASCADE
);

CREATE TABLE notifications(
	notification_id int primary key auto_increment,
    notification_type int not null,
    source_id int,
    receiver_id int not null,
    notification_status int not null,
    foreign key (source_id) references users(user_id) ON DELETE CASCADE,
    foreign key (receiver_id) references users(user_id) ON DELETE CASCADE
    
);

