package com.example.riden.activities.helpers;

import com.example.riden.models.Ride;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private Trie children[];
    private boolean isEndOfWord;
    private Ride ride;

    public Trie() {
        //make 255
        children = new Trie[255];
        ride = null;
        isEndOfWord = false;
    }

    public void insert(Ride ride) {
        String city = ride.getCityDestination();
        city = city.toLowerCase().replace(" ", "");

        Trie curr = this;
        for (char c : city.toCharArray()) {
            if (curr.children[c] == null) {
                curr.children[c] = new Trie();
            }
            curr = curr.children[c];
        }
        curr.isEndOfWord = true;
        curr.ride = ride;
    }

    public List<Ride> findTrieRides(String search) {
        List<Ride> rides;
        Trie curr = this;
        for (char c : search.toCharArray()) {
            curr = curr.children[c];
            if (curr == null) {
                return new ArrayList<>();
            }
        }
        rides = getRidesFromNode(curr);
        return rides;
    }

    static void findAllRides(Trie root, List<Ride> rides) {
        if (root == null) {
            return;
        }

        if (root.isEndOfWord) {
            rides.add(root.ride);
            return;
        }

        for (int i = 0; i < root.children.length; i++) {
            findAllRides(root.children[i], rides);
        }
    }

    static List<Ride> getRidesFromNode(Trie root) {
        if(root == null) {
            return new ArrayList<>();
        }

        List<Ride> rides = new ArrayList<>();
        findAllRides(root, rides);
        return rides;
    }
}
