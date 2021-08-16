package serenitylabs.tutorials.vetclinic.webdriver;

import net.bytebuddy.asm.Advice;

public enum TravelDay {
    Today,Tomorrow;

    private static int daysInFuture;

//    TravelDay(int daysInFuture) {
//        this.daysInFuture = daysInFuture;
//    }

    public static int getDaysInFuture() {
        return daysInFuture;
    }
    public static void setDaysInFuture(int daysInFutu) {daysInFuture = daysInFutu;
    }


}
