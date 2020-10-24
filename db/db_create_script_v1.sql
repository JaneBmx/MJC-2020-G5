CREATE SCHEMA IF NOT EXISTS `certificates` DEFAULT CHARACTER SET utf8 ;
USE `certificates` ;

CREATE TABLE IF NOT EXISTS `certificates`.`tag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);


CREATE TABLE IF NOT EXISTS `certificates`.`gift_certificate` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `price` DECIMAL NOT NULL,
  `create_date` TIMESTAMP NOT NULL,
  `last_update_date` TIMESTAMP NOT NULL,
  `duration` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `int_UNIQUE` (`id` ASC) VISIBLE);


CREATE TABLE IF NOT EXISTS `certificates`.`gift_certificate2tag` (
  `gift_sertificate_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  INDEX `gift_certificate_fk_idx` (`gift_sertificate_id` ASC) VISIBLE,
  INDEX `tag_id_fk_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `gift_certificate_id_fk`
    FOREIGN KEY (`gift_sertificate_id`)
    REFERENCES `certificates`.`gift_certificate` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_id_fk`
    FOREIGN KEY (`tag_id`)
    REFERENCES `certificates`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);