-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema softwaresmartphone
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema softwaresmartphone
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `softwaresmartphone` DEFAULT CHARACTER SET latin1 ;
USE `softwaresmartphone` ;

-- -----------------------------------------------------
-- Table `softwaresmartphone`.`Categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `softwaresmartphone`.`Categorias` (
  `idCategorias` INT(11) NOT NULL AUTO_INCREMENT,
  `sexo` TINYINT(1) NOT NULL DEFAULT '0',
  `categoria` VARCHAR(100) NOT NULL DEFAULT 'adulto',
  `tamanho` VARCHAR(45) NOT NULL DEFAULT 'M',
  PRIMARY KEY (`idCategorias`),
  UNIQUE INDEX `idCategorias_UNIQUE` (`idCategorias` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 31
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `softwaresmartphone`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `softwaresmartphone`.`Cliente` (
  `idCliente` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `cpf` VARCHAR(100) NOT NULL DEFAULT '0',
  `telefone` VARCHAR(100) NULL DEFAULT '0',
  PRIMARY KEY (`idCliente`),
  UNIQUE INDEX `idCliente_UNIQUE` (`idCliente` ASC),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `softwaresmartphone`.`Produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `softwaresmartphone`.`Produto` (
  `idProduto` INT(11) NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL DEFAULT NULL,
  `quantidade` INT(11) NOT NULL,
  `preco` DECIMAL(10,0) NOT NULL DEFAULT '0',
  `imagem` VARCHAR(200) NULL DEFAULT NULL,
  `Categorias_idCategorias` INT(11) NOT NULL,
  PRIMARY KEY (`idProduto`, `Categorias_idCategorias`),
  UNIQUE INDEX `idProduto_UNIQUE` (`idProduto` ASC),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC),
  INDEX `fk_Produto_Categorias1_idx` (`Categorias_idCategorias` ASC),
  CONSTRAINT `fk_Produto_Categorias1`
    FOREIGN KEY (`Categorias_idCategorias`)
    REFERENCES `softwaresmartphone`.`Categorias` (`idCategorias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 37
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `softwaresmartphone`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `softwaresmartphone`.`venda` (
  `create_time` TIMESTAMP NULL DEFAULT NULL,
  `idvenda` INT(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idvenda`),
  UNIQUE INDEX `idvenda_UNIQUE` (`idvenda` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `softwaresmartphone`.`venda_prod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `softwaresmartphone`.`venda_prod` (
  `idvenda_prod` INT(11) NOT NULL AUTO_INCREMENT,
  `Cliente_idCliente` INT(11) NOT NULL,
  `Produto_idProduto` INT(11) NOT NULL,
  `venda_idvenda` INT(11) NOT NULL,
  `quantidade` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idvenda_prod`, `Cliente_idCliente`, `Produto_idProduto`, `venda_idvenda`),
  UNIQUE INDEX `idvenda_prod_UNIQUE` (`idvenda_prod` ASC),
  INDEX `fk_venda_prod_Cliente_idx` (`Cliente_idCliente` ASC),
  INDEX `fk_venda_prod_Produto1_idx` (`Produto_idProduto` ASC),
  INDEX `fk_venda_prod_venda1_idx` (`venda_idvenda` ASC),
  CONSTRAINT `fk_venda_prod_Cliente`
    FOREIGN KEY (`Cliente_idCliente`)
    REFERENCES `softwaresmartphone`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_prod_Produto1`
    FOREIGN KEY (`Produto_idProduto`)
    REFERENCES `softwaresmartphone`.`Produto` (`idProduto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_venda_prod_venda1`
    FOREIGN KEY (`venda_idvenda`)
    REFERENCES `softwaresmartphone`.`venda` (`idvenda`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
