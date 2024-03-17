package com.example.springbatchexample.chunk.policy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class MediateCompletionPolicy implements CompletionPolicy {

    private int chunkSize;
    private int processed;

    private long startTime;
    private long endTime;
    private long mediateTime;

    private final static int MAX_CHUNK_SIZE = 10000;

    public MediateCompletionPolicy(long mediateTime) {
        this.chunkSize = 0;
        this.processed = 0;

        this.startTime = 0L;
        this.endTime = 0L;
        this.mediateTime = mediateTime;
    }

    @Override
    public boolean isComplete(RepeatContext context) {
        return this.processed >= chunkSize;
    }

    @Override
    public boolean isComplete(RepeatContext context, RepeatStatus result) {
        this.endTime = System.currentTimeMillis();
        return isComplete(context) || RepeatStatus.FINISHED == result;
    }

    @Override
    public RepeatContext start(RepeatContext parent) {
        long processTime = endTime - startTime;

        if (chunkSize > 0 && processTime > 0) {
            this.chunkSize = (int) Math.min(
                    this.chunkSize * mediateTime / processTime,
                    MAX_CHUNK_SIZE);
        } else {
            this.chunkSize = 100;
        }

        this.startTime = System.currentTimeMillis();
        this.processed = 0;

        log.info("\n");
        log.info("=== Chunk Start ===");
        log.info(" == processTime: {}", processTime);
        log.info(" == chunkSize: {}", this.chunkSize);
        log.info(" == startTime: {}", this.startTime);

        return parent;
    }

    @Override
    public void update(RepeatContext context) {
        this.processed++;

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (isComplete(context)) {
            this.endTime = System.currentTimeMillis();

            log.info(" == endTime: {}", this.endTime);
            log.info("=== Chunk End ===");
        }
    }
}