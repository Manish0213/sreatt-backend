-- =============================
-- MASTER TABLES
-- =============================

CREATE TABLE IF NOT EXISTS brand (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS battery_chemistry (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS vehicle_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- =============================
-- USER TABLE
-- =============================

CREATE TABLE IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) UNIQUE,
    role VARCHAR(50) NOT NULL
);

-- =============================
-- DISTRIBUTOR
-- =============================

CREATE TABLE IF NOT EXISTS distributor (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    dealer_code VARCHAR(255) NOT NULL UNIQUE,
    shop_name VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

    CONSTRAINT fk_distributor_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE
);

-- =============================
-- PRODUCT
-- =============================

CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    serial_no VARCHAR(100) UNIQUE NOT NULL,

    brand_id BIGINT NOT NULL,
    battery_chemistry_id BIGINT NOT NULL,

    voltage DOUBLE PRECISION
    cca INTEGER,
    amp_hours INTEGER,
    reserve_capacity INTEGER,
    warranty_months INTEGER,

    /* price NUMERIC(12,2) NOT NULL DEFAULT 0.00, */
    price DOUBLE PRECISION NOT NULL DEFAULT 0.00,
    stock INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product_brand
        FOREIGN KEY (brand_id)
        REFERENCES brand(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_product_chemistry
        FOREIGN KEY (battery_chemistry_id)
        REFERENCES battery_chemistry(id)
);

-- =============================
-- PRODUCT - VEHICLE TYPE (M:N)
-- =============================

CREATE TABLE IF NOT EXISTS product_vehicle_type (
    product_id BIGINT NOT NULL,
    vehicle_type_id BIGINT NOT NULL,

    PRIMARY KEY (product_id, vehicle_type_id),

    CONSTRAINT fk_pvt_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pvt_vehicle_type
        FOREIGN KEY (vehicle_type_id)
        REFERENCES vehicle_type(id)
        ON DELETE CASCADE
);

-- =============================
-- PRODUCT IMAGE
-- =============================

CREATE TABLE IF NOT EXISTS product_image (
    product_id BIGINT NOT NULL,
    image_url TEXT NOT NULL,

    PRIMARY KEY (product_id, image_url),

    CONSTRAINT fk_product_image_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE CASCADE
);

-- =============================
-- WARRANTY
-- =============================

CREATE TABLE IF NOT EXISTS warranty (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    distributor_id BIGINT NOT NULL,
    product_id BIGINT UNIQUE,

    dealer_code VARCHAR(255) NOT NULL,
    purchase_date DATE,
    serial_no VARCHAR(255) UNIQUE,

    city VARCHAR(150),
    state VARCHAR(150),
    area VARCHAR(150),

    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_warranty_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_warranty_distributor
        FOREIGN KEY (distributor_id)
        REFERENCES distributor(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_warranty_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
        ON DELETE SET NULL
);