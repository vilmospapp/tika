/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.microsoft;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.IOException;

/**
 * Outlook Message Parser.
 */
public class OutlookMessageParser extends AbstractParser {
    public void parse(InputStream stream, ContentHandler handler, Metadata metadata)
            throws IOException, TikaException, SAXException {
        try {
            XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
            xhtml.startDocument();

            MAPIMessage msg = new MAPIMessage(stream);
            metadata.add("from", msg.getDisplayFrom());
            metadata.add("to", msg.getDisplayTo());
            metadata.add(Metadata.SUBJECT, msg.getSubject());
            metadata.add("messageClass", msg.getMessageClass());
            metadata.add("conversationTopic", msg.getConversationTopic());

            xhtml.element("p", msg.getTextBody());
            xhtml.endDocument();
        }
        catch (ChunkNotFoundException ex) {
            throw new TikaException("Error parsing message.");
        }
    }
}
