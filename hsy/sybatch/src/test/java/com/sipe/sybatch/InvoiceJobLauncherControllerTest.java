package com.sipe.sybatch;

import static org.assertj.core.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.sipe.sybatch.config.InvoiceJobConfiguration;
import com.sipe.sybatch.ui.InvoiceJobLauncherController;
import com.sipe.sybatch.ui.dto.InvoiceJobRequest;

class InvoiceJobLauncherControllerTest {

	@Autowired
	private InvoiceJobLauncherController invoiceJobLauncherController;

	@Test
	@DisplayName("")
	void runJobTest() throws Exception {
		// TODO request Param 수정 필요
		InvoiceJobRequest request = new InvoiceJobRequest(InvoiceJobConfiguration.JOB_NAME, new Properties());

		ExitStatus exitStatus = invoiceJobLauncherController.runCreateInvoiceJob(request);

		assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
	}

}
