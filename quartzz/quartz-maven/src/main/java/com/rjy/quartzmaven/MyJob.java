package com.rjy.quartzmaven;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class MyJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println(arg0.getJobDetail().getKey().getGroup());
		System.out.println(arg0.getJobDetail().getKey().getName());
		try {
			System.out.println(arg0.getScheduler().getSchedulerInstanceId());
			System.out.println(arg0.getScheduler().getSchedulerName());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("job is running！！"+ new Date());
	}

}
