/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.apps.android.rest.client;

import com.loopj.android.http.AsyncHttpClient;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.QueryParamsConverter;
import org.apache.http.entity.FileEntity;

import java.io.File;
import java.util.Map;

/**
 * Default impl of the RestClient that delegates calls to AsyncHttpClient
 */
public class DefaultRestClientImpl implements RestClient {

    private AsyncHttpClient client;

    private QueryParamsConverter queryParamsConverter;

    private BodyConverter converter;

    public DefaultRestClientImpl(AsyncHttpClient client, QueryParamsConverter queryParamsConverter, BodyConverter converter) {
        this.client = client;
        this.queryParamsConverter = queryParamsConverter;
        this.converter = converter;
    }

    public DefaultRestClientImpl(AsyncHttpClient client, QueryParamsConverter queryParamsConverter, BodyConverter converter, Map<String, String> defaultHeaders) {
        this(client, queryParamsConverter, converter);
        for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
            client.removeHeader(header.getKey());
            client.addHeader(header.getKey(), header.getValue());
        }
    }

    private <T> void prepareRequest(Callback<T> delegate) {
        delegate.setBodyConverter(converter);
    }

    @Override
    public <T> void get(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.get(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    @Override
    public <T> void delete(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.delete(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    @Override
    public <T> void post(String url, Object body, Callback<T> delegate) {
        prepareRequest(delegate);
        client.post(delegate.getContext(), url, delegate.getAdditionalHeaders(), converter.toRequestBody(body, delegate.getRequestContentType()), delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void post(String url, File file, Callback<T> delegate) {
        prepareRequest(delegate);
        client.post(delegate.getContext(), url, delegate.getAdditionalHeaders(), new FileEntity(file, delegate.getRequestContentType()), delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void put(String url, Object body, Callback<T> delegate) {
        prepareRequest(delegate);
        client.put(delegate.getContext(), url, delegate.getAdditionalHeaders(), converter.toRequestBody(body, delegate.getRequestContentType()), delegate.getRequestContentType(), delegate);
    }

    @Override
    public <T> void head(String url, Callback<T> delegate) {
        prepareRequest(delegate);
        client.head(delegate.getContext(), url, delegate.getAdditionalHeaders(), null, delegate);
    }

    public QueryParamsConverter getQueryParamsConverter() {
        return queryParamsConverter;
    }
}
