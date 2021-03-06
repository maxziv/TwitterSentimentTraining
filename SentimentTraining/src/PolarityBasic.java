import com.aliasi.util.Files;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;

import com.aliasi.lm.NGramProcessLM;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class PolarityBasic {

	File mPolarityDir;
	String[] mCategories;
	DynamicLMClassifier<NGramProcessLM> mClassifier;
	Tweet tweets[];
	String folder = "tweet_training_set";
	String kwidx[] = {"i feel", "i am feeling", "i'm feeling", "i dont feel", "i'm", "Im", "I am", "makes me"};
	String loc[] = {"LA", "NYC", "CHI", "ATL"};
	int posCount = 0;
	int negCount = 0;
	AmazonS3Handler s3;
	private PolarityBasic(String[] args) throws IOException {

		File file = new File(folder+"/.DS_Store");
		file.delete();
		mPolarityDir = new File(folder);
		mCategories = mPolarityDir.list();
		System.out.println("\nData Directory=" + mPolarityDir);
		for(String e:mCategories)
		{
			file = new File(folder+"/"+e+"/.DS_Store");
			file.delete();
			System.out.println(e);
		}
		int nGram = 10;
		mClassifier 
		= DynamicLMClassifier
		.createNGramProcess(mCategories,nGram);

		s3 = new AmazonS3Handler();
	}

	void run() throws ClassNotFoundException, IOException {
		train();
		//		evaluateTest();
		evaluate();
		

	}

	private static File createEmotionFile(Tweet[] tweets) throws IOException {
		File file = File.createTempFile("emotion",".json");
		file.deleteOnExit();

		@SuppressWarnings("resource")
		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write(new Gson().toJson(tweets));
		return file;
	}
	boolean isTrainingFile(File file) {
		return file.getName().charAt(10) != '0';  // test on fold 9
	}

	void train() throws IOException {
		int numTrainingCases = 0;
		int numTrainingChars = 0;
		System.out.println("\nTraining.");
		for (int i = 0; i < mCategories.length; ++i) {
			String category = mCategories[i];
			Classification classification
			= new Classification(category);
			File file = new File(mPolarityDir,mCategories[i]);
			File[] trainFiles = file.listFiles();
			for (int j = 0; j < trainFiles.length; ++j) {
				File trainFile = trainFiles[j];
				if (isTrainingFile(trainFile)) {
					++numTrainingCases;
					String review = Files.readFromFile(trainFile,"ISO-8859-1");
					numTrainingChars += review.length();
					Classified<CharSequence> classified
					= new Classified<CharSequence>(review,classification);
					mClassifier.handle(classified);
				}
			}
		}
		System.out.println("  # Training Cases=" + numTrainingCases);
		System.out.println("  # Training Chars=" + numTrainingChars);
	}
	void evaluateTest() throws IOException {
		System.out.println("\nEvaluating.");
		int numTests = 0;
		int numCorrect = 0;
		for (int i = 0; i < mCategories.length; ++i) {
			String category = mCategories[i];
			File file = new File(mPolarityDir,mCategories[i]);
			File[] trainFiles = file.listFiles();
			for (int j = 0; j < trainFiles.length; ++j) {
				File trainFile = trainFiles[j];
				if (!isTrainingFile(trainFile)) {
					String review = Files.readFromFile(trainFile,"ISO-8859-1");
					++numTests;
					Classification classification
					= mClassifier.classify(review);
					if (classification.bestCategory().equals(category))
						++numCorrect;
				}
			}
		}
		System.out.println("  # Test Cases=" + numTests);
		System.out.println("  # Correct=" + numCorrect);
		System.out.println("  % Correct=" 
				+ ((double)numCorrect)/(double)numTests);
	}

	void evaluate() throws IOException {
		for(int k = 0; k < 1; k++)
		{
			tweets = new JsonToJava().jsonToJava(k);
			System.out.println("\nEvaluating: "+loc[k]);
			int numTests = 0;
			int numCorrect = 0;
			for (int i = 0; i < mCategories.length; ++i) {
				String category = mCategories[i];
				File file = new File(mPolarityDir,mCategories[i]);
				File[] trainFiles = file.listFiles();
				for (int j = 0; j < tweets.length; ++j) {

					String text = tweets[j].getText();
					++numTests;
					Classification classification
					= mClassifier.classify(text);
					String cl = classification.bestCategory();
					if(cl.equals("pos"))
						posCount++;
					else
						negCount++;
					//System.out.println(text+":"+ cl);
					tweets[j].setClassification(cl);
				}
			}
//			s3.putObject(createEmotionFile(tweets), "emotion" + kwidx[k]  + tweets.length+ ".json");
			s3.putObject(createEmotionFile(tweets), "city" + loc[k] +  tweets.length+ ".json");
			System.out.println("posCount "+posCount + ", negCount "+negCount);
		}
	}

	public static void main(String[] args) {
		try {
			new PolarityBasic(args).run();
		} catch (Throwable t) {
			System.out.println("Thrown: " + t);
			t.printStackTrace(System.out);
		}

	}

}

