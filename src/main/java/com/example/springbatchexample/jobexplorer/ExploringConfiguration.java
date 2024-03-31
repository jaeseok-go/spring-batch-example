package com.example.springbatchexample.jobexplorer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExploringConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final JobExplorer jobExplorer;

    @Bean
    public Job exploreJob() {
        return new JobBuilder("exploreJob", jobRepository)
                .start(exploreStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step exploreStep() {
        return new StepBuilder("exploreStep", jobRepository)
                .tasklet(exploreTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet exploreTasklet() {
        return new ExploreTasklet(jobExplorer);
    }
}
