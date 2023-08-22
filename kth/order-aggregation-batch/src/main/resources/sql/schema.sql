CREATE TABLE `online_retail_order` (
  `id` binary(16) NOT NULL,
  `invoice_no` varchar(31) NOT NULL,
  `stock_code` varchar(31) NOT NULL,
  `description` varchar(127) DEFAULT NULL,
  `quantity` int NOT NULL,
  `invoice_date` datetime DEFAULT NULL,
  `unit_price` float NOT NULL,
  `customer_id` bigint DEFAULT NULL,
  `country` varchar(31) DEFAULT NULL,
  `recorded_date` varchar(31) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  INDEX `online_retail_order_cid` (`customer_id`)
);