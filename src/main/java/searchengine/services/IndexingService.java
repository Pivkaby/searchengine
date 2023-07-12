package searchengine.services;
import searchengine.model.*;
import searchengine.responses.ResponseService;

import java.util.List;
import java.util.Optional;

public interface IndexingService {
    ResponseService startIndexingAll();
    ResponseService stopIndexing();
    ResponseService startIndexingOne(String url);
    List<Index> getAllIndexingByLemma(Lemma lemma);
    List<Index> getAllIndexingByPage(Page page);
    Index getIndexing(Lemma lemma, Page page);
    void save(Index indexing);
    void saveAll(Iterable<LemmaAll> entities);
    void saveLemma();
    void saveIndex();
    void deleteLemmaAll();
    List<Lemma> getLemma (String lemmaName);
    void save(Lemma lemma);
    long lemmaCount();
    long lemmaCount(long siteId);
    void deleteAllLemmas(List<Lemma> lemmaList);
    List<Lemma> findLemmasByIndexing(List<Index> indexingList);
    void saveLemma(String lemma, int frequency, int siteId);
    int findLemmaIdByNameAndSiteId(String lemma, int siteId);
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
}
