package uj.wmii.pwj.w7.insurance;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FloridaInsurance {

    public static void main(String[] args) {
        List<InsuranceEntry> insuracnceEntryList = new ArrayList<>();
        try {
            insuracnceEntryList = getListOfInsuranceEntryFromFile("FL_insurance.csv.zip");
        } catch (IOException e) {
            System.out.println("Error unzipping file FL_insurance.csv.zip" + e);
        }
        createFileWithNumberOfDifferentCountry(insuracnceEntryList);
        createFileWithSumOf_tiv_2012(insuracnceEntryList);
        createFileWithTwoColumsCountryAndValue(insuracnceEntryList);
    }

    static List<InsuranceEntry> getListOfInsuranceEntryFromFile(String nameFile) throws IOException {
        List<InsuranceEntry> insuranceEntryList = new ArrayList<>();
        ZipFile zipFile = new ZipFile(nameFile);
        ZipEntry zipEntry = zipFile.getEntry("FL_insurance.csv");
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            insuranceEntryList.add(new InsuranceEntry(line));
        }
        return insuranceEntryList;
    }

    static void createFileWithNumberOfDifferentCountry(List<InsuranceEntry> list) {
        Path filePath = Path.of("count.txt");
        String stringOfnumberOfDifferentCountry = String.valueOf(list.stream()
                .skip(1)
                .map(input -> input.getCountry())
                .distinct()
                .count());
        try {
            Files.writeString(filePath, stringOfnumberOfDifferentCountry);
        } catch (IOException e) {
            System.out.println("Cannot write into file: " + filePath);
        }
    }

    static void createFileWithSumOf_tiv_2012(List<InsuranceEntry> list) {
        Path filePath = Path.of("tiv2012.txt");
        String stringOfSum = String.valueOf(list.stream()
                .skip(1)
                .map(input -> input.getTiv_2012())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
        try {
            Files.writeString(filePath, stringOfSum);
        } catch (IOException e) {
            System.out.println("Cannot write into file: " + filePath);
        }
    }

    static void createFileWithTwoColumsCountryAndValue(List<InsuranceEntry> list) {
        String fileName = "most_valuable.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("country,value\n");
            list.stream()
                    .skip(1)
                    .collect(Collectors.groupingBy(entry -> entry.getCountry(), Collectors.summingDouble(entry -> entry.getTiv_2012().doubleValue() - entry.getTiv_2011().doubleValue())))
                    .entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .limit(10)
                    .forEach(x -> {
                        try {
                            writer.write(x.getKey() + "," + String.format(Locale.US, "%.2f", x.getValue()) + "\n");
                        } catch (IOException e) {
                            System.out.println("Cannot write into file: " + fileName);
                        }
                    });
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot write into file: " + fileName);
        }

    }
}
