INSERT INTO client (client_id,client_name) VALUES('C0001','sampleco');
INSERT INTO client (client_id,client_name) VALUES('C0002','sampleco2');



INSERT INTO product(product_id,product_code) VALUES ('P0001','ABCDEFG');
INSERT INTO product(product_id,product_code) VALUES ('P0002','1234567');
INSERT INTO product(product_id,product_code) VALUES ('P0003','あいうえお');
INSERT INTO product(product_id,product_code) VALUES ('P0004','かきくけこ');
INSERT INTO product(product_id,product_code) VALUES ('P0005','さしすせそ');
INSERT INTO product(product_id,product_code) VALUES ('P0006','たちつてと');

INSERT INTO stock (stock_id,product_id,actual_num,future_num,standard_num) VALUES('S0001','P0001',10,10,5);
INSERT INTO stock (stock_id,product_id,actual_num,future_num,standard_num) VALUES('S0002','P0002',10,10,5);
INSERT INTO stock (stock_id,product_id,actual_num,future_num,standard_num) VALUES('S0003','P0003',10,10,5);