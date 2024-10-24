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

        String url = "https://www.dlsite.com/maniax/work/=/product_id/RJ378488.html" ;

        try {
            Document document = Jsoup.connect(url).get();

            Elements select = document.getElementsByTag("table")
                    .attr("cellspacing", "0")
                    .attr("id", "work_outline")
                    .select("tr");

//            System.out.println(document.getElementsByTag("h1").attr("id" , "work_name"));

            for (Element element : select) {

                System.out.println(element);
                System.out.println("\n \n");

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testDlsite2() {
        System.setProperty("http.proxyHost" , "127.0.0.1");
        System.setProperty("https.proxyHost" , "127.0.0.1");
        System.setProperty("http.proxyPort" , "7890");
        System.setProperty("https.proxyPort" , "7890");

        String url = "https://www.dlsite.com/maniax/fsr/=/language/jp/sex_category%5B0%5D/male/keyword/%E5%AF%86%E7%9D%80/order%5B0%5D/trend/options_and_or/and/per_page/30/from/fs.header" ;

        try {
            Document document = Jsoup.connect(url).get();

            Elements elements = document.getElementsByTag("div")
                    .attr("id" , "search_result_img_box_inner type_exclusive_01 ")
                    .attr("class" , "multiline_truncate")
                    .select("a[href][title]");

            for (Element element : elements) {
                System.out.println(element.attr("href"));
                System.out.println(element.attr("title"));

                System.out.println("\n \n");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
