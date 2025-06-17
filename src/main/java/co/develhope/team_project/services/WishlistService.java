package co.develhope.team_project.services;

import co.develhope.team_project.entities.Wishlist;
import co.develhope.team_project.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    // crea una nuova wishlist:
    public Wishlist addWishlist(Wishlist wishlist) {
        Wishlist nuovaWishlist = wishlistRepository.save(wishlist);

        return nuovaWishlist;
    }

    // ottieni una lista di tutte le wishlist:
    public List<Wishlist> getWishlists() {
        List<Wishlist> listaWishlists = wishlistRepository.findAll();

        return listaWishlists;
    }

    // trova una wishlist per id:
    public Optional<Wishlist> findWishlist(Long id) {
        Optional<Wishlist> wishlistToFind = wishlistRepository.findById(id);

        if (wishlistToFind.isPresent()) {
            return wishlistToFind;
        }
        return Optional.empty();
    }

    // cancella una wishlist:
    public Optional<Wishlist> deleteWishlist(Long id) {
        Optional<Wishlist> wishlistToFind = wishlistRepository.findById(id);

        if (wishlistToFind.isPresent()) {
            wishlistRepository.deleteById(id);
            return wishlistToFind;
        }
        return Optional.empty();
    }

    // modifica una wishlist:
    public Optional<Wishlist> updateWishlist(Long id, Wishlist wishlistDetails) {
        Optional<Wishlist> wishlistToFind = wishlistRepository.findById(id);

        if (wishlistToFind.isPresent()) {

            wishlistToFind.get().setDataCreazione(wishlistDetails.getDataCreazione());

            Wishlist wishlistModificata = wishlistRepository.save(wishlistToFind.get());
            return Optional.of(wishlistModificata);
        }
        return Optional.empty();
    }
}
