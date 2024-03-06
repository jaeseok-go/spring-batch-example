package com.example.springbatchexample.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SimpleJobConfiguration {
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("job", jobRepository)
                .start(step(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloWorldTasklet(null),
                        platformTransactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTasklet(@Value("#{jobParameters['name']}") String name) {
        return (contribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }
}
