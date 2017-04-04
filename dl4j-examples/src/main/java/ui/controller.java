package ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang.WordUtils;
import org.deeplearning4j.examples.nlp.word2vec.Word2VecRawLearning;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

/**
 * Created by akshayrajgollahalli on 15/08/15.
 */

public class controller{

    @FXML
    private Button openFileBtn;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField nounTxtF;

    @FXML
    private TextField wordCountTxtF;

    @FXML
    private Button designBtn;

    @FXML
    private Text sloganTxt;

    private String noun;
    private int wordcount;

    File document;

    @FXML
    private void initialize(){
        /* Step 1: Select file */
        openFileBtn.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text based documents", "*.txt", "*.pdf" ,"*.doc"  ,"*.docx");
            fileChooser.getExtensionFilters().add(extFilter);

            Stage primaryStage = (Stage) openFileBtn.getScene().getWindow();

            document = fileChooser.showOpenDialog(primaryStage);

            tabPane.getSelectionModel().select(1);
        });

        /* Step 2: Select noun, num words */
        designBtn.setOnAction((event -> {
            noun =  nounTxtF.getText().toLowerCase();
            wordcount = Integer.parseInt(wordCountTxtF.getText());

            tabPane.getSelectionModel().select(2);


            Stage logStage = new Stage();
            TextArea textArea = new TextArea();
            VBox vbox = new VBox(textArea);
            textArea.setPrefRowCount(50);
            Scene scene = new Scene(vbox, 800, 400);
            logStage.setScene(scene);
            logStage.setY(550);
            logStage.show();



            PrintStream out = new PrintStream(new ByteArrayOutputStream() {
                public synchronized void flush() throws IOException {
                    textArea.setText(toString());
                    textArea.setScrollTop(Double.MAX_VALUE);
                }
            }, true);

            System.setErr(out);
            System.setOut(out);


           Thread worker = new Thread() {
                @Override
                public void run() {
                    learn();

                    design();
                }
            };

            worker.start();

        }));
    }

    private void learn() {
        try {
            Collection<String> sloganWords = Word2VecRawLearning.learn(document, noun, wordcount);

            StringBuilder sb = new StringBuilder();

            sb.append(WordUtils.capitalize(noun)+". ");
            for ( String s : sloganWords ) {
                sb.append(WordUtils.capitalize(s)+". ");
            }
            sb.append("Namics.");

            System.out.println(sb.toString());
            sloganTxt.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Step 3: Design */
    private void design() {
    }


}
