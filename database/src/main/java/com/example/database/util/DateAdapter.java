package com.example.database.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        if(jsonElement == null){
            return null;
        }else{
            return new Date(jsonElement.getAsLong());
        }

    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        if(date == null){
            return null;
        }else{
         return new JsonPrimitive(date.getTime());
        }
    }
}
