package com.openpayd.exchange.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ErrorCode;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author suleyman.yildirim
 */
@Service
@Setter
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    @Value("${currencyapi.api.key}")
    private String apiKey;

    @Cacheable("exchangeRates")
    public double getExchangeRate(String sourceCurrency, String targetCurrency) throws CurrencyNotFoundException, IOException {

        logger.debug("Fetching exchange rate from {} to {}", sourceCurrency, targetCurrency);
        String route = String.format("https://api.currencyapi.com/v3/latest?apikey=%s&base_currency=%s&currencies=%s", apiKey, sourceCurrency, targetCurrency);
        HttpURLConnection request = createConnection(route);

        try {
            logger.debug("Connecting to URL: {}", route);
            request.connect();
            InputStream inputStream = request.getInputStream();
            JsonElement root = JsonParser.parseReader(new InputStreamReader(inputStream));
            JsonObject jsonobj = root.getAsJsonObject();
            JsonObject data = jsonobj.getAsJsonObject("data");
            if (!data.has(targetCurrency)) {
                logger.error("Currency not found: {}", targetCurrency);
                throw new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND);
            }
            JsonObject targetData = data.getAsJsonObject(targetCurrency);
            var rate = targetData.get("value").getAsDouble();
            logger.debug("Fetched exchange rate from {} to {}: {}", sourceCurrency, targetCurrency, rate);
            return rate;

        }  catch (CurrencyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while fetching exchange rate from {} to {}", sourceCurrency, targetCurrency, e);
            throw new CurrencyNotFoundException(ErrorCode.CURRENCY_NOT_FOUND);
        }
    }

    protected HttpURLConnection createConnection(String route) throws IOException {
        URL url = new URL(route);
        return (HttpURLConnection) url.openConnection();
    }
}
