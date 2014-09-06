<?php

$conn_string = "host=ec2-54-243-51-102.compute-1.amazonaws.com port=5432 dbname=dcp9q7v46t4do user=yqpidwymdeqdkz password=D-nib34Pj2J0yS7PrPBAj3Skz6";
$dbconn = pg_connect($conn_string);


$create = "CREATE TABLE article_log(
	id SERIAL PRIMARY KEY,
    title VARCHAR (200) NOT NULL,
    author VARCHAR (50) NOT NULL,
    pub_date TIMESTAMP (8) NOT NULL,
    created_date TIMESTAMP (8) NOT NULL,
    source VARCHAR (50) NOT NULL,
    url VARCHAR (200) NOT NULL,
    body TEXT NOT NULL
);";