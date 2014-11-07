/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.elakito.testzone.samples.camel.websocket;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class WebsocketProcessor implements Processor {
    public void process(Exchange exchange) throws Exception {
        Object request = exchange.getIn().getBody();
        String text;
        if (request instanceof String) {
        	text = (String)request;
        } else if (request instanceof List) {
        	StringBuilder sb = new StringBuilder();
        	for (Object o : ((List<?>)request)) {
        		sb.append(o).append("\n");
        	}
        	text = sb.toString();
        } else {
        	// unknown
        	text = request.toString();
        }
        exchange.getIn().setBody(text);
    }
}
