package com.example.productservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@Entity
public class productModel extends BaseModel{

    private String title;
    private String description;
    @Column(name = "price_minor", nullable = false)
    private Long priceMinor;
    @Column(nullable = false, length = 3)
    private String currency = "INR";
    @Version
    private Long version;
    @ManyToOne (cascade = {CascadeType.PERSIST})
    private Category category;
    private String ImageURL;

    /**
     * Temporary compatibility view for clients still sending decimal prices.
     * priceMinor is the persisted and authoritative money value.
     */
    public Double getPrice() {
        return priceMinor == null ? null : BigDecimal.valueOf(priceMinor, 2).doubleValue();
    }

    public void setPrice(Double price) {
        this.priceMinor = price == null ? null
                : BigDecimal.valueOf(price).movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }
}
