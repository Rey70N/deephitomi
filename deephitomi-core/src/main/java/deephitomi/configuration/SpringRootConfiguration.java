package deephitomi.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Reyton on 2017/7/11.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "studio.deepsea.deephitomi",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class SpringRootConfiguration {

}