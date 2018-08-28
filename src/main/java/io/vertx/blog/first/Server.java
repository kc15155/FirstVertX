package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class Server extends AbstractVerticle {

    private Map<Integer, Text> currentWords = new LinkedHashMap<>();
    private String [] words = new String [0];

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

                        config().getInteger("http.port", 8144),
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
        String closestWordVal,closestWordLex="";
        String body = context.getBodyAsString();
        String wanted = body.substring(7,body.length()-2);
        Text toAdd = new Text(wanted);
        String [] newWords = new String [words.length+1];
        for (int i=0; i<words.length; i++)
        {
            newWords[i]=words[i];
        }
        newWords[newWords.length-1]=wanted;
        Arrays.sort(newWords);
        words=newWords;
        if (words.length!=1) {
            int j;
            for (j = 0; j < words.length; j++) {
                if (words[j].compareTo(wanted) == 0)
                    break;
            }
            if (j == 0)
                closestWordLex = words[1];
            else if (j == words.length - 1)
                closestWordLex = words[words.length - 2];
            else {
                closestWordLex = findClosestLex(words[j - 1], words[j], words[j + 1]);
            }
        }
        if (!currentWords.isEmpty()) {
            int diffVal = Math.abs(toAdd.getValue()-currentWords.get(0).getValue());
            int closestVal=0;
            for (int i=0; i<currentWords.values().size(); i++)
            {
                if (Math.abs(toAdd.getValue()-currentWords.get(i).getValue())<diffVal)
                {
                    diffVal=Math.abs(toAdd.getValue()-currentWords.get(i).getValue());
                    closestVal=i;
                }
            }
            closestWordVal = currentWords.get(closestVal).getText();
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

    public String findClosestLex (String s1, String s2, String s3)
    {
        int minLength = Math.min(s1.length(),s2.length());
        minLength = Math.min(minLength,s3.length());
        for (int i=0; i<minLength; i++)
        {
            if (Math.abs(s1.charAt(i)-s2.charAt(i))<Math.abs(s3.charAt(i)-s2.charAt(i)))
                return s1;
            if (Math.abs(s1.charAt(i)-s2.charAt(i))>Math.abs(s3.charAt(i)-s2.charAt(i)))
                return s3;
        }
        if (Math.abs(s1.length()-s2.length())<Math.abs(s3.length()-s2.length()))
             return s1;

        return s3;
    }

}
