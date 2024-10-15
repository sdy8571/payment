package com.payment;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Date;

@Slf4j
@SpringBootApplication
@EnableAsync
public class PaymentApplication {
    public static void main(String[] args) {
        System.setProperty("zookeeper.sasl.client", "false");
        SpringApplication.run(PaymentApplication.class, args);
        log.info("启动时间：" + DateUtil.formatDateTime(new Date()));
        log.info("\n"
                + " (♥◠‿◠)ﾉﾞ  服务启动成功   ლ(´ڡ`ლ)ﾞ \n"
                + "              ___                            ___     \n"
                + "            ,--.'|_                        ,--.'|_   \n"
                + "            |  | :,'              __  ,-.  |  | :,'  \n"
                + "  .--.--.   :  : ' :            ,' ,'/ /|  :  : ' :  \n"
                + " /  /    '.;__,'  /    ,--.--.  '  | |' |.;__,'  /   \n"
                + "|  :  /`./|  |   |    /       \\ |  |   ,'|  |   |    \n"
                + "|  :  ;_  :__,'| :   .--.  .-. |'  :  /  :__,'| :    \n"
                + " \\  \\    `. '  : |__  \\__\\/: . .|  | '     '  : |__  \n"
                + "  `----.   \\|  | '.'| ,\" .--.; |;  : |     |  | '.'| \n"
                + " /  /`--'  /;  :    ;/  /  ,.  ||  , ;     ;  :    ; \n"
                + "'--'.     / |  ,   /;  :   .'   \\---'      |  ,   /  \n"
                + "  `--'---'   ---`-' |  ,     .-./           ---`-'   \n"
                + "                     `--`---'                        \n"
                + "\n");
    }
}