package mapper.interfaces;

import exceptions.FileCorruptedException;
import models.BaseModel;


public interface Mapper<T extends BaseModel>{

    T toObject(String row) throws FileCorruptedException;

    String toString(T object);
}
