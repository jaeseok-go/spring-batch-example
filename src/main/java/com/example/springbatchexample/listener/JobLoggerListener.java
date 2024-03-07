package com.example.springbatchexample.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobLoggerListener {

    private static final String JOB_START_MESSAGE = "%s이 시작됨";

    private static final String JOB_END_MESSAGE = "%s이 %s 상태로 종료됨";

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        System.out.println(String.format(JOB_START_MESSAGE, jobName));
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String jobStatus = jobExecution.getStatus().name();
        System.out.println(String.format(JOB_END_MESSAGE, jobName, jobStatus));
    }
}
