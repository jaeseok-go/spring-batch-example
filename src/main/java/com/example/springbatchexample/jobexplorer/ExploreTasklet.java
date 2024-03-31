package com.example.springbatchexample.jobexplorer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ExploreTasklet implements Tasklet {

    private final JobExplorer jobExplorer;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobName = chunkContext.getStepContext().getJobName();

        List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, Integer.MAX_VALUE);

        log.info("{}개의 {} 잡 인스턴스가 존재합니다.", instances.size(), jobName);
        log.info("*********************************** 잡 인스턴스 목록 ***********************************");

        for (JobInstance instance : instances) {
            List<JobExecution> jobExecutions = this.jobExplorer.getJobExecutions(instance);
            log.info("{} 잡 인스턴스가 {} 번 실행됨", instance.getInstanceId(), jobExecutions.size());

            for (JobExecution jobExecution : jobExecutions) {
                log.info("{} 잡 실행의 종료 코드 {} 발생", jobExecution.getId(), jobExecution.getExitStatus());
            }
        }

        return RepeatStatus.FINISHED;
    }
}
