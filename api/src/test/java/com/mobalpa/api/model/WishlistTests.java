package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mobalpa.api.repository.UserRepository;

@DataJpaTest
public class WishlistTests {

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAddItemToEmptyWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItems("[]");
        user.setWishlist(wishlist);
        userRepository.save(user);

        List<WishlistItem> items = new ArrayList<>();
        items.add(new WishlistItem("12345", "Canapé Moderne", 1));
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {
        }).size());
    }

    @Test
    public void testAddItemToExistingWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Wishlist wishlist = new Wishlist();
        wishlist.setItems("[{\"productId\": \"12345\", \"productName\": \"Canapé Moderne\", \"quantity\": 1}, {\"productId\": \"67890\", \"productName\": \"Table Basse\", \"quantity\": 1}]");
        wishlist.setUser(user);
        user.setWishlist(wishlist);
        userRepository.save(user);

        List<WishlistItem> items = objectMapper.readValue(user.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {});
        items.add(new WishlistItem("54321", "Chaise", 1));
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(3, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).size());
    }

    @Test
    public void testAddSecondProductToWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        List<WishlistItem> items = new ArrayList<>();
        items.add(new WishlistItem("12345", "Canapé Moderne", 1));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItems(objectMapper.writeValueAsString(items));
        user.setWishlist(wishlist);
        userRepository.save(user);

        items.add(new WishlistItem("67890", "Table Basse", 1));
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(2, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).size());
    }

    @Test
    public void testRemoveItemFromEmptyWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItems("[]");
        user.setWishlist(wishlist);
        userRepository.save(user);

        List<WishlistItem> items = new ArrayList<>();
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(0, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).size());
    }

    @Test
    public void testRemoveItemFromExistingWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        List<WishlistItem> items = new ArrayList<>();
        items.add(new WishlistItem("12345", "Canapé Moderne", 1));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItems(objectMapper.writeValueAsString(items));
        user.setWishlist(wishlist);
        userRepository.save(user);

        items.removeIf(item -> item.getProductId().equals("12345"));
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(0, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).size());
    }

    @Test
    public void testRemoveQuantityFromWishlist() throws Exception {
        User user = new User();
        user.setFirstname("Emma");
        user.setLastname("Stone");
        user.setEmail("emma.stone@example.com");
        user.setPassword("password");
        user.setPhoneNumber("6667778888");
        user.setBirthdate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setZipcode("12345");
        user.setCity("Anytown");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        List<WishlistItem> items = new ArrayList<>();
        items.add(new WishlistItem("12345", "Canapé Moderne", 2));
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setItems(objectMapper.writeValueAsString(items));
        user.setWishlist(wishlist);
        userRepository.save(user);

        items.stream().filter(item -> item.getProductId().equals("12345")).forEach(item -> item.setQuantity(item.getQuantity() - 1));
        user.getWishlist().setItems(objectMapper.writeValueAsString(items));
        userRepository.save(user);

        User savedUser = userRepository.findById(user.getUuid()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(1, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).size());
        assertEquals(1, objectMapper.readValue(savedUser.getWishlist().getItems(), new TypeReference<List<WishlistItem>>() {}).get(0).getQuantity());
    }
}