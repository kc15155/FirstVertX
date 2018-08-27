package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.LinkedHashMap;
import java.util.Map;


public class Server extends AbstractVerticle {

    private Map<Integer, Text> currentWords = new LinkedHashMap<>();

    public void start(Future<Void> fut) {

        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("Welcome! /analyze to analyze new word, /words to view words.");
        });
        router.route().handler(BodyHandler.create());
        router.post("/analyze").handler(this::analyze);
        router.get("/words").handler(this::getAll);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(

                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }


    public void analyze(RoutingContext context) {
        String body = context.getBodyAsString();
        String wanted = body.substring(7,body.length()-2);
        Text toAdd = new Text(wanted);
        String closestWordVal,closestWordLex;
        if (!currentWords.isEmpty()) {
            int diffLex = Math.abs(toAdd.getText().compareTo(currentWords.get(0).getText()));
            int diffVal = Math.abs(toAdd.getValue()-currentWords.get(0).getValue());
            int closestLex=0;
            int closestVal=0;
            for (int i=0; i<currentWords.values().size(); i++)
            {
                if (Math.abs(toAdd.getValue()-currentWords.get(i).getValue())<diffVal)
                {
                    diffVal=Math.abs(toAdd.getValue()-currentWords.get(i).getValue());
                    closestVal=i;
                }
                if (Math.abs(toAdd.getText().compareTo(currentWords.get(i).getText()))<diffLex)
                {
                    diffLex=Math.abs(toAdd.getText().compareTo(currentWords.get(i).getText()));
                    closestLex=i;
                }
            }
            closestWordVal = currentWords.get(closestVal).getText();
            closestWordLex = currentWords.get(closestLex).getText();
        }
        else
        {
            closestWordLex=null;
            closestWordVal=null;
        }
        currentWords.put(toAdd.getId(),toAdd);
        JsonObject output = new JsonObject().put("value",closestWordVal).put("lexical",closestWordLex);
        context.response().end(Json.encodePrettily(output));
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(currentWords.values()));
    }

}
