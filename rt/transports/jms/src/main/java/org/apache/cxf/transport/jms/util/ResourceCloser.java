/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.transport.jms.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.AbstractSequentialList;
import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ResourceCloser implements Closeable {
    private AbstractSequentialList<Closeable> resources;

    public ResourceCloser() {
        resources = new LinkedList<Closeable>();
    }
    
    public <E extends Closeable> E register(E resource) {
        resources.add(0, resource);
        return resource;
    }
    
    public javax.jms.Connection register(final javax.jms.Connection connection) {
        resources.add(0, new Closeable() {
            
            @Override
            public void close() throws IOException {
                try {
                    connection.close();
                } catch (JMSException e) {
                    // Ignore
                }
            }
        });
        return connection;
    }
    
    public Session register(final Session session) {
        resources.add(0, new Closeable() {
            
            @Override
            public void close() throws IOException {
                try {
                    session.close();
                } catch (JMSException e) {
                    // Ignore
                }
            }
        });
        return session;
    }
    
    public MessageConsumer register(final MessageConsumer consumer) {
        resources.add(0, new Closeable() {
            
            @Override
            public void close() throws IOException {
                try {
                    consumer.close();
                } catch (JMSException e) {
                    // Ignore
                }
            }
        });
        return consumer;
    }
    
    public MessageProducer register(final MessageProducer producer) {
        resources.add(0, new Closeable() {
            
            @Override
            public void close() throws IOException {
                try {
                    producer.close();
                } catch (JMSException e) {
                    // Ignore
                }
            }
        });
        return producer;
    }

    @Override
    public void close() {
        for (Closeable resource : resources) {
            try {
                resource.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    

}
