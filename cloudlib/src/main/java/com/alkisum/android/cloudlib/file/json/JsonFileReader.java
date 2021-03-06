package com.alkisum.android.cloudlib.file.json;

import android.os.AsyncTask;

import com.alkisum.android.cloudlib.events.JsonFileReaderEvent;
import com.alkisum.android.cloudlib.file.CloudFile;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Task reading data from files and converting it to JSON file objects.
 *
 * @author Alkisum
 * @version 1.3
 * @since 1.0
 */
public class JsonFileReader extends AsyncTask<Void, Void, List<JsonFile>> {

    /**
     * List of files to read.
     */
    private final List<CloudFile> files;

    /**
     * Exception that can be set when an exception is caught during the task.
     */
    private Exception exception;

    /**
     * Subscriber ids allowed to process the events.
     */
    private final Integer[] subscriberIds;

    /**
     * JsonFileReader constructor.
     *
     * @param files         List of files to read
     * @param subscriberIds Subscriber ids allowed to process the events
     */
    public JsonFileReader(final List<CloudFile> files,
                          final Integer[] subscriberIds) {
        this.files = files;
        this.subscriberIds = subscriberIds;
    }

    @Override
    protected final List<JsonFile> doInBackground(final Void... params) {
        List<JsonFile> jsonFiles = new ArrayList<>();
        BufferedReader br = null;
        try {
            for (CloudFile file : files) {
                br = new BufferedReader(new FileReader(file.getFile()));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String jsonString = sb.toString();
                JsonFile jsonFile = new JsonFile(
                        file.getName(),
                        new JSONObject(jsonString),
                        file.getFile(),
                        file.getCreationTime(),
                        file.getModifiedTime());
                jsonFiles.add(jsonFile);
            }
        } catch (IOException | JSONException e) {
            exception = e;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                exception = e;
            }
        }
        return jsonFiles;
    }

    @Override
    protected final void onPostExecute(final List<JsonFile> jsonFiles) {
        if (exception == null) {
            EventBus.getDefault().post(new JsonFileReaderEvent(subscriberIds,
                    JsonFileReaderEvent.OK, jsonFiles));
        } else {
            EventBus.getDefault().post(new JsonFileReaderEvent(subscriberIds,
                    JsonFileReaderEvent.ERROR, exception));
        }
    }
}
