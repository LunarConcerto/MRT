import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestJsoup {

    @BeforeEach
    void setUp() {
        System.setProperty("http.proxyHost" , "127.0.0.1");
        System.setProperty("https.proxyHost" , "127.0.0.1");
        System.setProperty("http.proxyPort" , "7890");
        System.setProperty("https.proxyPort" , "7890");
    }

    @Test
    void testDlsite() {
        System.setProperty("http.proxyHost" , "127.0.0.1");
        System.setProperty("https.proxyHost" , "127.0.0.1");
        System.setProperty("http.proxyPort" , "7890");
        System.setProperty("https.proxyPort" , "7890");

        String url = "https://www.dlsite.com/maniax/work/=/product_id/RJ422979.html" ;

        try {
            Document document = Jsoup.connect(url).get();

            Elements select = document.getElementsByTag("table")
                    .attr("cellspacing", "0")
                    .attr("id", "work_outline")
                    .select("tr");

            for (Element element : select) {
                System.out.println(element + "\n\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
