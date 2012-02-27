package net.intelie.lognit.cli.state;

import net.intelie.lognit.cli.json.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestState;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class RestStateStorage {

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
            //verbose logging here
        }
    }

    public void storeFrom(RestClient client) {
        try {
            file.getParentFile().mkdirs();
            String json = jsonizer.to(client.getState());
            FileUtils.writeStringToFile(file, json);
        } catch (Exception ex) {
            //verbose logging here
        }
    }
}
