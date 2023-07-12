package searchengine.services;

import searchengine.model.Page;
import searchengine.model.Request;
import searchengine.model.Site;
import searchengine.services.responses.ResponseService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    void save(Page page);

    Optional<Page> findPageByPageIdAndSite(int pageId, Site site);

    long pageCount();

    long pageCount(long siteId);

    Optional<Page> findPageByPagePathAndSiteId(String pagePath, int siteId);

    List<Page> getAllPagesBySiteId(int siteId);

    void deletePagesBySiteId(int siteId);
    Optional<Site> getSite (String url);
    Site getSite (int siteId);
    void save(Site site);
    long siteCount();
    List<Site> getAllSites();
    ResponseService getResponse (Request request, String url, int offset, int limit) throws IOException;
}
