package org.deeplearning4j.examples.nlp.word2vec;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * This is simple example for model weights update after initial vocab building.
 * If you have built your w2v model, and some time later you've decided that it can be
 * additionally trained over new corpus, here's an example how to do it.
 *
 * PLEASE NOTE: At this moment, no new words will be added to vocabulary/model.
 * Only weights update process will be issued. It's often called "frozen vocab training".
 *
 * @author raver119@gmail.com
 */
public class Word2VecUptrainingExample {

    private static Logger log = LoggerFactory.getLogger(Word2VecUptrainingExample.class);

    public static void main(String[] args) throws Exception {
        /*
                Initial model training phase
         */
        String filePath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();

        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        // manual creation of VocabCache and WeightLookupTable usually isn't necessary
        // but in this case we'll need them
        InMemoryLookupCache cache = new InMemoryLookupCache();
        WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>()
                .vectorLength(100)
                .useAdaGrad(false)
                .cache(cache)
                .lr(0.025f).build();

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .epochs(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .lookupTable(table)
                .vocabCache(cache)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        String noun = "office";
        int wordsCount  = 7;

        Collection<String> lst = vec.wordsNearest(noun, wordsCount);
        log.info("Closest words on 1st run: " + lst);

        System.out.println("");
        System.out.println("");
        System.out.println("");

        System.out.print(toTitleCase(noun)+". ");
        for(String s : lst) {
            System.out.print(toTitleCase(s) + ". ");
        }
        System.out.println("Namics. ");
        System.out.println("");
        System.out.println("");


        /*
            at this moment we're supposed to have model built, and it can be saved for future use.
         */
        WordVectorSerializer.writeFullModel(vec, "pathToSaveModel.txt");

        /*
            Let's assume that some time passed, and now we have new corpus to be used to weights update.
            Instead of building new model over joint corpus, we can use weights update mode.
         */
        Word2Vec word2Vec = WordVectorSerializer.loadFullModel("pathToSaveModel.txt");

        /*
            PLEASE NOTE: after model is restored, it's still required to set SentenceIterator and TokenizerFactory, if you're going to train this model
         */
        SentenceIterator iterator = new BasicLineIterator(filePath);
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        word2Vec.setTokenizerFactory(tokenizerFactory);
        word2Vec.setSentenceIterator(iterator);


        log.info("Word2vec uptraining...");

        word2Vec.fit();

        lst = word2Vec.wordsNearest(noun, wordsCount);
        log.info("Closest words on 2nd : " + lst);


        System.out.println("");
        System.out.println("");
        System.out.println("");

        System.out.print(toTitleCase(noun)+". ");
        for(String s : lst) {
            System.out.print(toTitleCase(s) + ". ");
        }
        System.out.println("Namics. ");
        System.out.println("");
        System.out.println("");

        /*
            Model can be saved for future use now
         */
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
