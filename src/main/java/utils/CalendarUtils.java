package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CalendarUtils {

    public static String getDateStamp(String date_format) {
        DateFormat dateFormat = new SimpleDateFormat(date_format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String convertDateFormat(String tempdob, String fromFormat, String toFormat) {
        DateFormat formatter = new SimpleDateFormat(fromFormat);
        Date date;
        try {
            date = formatter.parse(tempdob);
            SimpleDateFormat newFormat = new SimpleDateFormat(toFormat);
            tempdob = newFormat.format(date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return tempdob;
    }

    public static String manipulateDate(String date, String plusMinusIndicator, int days, int weeks, int months,
                                        int years) {

        // change the format of the date in string as 2021-02-17
        String beforeDate = convertDateFormat(date, "MM/dd/yyyy", "yyyy-MM-dd");

        // parse the date to local date format
        LocalDate oldDate = LocalDate.parse(beforeDate);

        // define the output format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // declare after date variable
        String afterDate = null;

        // operation whether plus or minus needed and store it to after date
        if (plusMinusIndicator.equalsIgnoreCase("Plus"))
            afterDate = formatter.format(oldDate.plusDays(days).plusWeeks(weeks).plusMonths(months).plusYears(years));
        else if (plusMinusIndicator.equalsIgnoreCase("Minus"))
            afterDate = formatter
                    .format(oldDate.minusDays(days).minusWeeks(weeks).minusMonths(months).minusYears(years));

        return afterDate;

    }

}
