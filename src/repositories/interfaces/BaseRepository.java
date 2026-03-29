package repositories.interfaces;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import exceptions.NotFoundException;
import models.BaseModel;

import java.util.function.Predicate;
import java.io.IOException;
import java.util.List;

public interface BaseRepository <T extends BaseModel>{

    List<T> getAll() throws IOException, FileCorruptedException;


    T getOne(String id) throws FileCorruptedException, IOException;

    List<T> getAll(Predicate<T> filter) throws FileCorruptedException;

    void update(T object) throws FileCorruptedException, IOException, NotFoundException;
    void create(T object) throws IOException;
    void delete(String id) throws IOException, FileCorruptedException, DeleteException;
}
