package be.sonck.itunes.libreader.api.service;

import be.sonck.itunes.libreader.impl.service.SpringConfig;
import be.sonck.itunes.libreader.impl.service.sax.GenericContentHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by johansonck on 04/02/16.
 */
public class ITunesLibraryParser {

    private ApplicationContext applicationContext;

    public static void main(String argv[]) {
        new ITunesLibraryParser().parse();
    }

    public void parse() {
        InputStream inputStream = getClass().getResourceAsStream("/iTunes Library POC.xml");
        GenericContentHandler handler = context().getBean(GenericContentHandler.class);

        parseStream(inputStream, handler);
    }

    private void parseStream(InputStream inputStream, GenericContentHandler handler) {
        try {
            SAXParser saxParser = context().getBean(SAXParser.class);
            saxParser.parse(inputStream, handler);

        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ApplicationContext context() {
        if (applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        }

        return applicationContext;
    }
}
