package Reducers;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class CountReducer<V2> extends MapReduceBase implements
		Reducer<Text, V2, Text, LongWritable> {

	@Override
	public void reduce(Text key, Iterator<V2> values,
			OutputCollector<Text, LongWritable> output, Reporter reporter)
			throws IOException {
		
		int count = 0;
		while (values.hasNext()) {
			count++;
			values.next();
		}

		output.collect(key, new LongWritable(count));

	}

}
