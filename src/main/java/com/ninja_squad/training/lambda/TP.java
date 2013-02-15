package com.ninja_squad.training.lambda;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Le TP Lambda
 * @author JB
 */
public class TP {

    /**
     * Ecrivez sur la sortie standard, sans utiliser de boucle, la liste des dates des tweets
     */
    public static void step1() {
        List<Date> dates = Tweet.TWEETS.stream().map(t -> t.getDate()).collect(Collectors.toList());
        System.out.println(dates);
    }

    /**
     * Faites la même chose, sans appeler getDate() ni System.out.println()
     */
    public static void step2() {
        Tweet.TWEETS.stream().map(Tweet::getDate).forEach(System.out::println);
    }

    /**
     * Extrayez une List<String> qui contient les senders des tweets
     */
    public static List<String> step3() {
        return Tweet.TWEETS.stream().map(Tweet::getSender).collect(Collectors.toList());
    }

    /**
     * Extrayez une List<String> qui contient les senders des tweets, sans duplicata
     */
    public static List<String> step4() {
        return Tweet.TWEETS.stream().map(Tweet::getSender).distinct().collect(Collectors.toList());
    }

    /**
     * Extrayez une List<String> qui contient les senders des tweets, sans duplicata, triés par ordre alphabétique
     */
    public static List<String> step5() {
        return Tweet.TWEETS.stream().map(Tweet::getSender).sorted().distinct().collect(Collectors.toList());
    }

    /**
     * Extrayez une List<Tweet> qui contient les tweets contenant le hashtag #lambda
     */
    public static List<Tweet> step6() {
        return Tweet.TWEETS.stream().filter(t -> t.getHashTags().contains("#lambda")).collect(Collectors.toList());
    }

    /**
     * Extrayez une List<Tweet> qui contient les tweets contenant le hashtag #lambda, triés par sender puis par date
     */
    public static List<Tweet> step7() {
        return Tweet.TWEETS.stream().filter(t -> t.containsHashTag("#lambda"))
                .sorted(Comparators.<Tweet, String>comparing(Tweet::getSender).thenComparing(Tweet::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Extrayez un Set<String> qui contient l'ensemble des hash tags des tweets
     */
    public static Set<String> step8() {
        return Tweet.TWEETS.stream().explode((Stream.Downstream<String> dwnstream, Tweet t) -> dwnstream.send(t.getHashTags())).collect(Collectors.toSet());
    }

    /**
     * Créez une Map<String, List<Tweet>> qui contient, pour chaque sender, les tweets envoyés par ce sender
     */
    public static Map<String, List<Tweet>> step9() {
        return Tweet.TWEETS.stream().collect(Collectors.groupingBy(Tweet::getSender, HashMap<String, List<Tweet>>::new, ArrayList<Tweet>::new));
    }
    public static Map<String, List<Tweet>> step9_bis() {
        return Tweet.TWEETS.stream().collect(Collectors.groupingBy(Tweet::getSender, Collectors.toList()));
    }

    /**
     * Extrayez deux listes: les tweets qui contiennent le hash tag #lambda, et ceux qui ne les contiennent pas.
     */
    public static Map<Boolean, List<Tweet>> step10() {
        return Tweet.TWEETS.stream().collect(Collectors.groupingBy(t -> t.containsHashTag("#lambda"), Collectors.toList()));
    }

    public static class Stats {

        private int numberOfTweets;
        private int totalTextLength;

        public int getAverage() {
            return totalTextLength / numberOfTweets;
        }

        public int getTotal() {
            return totalTextLength;
        }

        public Stats() {

        }

        public Stats(Tweet t) {
            mixinTweet(t);
        }

        public void mixinTweet(Tweet t) {
            this.numberOfTweets++;
            this.totalTextLength += t.getText().length();
        }

        public Stats reduceStats(Stats st) {
            this.numberOfTweets += st.numberOfTweets;
            this.totalTextLength += st.totalTextLength;
            return this;
        }

    }

    /**
     * Calculez le total et la moyenne du nombre de caractères des textes des tweets.
     * Hints:
     *     Créez une class Stats
     *     Utilisez stream.collect(..., ..., ...) ou stream.map(...).reduce(...)
     */
    public static Stats step11() {
        return Tweet.TWEETS.stream().collect(Stats::new, Stats::mixinTweet, null);
    }
    public static Stats step11_bis() {
        return Tweet.TWEETS.stream().map(Stats::new).reduce(Stats::reduceStats).get();
    }

    /**
     * Faites la même chose, mais de manière parallèle
     */
    public static Stats step12() {
        return Tweet.TWEETS.parallelStream().map(Stats::new).reduce(Stats::reduceStats).get();
    }
}
