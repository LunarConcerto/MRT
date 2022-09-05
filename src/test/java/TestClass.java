import com.github.lunarconcerto.magicalrenametool.MRTStarter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClass {

    @Test
    void method1() throws URISyntaxException, MalformedURLException {
        String path = "com/github/lunarconcerto/magicalrenametool/rule";

        URL url = MRTStarter.class.getClassLoader().getResource(path);

        assert url != null;

        File root = new File(url.toURI());

        File[] files = root.listFiles();

        assert files != null;

        for (File file : files) {
            try (URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()})) {
                Class<?> aClass = loader.loadClass(file.getName());
                System.out.println(1);
                System.out.println(aClass.getName());
                System.out.println(aClass.getTypeName());
            }catch (Exception e){

            }


        }
    }


}
