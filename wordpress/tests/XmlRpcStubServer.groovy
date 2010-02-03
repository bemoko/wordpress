/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright @2010 bemoko 
 */

/*
 * Stub XML RPC server used for unit testing only
 */ 

package wordpress.tests

import org.apache.xmlrpc.client.XmlRpcLocalTransportFactory
import org.apache.xmlrpc.client.XmlRpcTransportImpl
import org.apache.xmlrpc.client.XmlRpcTransport
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.XmlRpcRequest
import org.apache.xmlrpc.parser.XmlRpcResponseParser
import org.apache.xmlrpc.util.SAXParsers

import org.xml.sax.XMLReader

@Grab(group='org.apache.xmlrpc', module='xmlrpc-client', version='3.1.2')
class XmlRpcStubServer extends XmlRpcLocalTransportFactory {
    public XmlRpcStubServer(XmlRpcClient pClient) {
        super(pClient)
    }
    
    public XmlRpcTransport getTransport(){
        return new StubTransport(getClient())
    }
}

@Grab(group='org.apache.xmlrpc', module='xmlrpc-client', version='3.1.2')
class StubTransport extends XmlRpcTransportImpl {
    
    protected StubTransport(def pClient) {
        super(pClient)
    }
    
    public Object sendRequest(XmlRpcRequest request){
        XMLReader xr = SAXParsers.newXMLReader()
        XmlRpcResponseParser xp;
        xp = new XmlRpcResponseParser(request.config, getClient().getTypeFactory());
        xr.setContentHandler(xp);
        xr.parse(request.config.serverURL.toString())
        
        if (xp.isSuccess()) {
            return xp.getResult();
        }
    }
}