create TABLE `online_retail_order` (
  `invoice_no` VARCHAR(31) NOT NULL,
  `stock_code` VARCHAR(31) NOT NULL,
  `description` VARCHAR(127) NULL,
  `quantity` INT NOT NULL,
  `invoice_date` DATETIME NULL,
  `unit_price` FLOAT NOT NULL,
  `customer_id` BIGINT(20) NULL,
  `country` VARCHAR(31) NULL,
  PRIMARY KEY (`invoice_no`),
  UNIQUE INDEX `idonline_retail_UNIQUE` (`invoice_no` ASC) VISIBLE);
