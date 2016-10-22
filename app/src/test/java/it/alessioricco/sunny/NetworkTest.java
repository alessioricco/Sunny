package it.alessioricco.sunny;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import static org.junit.Assert.*;

import it.alessioricco.sunny.util.TestObjectGraphInitializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import util.Environment;

/**
 * Created by alessioricco on 22/10/2016.
 */

public class NetworkTest {

    private static final String TAG = "TEST";

    @Before
    public void init() {
        TestObjectGraphInitializer.init();
    }

    @After
    public void tearDown() {
        TestObjectGraphInitializer.close();
    }

    /**
     * Test OkHttp Client
     *
     * @throws Exception
     */
    @Test
    public void testHTTP() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(new URL(Environment.OPENWEATHER_API_URL)).build();

        Response response = client.newCall(request).execute();
        assertEquals(response.code(),200);
        ResponseBody body = response.body();
        assertNotNull(body);
        String html = body.string();
        assertNotNull(html);
    }

    //todo: test service api calls

}
