package uj.wmii.pwj.w7.insurance;

import java.math.BigDecimal;

public class InsuranceEntry {
    private final String[] details;

    InsuranceEntry(String line) {
        details = line.split(",");
    }

    public String getCountry() {
        return details[2];
    }

    public BigDecimal getTiv_2012() {
        double value = 0;
        try {
            value = Double.parseDouble(details[8]);
        } catch (Exception e) {
            System.out.println("Cannot parse to double");
        }
        return BigDecimal.valueOf(value);
    }

    public BigDecimal getTiv_2011() {
        double value = 0;
        try {
            value = Double.parseDouble(details[7]);
        } catch (Exception e) {
            System.out.println("Cannot parse to double");
        }
        return BigDecimal.valueOf(value);
    }
}
