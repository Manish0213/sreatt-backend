-- =============================
-- BRAND MASTER DATA
-- =============================

INSERT INTO brand (name) VALUES
('Exide'),
('Amaron'),
('SF Sonic'),
('Luminous')
ON CONFLICT (name) DO NOTHING;

-- =============================
-- BATTERY CHEMISTRY MASTER DATA
-- =============================

INSERT INTO battery_chemistry (name) VALUES
('Lead Acid'),
('Lithium-Ion'),
('AGM'),
('Gel')
ON CONFLICT (name) DO NOTHING;

-- =============================
-- VEHICLE TYPE MASTER DATA
-- =============================

INSERT INTO vehicle_type (name) VALUES
('Bike'),
('Car'),
('Truck'),
('Inverter'),
('Solar')
ON CONFLICT (name) DO NOTHING;

-- ============================================
-- V3: Seed Default Users (Admin, Distributor, User)
-- ============================================

INSERT INTO app_user (name, email, password, phone_number, role)
VALUES 
('Admin', 'admin@sreatt.com', 'admin123', '9999999999', 'ADMIN'),
('Distributor', 'distributor@sreatt.com', 'distributor123', '8888888888', 'DISTRIBUTOR'),
('User', 'user@sreatt.com', 'user123', '7777777777', 'USER')
ON CONFLICT (email) DO NOTHING;