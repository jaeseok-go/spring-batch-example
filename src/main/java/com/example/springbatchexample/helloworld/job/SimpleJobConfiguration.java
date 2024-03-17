package com.example.springbatchexample.helloworld.job;

import com.example.springbatchexample.helloworld.listener.JobLoggerListener;
import com.example.springbatchexample.helloworld.validator.ParameterValidator;
import com.example.springbatchexample.helloworld.tasklet.HelloWorldTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final HelloWorldTasklet helloWorldTasklet;

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, platformTransactionManager))
                .incrementer(new RunIdIncrementer())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloWorldTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public CompositeJobParametersValidator validator() {
        DefaultJobParametersValidator defaultJobParametersValidator =
                new DefaultJobParametersValidator(new String[] {"fileName"}, new String[] {"name", "currentDate"});
        defaultJobParametersValidator.afterPropertiesSet();

        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(
                Arrays.asList(new ParameterValidator(), defaultJobParametersValidator)
        );

        return validator;
    }
}
