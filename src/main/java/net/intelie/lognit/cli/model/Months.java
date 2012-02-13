package net.intelie.lognit.cli.model;

public class Months {
    private static String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String forNumber(String monthStringNumber) {
        try {
            int monthNumber = Integer.parseInt(monthStringNumber);
            return months[monthNumber - 1];
        } catch (Exception ex) {
            return "?" + monthStringNumber + "?";
        }
    }
}
