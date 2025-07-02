package at.jkvn.dtosimplify.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@AutoConfiguration
@ConditionalOnClass(name="org.springframework.boot.SpringApplication")
public class DtoSimplifyConfiguration {
    
}