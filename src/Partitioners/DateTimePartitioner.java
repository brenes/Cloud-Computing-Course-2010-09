package Partitioners;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

import Types.Tweet;

public class DateTimePartitioner implements Partitioner<Text, Tweet> {

	private int partitionInterval;
	private Date minDate;

	@Override
	public int getPartition(Text key, Tweet value, int numPartitions) {
		if (this.partitionInterval <= 0)
		{
			return (int)Math.floor((Math.random())*numPartitions);
		}
		else
		{
			long minutes = (value.getDatetime().getTime() - minDate.getTime());
			int partition = (int)Math.ceil((float)minutes / (partitionInterval*1000*60));
			
			return Math.min(partition, numPartitions); 
		}
	}

	@Override
	public void configure(JobConf job) {
		this.partitionInterval = job.getInt(
				"date_time_partitioner.partition_interval", -1);
		SimpleDateFormat shortDateFormat = new SimpleDateFormat(
				"dd/MM/yyyy hh:mm:ss");

		try {
			this.minDate = shortDateFormat.parse(job
					.get("twitter_input.min_date"));
		} catch (Exception e) {
			try {
				this.minDate = (Date) shortDateFormat
						.parse("01/01/1970 00:00:00");
			} catch (ParseException e1) {
			}
		}
	}

}
