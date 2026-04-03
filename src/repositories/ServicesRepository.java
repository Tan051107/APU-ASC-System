package repositories;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import exceptions.NotFoundException;
import mapper.interfaces.Mapper;
import models.BaseModel;
import repositories.interfaces.BaseRepository;


import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ServicesRepository<T extends BaseModel> implements BaseRepository<T> {
    private final String filePath;
    private final Mapper<T> mapper;

    public ServicesRepository(String filePath, Mapper<T> mapper) {
        this.filePath = filePath;
        this.mapper = mapper;
    }


    @Override
    public List<T> getAll() throws FileCorruptedException {
        List<T> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) !=null){
                T object =  mapper.toObject(line);
                list.add(object);
            }
        }
        catch (IOException e){
            throw new FileCorruptedException("Failed to get data in file");
        }
        return list;
    }

    @Override
    public T getOne(String id) throws FileCorruptedException{
        List<T> objects= getAll();

        return objects.stream()
                .filter(object->object.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> getAll(Predicate<T> filter) throws FileCorruptedException {
        return getAll().stream()
                .filter(filter)
                .toList();
    }

    @Override
    public void update(T objectToUpdate) throws FileCorruptedException, NotFoundException {
        List<T> objects = getAll();
        boolean isExists = false;
        for(int i = 0 ; i< objects.size() ; i++){
            if(objects.get(i).getId().equalsIgnoreCase(objectToUpdate.getId())){
                objects.get(i).setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));
                objects.set(i,objectToUpdate);
                isExists = true;
            }
        }
        if(isExists){
            writeAll(objects);
        }
        else{
            throw new NotFoundException("Id not found");
        }
    }

    @Override
    public void create(T object) throws IOException {
        object.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));
        object.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){
            String row = mapper.toString(object);
            writer.println(row);
        }
    }

    @Override
    public void delete(String id) throws DeleteException {
        try{
            List<T> objects = getAll();
            if(objects.isEmpty()){
                throw new DeleteException("File is empty");
            }
            boolean isExists = false;
            for(int i = 0 ; i< objects.size() ; i++){
                if(objects.get(i).getId().equals(id)){
                    objects.remove(objects.get(i));
                    isExists = true;
                }
            }
            if(isExists){
                writeAll(objects);
            }
            else{
                throw new DeleteException("Id not found");
            }
        }
        catch (FileCorruptedException e){
            throw new DeleteException("File is corrupted");
        }
    }

    private void writeAll(List<T> objects){
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            for(T object:objects){
                String row = mapper.toString(object);
                writer.println(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
