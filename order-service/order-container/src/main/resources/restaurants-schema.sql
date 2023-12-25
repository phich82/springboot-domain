DROP SCHEMA IF EXISTS restaurants CASCADE;

CREATE SCHEMA restaurants;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS restaurants.shc_restaurant CASCADE;

CREATE TABLE restaurants.shc_restaurant (
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    active boolean NOT NULL,
    CONSTRAINT shc_restaurant_pkey PRIMARY KEY (id)
);

DROP TYPE IF EXISTS approval_status;

CREATE TYPE approval_status AS ENUM ('APPROVED', 'REJECTED');

DROP TABLE IF EXISTS restaurants.shc_order_approval CASCADE;

CREATE TABLE restaurants.shc_order_approval (
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    order_id uuid NOT NULL,
    status approval_status NOT NULL,
    CONSTRAINT shc_order_approval_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restaurants.shc_product CASCADE;

CREATE TABLE restaurants.shc_product (
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    price numeric(10, 2) NOT NULL,
    available boolean NOT NULL,
    CONSTRAINT shc_product_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restaurants.shc_restaurant_product CASCADE;

CREATE TABLE restaurants.shc_restaurant_product (
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT shc_restaurant_product_pkey PRIMARY KEY (id)
);

ALTER TABLE restaurants.shc_restaurant_product
    ADD CONSTRAINT "FK_RESTAURANT_ID" FOREIGN KEY (restaurant_id)
    REFERENCES restaurants.shc_restaurant (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

ALTER TABLE restaurants.shc_restaurant_product
    ADD CONSTRAINT "FK_SHC_PRODUCT_ID" FOREIGN KEY (product_id)
    REFERENCES restaurants.shc_product (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

DROP MATERIALIZED VIEW IF EXISTS restaurants.shc_order_restaurant_m_view;

CREATE MATERIALIZED VIEW restaurants.shc_order_restaurant_m_view
TABLESPACE pg_default
AS
 SELECT r.id AS restaurant_id,
    r.name AS restaurant_name,
    r.active AS restaurant_active,
    p.id AS product_id,
    p.name AS product_name,
    p.price AS product_price,
    p.available AS product_available
   FROM restaurants.shc_restaurant r,
    restaurants.shc_product p,
    restaurants.shc_restaurant_product rp
  WHERE r.id = rp.restaurant_id AND p.id = rp.product_id
WITH DATA;

refresh materialized VIEW restaurants.shc_order_restaurant_m_view;

DROP function IF EXISTS restaurants.shc_refresh_order_restaurant_m_view;

CREATE OR replace function restaurants.shc_refresh_order_restaurant_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW restaurants.shc_order_restaurant_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS shc_refresh_order_restaurant_m_view ON restaurants.shc_restaurant_product;

CREATE trigger shc_refresh_order_restaurant_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON restaurants.shc_restaurant_product FOR each statement
EXECUTE PROCEDURE restaurants.shc_refresh_order_restaurant_m_view();