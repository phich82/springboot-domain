------------------ PAYMENTS SCHEMA ------------------
DROP SCHEMA IF EXISTS payments CASCADE;

CREATE SCHEMA payments;

------------------ UUID-OSSP EXTENSION ------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

------------------ PAYMENT_STATUS DATA TYPE ------------------
DROP TYPE IF EXISTS payment_status;

CREATE TYPE payment_status AS ENUM ('COMPLETED', 'CANCELLED', 'FAILED');

-- Fix ERROR: column "payment_status" is of type payment_status but expression is of type character varying
CREATE CAST (varchar AS payment_status) WITH INOUT AS IMPLICIT;

------------------ PAYMENT TABLE ------------------
DROP TABLE IF EXISTS payments.shc_payment CASCADE;

CREATE TABLE payments.shc_payment (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    price numeric(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status payment_status NOT NULL,
    CONSTRAINT shc_payment_pkey PRIMARY KEY (id)
);

------------------ CREDIT_ENTRY TABLE ------------------
DROP TABLE IF EXISTS payments.shc_credit_entry CASCADE;

CREATE TABLE payments.shc_credit_entry (
    id bigint NOT NULL,
    customer_id uuid NOT NULL,
    total_credit_amount numeric(10, 2) NOT NULL,
    CONSTRAINT shc_credit_entry_pkey PRIMARY KEY (id)
);

------------------ TRANSACTION_TYPE DATA TYPE ------------------
DROP TYPE IF EXISTS transaction_type;

CREATE TYPE transaction_type AS ENUM ('DEBIT', 'CREDIT');

-- Fix ERROR: column "transaction_type" is of type transaction_type but expression is of type character varying
CREATE CAST (varchar AS transaction_type) WITH INOUT AS IMPLICIT;

------------------ CREDIT_HISTORY TABLE ------------------
DROP TABLE IF EXISTS payments.shc_credit_history CASCADE;

CREATE TABLE payments.shc_credit_history(
    id uuid NOT NULL,
    customer_id uuid UNIQUE NOT NULL,
    amount numeric(10, 2) NOT NULL,
    type transaction_type NOT NULL,
    CONSTRAINT shc_credit_history_pkey PRIMARY KEY (id)
);
