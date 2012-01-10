package net.intelie.lognit.cli.http;

import com.google.inject.Inject;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CookieStorage {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final File file;

    public CookieStorage(File file) {
        this.file = file;
    }

    public void recoverTo(HttpClient client) {
        try {
            if (!file.exists()) return;
            client.getState().addCookies((Cookie[]) SerializationUtils.deserialize(
                    FileUtils.readFileToByteArray(file)));
        } catch (Exception ex) {
            logger.warn("Unable to recover cookies", ex);
        }
    }

    public void storeFrom(HttpClient client) {
        try {
            file.getParentFile().mkdirs();
            FileUtils.writeByteArrayToFile(file,
                    SerializationUtils.serialize(client.getState().getCookies()));
        } catch (Exception ex) {
            logger.warn("Unable to store cookies", ex);
        }
    }
}
