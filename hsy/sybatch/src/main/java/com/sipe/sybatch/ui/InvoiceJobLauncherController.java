package com.sipe.sybatch.ui;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sipe.sybatch.ui.dto.InvoiceJobRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InvoiceJobLauncherController {

	private final JobLauncher jobLauncher;

	private final ApplicationContext context;

	@PostMapping("/invoice")
	public ExitStatus runCreateInvoiceJob(@RequestBody InvoiceJobRequest request) throws Exception {
		Job job = context.getBean(request.getName(), Job.class);
		return jobLauncher.run(job, request.getJobParameters()).getExitStatus();
	}
}
