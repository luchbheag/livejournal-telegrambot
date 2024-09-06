package com.github.luchbheag.livejournal_telegrambot.parser;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: make it a component so it can be autowired
@Getter
@Setter
@Component
public class LivejournalParserImpl implements LivejournalParser {
    private int limit;

    @Override
    public ArticlePreview getFirstArticlePreview(String journalName) throws HttpStatusException {
        ArticlePreview articlePreview = null;
        // TODO: try to get this without list, just by one select (except settings)
        try {
            String stringUrl = String.format("https://%s.livejournal.com/", journalName);
            articlePreview = getAllArticlePreviewsFromPageSinceId(stringUrl, 0, 1).get(0);
        } catch (HttpStatusException httpException) {
            // no such a page, should be thrown futher
            httpException.printStackTrace();
            httpException.getStatusCode();
            throw httpException;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return articlePreview;
    }

    @Override
    public List<ArticlePreview> getAllArticlePreviewsSinceId(String journalName, int id) {
        List<ArticlePreview> articlePreviews = new ArrayList<>();
        int skip = 0;
        int size;
        try {
            do  {
                String stringUrl = String.format("https://%s.livejournal.com/?skip=%d", journalName, skip);
                size = articlePreviews.size();
                articlePreviews.addAll(getAllArticlePreviewsFromPageSinceId(stringUrl, id, limit));
                skip += 10;
            } while (articlePreviews.size() < limit && size != articlePreviews.size());
        } catch (HttpStatusException httpException) {
            // no such a page
            httpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return articlePreviews;
    }

    private List<ArticlePreview> getAllArticlePreviewsFromPageSinceId(String stringUrl, int id, int limit) throws HttpStatusException, IOException {
        Document doc = Jsoup.connect(stringUrl).get();
        Elements elements = doc.select("div.post-asset");
        List<ArticlePreview> articlePreviews = new ArrayList<>();
        for (Element element : elements) {
            if (isNotStickyArticle(element)) {
                ArticlePreview articlePreview = getArticlePreviewFromElement(element);
                if (articlePreview.getId() <= id || limit <= articlePreviews.size()) {
                    break;
                }
                articlePreviews.add(articlePreview);
            }
        }
        return articlePreviews;
    }

    private ArticlePreview getArticlePreviewFromElement(Element element) {
        int id = Integer.parseInt(element.id().split("-")[2]);
        Elements h2 = element.select("h2");
        String mainHeader = h2.first().text();
        String subHeader = h2.size() == 1 ? "" : h2.get(1).text();
        String link = element.select("a.subj-link").attr("href");
        // TODO do date element
//        Date date = parseDate(element.select("abbr.datetime").text());
        //Date date = new Date();
        String text = element.select("div.asset-body p").text();

        return new ArticlePreview(id, mainHeader, subHeader, text, link);
    }

    private boolean isNotStickyArticle(Element element) {
        return element.select("h2 span img[title=[sticky post]]").isEmpty();
    }
}
