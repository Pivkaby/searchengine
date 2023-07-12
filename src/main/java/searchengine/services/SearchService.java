package searchengine.services;

import searchengine.model.Page;
import searchengine.model.Request;
import searchengine.model.Site;
import searchengine.services.responses.ResponseService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    ResponseService getResponse (Request request, String url, int offset, int limit) throws IOException;
}
