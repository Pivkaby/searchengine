package searchengine.services;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.LemmaAll;
import searchengine.model.Page;
import searchengine.services.responses.ResponseService;

import java.util.List;

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
}
