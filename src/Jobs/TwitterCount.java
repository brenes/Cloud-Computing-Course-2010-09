package Jobs;

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
import StopWordMapper.NoStopWordCounterMapper;

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
         
        if (args.length > 2)
        {
        	job.set("twitter_input.min_date", args[2]);
        }

        if (args.length > 3)
        {
        	job.set("twitter_input.max_date", args[3]);
        }
        Path in = new Path(args[0]); 
        Path out = new Path(args[1]); 
        FileInputFormat.setInputPaths(job, in); 
        FileOutputFormat.setOutputPath(job, out); 
         
        job.setJobName("MyJob"); 
        job.setMapperClass(NoStopWordCounterMapper.class); 
        job.setReducerClass(LongSumReducer.class); 
        
        job.setInputFormat(TwitterInputFormat.class);
        
        job.setOutputFormat(TextOutputFormat.class); 
        job.setOutputKeyClass(Text.class); 
        job.setOutputValueClass(LongWritable.class);
        
        // Esto hará que solo tengamos un fichero de salida, aunque restringirá el número de reducers a 1
        job.setNumReduceTasks(1);
        
        JobClient.runJob(job); 
         
        return 0;
		
	}
	   
    public static void main(String[] args) throws Exception {  
        int res = ToolRunner.run(new Configuration(), new TwitterCount(), args); 
         
        System.exit(res); 
    }
    
}
