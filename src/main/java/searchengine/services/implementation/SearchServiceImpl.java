package searchengine.services.implementation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repo.PageRepository;
import searchengine.repo.SiteRepository;
import searchengine.search.Search;
import searchengine.model.Request;
import searchengine.services.SearchService;
import searchengine.services.responses.FalseResponseService;
import searchengine.services.responses.ResponseService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Log log = LogFactory.getLog(SearchServiceImpl.class);

    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final Search search;


    public SearchServiceImpl(PageRepository pageRepository, SiteRepository siteRepository, Search search) {
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
        this.search = search;
    }

    @Override
    public synchronized void save(Page page) {
        pageRepository.save(page);
    }

    @Override
    public Optional<Page> findPageByPageIdAndSite(int pageId, Site site) {
        return pageRepository.findByIdAndSite(pageId, site);
    }

    @Override
    public long pageCount() {
        return pageRepository.count();
    }

    @Override
    public long pageCount(long siteId) {
        return pageRepository.count(siteId);
    }

    @Override
    public Optional<Page> findPageByPagePathAndSiteId(String pagePath, int siteId) {
        return pageRepository.findByPathAndSiteId(pagePath, siteId);
    }
    @Override
    public List<Page> getAllPagesBySiteId(int siteId) {
        return pageRepository.getAllPagesBySiteId(siteId);
    }
    @Override
    public synchronized void deletePagesBySiteId(int siteId) {
        pageRepository.deleteAll(getAllPagesBySiteId(siteId));
    }

    @Override
    public Optional<Site> getSite(String url) {
        return siteRepository.findByUrl(url);
    }
    public Site getSite(int siteId) {
        Optional<Site> optional = siteRepository.findById(siteId);
        Site site = null;
        if(optional.isPresent()){
            site = optional.get();
        }
        return site;
    }

    @Override
    public synchronized void save(Site site) {
        siteRepository.save(site);
    }

    @Override
    public long siteCount(){
        return siteRepository.count();
    }

    @Override
    public List<Site> getAllSites() {
        List<Site> siteList = new ArrayList<>();
        Iterable<Site> it = siteRepository.findAll();
        it.forEach(siteList::add);
        return siteList;
    }

    @Override
    public ResponseService getResponse(Request request, String url, int offset, int limit) {
        log.info("Запрос на поиск строки- \"" + request.getReq() + "\"");
        ResponseService response;
        if (request.getReq().equals("")){
            response = new FalseResponseService("Задан пустой поисковый запрос");
            log.warn("Задан пустой поисковый запрос");
            return response;
            }
        if(url.equals("")) {
            response = search.searchService(request, null, offset, limit);
        } else {
            response = search.searchService(request, url, offset, limit);
        }
        if (response.getResult()) {
            log.info("Запрос на поиск строки обработан, результат получен.");
            return response;
        } else {
            log.warn("Запрос на поиск строки обработан, указанная страница не найдена.");
            return new FalseResponseService("Указанная страница не найдена");
        }
    }
}
