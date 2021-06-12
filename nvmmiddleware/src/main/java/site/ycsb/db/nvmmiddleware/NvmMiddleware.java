/**
 * Copyright (c) 2013 - 2021 YCSB contributors. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

package site.ycsb.db.nvmmiddleware;

import site.ycsb.*;

import java.io.*;
import java.util.*;
import site.ycsb.StringByteIterator;

import com.nvmmiddleware.grpc.client.NvmMiddlewareClient;

/**
 * Class responsible for implementing our nvm middleware client.
 */
public class NvmMiddleware extends DB {
  private static final String HOST_PROPERTY = "nvmmiddleware.host";
  private static final String PORT_PROPERTY = "nvmmiddleware.port";
  private static final String MODE_PROPERTY = "nvmmiddleware.mode";
  private NvmMiddlewareClient client;
  private Properties props;
  private String host;
  private int port;
  private String mode;
    
  @Override
  public void init() throws DBException {
    props = getProperties();
    host = props.getProperty(HOST_PROPERTY, "localhost");
    port = Integer.valueOf(props.getProperty(PORT_PROPERTY, "5000"));
    mode = props.getProperty(MODE_PROPERTY, "interactive");
    setupClient();
  }
    
  private void setupClient() {
    client = new NvmMiddlewareClient(host, port, mode);
  }

  @Override
  public Status read(final String table, final String key, final Set<String> fields,
                            final Map<String, ByteIterator> result) {
    try {
      String value = client.Get(key);
      result.put("response", new StringByteIterator(value));
    } catch (Exception e) {
      return Status.NOT_FOUND;
    }
    return Status.OK;
  }

  @Override
  public Status update(final String table, final String key, final Map<String, ByteIterator> values) {
    try {
      client.Put(key, values.get(key).toString());
    } catch (Exception e) {
      return Status.ERROR;
    }
    return Status.OK;
  }

  @Override
  public Status delete(final String table, final String key) {
    return Status.NOT_IMPLEMENTED;
  }

  @Override
  public Status insert(final String table, final String key, final Map<String, ByteIterator> values) {
    return Status.NOT_IMPLEMENTED;
  }

  @Override
  public Status scan(final String table, final String startkey, final int recordcount, final Set<String> fields,
                          final Vector<HashMap<String, ByteIterator>> result) {
    return Status.NOT_IMPLEMENTED;
  }
}
