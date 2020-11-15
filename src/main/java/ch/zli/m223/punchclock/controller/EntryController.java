package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
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
    public List<Entry> getAllEntries(Principal user) {
        List<Entry> entries = entryService.findAll(user.getName());
        // Remove user details as a security precaution
        entries.forEach((entry -> entry.setUser(null)));
        return entries;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry createEntry(Principal user, @Valid @RequestBody Entry entry) {
        return entryService.createEntry(user.getName(), entry);
    }

    @RequestMapping(value = "/{entryId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteEntry(Principal user, @Valid @PathVariable long entryId) {
        if (!entryService.deleteEntry(user.getName(), entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{entryId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Entry updateEntry(Principal user, @Valid @PathVariable long entryId, @Valid @RequestBody Entry entry) {
        if (entryService.updateEntry(user.getName(), entryId, entry) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return entry;
    }

}
