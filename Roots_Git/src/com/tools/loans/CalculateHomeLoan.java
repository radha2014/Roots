package com.tools.loans;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.roots.tools.LogHandler;

/**
 * @author Radha Krishnan, India
 * 
 */
public class CalculateHomeLoan {

    private static boolean emiChanged = false;

    static Map<String, Integer> prepaymentMap = null;

    static Map<String, Integer> emiChangeMap = null;

    static Map<String, Integer> additionalLoanMap = null;

    static String INTEREST_RATES = null;

    static double[] INTEREST_RATE = null;// percent per year

    static double LOAN_AMOUNT;

    static int TENURE;// in years

    static double EMI;

    static int START_YEAR;

    static int START_MONTH;

    static boolean prepaymentMade = false;

    private static boolean additionalLoan;

    private static LogHandler _logger;

    static boolean propertyFileInit() {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("loans");
        } catch (Exception e) {
            e.printStackTrace();
            _logger.println("Initialization from property file failed...");
            return false;
        }
        INTEREST_RATES = rb.getString("INTEREST_RATES");
        if (INTEREST_RATES.trim().length() == 0) {
            try {
                for (int i = 0;; i++) {
                    INTEREST_RATES += rb.getString("INTEREST_RATES_" + i);
                    INTEREST_RATES += ",";
                }
            } catch (Exception e) {
            }
        }
        INTEREST_RATE = getInterestRates(INTEREST_RATES.split(","));
        LOAN_AMOUNT = Integer.parseInt(rb.getString("LOAN_AMOUNT"));
        TENURE = Integer.parseInt(rb.getString("TENURE"));
        EMI = Integer.parseInt(rb.getString("EMI"));
        START_YEAR = Integer.parseInt(rb.getString("START_YEAR"));
        START_MONTH = Integer.parseInt(rb.getString("START_MONTH"));
        prepaymentMade = rb.getString("PREPAYMENTS").trim().length() != 0;
        emiChanged = rb.getString("EMI_CHANGES").trim().length() != 0;
        additionalLoan = rb.getString("ADDITIONAL_LOANS").trim().length() != 0;
        if (prepaymentMade) {
            String str[] = rb.getString("PREPAYMENTS").split(",");
            prepaymentMap = new HashMap<String, Integer>(str.length);
            for (int i = 0; i < str.length; i++) {
                String[] payment = str[i].split("-");
                prepaymentMap.put(payment[0] + "-" + payment[1],
                        Integer.parseInt(payment[2]));
            }
        }
        if (emiChanged) {
            String str[] = rb.getString("EMI_CHANGES").split(",");
            emiChangeMap = new HashMap<String, Integer>(str.length);
            for (int i = 0; i < str.length; i++) {
                String[] payment = str[i].split("-");
                emiChangeMap.put(payment[0] + "-" + payment[1],
                        Integer.parseInt(payment[2]));
            }
        }
        if (additionalLoan) {
            String str[] = rb.getString("ADDITIONAL_LOANS").split(",");
            additionalLoanMap = new HashMap<String, Integer>(str.length);
            for (int i = 0; i < str.length; i++) {
                String[] payment = str[i].split("-");
                additionalLoanMap.put(payment[0] + "-" + payment[1],
                        Integer.parseInt(payment[2]));
            }
        }
        printCurrentDate();
        _logger.println("Program Initialized from property file...");
        return true;
    }

    static void arguementsInit(String[] args) {
        _logger.println("Program Initialized...");
        LOAN_AMOUNT = Integer.parseInt(args[0]);
        TENURE = Integer.parseInt(args[1]);
        EMI = Integer.parseInt(args[2]);
        START_YEAR = Integer.parseInt(args[3]);
        START_MONTH = Integer.parseInt(args[4]);
    }

    static void defaultInit() {
        _logger.println("Program using default Initialization...");
        INTEREST_RATES = "10.0";
        INTEREST_RATE = getInterestRates(INTEREST_RATES.split(","));
        LOAN_AMOUNT = 1000000;
        TENURE = 10;// in years
        EMI = 10000;
        START_YEAR = 2007;
        START_MONTH = 4;
        prepaymentMade = false;
    }

    static void logInit() {
        _logger = LogHandler.getInstance(null);
    }

    public static void main(String[] args) {
        logInit();
        if (args.length != 0) {

            // java program_name <LOAN_AMOUNT> <TENURE> <EMI> <START_YEAR> <START_MONTH>
            arguementsInit(args);

        } else {

            if (!propertyFileInit()) {
                defaultInit();
            }

        }

        double principal = LOAN_AMOUNT;
        double interestRate = INTEREST_RATE[0] / 12;
        int n = TENURE * 12;
        int year = START_YEAR;
        int month = START_MONTH;
        double yearlyPrincipal = 0.0;
        double yearlyInterest = 0.0;

        printHeader();

        int idx = 0;
        double thisYearPrepaymentAmount = 0.0;
        boolean thisYearPrepaymentMade = false;

        double totalPrincipalPaid = 0.0;
        double totalInterestPaid = 0.0;
        while (principal > 0) {

            if (INTEREST_RATE.length > idx + 1) {// in case interest rate changes
                interestRate = INTEREST_RATE[idx] / 12;
            }
            double monthlyPaidInterest = getAmount(principal, interestRate, n) / 100;
            if (EMI > principal) {
                EMI = principal + monthlyPaidInterest;
            }
            double monthlyPaidPrincipal = (EMI - monthlyPaidInterest);
            yearlyPrincipal = yearlyPrincipal + monthlyPaidPrincipal;
            yearlyInterest = yearlyInterest + monthlyPaidInterest;
            principal = principal - monthlyPaidPrincipal;
            _logger.println(++idx + ".\t(" + year + "-" + month + ")" + "\t"
                    + format(EMI) + "\t\t" + format(monthlyPaidPrincipal)
                    + "\t" + format(monthlyPaidInterest) + "\t" + interestRate
                    * 12 + "\t\t" + format(principal));

            if (month++ == 12) {// move to next month
                month = 1;
                year++;
            }

            if (month == 4) {
                _logger.println("-------------------------------------------------------------------------------------------");
                _logger.print((year - 1) + "-" + year
                        + ": Yearly Principal Paid = "
                        + format(Math.ceil(yearlyPrincipal)));
                if (thisYearPrepaymentMade) {
                    _logger.print("* (includes prepayment of "
                            + format(thisYearPrepaymentAmount) + ")");
                }
                _logger.println(", Yearly Interest Paid = "
                        + format(Math.floor(yearlyInterest)));
                totalPrincipalPaid += yearlyPrincipal;
                totalInterestPaid += yearlyInterest;
                _logger.println("Total Principal Paid = "
                        + format(totalPrincipalPaid)
                        + ", Total Interest Paid = "
                        + format(totalInterestPaid) + ", Total Amount Paid = "
                        + format(totalPrincipalPaid + totalInterestPaid));
                _logger.println("-------------------------------------------------------------------------------------------");
                _logger.println("adding...");
                yearlyPrincipal = 0.0;
                yearlyInterest = 0.0;
                thisYearPrepaymentAmount = 0;
                thisYearPrepaymentMade = false;
            }

            // in case of any prepayments
            if (prepaymentMade && prepaymentMap.get(year + "-" + month) != null) {

                int prepaidAmount = prepaymentMap.get(year + "-" + month);
                principal = principal - prepaidAmount;
                yearlyPrincipal = yearlyPrincipal + prepaidAmount;
                thisYearPrepaymentAmount = thisYearPrepaymentAmount
                        + prepaidAmount;
                thisYearPrepaymentMade = true;
            }

            if (emiChanged && emiChangeMap.get(year + "-" + month) != null) {// incase EMI changes
                EMI = emiChangeMap.get(year + "-" + month);
            }

            if (additionalLoan
                    && additionalLoanMap.get(year + "-" + month) != null) {// incase of additional loan

                int additionalLoan = additionalLoanMap.get(year + "-" + month);
                principal = principal + additionalLoan;
            }

        }// while

        displaySummary(idx - 1, totalInterestPaid + yearlyInterest,
                totalPrincipalPaid + yearlyPrincipal);
    }

    private static void displaySummary(int no_of_months, double totalInterest,
            double totalPrincipal) {
        _logger.println("==============================================================");
        _logger.println("Total Interest Paid = " + format(totalInterest)
                + ", total Principal Paid = " + format(totalPrincipal));
        _logger.println("Paying " + no_of_months
                + " installments..in a duration of " + (no_of_months / 12)
                + "years and " + (no_of_months % 12) + "months.");
        _logger.println("==============================================================");
        printCurrentDate(no_of_months - 1);
    }

    private static void printCurrentDate(int no_of_months) {
        Calendar today = Calendar.getInstance();
        int current_year = today.get(Calendar.YEAR);
        int current_month = today.get(Calendar.MONTH);
        _logger.println("Today's Date: " + today.getTime());
        _logger.print("Start Year-Month: " + START_MONTH + "-" + START_YEAR);
        _logger.print("\t Current Year-Month: " + (current_month + 1) + "-"
                + current_year);
        Calendar endDate = Calendar.getInstance();
        endDate.set(START_YEAR, START_MONTH, 1);
        endDate.add(Calendar.MONTH, no_of_months);
        long millsec = endDate.getTimeInMillis() - today.getTimeInMillis();
        long remainingmonths = millsec / 1000 / 60 / 60 / 24 / 30;
        _logger.println("\t Last Year-Month: "
                + (endDate.get(Calendar.MONTH) + 1) + "-"
                + endDate.get(Calendar.YEAR));
        _logger.println("Remaining "
                + remainingmonths
                + " installments..for next "
                + ((remainingmonths / 12 > 0) ? (remainingmonths / 12)
                        + "years and " : "") + (remainingmonths % 12)
                + "months.");

    }

    private static void printCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int current_year = cal.get(Calendar.YEAR);
        int current_month = cal.get(Calendar.MONTH);
        _logger.println("Today's Date: " + cal.getTime());
        _logger.print("Start Year-Month: " + START_MONTH + "-" + +START_YEAR);
        _logger.println("\t\t Current Year-Month: " + (current_month + 1) + "-"
                + current_year);
    }

    private static void printHeader() {
        _logger.println("*****************************************************************************************************************");
        _logger.println("* Aboration chart for a loan of " + LOAN_AMOUNT
                + "0/- for a period of " + TENURE
                + "year(s) on an interest rate of " + INTEREST_RATE[0]
                + "%(variable) *");
        _logger.println("*****************************************************************************************************************");
        _logger.println("S.No(Year-Month)\tEMI Paid\tPricipal\tInterest\tInterest Rate\tRemaining Principal");
        _logger.println("-------------------------------------------------------------------------------------------");
    }

    public static double getAmount(double principal, double interestRate, int n) {
        return (principal * interestRate * Math.pow(1 + interestRate, n))
                / (Math.pow(1 + interestRate, n) - 1);
    }

    static double[] getInterestRates(String[] strArr) {
        double[] arr = new double[strArr.length];
        int j = 0;
        for (int i = 0; i < strArr.length; i++) {
            arr[j++] = Double.parseDouble(strArr[i]);
        }
        return arr;
    }

    /**
     * Return the number num in Indian currency format of lakhs instead of
     * millions
     * 
     * @param num
     * @return
     */
    public static String format(double num) {
        String returnValue;
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        // we never reach double digit grouping so return
        if (num < 100000)
            return formatter.format(num);

        // switch to double digit grouping
        formatter.applyPattern("#,##");

        // divide by 1000, so that we get everything before the 3-group
        returnValue = formatter.format((int) (num / 1000)) + ",";
        // switch back to triple grouping + decimal
        formatter.applyPattern("#,###.00");

        // remove value of number over 999 (so we just get the last
        // 5 digits)
        double last3digitNum = num - (int) (num / 1000) * 1000;
        String last3digitStr = formatter
                .format(num - (int) (num / 1000) * 1000);
        if (last3digitNum > 99)
            returnValue += last3digitStr;
        else if (last3digitNum > 9) {
            returnValue += "0" + last3digitStr;
        } else {
            returnValue += "00" + last3digitStr;
        }

        return returnValue;
    }
}
