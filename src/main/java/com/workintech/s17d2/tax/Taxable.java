package com.workintech.s17d2.tax;

import org.springframework.stereotype.Repository;

@Repository
public interface Taxable {
    double getSimpleTaxRate();

    double getMiddleTaxRate();

    double getUpperTaxRate();
}
