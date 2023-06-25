package utils;

import com.github.javafaker.Faker;

import java.util.Random;

public class DataGenerator {

    static Faker faker = new Faker();

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static int generateNumber(int NumOfDigits) {
        Random random = new Random();
        int min = (int) Math.pow(10, NumOfDigits - 1);
        int max = (int) Math.pow(10, NumOfDigits) - 1;
        return random.nextInt(max - min + 1) + min;
    }

    public static String generateGender(){
        String title = faker.name().prefix();
        if(title.equals("Mr.") | title.equals("Dr."))
            return "male";
        else
            return "female";
    }

}
