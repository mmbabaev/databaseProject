package model.entities;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Yaroslav on 10.06.16.
 */
public class PriceChange implements Comparable<PriceChange>{
    private Date date;
    private double price;
    public PriceChange(){
        date = new Date();
        price = 0.0;
    }


    public PriceChange(String date, String price){
        int year = 0, month = 0, day = 0, hrs = 0, min = 0, sec = 0;

        try {
            year = Integer.parseInt(date.split(" ")[0].split("-")[0]);
            month = Integer.parseInt(date.split(" ")[0].split("-")[1]);
            day = Integer.parseInt(date.split(" ")[0].split("-")[2]);
            hrs = Integer.parseInt(date.split(" ")[1].split(":")[0]);
            min = Integer.parseInt(date.split(" ")[1].split(":")[1]);
            sec = Integer.parseInt(date.split(" ")[1].split(":")[2]);
        }catch (Exception e){
            System.out.println("МдаDate");
        }

        this.date = new Date(year, month - 1, day, hrs, min, sec);


        try {
            this.price = Double.parseDouble(price);
        }catch (Exception e){
            System.out.println("МдаPrice");
        }

    }

    @Override
    public int compareTo(PriceChange pr) {
        return date.compareTo(pr.getDate());
    }

    public String getPriceString(){
        return String.valueOf(price);
    }

    public double getPriceDouble(){
        return price;
    }

    public Date getDate(){
        return date;
    }

    public String getString(){
        String result = date.toString().split(" ")[2] + " " + date.toString().split(" ")[1] +
                " " + String.valueOf(date.getYear());
        return result;
    }


}

