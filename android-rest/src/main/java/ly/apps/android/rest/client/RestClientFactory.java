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
import ly.apps.android.rest.converters.impl.DelegatingConverterService;
import ly.apps.android.rest.converters.impl.JacksonHttpFormValuesConverter;
import ly.apps.android.rest.converters.impl.JacksonBodyConverter;
import ly.apps.android.rest.converters.impl.JacksonQueryParamsConverter;

/**
 * Factory to obtain @see RestClient instances
 */
public class RestClientFactory {

    /**
     * the singleton shared instance
     */
    private final static RestClient instance;

    static {
        instance = new DefaultRestClientImpl(
                new AsyncHttpClient(),
                new JacksonQueryParamsConverter(),
                new DelegatingConverterService(){{
                    addConverter(new JacksonBodyConverter());
                    addConverter(new JacksonHttpFormValuesConverter());
                }}
        );
    }

    /**
     * Static factory method to obtain RestClient instances
     *
     * @return a singleton instance of a RestClient based on @see DefaultRestClientImpl
     */
    public static RestClient getClient() {
        return instance;
    }

}
