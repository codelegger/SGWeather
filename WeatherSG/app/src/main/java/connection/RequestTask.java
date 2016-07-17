package connection;

/**
 * Created by uba on 14/7/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;



public abstract class RequestTask extends AsyncTask<String, String, Response> {
    private Context ctx;
    private void incLoadingCounter() {
//        loading++;
    }

    private void decLoadingCounter() {
        //  loading--;
    }

    @Override
    protected void onPreExecute() {
//        incLoadingCounter();
//        if(!progressDialog.isShowing()) {
//            progressDialog.setMessage(getString(R.string.downloading_data));
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//        }
    }

    @Override
    protected Response doInBackground(String... params) {
        Response output = new Response();

        String response = "";
        String urlString = params[0];

//        if (params != null && params.length > 0) {
//            final String zeroParam = params[0];
//            if ("cachedResponse".equals(zeroParam)) {
//                response = params[1];
//                // Actually we did nothing in this case :)
//                output.taskResult = TaskResult.SUCCESS;
//            } else if ("coords".equals(zeroParam)) {
//                String lat = params[1];
//                String lon = params[2];
//                coords = new String[]{lat, lon};
//            }
//        }

        if (response.isEmpty()) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(inputStreamReader);

                    int responseCode = urlConnection.getResponseCode();
                    String line = null;
                    while ((line = r.readLine()) != null) {
                        response += line + "\n";
                    }
                    try {
                        if (r != null) {
                            r.close();
                        }
                    } catch (IOException e) {
                        Log.e("IOException Data", "Error occurred while closing stream");
                    }
                    urlConnection.disconnect();
                    // Background work finished successfully
                    Log.i("Task", "done successfully");
                    output.taskResult = Response.TaskResult.SUCCESS;
                    // Save date/time for latest successful result
                    //saveLastUpdateTime(PreferenceManager.getDefaultSharedPreferences(ctx));todo
                } else if (urlConnection.getResponseCode() == 429) {
                    // Too many requests
                    Log.i("Task", "too many requests");
                    output.taskResult = Response.TaskResult.TOO_MANY_REQUESTS;
                } else {
                    // Bad response from server
                    Log.i("Task", "bad response");
                    output.taskResult = Response.TaskResult.BAD_RESPONSE;
                }
            } catch (IOException e) {
                Log.e("IOException Data", response);
                e.printStackTrace();
                // Exception while reading data from url connection
                output.taskResult = Response.TaskResult.IO_EXCEPTION;
            }
        }

        if (Response.TaskResult.SUCCESS.equals(output.taskResult)) {
            // Parse JSON data
            Response.ParseResult parseResult = parseResponse(response);
            if (Response.ParseResult.DATA_EXCEPTION.equals(parseResult)) {
                // Retain previously specified city if current one was not recognized
//                restorePreviousCity();
            }
            output.parseResult = parseResult;
        }

        return output;
    }

    @Override
    protected void onPostExecute(Response output) {
//        if(loading == 1) {
//            progressDialog.dismiss();
//        }
//        decLoadingCounter();

        updateMainUI();

        handleTaskOutput(output);

    }



    protected void updateMainUI() { }

    protected abstract Response.ParseResult parseResponse(String response);
    protected abstract String getAPIName();
    protected final void handleTaskOutput(Response output) {
        switch (output.taskResult) {
            case SUCCESS: {
//                ParseResult parseResult = output.parseResult;
//                if (ParseResult.CITY_NOT_FOUND.equals(parseResult)) {
//                    Snackbar.make(appView, getString(R.string.msg_city_not_found), Snackbar.LENGTH_LONG).show();
//                } else if (ParseResult.JSON_EXCEPTION.equals(parseResult)) {
//                    Snackbar.make(appView, getString(R.string.msg_err_parsing_json), Snackbar.LENGTH_LONG).show();
//                }
                break;
            }
            case TOO_MANY_REQUESTS: {
//                Snackbar.make(appView, getString(R.string.msg_too_many_requests), Snackbar.LENGTH_LONG).show();
                break;
            }
            case BAD_RESPONSE: {
//                Snackbar.make(appView, getString(R.string.msg_connection_problem), Snackbar.LENGTH_LONG).show();
                break;
            }
            case IO_EXCEPTION: {
//                Snackbar.make(appView, getString(R.string.msg_connection_not_available), Snackbar.LENGTH_LONG).show();
                break;
            }
        }
    }
}