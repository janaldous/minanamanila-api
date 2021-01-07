--liquibase formatted sql
--changeset janaldous:colors-1 

INSERT INTO product_color(id, color)
VALUES 	(1, 'Space Gray'),
		(2, 'Silver'),
		(3, 'Gold');

INSERT INTO product_colors(products_id, colors_id)
VALUES 	(1, 1),
		(1, 2),
		(1, 3);
		
--rollback DELETE FROM product_color WHERE color = 'Space Gray' or 'Silver' or 'Gold';

--changeset janaldous:colors-2

INSERT INTO product_color(id, color)
VALUES 	(4, 'Red'),
		(5, 'Black'),
		(6, 'White');
		
INSERT INTO product_colors(products_id, colors_id)
VALUES 	(2, 1),
		(2, 2),
		(2, 3),
		(3, 1),
		(3, 2),
		(4, 1),
		(4, 2),
		(22, 4),
		(22, 5),
		(22, 6),
		(23, 4),
		(23, 5),
		(23, 6),
		(24, 4),
		(24, 5),
		(24, 6),
		(25, 3);