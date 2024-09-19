package com.journaling.journalApp.controllers;

//import com.edigest.journalApp.entity.journalEntry;
//import org.springframework.web.bind.annotation.*;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/_journal")             // maps this whole class to this endpoint
//
//public class JournalEntryController {
////    All the controllers that are used in the project are stored in this directory
//    private Map<Long, journalEntry> journalEntries = new HashMap<>();
//
//    public JournalEntryController() {
//        // Example entries
//        journalEntries.put(1L, new journalEntry(1, "Happy", "I am Happy !"));
//        journalEntries.put(2L, new journalEntry(2, "Sad", "I am Sad !"));
//        journalEntries.put(3L, new journalEntry(3, "Fine", "I am Fine !"));
//    }
//
//    @GetMapping("/get-All")     // localhost:8080/journal/get-ALl
//    public List<journalEntry> getAll(){
//        return new ArrayList<>(journalEntries.values());
//    }
//
//    @PostMapping("/post")           // localhost:8080/journal/post
//    public String createEntry(@RequestBody journalEntry myEntry){
//        journalEntries.put(myEntry.getId(), myEntry);
//        return "Entry Successfully Updated !";
//    }
//
//    @GetMapping("/id/{myID}")       // localhost:8080/journal/id/{myID}
//    public journalEntry getEntryByID(@PathVariable Long myID){
//        return journalEntries.get(myID);
//    }
//
//    @DeleteMapping("/id/{myID}")
//    public String deleteEntryByID(@PathVariable Long myID){
//        journalEntries.remove(myID);
//        return "Entry Successfully Deleted !";
//    }
//
//    @PutMapping("/id/{id}")
//    public boolean updateJournalByID(@PathVariable Long id, @RequestBody journalEntry myEntry){
//        journalEntries.put(id, myEntry);
//        return true;
//    }
//
//}
