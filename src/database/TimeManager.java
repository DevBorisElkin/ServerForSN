package database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeManager {

    public static long calcSeconds(long millis){ return TimeUnit.MILLISECONDS.toSeconds(millis); }
    public static long calcMinutes(long millis){ return TimeUnit.MILLISECONDS.toMinutes(millis); }
    public static long calcHours(long millis){ return TimeUnit.MILLISECONDS.toHours(millis); }
    public static long calcDays(long millis){ return TimeUnit.MILLISECONDS.toDays(millis); }
    public static long calcWeeks(long millis){ return  TimeUnit.MILLISECONDS.toDays(millis)/4; }
    public static long calcMonths(long millis){ return  TimeUnit.MILLISECONDS.toDays(millis)/30; }
    public static long calcYears(long millis){ return  TimeUnit.MILLISECONDS.toDays(millis)/(30*12); }

    public static String calcTimeSince(long last, long now){
        long current=now-last;

        long years, months, days, hours, minutes;
        years=calcYears(current);
        months=calcMonths(current);
        days=calcDays(current);
        hours=calcHours(current);
        minutes=calcMinutes(current);

        if(years>0)months-=(12*years);

        if(months>0)days-=(months*28);
        if(years>0)days-=(365*years);

        if(hours>0)hours-=(24*days);
        if(months>0)hours-=(months*696);
        if(years>0)hours-=(years*8766);

        if(hours>0)minutes-=(60*hours);
        if(days>0)minutes-=(days*24*60);
        if(months>0)minutes-=(months*40320);
        if(years>0)minutes-=(years*525960);

        StringBuilder time = new StringBuilder("time");
        if(years>0)time.append("$years:"+years);
        if(months>0)time.append("$months:"+months);
        if(days>0)time.append("$days:"+days);
        if(hours>0)time.append("$hours:"+hours);
        if(minutes>=0)time.append("$minutes:"+minutes);

        return time.toString();
    }

    public static String CheckCalculation(long last, long now){
        long current=now-last;
        StringBuilder time = new StringBuilder("time");
        if(calcYears(current)>0)time.append("$years:"+calcYears(current));
        if(calcMonths(current)>0)time.append("$months:"+calcMonths(current));
        if(calcWeeks(current)>0)time.append("$weeks:"+calcWeeks(current));
        if(calcDays(current)>0)time.append("$days:"+calcDays(current));
        if(calcHours(current)>0)time.append("$hours:"+calcHours(current));
        if(calcMinutes(current)>=0)time.append("$minutes:"+calcMinutes(current));
        return time.toString();
    }
    /*  Example
        System.out.println(calcTimeSince(1548850025221L,System.currentTimeMillis()+1420000L)+"\n");
        System.out.println("----------OLD CALCULATION------------");
        System.out.println(CheckCalculation(1548850025221L,System.currentTimeMillis()+1420000L));
     */

    public static long getSecDifference(long old, long now){
        String tmp=CheckCalculation(old, now);
        int indexLast=tmp.lastIndexOf(":");
        String prep=tmp.substring(indexLast+1,tmp.length());
        return Long.parseLong(prep);
    }

    public static String parseString(String input){
        String token=""; String data = "";
        StringBuilder builder = new StringBuilder("was online");
        ArrayList<Integer> listDollars = new ArrayList<Integer>();
        char character = '$';
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == character){
                listDollars.add(i);
            }
        }
        ArrayList<Integer> listColons = new ArrayList<Integer>();
        character = ':';
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == character){
                listColons.add(i);
            }
        }

        for(int i=0; i<listColons.size();i++){
            token=input.substring(listDollars.get(i)+1,listColons.get(i));
            if(i+1>=listColons.size()){
                data=input.substring(listColons.get(i)+1,input.length());
            }else{
                data=input.substring(listColons.get(i)+1,listDollars.get(i+1));
            }
            if(Integer.parseInt(data)==1){
                token=token.substring(0,token.length()-1);
            }
            builder.append(" "+data+" "+token);
        }
        builder.append(" ago");
/**   >>>time$hours:22$minutes:40
 *    >>>was online 22 hours 40 minutes ago   **/
        return builder.toString();
    }
}
