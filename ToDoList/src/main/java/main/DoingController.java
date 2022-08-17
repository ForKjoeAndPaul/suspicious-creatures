package main;

import main.model.Doing;
import main.model.DoingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DoingController {

    @Autowired
    private DoingRepository doingRepository;

    public DoingController(DoingRepository doingRepository) {
        this.doingRepository = doingRepository;
    }


    /**
     * Get all doings
     */
    @GetMapping("/doings/")
    public List<Doing> list() {
        Iterable<Doing> doingIterable = doingRepository.findAll();
        ArrayList<Doing> doingArrayList = new ArrayList<>();
        for (Doing doing : doingIterable) {
            doingArrayList.add(doing);
        }
        return doingArrayList;
    }
    /**
     * Add the doing
     */
    @PostMapping("/doings/")
    public int add(Doing doing) {
        Doing newDoing = doingRepository.save(doing);
        return newDoing.getId();
    }

    /**
     * Get the doing by ID
     */
    @GetMapping("/doings/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        Optional<Doing> optionalDoing = doingRepository.findById(id);
        if (!optionalDoing.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(optionalDoing.get(), HttpStatus.OK);
    }


    /**
     * Delete the doing by ID
     */
    @DeleteMapping("/doings/{id}")
    public ResponseEntity<?> remove(@PathVariable int id) {
        Optional<Doing> optionalDoing = doingRepository.findById(id);
        if (!optionalDoing.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            synchronized (optionalDoing) {
                doingRepository.deleteById(id);
            }
            return new ResponseEntity<>("Дело успешно удалено", HttpStatus.OK);
        }
    }

    /**
     * Delete all doings
     */
    @DeleteMapping("/doings/")
    public ResponseEntity<?> removeAll() {
        doingRepository.deleteAll();
        return new ResponseEntity<>("Все данные успешно удалены", HttpStatus.OK);
    }


    /**
     * Change the doing by ID
     */
    @PutMapping("/doings/{id}")
    public ResponseEntity<?> change(@PathVariable int id, String name, String description) {
        Optional<Doing> optionalDoing = doingRepository.findById(id);
        if (!optionalDoing.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            synchronized (optionalDoing) {
                Doing doing = optionalDoing.get();
                doing.setName(name);
                doing.setDescription(description);
                doingRepository.save(doing);
            }
            return new ResponseEntity<>("Дело успешно обновлено", HttpStatus.OK);
        }
    }

    /**
     * Change all doings
     */
    @PutMapping("/doings/")
    public ResponseEntity<?> changeAll(String name, String description) {
        Iterable<Doing> doingIterable = doingRepository.findAll();

        if (doingIterable.iterator().hasNext()) {
            synchronized (doingIterable) {
                for (Doing doing : doingIterable) {
                    doing.setName(name);
                    doing.setDescription(description);
                    doingRepository.save(doing);
                }
            }
            return new ResponseEntity<>("Все дела успешно обновлены", HttpStatus.OK);
        }
        return new ResponseEntity<>("Список дел пуст", HttpStatus.OK);
    }

}
