package org.deeplearning4j.examples.nlp.word2vec;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Created by Stefan on 04.04.2017..
 */
public class TikaParser {

    public static File parseToPlainText(File source) {

        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(source)) {
            parser.parse(stream, handler, metadata);

            File parsedFile = new File("rawtext.txt");

           FileUtils.writeStringToFile(parsedFile,  handler.toString());
           return parsedFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
