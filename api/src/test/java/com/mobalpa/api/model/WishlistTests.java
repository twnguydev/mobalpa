package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mobalpa.api.repository.UserRepository;
import com.mobalpa.api.repository.WishlistRepository;
import com.mobalpa.api.dto.catalogue.ProductDTO;
import com.mobalpa.api.service.CatalogueService;

@DataJpaTest
public class WishlistTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CatalogueService catalogueService;

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
        user.setWishlist(wishlist);
        userRepository.save(user);

        UUID productUuid = UUID.randomUUID();
        Optional<ProductDTO> productOpt = Optional.ofNullable(catalogueService.getProductById(productUuid));

        if (productOpt.isPresent()) {
            WishlistItem newItem = new WishlistItem();
            newItem.setProductUuid(productUuid);
            newItem.setQuantity(1);
            newItem.setWishlist(wishlist);

            wishlist.getItems().add(newItem);
            wishlistRepository.save(wishlist);

            User savedUser = userRepository.findById(user.getUuid()).orElse(null);
            assertNotNull(savedUser);
            assertEquals(1, savedUser.getWishlist().getItems().size());
        }
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
        wishlist.setUser(user);

        UUID productUuid1 = UUID.randomUUID();
        UUID productUuid2 = UUID.randomUUID();

        if (catalogueService.getProductById(productUuid1) != null && catalogueService.getProductById(productUuid2) != null) {
            WishlistItem item1 = new WishlistItem();
            item1.setProductUuid(productUuid1);
            item1.setQuantity(1);
            item1.setWishlist(wishlist);

            WishlistItem item2 = new WishlistItem();
            item2.setProductUuid(productUuid2);
            item2.setQuantity(1);
            item2.setWishlist(wishlist);

            wishlist.getItems().add(item1);
            wishlist.getItems().add(item2);
            wishlistRepository.save(wishlist);

            User savedUser = userRepository.findById(user.getUuid()).orElse(null);
            assertNotNull(savedUser);
            assertEquals(2, savedUser.getWishlist().getItems().size());
        }
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

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        UUID productUuid1 = UUID.randomUUID();
        UUID productUuid2 = UUID.randomUUID();

        if (catalogueService.getProductById(productUuid1) != null && catalogueService.getProductById(productUuid2) != null) {
            WishlistItem item1 = new WishlistItem();
            item1.setProductUuid(productUuid1);
            item1.setQuantity(1);
            item1.setWishlist(wishlist);

            wishlist.getItems().add(item1);
            wishlistRepository.save(wishlist);

            WishlistItem item2 = new WishlistItem();
            item2.setProductUuid(productUuid2);
            item2.setQuantity(1);
            item2.setWishlist(wishlist);

            wishlist.getItems().add(item2);
            wishlistRepository.save(wishlist);

            User savedUser = userRepository.findById(user.getUuid()).orElse(null);
            assertNotNull(savedUser);
            assertEquals(2, savedUser.getWishlist().getItems().size());
        }
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

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        UUID productUuid = UUID.randomUUID();

        if (catalogueService.getProductById(productUuid) != null) {
            WishlistItem item = new WishlistItem();
            item.setProductUuid(productUuid);
            item.setQuantity(2);
            item.setWishlist(wishlist);

            wishlist.getItems().add(item);
            wishlistRepository.save(wishlist);

            wishlist.getItems().removeIf(existingItem -> existingItem.getProductUuid().equals(productUuid));
            wishlistRepository.save(wishlist);

            User savedUser = userRepository.findById(user.getUuid()).orElse(null);
            assertNotNull(savedUser);
            assertEquals(0, savedUser.getWishlist().getItems().size());
        }
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

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        UUID productUuid = UUID.randomUUID();

        if (catalogueService.getProductById(productUuid) != null) {
            WishlistItem item = new WishlistItem();
            item.setProductUuid(productUuid);
            item.setQuantity(2);
            item.setWishlist(wishlist);

            wishlist.getItems().add(item);
            wishlistRepository.save(wishlist);

            wishlist.getItems().stream().filter(existingItem -> existingItem.getProductUuid().equals(productUuid))
                    .forEach(existingItem -> existingItem.setQuantity(existingItem.getQuantity() - 1));

            wishlistRepository.save(wishlist);

            User savedUser = userRepository.findById(user.getUuid()).orElse(null);
            assertNotNull(savedUser);
            assertEquals(1, savedUser.getWishlist().getItems().get(0).getQuantity());
        }
    }
}