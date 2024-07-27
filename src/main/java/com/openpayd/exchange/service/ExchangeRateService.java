package com.openpayd.exchange.service;

import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author suleyman.yildirim
 */
@Service
public class ExchangeRateService {

    @Value("${currencyapi.api.key}")
    private String apiKey;

    public double getExchangeRate(String sourceCurrency, String targetCurrency) throws Exception {
        String route = String.format("https://api.currencyapi.com/v3/latest?apikey=%s&base_currency=%s&currencies=%s", apiKey, sourceCurrency, targetCurrency);
        HttpURLConnection request = createConnection(route);
        request.connect();

        InputStream inputStream = request.getInputStream();
        JsonElement root = JsonParser.parseReader(new InputStreamReader(inputStream));
        JsonObject jsonobj = root.getAsJsonObject();

        JsonObject data = jsonobj.getAsJsonObject("data");

        if (!data.has(targetCurrency)) {
            throw new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND);
        }

        JsonObject targetData = data.getAsJsonObject(targetCurrency);
        return targetData.get("value").getAsDouble();

    }

    protected HttpURLConnection createConnection(String route) throws Exception {
        URL url = new URL(route);
        return (HttpURLConnection) url.openConnection();
    }
}
