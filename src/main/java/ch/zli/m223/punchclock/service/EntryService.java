package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.repository.EntryRepository;
import ch.zli.m223.punchclock.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryService {
    private EntryRepository entryRepository;
    private UserRepository userRepository;

    public EntryService(EntryRepository entryRepository, UserRepository userRepository) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
    }

    public Entry createEntry(String username, Entry entry) {
        entry.setUser(userRepository.findByUsername(username));
        return entryRepository.saveAndFlush(entry);
    }

    public Boolean deleteEntry(String username, long entryId) {
        if (entryRepository.existsById(entryId)) {
            return entryRepository.deleteEntryByIdAndUser_Username(entryId, username) == 1;
        }
        return false;
    }

    public Entry updateEntry(String username, long entryId, Entry entry) {
        if (entryRepository.existsById(entryId)) {
            entry.setUser(userRepository.findByUsername(username));
            entry.setId(entryId);
            entryRepository.deleteById(entryId);
            return entryRepository.saveAndFlush(entry);
        }
        return null;
    }

    public List<Entry> findAll(String username) {
        return entryRepository.findEntriesByUser_Username(username);
    }
}
