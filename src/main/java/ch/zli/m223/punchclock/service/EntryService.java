package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.PunchClockUser;
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
        Entry savedEntry = entryRepository.saveAndFlush(entry);
        savedEntry.setUser(null);
        return savedEntry;
    }

    public Boolean deleteEntry(String username, long entryId) {
        if (entryRepository.existsById(entryId)) {
            entryRepository.deleteDistinctByUser_UsernameAndId(username, entryId);
            return true;
        }
        return false;
    }

    public Entry updateEntry(String username, long entryId, Entry entry) {
        entry.setId(entryId);
        if (entryRepository.existsById(entryId) && entry.getUser().getUsername().equals(username)) {
            entryRepository.deleteById(entryId);
            Entry updatedEntry = entryRepository.saveAndFlush(entry);
            updatedEntry.setUser(null);
            return updatedEntry;
        }
        return null;
    }

    public List<Entry> findAll(String username) {
        return entryRepository.findEntriesByUser_Username(username);
    }
}
