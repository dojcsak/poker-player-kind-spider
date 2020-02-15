package org.leanpoker.player;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.fluent.Request;
import org.leanpoker.player.model.Card;
import org.apache.http.client.utils.URIBuilder;
import org.leanpoker.player.model.Rank;

import java.net.URISyntaxException;
import java.util.List;

public class RankService {

    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private String RAINMAN_URL = "http://rainman.leanpoker.org/rank";

    public Rank getRank(List<Card> cards) {
        try {
            URIBuilder uriBuilder = new URIBuilder(RAINMAN_URL);
            uriBuilder.setParameter("cards", gson.toJson(cards));

            String response = Request.Get(uriBuilder.build()).execute().returnContent().asString();

            return gson.fromJson(response, Rank.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
