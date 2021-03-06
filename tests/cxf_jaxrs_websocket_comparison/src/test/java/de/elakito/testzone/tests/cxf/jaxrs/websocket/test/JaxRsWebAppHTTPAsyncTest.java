package de.elakito.testzone.tests.cxf.jaxrs.websocket.test;

import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Assert;

public class JaxRsWebAppHTTPAsyncTest extends JaxRsWebAppBaseTest {
    private WebClient client;
    private AsyncInvoker invoker;
    private Entity<?> body;
    private Future<?>[] asyncResults = new Future<?>[CALL_COUNT];
    private int index;
        
    @Override
    protected String getWebResourcePath() {
        return "/jaxrs_http_webapp";
    }

    @Override
    protected void setUpClient() {
        // taken care at before*Test
    }


    @Override
    protected void cleanUpClient() {
        // taken care at after*Test
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
                client = WebClient.create("http://localhost:8181/endpoint/get/10");
                invoker = client.async();
                index = 0;
            }
            @Override
            public void afterMethodTest() {
                Assert.assertFalse("Not done after 5 secs", waitForResults(asyncResults, 5000));
                
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
                client = WebClient.create("http://localhost:8181/endpoint/post/20");
                invoker = client.async();
                body = Entity.text("22");
                index = 0;
            }
            @Override
            public void afterMethodTest() {
                Assert.assertFalse("Not done after 5 secs", waitForResults(asyncResults, 5000));
                
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
