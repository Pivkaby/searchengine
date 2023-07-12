package searchengine.services.implementation;

import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.LemmaAll;
import searchengine.model.Page;
import searchengine.repo.IndexRepository;
import searchengine.repo.LemmaAllRepository;
import searchengine.repo.LemmaRepository;
import searchengine.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import searchengine.indexing.IndexBuilding;
import searchengine.services.responses.FalseResponseService;
import searchengine.services.responses.ResponseService;
import searchengine.services.responses.TrueResponseService;

import java.util.List;

@Service
public class IndexingServiceImpl implements IndexingService {

    private final IndexBuilding indexBuilding;
    private final IndexRepository indexRepository;
    private final LemmaAllRepository lemmaAllRepository;
    private final LemmaRepository lemmaRepository;
    private static final Logger log = LogManager.getLogger();
    public IndexingServiceImpl(IndexBuilding indexBuilding, IndexRepository indexRepository, LemmaAllRepository lemmaAllRepository, LemmaRepository lemmaRepository) {
        this.indexBuilding = indexBuilding;
        this.indexRepository = indexRepository;
        this.lemmaAllRepository = lemmaAllRepository;
        this.lemmaRepository = lemmaRepository;
    }

    @Override
    public ResponseService startIndexingAll() {
        ResponseService response;
        boolean indexing;
        try {
            indexing = indexBuilding.allSiteIndexing();
            log.info("Попытка запуска индексации всех сайтов");
        } catch (InterruptedException e) {
            response = new FalseResponseService("Ошибка запуска индексации");
            log.error("Ошибка запуска индексации", e);
            return response;
        }
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация всех сайтов запущена");
        } else {
            response = new FalseResponseService("Индексация уже запущена");
            log.warn("Индексация всех сайтов не запущена. Т.к. процесс индексации был запущен ранее.");
        }
        return response;
    }

    @Override
    public ResponseService stopIndexing() {
        boolean indexing = indexBuilding.stopSiteIndexing();
        log.info("Попытка остановки индексации");
        ResponseService response;
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация остановлена");
        } else {
            response = new FalseResponseService("Индексация не запущена");
            log.warn("Остановка индексации не может быть выполнена, потому что процесс индексации не запущен.");
        }
        return response;
    }

    @Override
    public ResponseService startIndexingOne(String url) {
        ResponseService resp;
        String response;
        try {
            response = indexBuilding.checkedSiteIndexing(url);
        } catch (InterruptedException e) {
            resp = new FalseResponseService("Ошибка запуска индексации");
            return resp;
        }

        if (response.equals("not found")) {
            resp = new FalseResponseService("Страница находится за пределами сайтов," +
                    " указанных в конфигурационном файле");
        } else if (response.equals("false")) {
            resp = new FalseResponseService("Индексация страницы уже запущена");
        } else {
            resp = new TrueResponseService();
        }
        return resp;
    }

    @Override
    public List<Index> getAllIndexingByLemma(Lemma lemma) {
        return indexRepository.findByLemma(lemma);
    }

    @Override
    public List<Index> getAllIndexingByPage(Page page) {
        return indexRepository.findByPage(page);
    }

    @Override
    public Index getIndexing(Lemma lemma, Page page) {
        Index indexing = null;
        try{
            indexing = indexRepository.findByLemmaAndPage(lemma, page);
        } catch (Exception e) {
            log.info("lemmaId: " + lemma.getId() + " + pageId: " + page.getId() + " not unique");
            e.printStackTrace();
        }
        return indexing;
    }

    @Override
    public synchronized void save(Index index) {
        indexRepository.save(index);
    }

    @Override
    public synchronized void saveAll(Iterable<LemmaAll> entities) {
        lemmaAllRepository.saveAll(entities);
    }

    @Override
    public void saveLemma() {
        lemmaAllRepository.saveLemma();
    }
    @Override
    public void saveIndex() {
        lemmaAllRepository.saveIndex();
    }
    @Override
    public void deleteLemmaAll() {
        lemmaAllRepository.deleteAll();
    }

    @Override
    public List<Lemma> getLemma(String lemmaName) {
        List<Lemma> lemmas = null;
        try {
            lemmas = lemmaRepository.findByLemma(lemmaName);
        } catch (Exception e) {
            log.info(lemmaName);
            e.printStackTrace();
        }
        return lemmas;
    }

    @Override
    public synchronized void save(Lemma lemma) {
        lemmaRepository.save(lemma);
    }

    @Override
    public long lemmaCount() {
        return lemmaRepository.count();
    }

    @Override
    public long lemmaCount(long siteId) {
        return lemmaRepository.count(siteId);
    }

    @Override
    public synchronized void deleteAllLemmas(List<Lemma> lemmaList) {
        if (lemmaList.size() > 0) {
            lemmaRepository.deleteAll(lemmaList);
        } else {
            lemmaRepository.deleteAll();
        }
    }

    @Override
    public List<Lemma> findLemmasByIndexing(List<Index> indexingList) {
        int[] lemmaIdList = new int[indexingList.size()];
        for (int i = 0; i < indexingList.size(); i++) {
            lemmaIdList[i] = indexingList.get(i).getLemmaId();
        }
        return lemmaRepository.findByIds(lemmaIdList);
    }

    @Override
    public void saveLemma(String lemma, int frequency, int siteId) {
        lemmaRepository.saveLemma(lemma, frequency, siteId);
    }

    @Override
    public int findLemmaIdByNameAndSiteId(String lemma, int siteId) {
        return lemmaRepository.findLemmaIdByNameAndSiteId(lemma, siteId);
    }
}
