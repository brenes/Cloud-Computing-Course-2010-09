package Jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.LongSumReducer;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.lib.TokenCountMapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import InputFormats.TwitterInputFormat;
import Partitioners.DateTimePartitioner;
import Reducers.CountReducer;
import StopWordMapper.NoStopWordCounterMapper;
import Types.Tweet;

public class TwitterCount extends Configured implements Tool {

	public TwitterCount(Configuration conf) {
		super(conf);
	}

	public TwitterCount() {
		super();
	}

	@Override
	public int run(String[] args) throws Exception {

		Configuration conf = getConf();

		JobConf job = new JobConf(conf, TwitterCount.class);

		if (args.length > 2) {
			job.setInt("date_time_partitioner.partition_interval", Integer
					.parseInt(args[2]));
		}

		if (args.length > 3) {
			job.set("twitter_input.min_date", args[3]);
		} else {
			job.set("twitter_input.min_date", "01/01/1970 00:00:00");
		}

		if (args.length > 4) {
			job.set("twitter_input.max_date", args[4]);
		} else {
			job.set("twitter_input.min_date", "01/01/2070 00:00:00");
		}

		Path in = new Path(args[0]);
		Path out = new Path(args[1]);
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);

		job.setJobName("MyJob");

		job.setInputFormat(TwitterInputFormat.class);


		job.setMapperClass(NoStopWordCounterMapper.class);
		// Set the outputs for the Map
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Tweet.class);


		job.setReducerClass(CountReducer.class);
		
		// Calculamos la cantidad de tiempo entre inicio y fin y ponemos el
		// número de tareas reductoras

		if (args.length > 2) {
			job.setPartitionerClass(DateTimePartitioner.class);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			Date minDate = dateFormat.parse(job.get("twitter_input.min_date"));
			Date maxDate = dateFormat.parse(job.get("twitter_input.max_date"));

			job.setNumReduceTasks((int) (maxDate.getTime() - minDate.getTime())
				/ (job.getInt("date_time_partitioner.partition_interval", 0)*1000*60));
		}
		
		// Set the outputs for the Reducer
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setOutputFormat(TextOutputFormat.class);

		

		JobClient.runJob(job);

		return 0;

	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new TwitterCount(), args);

		System.exit(res);
	}

}
