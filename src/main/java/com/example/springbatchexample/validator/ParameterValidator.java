package com.example.springbatchexample.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String filename = parameters.getString("fileName");

        if (!StringUtils.hasText(filename)) {
            throw new JobParametersInvalidException("fileName 파라미터 누락");
        }
        if (!StringUtils.endsWithIgnoreCase(filename, "csv")) {
            throw new JobParametersInvalidException("fileName 파라미터가 csv 확장자가 아님");
        }
    }
}
