package be.sonck.itunes.libreader.impl.service.mapper;

import be.sonck.itunes.libreader.impl.service.model.Dictionary;

/**
 * Created by johansonck on 08/02/16.
 */
public interface DictionaryMapper<T> {

    T mapDictionary(Dictionary dictionary);
}
