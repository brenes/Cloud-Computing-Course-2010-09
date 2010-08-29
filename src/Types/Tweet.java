package Types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.hadoop.io.WritableComparable;

public class Tweet implements WritableComparable<Tweet> {

	private Date datetime;
	private String message;
	private SimpleDateFormat longDateFormat;

	public Tweet() {
		this.longDateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
	}

	public Tweet(Date datetime, String message) {
		this.datetime = datetime;
		this.message = message;		
		this.longDateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		String line = input.readUTF();
		String[] tokens = line.split("||", 2);
		try {
			this.setDatetime(this.longDateFormat.parse(tokens[0]));
		} catch (ParseException e) {
		}
		this.setMessage(tokens[1]);

	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.longDateFormat.format(this.getDatetime()) + " || "
				+ this.getMessage());
	}

	@Override
	public int compareTo(Tweet other) {
		return this.getDatetime().equals(other.getDatetime()) ? (this
				.getMessage().compareTo(other.getMessage())) : this
				.getDatetime().compareTo(other.getDatetime());
	}
}
