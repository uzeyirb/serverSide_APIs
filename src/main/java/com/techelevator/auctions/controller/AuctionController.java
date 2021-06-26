package com.techelevator.auctions.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.techelevator.auctions.DAO.AuctionDAO;
import com.techelevator.auctions.exception.AuctionNotFoundException;
import com.techelevator.auctions.model.Auction;

import javax.validation.Valid;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionDAO dao;

    public AuctionController(AuctionDAO dao) {
        this.dao = dao;
    }

    /**
     * Gets auction list (GET)
     * @param title_like
     * @param currentBid_lte
     * @return
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Auction> list(@Valid @RequestParam(defaultValue = "") String title_like, @RequestParam(defaultValue = "0") double currentBid_lte) {

        if (!title_like.equals("")) {
            return dao.searchByTitle(title_like);
        }
        if (currentBid_lte > 0) {
            return dao.searchByPrice(currentBid_lte);
        }

        return dao.list();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Auction get(@PathVariable int id) throws AuctionNotFoundException {
        return dao.get(id);
    }

    /**
     * Creates new auction (POST)
     * @param auction
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Auction create(@Valid @RequestBody Auction auction) {
        return dao.create(auction);
    }

    /**
     * Updates an existing auction (PUT)
     * @param auction
     * @param auctionId
     * @return
     * @throws AuctionNotFoundException
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    //we telling look into request body take json and change it to string representation of the object
    public Auction update(@Valid @RequestBody Auction auction,
                          @PathVariable(name = "id") int auctionId) throws AuctionNotFoundException {

        dao.update(auction, auctionId);
        return auction; //return does the deserialization
    }

    /**
     * Deletes an existing auction (DELETE)
     * @param auctionId
     * @throws AuctionNotFoundException
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name ="id") int auctionId) throws AuctionNotFoundException {
            dao.delete(auctionId);
    }

}
