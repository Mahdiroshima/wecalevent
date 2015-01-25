drop schema if exists wecalevent;

create schema wecalevent;

use wecalevent;

ALTER DATABASE wecalevent DEFAULT CHARACTER SET utf8;

create table User (
	user_id int not null auto_increment,
    email varchar(255) not null,
    name varchar(100) not null,
    surname varchar(100) not null,
    pass varchar(100) not null,
    calendar varchar(10) not null,
    unique (email),
    primary key (user_id)    
);

insert into User values(
	DEFAULT,
    'admin',
    'Admin',
    'Master',
    '1234',
    'public'
);

create table Weather (
	weather_id int not null auto_increment,
    weather_date DateTime not null,
    city varchar(100) not null,
    weather_condition varchar(10),
    primary key (weather_id)    
);

create table Event (
	event_id int not null auto_increment,
    event_name varchar(100) not null,
    event_description text not null,
    event_type varchar(10) not null,
    desired_weather varchar(100) not null,
    visibility varchar(100) not null,
    location_city varchar(100) not null,
    starting_date DateTime not null,
    ending_date DateTime not null,
    creator_id int not null,
    weather_id int not null,
    primary key (event_id),
    foreign key (creator_id) references User (user_id),
    foreign key (weather_id) references Weather (weather_id)
);

create table participate (
	event_id int not null,
    user_id int not null,
    foreign key (event_id) references Event (event_id) ON DELETE CASCADE,
    foreign key (user_id) references User (user_id)
);

create table intived (
	event_id int not null,
    user_id int not null,
    foreign key (event_id) references Event (event_id) ON DELETE CASCADE,
    foreign key (user_id) references User (user_id)
);

create table notification (
	notif_id int not null auto_increment,
    notif_type int not null, #0 invitation, 1 forecast_change, 2 postpone_suggestion
	user_id int not null,
    related_to int not null,
    notification text not null,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (notif_id),
    foreign key (user_id) references User (user_id),
    foreign key (related_to) references Event (event_id) ON DELETE CASCADE
);
