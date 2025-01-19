package com.johnboscoltd.enums;

public enum Product {

    Standard("10"),
    Classic("20"),
    Premium("40");


    private final String value;

    Product(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Static method to convert a string to a Product enum
    public static Product fromString(String productName) {
        for (Product product : Product.values()) {
            if (product.name().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        throw new IllegalArgumentException("Invalid product name: " + productName);
    }
}
