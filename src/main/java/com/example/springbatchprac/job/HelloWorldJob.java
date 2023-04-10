package com.example.springbatchprac.job;

import com.example.springbatchprac.batch.JobLoggerListener;
import com.example.springbatchprac.batch.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableBatchProcessing
@SpringBootApplication
public class HelloWorldJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator =
                new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator =
                new DefaultJobParametersValidator(
                        new String[] {"fileName"},
                        new String[] {"name", "run.id"});

        defaultJobParametersValidator.afterPropertiesSet();

        // 파라미터 밸리데이터는 이름에 csv가 포함되어 있는지에 대한 유효성 검증을 진행하고, defaultJobParamterValidator는 fileName이 전달되는지에 대한 유효성 검증을 진행한다.
        validator.setValidators(
                Arrays.asList(new ParameterValidator(),
                        defaultJobParametersValidator));

        return validator;
    }

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
                .incrementer(new RunIdIncrementer())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step2")
                .tasklet(helloWorldTasklet(null)).build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTasklet(@Value("#{jobParameters['name']}") String name) {
        return ((stepContribution, chunkContext) -> {

            System.out.println(String.format("Hello, %s!" , name));
            return RepeatStatus.FINISHED;
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldJob.class, args);
    }
}
