package com.jkm.scheduled;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Lazy(false)
public class Test {
    private static int COUNT = 0;
//    @Scheduled(cron = "0/1 * * * * ?")
    public void init(){
        COUNT++;
        System.out.println(COUNT);
    }
}
