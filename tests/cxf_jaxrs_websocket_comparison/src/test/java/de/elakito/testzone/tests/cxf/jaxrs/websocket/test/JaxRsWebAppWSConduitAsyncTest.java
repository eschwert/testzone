package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Assert;

// disable this for now as there is some issue
@org.junit.Ignore
public class JaxRsWebAppWSConduitAsyncTest extends JaxRsWebAppBaseTest {
    private WebClient client;
    private AsyncInvoker invoker;
    private Entity<String> body;
    private Future<?>[] asyncResults = new Future<?>[CALL_COUNT];
    private int index;
        
    @Override
    protected String getWebResourcePath() {
        return "/jaxrs_ws_webapp";
    }

    @Override
    protected void setUpClient() {
    }

    @Override
    protected void cleanUpClient() {
    }

    @Override
    protected Tester createGETTester() {
        return new Tester() {
            @Override
            public String getMethod() {
                return "GET";
            }
            @Override
            public void invokeMethod() {
                asyncResults[index++] = invoker.get(String.class);
            }
            @Override
            public void beforeMethodTest() {
                client = WebClient.create("ws://localhost:8181/endpoint/get/10");
                invoker = client.async();
                index = 0;
            }
            @Override
            public void afterMethodTest() {
                Assert.assertFalse("Not done", waitForResults(asyncResults, 1000));
                
                for (int i = 0; i < CALL_COUNT; i++) {
                    try {
                        Assert.assertEquals("10", asyncResults[i].get());
                    } catch (Exception e) {
                        Assert.fail("error in retrieving the result");
                    }
                }
                client.close();
            }
        };
    }

    @Override
    protected Tester createPOSTTester() {
        return new Tester() {
            @Override
            public String getMethod() {
                return "POST";
            }
            @Override
            public void invokeMethod() {
                asyncResults[index++] = invoker.post(body, String.class);
            }
            @Override
            public void beforeMethodTest() {
                client = WebClient.create("ws://localhost:8181/endpoint/post");
                client.type("text/plain");
                invoker = client.async();
                body = Entity.text("20");
                index = 0;
            }
            @Override
            public void afterMethodTest() {
                Assert.assertFalse("Not done", waitForResults(asyncResults, 1000));
                
                for (int i = 0; i < CALL_COUNT; i++) {
                    try {
                        Assert.assertEquals("20", asyncResults[i].get());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Assert.fail("error in retrieving the result");
                    }
                }
                client.close();
            }
        };
    }
}
