package com.luan.craftattack.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateManager {
    private static final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
    private static final int maxYears = 100000;

    private DateManager() {
    }

    public static String removeTimePattern(final String input) {
        return timePattern.matcher(input).replaceFirst("").trim();
    }

    public static long parseDateDiff(String time, boolean future) throws Exception {
        return parseDateDiff(time, future, false);
    }

    public static long parseDateDiff(String time, boolean future, boolean emptyEpoch) throws Exception {
        final Matcher matcher = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (matcher.find()) {
            if (matcher.group() == null || matcher.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < matcher.groupCount(); i++) {
                if (matcher.group(i) != null && !matcher.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
                    years = Integer.parseInt(matcher.group(1));
                }
                if (matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                    months = Integer.parseInt(matcher.group(2));
                }
                if (matcher.group(3) != null && !matcher.group(3).isEmpty()) {
                    weeks = Integer.parseInt(matcher.group(3));
                }
                if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                    days = Integer.parseInt(matcher.group(4));
                }
                if (matcher.group(5) != null && !matcher.group(5).isEmpty()) {
                    hours = Integer.parseInt(matcher.group(5));
                }
                if (matcher.group(6) != null && !matcher.group(6).isEmpty()) {
                    minutes = Integer.parseInt(matcher.group(6));
                }
                if (matcher.group(7) != null && !matcher.group(7).isEmpty()) {
                    seconds = Integer.parseInt(matcher.group(7));
                }
                break;
            }
        }
        if (!found) {
            throw new Exception("Illegal date format: " + time);
        }
        final Calendar calendar = new GregorianCalendar();

        if (emptyEpoch) {
            calendar.setTimeInMillis(0);
        }

        if (years > 0) {
            if (years > maxYears) {
                years = maxYears;
            }
            calendar.add(Calendar.YEAR, years * (future ? 1 : -1));
        }
        if (months > 0) {
            calendar.add(Calendar.MONTH, months * (future ? 1 : -1));
        }
        if (weeks > 0) {
            calendar.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }
        if (days > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
        }
        if (hours > 0) {
            calendar.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }
        if (minutes > 0) {
            calendar.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }
        if (seconds > 0) {
            calendar.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }
        final Calendar max = new GregorianCalendar();
        max.add(Calendar.YEAR, 10);
        if (calendar.after(max)) {
            return max.getTimeInMillis();
        }
        return calendar.getTimeInMillis();
    }

    static int dateDiff(final int type, final Calendar fromDate, final Calendar toDate, final boolean future) {
        final int year = Calendar.YEAR;

        final int fromYear = fromDate.get(year);
        final int toYear = toDate.get(year);
        if (Math.abs(fromYear - toYear) > maxYears) {
            toDate.set(year, fromYear +
                    (future ? maxYears : -maxYears));
        }

        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(final long date) {
        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date);
        final Calendar now = new GregorianCalendar();
        return DateManager.formatDateDiff(now, calendar);
    }

    public static String formatDateDiff(final Calendar fromDate, final Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "now";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        toDate.add(Calendar.MILLISECOND, future ? 50 : -50);
        final StringBuilder stringBuilder = new StringBuilder();
        final int[] types = new int[] {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
        final String[] names = new String[] {"year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds"};
        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            final int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                stringBuilder.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
            }
        }
        toDate.add(Calendar.MILLISECOND, future ? -50 : 50);
        if (stringBuilder.length() == 0) {
            return "now";
        }
        return stringBuilder.toString().trim();
    }
}