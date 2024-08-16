package com.openpayd.exchange.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.openpayd.exchange.exception.CurrencyNotFoundException;
import com.openpayd.exchange.exception.ExternalApiException;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

    @Cacheable(value = "exchangeRates", key = "#sourceCurrency + '-' + #targetCurrency", unless="#result == null")
    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {

        logger.debug("Fetching exchange rate from {} to {}", sourceCurrency, targetCurrency);
        String route = String.format("https://api.currencyapi.com/v3/latest?apikey=%s&base_currency=%s&currencies=%s", apiKey, sourceCurrency, targetCurrency);

        try {
            HttpURLConnection request = createConnection(route);
            request.connect();
            InputStream inputStream = request.getInputStream();
            JsonElement root = JsonParser.parseReader(new InputStreamReader(inputStream));
            JsonObject jsonobj = root.getAsJsonObject();
            JsonObject data = jsonobj.getAsJsonObject("data");

            if (!data.has(targetCurrency)) {
                logger.error("Currency not found: {}", targetCurrency);
                throw new CurrencyNotFoundException(targetCurrency);
            }

            JsonObject targetData = data.getAsJsonObject(targetCurrency);
            BigDecimal rate = targetData.get("value").getAsBigDecimal().setScale(2, RoundingMode.HALF_UP);
            logger.debug("Fetched exchange rate from {} to {}: {}", sourceCurrency, targetCurrency, rate);
            return rate;

        } catch (CurrencyNotFoundException e) {
            // No need to re-throw since the exception is specific and already contains relevant information.
            throw e;
        } catch (IOException e) {
            throw new ExternalApiException("Failed to connect to external API", e);
        } catch (Exception e) {
            throw new ExternalApiException("Unexpected error occurred while fetching exchange rate", e);
        }
    }

    public HttpURLConnection createConnection(String route) {
        try {
            URL url = new URL(route);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new ExternalApiException("Malformed URL: " + route, e);
        } catch (IOException e) {
            throw new ExternalApiException("I/O error while opening connection to URL: " + route, e);
        }
    }

}
