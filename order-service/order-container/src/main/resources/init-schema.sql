------------------ ORDER SCHEMA ------------------
DROP SCHEMA IF EXISTS orders CASCADE;

CREATE SCHEMA orders;

------------------ UUID-OSSP EXTENSION ------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

------------------ ORDER_STATUS DATA TYPE ------------------
DROP TYPE IF EXISTS order_status;

CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING');

-- Fix ERROR: column "order_status" is of type order_status but expression is of type character varying
CREATE CAST (varchar AS order_status) WITH INOUT AS IMPLICIT;

------------------ ORDER TABLE ------------------
DROP TABLE IF EXISTS orders.shc_order CASCADE;

CREATE TABLE orders.shc_order (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    tracking_id uuid NOT NULL,
    price numeric(10, 2) NOT NULL,
    order_status order_status NOT NULL,
    failure_messages character varying COLLATE pg_catalog."default",
    CONSTRAINT shc_order_pkey PRIMARY KEY (id)
);

------------------ ORDER_ITEM TABLE ------------------
DROP TABLE IF EXISTS orders.shc_order_item CASCADE;

CREATE TABLE orders.shc_order_item (
    id bigint NOT NULL,
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    price numeric(10, 2) NOT NULL,
    quantity integer NOT NULL,
    sub_total numeric(10, 2) NOT NULL,
    CONSTRAINT shc_order_item_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE orders.shc_order_item
    ADD CONSTRAINT "FK_SHC_ORDER_ID" FOREIGN KEY (order_id)
    REFERENCES orders.shc_order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;

------------------ ORDER_ADDRESS TABLE ------------------
DROP TABLE IF EXISTS orders.shc_order_address CASCADE;

CREATE TABLE orders.shc_order_address (
    id uuid NOT NULL,
    order_id uuid UNIQUE NOT NULL,
    street character varying COLLATE pg_catalog."default" NOT NULL,
    postal_code character varying COLLATE pg_catalog."default" NOT NULL,
    city character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT shc_order_address_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE orders.shc_order_address
    ADD CONSTRAINT "FK_SHC_ORDER_ID" FOREIGN KEY (order_id)
    REFERENCES orders.shc_order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;