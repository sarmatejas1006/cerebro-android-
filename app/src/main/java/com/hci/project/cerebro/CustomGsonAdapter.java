package com.hci.project.cerebro;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Malavika Ramprasad on 11/29/2017.
 */

public class CustomGsonAdapter {
    public static class UserAdapter implements JsonSerializer<CreateUser> {
        public JsonElement serialize(CreateUser user, Type typeOfSrc,
                                     JsonSerializationContext context) {
            Gson gson = new Gson();
            JsonElement je = gson.toJsonTree(user);
            JsonObject jo = new JsonObject();
            jo.add("user", je);
            return jo;
        }
    }
}