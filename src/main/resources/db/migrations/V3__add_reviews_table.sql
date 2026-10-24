CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating DECIMAL(3, 1) NOT NULL,
    comment TEXT,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_review_order FOREIGN KEY (order_id) REFERENCES orders(id)
);