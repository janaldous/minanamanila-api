--liquibase formatted sql
--changeset janaldous:colors-1 

INSERT INTO product_color(id, color)
VALUES 	(NEXTVAL('hibernate_sequence'), 'Space Gray'),
		(NEXTVAL('hibernate_sequence'), 'Silver'),
		(NEXTVAL('hibernate_sequence'), 'Gold');

INSERT INTO product_colors(products_id, colors_id)
VALUES 	(1, 1),
		(1, 2),
		(1, 3);
		
--rollback DELETE FROM product_color WHERE color = 'Space Gray' or 'Silver' or 'Gold';