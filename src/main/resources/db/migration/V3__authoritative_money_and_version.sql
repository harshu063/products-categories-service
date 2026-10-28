ALTER TABLE product_model
    ADD price_minor BIGINT NULL,
    ADD currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    ADD version BIGINT NOT NULL DEFAULT 0;

UPDATE product_model
SET price_minor = ROUND(COALESCE(price, 0) * 100)
WHERE price_minor IS NULL;

ALTER TABLE product_model
    MODIFY price_minor BIGINT NOT NULL;
