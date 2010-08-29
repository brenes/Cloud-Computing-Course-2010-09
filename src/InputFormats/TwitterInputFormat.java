package InputFormats;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

import Types.Tweet;

/**
 * Clase que implementa el lector de datos. Nos vamos a basar en el
 * FileInputFormat porque vamos a leer de fichero y no nos interesa implementar
 * todas las operaciones.
 * 
 */
public class TwitterInputFormat extends FileInputFormat<LongWritable, Tweet>
		implements InputFormat<LongWritable, Tweet> {

	/**
	 * Implementación del Patrón Abstract Factory para obtener el RecordReader
	 * deseado.
	 */
	public RecordReader<LongWritable, Tweet> getRecordReader(InputSplit split,
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
			RecordReader<LongWritable, Tweet> {

		private LineRecordReader reader;
		private LongWritable key;
		private Tweet value;
		private JobConf job;
		private Date minDate;
		private Date maxDate;
		private DateFormat shortDateFormat;
		private DateFormat longDateFormat;

		public TwitterRecordReader(FileSplit split, JobConf job,
				Reporter reporter) throws IOException {
			this.reader = new LineRecordReader(job, split);
			this.key = this.createKey();
			this.value = this.createValue();
			this.job = job;

			this.shortDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy hh:mm:ss");
			this.longDateFormat = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
			
			try {
				this.minDate = this.shortDateFormat.parse(this.job
						.get("twitter_input.min_date"));
			} catch (Exception e) {
				try {
					this.minDate = (Date) shortDateFormat
							.parse("01/01/1970 00:00:00");
				} catch (ParseException e1) {
				}
			}
			
			try {
				this.maxDate = this.shortDateFormat.parse(this.job
						.get("twitter_input.max_date"));
			} catch (Exception e) {
				try {
					this.maxDate = (Date) shortDateFormat
							.parse("01/01/2070 00:00:00");
				} catch (ParseException e1) {
				}
			}

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
		public Tweet createValue() {
			return new Tweet();
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
		public boolean next(LongWritable key, Tweet value) throws IOException {
			Text lineValue = new Text();
			while (this.reader.next(key, lineValue)) {
				// Una vez hecha la lectura dividimos la línea por el carácter
				// ','(recordemos que se trata de un CSV) y cogemos el primero
				// de
				// los registros (el mensaje).
				String line = lineValue.toString();
				String[] tokens = line.split(",", 13);
				value.setMessage(tokens[0]);

				String time = tokens[11];

				try {
					// Después cogemos la fecha y vemos si está entre los
					// límites establecidos
					Date date = this.longDateFormat.parse(time);

					if ((date.equals(this.minDate))
							|| (date.equals(this.maxDate))
							|| ((date.after(this.minDate) && (date
									.before(this.maxDate))))) {
						value.setDatetime(date);
						return true;
					}
				} catch (ParseException e) {
				}
			}

			return false;
		}
	}

}
