package com.example.springbatchexample.helloworld.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTasklet implements Tasklet {

    private static final String HELLO_WORLD = "Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String name = (String) chunkContext.getStepContext().getJobParameters().get("name");

        ExecutionContext jobContext = chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobContext.put("user.name", name);

        System.out.println(String.format(HELLO_WORLD, name));
        return RepeatStatus.FINISHED;
    }
}
