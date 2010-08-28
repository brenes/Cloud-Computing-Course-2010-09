package InputFormats;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

/**
 * Clase que implementa el lector de datos. Nos vamos a basar en el
 * FileInputFormat porque vamos a leer de fichero y no nos interesa implementar
 * todas las operaciones.
 * 
 */
public class TwitterInputFormat extends FileInputFormat<LongWritable, Text>
		implements InputFormat<LongWritable, Text> {

	/**
	 * Implementación del Patrón Abstract Factory para obtener el RecordReader
	 * deseado.
	 */
	public RecordReader<LongWritable, Text> getRecordReader(InputSplit split,
			JobConf job, Reporter reporter) throws IOException {
		return new TwitterRecordReader((FileSplit) split, job, reporter);
	}

	/**
	 * RecordReader de nuestro InputFormat. Es el elemento que realmente realiza
	 * la lectura del fichero de datos.
	 * 
	 * Dado que se parece mucho a la lectura de una línea vamos a delegar parte
	 * de la funcionalidad en LineRecordReader intereceptando tan solo la
	 * lectura del próximo elemento para quedarnos tan solo con el texto del
	 * tweet.
	 */
	public static class TwitterRecordReader implements
			RecordReader<LongWritable, Text> {

		private LineRecordReader reader;
		private LongWritable key;
		private Text value;

		public TwitterRecordReader(FileSplit split, JobConf job,
				Reporter reporter) throws IOException {
			this.reader = new LineRecordReader(job, split);
			this.key = this.createKey();
			this.value = this.createValue();
		}

		@Override
		public void close() throws IOException {
			reader.close();
		}

		@Override
		public LongWritable createKey() {
			return new LongWritable();
		}

		@Override
		public Text createValue() {
			return new Text();
		}

		@Override
		public long getPos() throws IOException {
			return this.reader.getPos();
		}

		@Override
		public float getProgress() throws IOException {
			return this.reader.getProgress();
		}

		@Override
		public boolean next(LongWritable key, Text value) throws IOException {
			if (!this.reader.next(key, value)) {
				return false;
			}

			// Una vez hecha la lectura dividimos la línea por el carácter ','
			// (recordemos que se trata de un CSV) y cogemos el primero de los
			// registros.
			StringTokenizer tokens = new StringTokenizer(value.toString(), ",");
			value.set(tokens.nextToken());

			return true;
		}

	}

}
