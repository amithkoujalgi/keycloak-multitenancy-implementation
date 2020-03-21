package com.amithkoujalgi.auth.util;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;

public class HTTPUtil {

	public static void post( String url, Object body, Map<String, String> headers ) throws IOException
	{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		String json = new Gson().toJson(body);
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		for( Map.Entry e : headers.entrySet() )
		{
			httpPost.setHeader((String) e.getKey(), (String) e.getValue());
		}

		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.toString());
		client.close();
	}
}
