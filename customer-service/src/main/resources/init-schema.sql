DROP SCHEMA IF EXISTS "customers" CASCADE;

CREATE SCHEMA "customers";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "customers".shc_customer CASCADE;

CREATE TABLE "customers".shc_customer (
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT shc_customer_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customers.shc_order_customer_m_view;

CREATE MATERIALIZED VIEW customers.shc_order_customer_m_view
TABLESPACE pg_default
AS
    SELECT
        id,
        username,
        first_name,
        last_name
    FROM customers.shc_customer
WITH DATA;

refresh materialized VIEW customers.shc_order_customer_m_view;

DROP function IF EXISTS customers.shc_refresh_order_customer_m_view;

CREATE OR REPLACE function customers.shc_refresh_order_customer_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW customers.shc_order_customer_m_view;
    return null;
END;
' LANGUAGE plpgsql;

DROP trigger IF EXISTS shc_refresh_order_customer_m_view ON customers.shc_customer;

CREATE trigger shc_refresh_order_customer_m_view
after INSERT OR UPDATE OR DELETE OR TRUNCATE
ON customers.shc_customer FOR EACH STATEMENT
EXECUTE PROCEDURE customers.shc_refresh_order_customer_m_view();

