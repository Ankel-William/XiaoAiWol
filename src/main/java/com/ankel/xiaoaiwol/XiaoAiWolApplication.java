package com.ankel.xiaoaiwol;

import com.ankel.xiaoaiwol.bafa.BaichuanIOTConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BaichuanIOTConfig.class)
public class XiaoAiWolApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoAiWolApplication.class, args);
    }

}
