package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RestStateStorage {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final File file;
    private final Jsonizer jsonizer;

    public RestStateStorage(File file, Jsonizer jsonizer) {
        this.file = file;
        this.jsonizer = jsonizer;
    }

    public void recoverTo(RestClient client) {
        try {
            if (!file.exists()) return;
            RestState state = jsonizer.from(FileUtils.readFileToString(file), RestState.class);
            client.setState(state);
        } catch (Exception ex) {
            logger.warn("Unable to recover cookies", ex);
        }
    }

    public void storeFrom(RestClient client) {
        try {
            file.getParentFile().mkdirs();
            String json = jsonizer.to(client.getState());
            FileUtils.writeStringToFile(file, json);
        } catch (Exception ex) {
            logger.warn("Unable to store cookies", ex);
        }
    }
}
