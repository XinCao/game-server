CREATE TABLE IF NOT EXISTS `mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mail_box_id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `content` varchar(256) NOT NULL,
  `create_at` date NOT NULL,
  `status` enum('1','2','3') NOT NULL DEFAULT '1' COMMENT '1:"not read", 2:"read", 3:"delete"',
  PRIMARY KEY (`id`),
  KEY `mail_box_id` (`mail_box_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `mail_box` (
  `id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  `capacity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(256) NOT NULL,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account` (`account`(255)),
  KEY `name` (`name`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;