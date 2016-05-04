/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sweers.catchup.data;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
public class AuthInterceptor implements Interceptor {

  @NonNull private final String accessToken;
  @NonNull private final String key;

  public AuthInterceptor(@NonNull String key, @NonNull String accessToken) {
    this.key = key;
    this.accessToken = accessToken;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request().newBuilder()
        .addHeader("Authorization", key + " " + accessToken)
        .build();
    return chain.proceed(request);
  }
}
