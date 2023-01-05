package cn.zeroclian;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

/**
 * @author ZeroClian
 * @date 2022-11-07 23:22
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"cn.zeroclian.dao"})
public class ServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServiceApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功!!!");
        LOG.info("Service地址: http://127.0.0.1:{}", env.getProperty("server.port"));
    }
}
