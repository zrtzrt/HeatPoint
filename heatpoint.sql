CREATE DATABASE IF NOT EXISTS heatpoint 
	DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE heatpoint.location (
   `locatid` int(5) NOT NULL AUTO_INCREMENT,
   `locatname` varchar(50) DEFAULT NULL,
   `city` varchar(20) DEFAULT NULL,
   `detail` varchar(50) DEFAULT NULL,
   `longitude` double DEFAULT NULL,
   `latitude` double DEFAULT NULL,
   `province` varchar(10) DEFAULT NULL,
   PRIMARY KEY (`locatid`),
   UNIQUE KEY locatname (`locatname`)
 ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE heatpoint.othershow (
   `id` int(5) NOT NULL AUTO_INCREMENT,
   `name` varchar(200) NOT NULL,
   `picture` varchar(200) DEFAULT NULL,
   `startdate` date DEFAULT NULL,
   `enddate` date DEFAULT NULL,
   `locat` int(5) NOT NULL,
   `minprice` int(5) NOT NULL,
   `maxprice` int(5) DEFAULT NULL,
   `type` varchar(10) NOT NULL,
   `info` varchar(1000) DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `name` (`name`),
   KEY `FK_othershow` (`locat`),
   CONSTRAINT `FK_othershow` FOREIGN KEY (`locat`) 
   		REFERENCES heatpoint.location (`locatid`)
 ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE heatpoint.tradeshow (
   `id` int(5) NOT NULL AUTO_INCREMENT,
   `name` varchar(200) NOT NULL,
   `en_name` varchar(500) DEFAULT NULL,
   `picture` varchar(200) DEFAULT NULL,
   `startDate` date NOT NULL,
   `endDate` date DEFAULT NULL,
   `locat` int(5) NOT NULL,
   `industry` varchar(10) DEFAULT NULL,
   `host` varchar(500) DEFAULT NULL,
   `area` int(10) DEFAULT NULL,
   `times` int(3) DEFAULT NULL,
   `frequency` varchar(5) DEFAULT NULL,
   `used` varchar(1000) DEFAULT NULL,
   `hot` int(10) DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `name` (`name`),
   CONSTRAINT `FK_tradeshow` FOREIGN KEY (`locat`) 
   		REFERENCES heatpoint.location (`locatid`)
 ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE heatpoint.url (
   `urlid` int(10) NOT NULL AUTO_INCREMENT,
   `url` varchar(100) NOT NULL,
   `tradeid` int(2) DEFAULT NULL,
   `showid` int(5) DEFAULT NULL,
   `showtype` int(11) NOT NULL,
   PRIMARY KEY (`urlid`),
   UNIQUE KEY `url` (`url`),
   KEY `FK_url` (`tradeid`),
   KEY `FK_url_other` (`showid`),
   CONSTRAINT `FK_url` FOREIGN KEY (`tradeid`) 
   		REFERENCES heatpoint.tradeshow (`id`),
   CONSTRAINT `FK_url_other` FOREIGN KEY (`showid`) 
   		REFERENCES heatpoint.othershow (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE heatpoint.weatheralarm (
   `id` int(10) NOT NULL AUTO_INCREMENT,
   `color` varchar(10) DEFAULT NULL,
   `type` varchar(10) DEFAULT NULL,
   `locat` varchar(50) DEFAULT NULL,
   `file` varchar(50) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE VIEW heatpoint.oshow AS (
select
  `l`.`locatid`   AS `locatid`,
  `l`.`locatname` AS `locatname`,
  `l`.`city`      AS `city`,
  `l`.`detail`    AS `detail`,
  `l`.`longitude` AS `longitude`,
  `l`.`latitude`  AS `latitude`,
  `l`.`province`  AS `province`,
  `s`.`id`        AS `id`,
  `s`.`name`      AS `name`,
  `s`.`picture`   AS `picture`,
  `s`.`startdate` AS `startdate`,
  `s`.`enddate`   AS `enddate`,
  `s`.`locat`     AS `locat`,
  `s`.`minprice`  AS `minprice`,
  `s`.`maxprice`  AS `maxprice`,
  `s`.`type`      AS `type`,
  `s`.`info`      AS `info`,
  `u`.`urlid`     AS `urlid`,
  `u`.`url`       AS `url`,
  `u`.`tradeid`   AS `tradeid`,
  `u`.`showid`    AS `showid`,
  `u`.`showtype`  AS `showtype`
from ((heatpoint.location `l`
    join heatpoint.othershow `s`)
   join heatpoint.url `u`)
where ((`l`.`locatid` = `s`.`locat`)
            and (`s`.`id` = `u`.`showid`)));

CREATE VIEW heatpoint.trade AS (
select
  `l`.`locatid`   AS `locatid`,
  `l`.`locatname` AS `locatname`,
  `l`.`city`      AS `city`,
  `l`.`detail`    AS `detail`,
  `l`.`longitude` AS `longitude`,
  `l`.`latitude`  AS `latitude`,
  `l`.`province`  AS `province`,
  `t`.`id`        AS `id`,
  `t`.`name`      AS `name`,
  `t`.`en_name`   AS `en_name`,
  `t`.`picture`   AS `picture`,
  `t`.`startDate` AS `startDate`,
  `t`.`endDate`   AS `endDate`,
  `t`.`locat`     AS `locat`,
  `t`.`industry`  AS `industry`,
  `t`.`host`      AS `host`,
  `t`.`hot`       AS `hot`,
  `t`.`area`      AS `area`,
  `t`.`times`     AS `times`,
  `t`.`frequency` AS `frequency`,
  `t`.`used`      AS `used`,
  `u`.`urlid`     AS `urlid`,
  `u`.`url`       AS `url`,
  `u`.`tradeid`   AS `tradeid`,
  `u`.`showtype`  AS `showtype`
from ((heatpoint.location `l`
    join heatpoint.tradeshow `t`)
   join heatpoint.url `u`)
where ((`l`.`locatid` = `t`.`locat`)
            and (`t`.`id` = `u`.`tradeid`)));