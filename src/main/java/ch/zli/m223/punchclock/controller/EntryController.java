package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/entries")
public class EntryController {
    private EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> getAllEntries() {
        return entryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry createEntry(@Valid @RequestBody Entry entry) {
        return entryService.createEntry(entry);
    }

    @RequestMapping(value = "/{entryId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteEntry(@Valid @PathVariable long entryId) {
        if (!entryService.deleteEntry(entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{entryId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Entry updateEntry(@Valid @PathVariable long entryId, @Valid @RequestBody Entry entry) {
        if (entryService.updateEntry(entryId, entry) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return entry;
    }

}
