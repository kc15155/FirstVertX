package io.vertx.blog.first;

import java.util.concurrent.atomic.AtomicInteger;

public class Text {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id;

    private String text;

    private int value;

    public Text(String input) {
        this.id = COUNTER.getAndIncrement();
        this.text = input;
        this.value = calculateValue(input);
    }

    public int calculateValue(String input) //Assumes no difference between uppercase and lowercase!
    {
        int sum=0;
        for (int i=0; i<input.length(); i++)
        {
            int temp;
            if (input.charAt(i)>96)
                temp = (input.charAt(i))-96;
            else
                temp = (input.charAt(i))-64;
            sum+=temp;
        }
        return sum;
    }

    public String getText() { return text; }

    public int getValue() { return value; }

    public int getId() {
        return id;
    }
}
