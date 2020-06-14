/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.fishingbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Main {

    private static List<String> possibleMinekraftNames = new ArrayList<>();
    private static final Pattern mcPattern = Pattern.compile("[A-Za-z0-9_]{3,15}");

    public static void main(String[] args) throws InterruptedException {
        BufferedReader words = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("nl.txt")));
        try {
            String line;
            while ((line = words.readLine()) != null) {
                if (mcPattern.matcher(line).matches())
                    possibleMinekraftNames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        ExecutorService scheduler = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            scheduler.execute(() -> new Stresser("K" + possibleMinekraftNames.remove(random.nextInt(possibleMinekraftNames.size()))).start());
            Thread.sleep(4500);
        }
    }
}
