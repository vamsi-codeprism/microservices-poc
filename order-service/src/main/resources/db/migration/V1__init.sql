CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        product_id BIGINT NOT NULL,
                        quantity INT NOT NULL,
                        total_price DOUBLE PRECISION NOT NULL
);
