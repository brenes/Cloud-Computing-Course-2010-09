package StopWordMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class NoStopWordCounterMapper extends MapReduceBase implements
		Mapper<LongWritable, Text, Text, LongWritable> {

	public static List<String> stopWords = Arrays.asList(new String[] { "de",
			"la", "que", "el", "en", "y", "a", "los", "del", "se", "las",
			"por", "un", "para", "con", "no", "una", "su", "al", "lo", "como",
			"más", "pero", "sus", "le", "ya", "o", "este", "sí", "porque",
			"esta", "entre", "cuando", "muy", "sin", "sobre", "", "también",
			"me", "hasta", "hay", "donde", "quien", "", "desde", "todo", "nos",
			"durante", "todos", "uno", "les", "ni", "contra", "otros", "ese",
			"eso", "ante", "ellos", "e", "esto", "mí", "antes", "algunos",
			"qué", "unos", "yo", "otro", "otras", "otra", "él", "tanto", "esa",
			"estos", "mucho", "quienes", "nada", "muchos", "cual", "poco",
			"ella", "estar", "estas", "", "algunas", "algo", "nosotros", "",
			"mi", "mis", "tú", "te", "ti", "tu", "tus", "ellas", "nosotras",
			"vosotros", "vosotras", "os", "mío", "mía", "míos", "mías", "tuyo",
			"tuya", "tuyos", "tuyas", "suyo", "suya", "suyos", "suyas",
			"nuestro", "nuestra", "nuestros", "nuestras", "vuestro", "vuestra",
			"vuestros", "vuestras", "esos", "esas", "", "estoy", "estás",
			"está", "estamos", "estáis", "están", "esté", "estés", "estemos",
			"estéis", "estén", "estaré", "estarás", "estará", "estaremos",
			"estaréis", "estarán", "estaría", "estarías", "estaríamos",
			"estaríais", "estarían", "estaba", "estabas", "estábamos",
			"estabais", "estaban", "estuve", "estuviste", "estuvo",
			"estuvimos", "estuvisteis", "estuvieron", "estuviera",
			"estuvieras", "estuviéramos", "estuvierais", "estuvieran",
			"estuviese", "estuvieses", "estuviésemos", "estuvieseis",
			"estuviesen", "estando", "estado", "estada", "estados", "estadas",
			"estad", "", "he", "has", "ha", "hemos", "habéis", "han", "haya",
			"hayas", "hayamos", "hayáis", "hayan", "habré", "habrás", "habrá",
			"habremos", "habréis", "habrán", "habría", "habrías", "habríamos",
			"habríais", "habrían", "había", "habías", "habíamos", "habíais",
			"habían", "hube", "hubiste", "hubo", "hubimos", "hubisteis",
			"hubieron", "hubiera", "hubieras", "hubiéramos", "hubierais",
			"hubieran", "hubiese", "hubieses", "hubiésemos", "hubieseis",
			"hubiesen", "habiendo", "habido", "habida", "habidos", "habidas",
			"", "soy", "eres", "es", "somos", "sois", "son", "sea", "seas",
			"seamos", "seáis", "sean", "seré", "serás", "será", "seremos",
			"seréis", "serán", "sería", "serías", "seríamos", "seríais",
			"serían", "era", "eras", "éramos", "erais", "eran", "fui",
			"fuiste", "fue", "fuimos", "fuisteis", "fueron", "fuera", "fueras",
			"fuéramos", "fuerais", "fueran", "fuese", "fueses", "fuésemos",
			"fueseis", "fuesen", "siendo", "sido", "", "tengo", "tienes",
			"tiene", "tenemos", "tenéis", "tienen", "tenga", "tengas",
			"tengamos", "tengáis", "tengan", "tendré", "tendrás", "tendrá",
			"tendremos", "tendréis", "tendrán", "tendría", "tendrías",
			"tendríamos", "tendríais", "tendrían", "tenía", "tenías",
			"teníamos", "teníais", "tenían", "tuve", "tuviste", "tuvo",
			"tuvimos", "tuvisteis", "tuvieron", "tuviera", "tuvieras",
			"tuviéramos", "tuvierais", "tuvieran", "tuviese", "tuvieses",
			"tuviésemos", "tuvieseis", "tuviesen", "teniendo", "tenido",
			"tenida", "tenidos", "tenidas", "tened", "a", "about", "above",
			"above", "across", "after", "afterwards", "again", "against",
			"all", "almost", "alone", "along", "already", "also", "although",
			"always", "am", "among", "amongst", "amoungst", "amount", "an",
			"and", "another", "any", "anyhow", "anyone", "anything", "anyway",
			"anywhere", "are", "around", "as", "at", "back", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "below", "beside", "besides",
			"between", "beyond", "bill", "both", "bottom", "but", "by", "call",
			"can", "cannot", "cant", "co", "con", "could", "couldnt", "cry",
			"de", "describe", "detail", "do", "done", "down", "due", "during",
			"each", "eg", "eight", "either", "eleven", "else", "elsewhere",
			"empty", "enough", "etc", "even", "ever", "every", "everyone",
			"everything", "everywhere", "except", "few", "fifteen", "fify",
			"fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full",
			"further", "get", "give", "go", "had", "has", "hasnt", "have",
			"he", "hence", "her", "here", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herself", "him", "himself", "his", "how",
			"however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep", "last",
			"latter", "latterly", "least", "less", "ltd", "made", "many",
			"may", "me", "meanwhile", "might", "mill", "mine", "more",
			"moreover", "most", "mostly", "move", "much", "must", "my",
			"myself", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "not",
			"nothing", "now", "nowhere", "of", "off", "often", "on", "once",
			"one", "only", "onto", "or", "other", "others", "otherwise", "our",
			"ours", "ourselves", "out", "over", "own", "part", "per",
			"perhaps", "please", "put", "rather", "re", "same", "see", "seem",
			"seemed", "seeming", "seems", "serious", "several", "she",
			"should", "show", "side", "since", "sincere", "six", "sixty", "so",
			"some", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "such", "system", "take", "ten", "than",
			"that", "the", "their", "them", "themselves", "then", "thence",
			"there", "thereafter", "thereby", "therefore", "therein",
			"thereupon", "these", "they", "thickv", "thin", "third", "this",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "to", "together", "too", "top", "toward", "towards",
			"twelve", "twenty", "two", "un", "under", "until", "up", "upon",
			"us", "very", "via", "was", "we", "well", "were", "what",
			"whatever", "when", "whence", "whenever", "where", "whereafter",
			"whereas", "whereby", "wherein", "whereupon", "wherever",
			"whether", "which", "while", "whither", "who", "whoever", "whole",
			"whom", "whose", "why", "will", "with", "within", "without",
			"would", "yet", "you", "your", "yours", "yourself", "yourselves",
			"the" });

	public static LongWritable one = new LongWritable(1);

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, LongWritable> output, Reporter reporter)
			throws IOException {
		StringTokenizer tokens = new StringTokenizer(value.toString());

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (!NoStopWordCounterMapper.stopWords.contains(token)) {
				output.collect(new Text(token), one);
			}
		}

	}

}
