package io.github.canertuzluca.demodisaster.ext.call;

import io.github.canertuzluca.demodisaster.models.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsApiClientImpl implements NewsApiService {

    private final NewsClient newsClient;

    @Autowired
    public NewsApiClientImpl(NewsClient newsClient) {
        this.newsClient = newsClient;
    }

    public NewsResponse getAllNews() {
        return newsClient.getAllNews();
    }
}
