package server;

import domain.Customer;
import domain.dealing.*;
import exception.DataIllegalException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class StockMarket {
    private static StockMarket stockMarket=null;
    private List<Instrument> instruments;
    private HashMap<String, Customer> customers;

    private StockMarket(){
        instruments = new ArrayList<>();
        customers = new HashMap<String, Customer>();
    }

    public static StockMarket getInstance(){
        if(stockMarket==null)
            stockMarket=new StockMarket();
        return stockMarket;
    }

    public void addNewCustomer(Customer newOne){
        customers.put(newOne.getId(), newOne);
    }

    public Boolean containCustomer(String id ){
        return customers.containsKey(id);
    }

    public void executeFinancialTransaction(String id, TransactionType type, Long amount){
        customers.get(id).executeTransaction(type, amount);
    }

    public Boolean customerHasEnoughMoney(String id,Long amount){
        return customers.get(id).hasEnoughMoney(amount);
    }

    public void executeSellingOffer(PrintWriter out, SellingOffer offer, String symbol){
        try {
            if(offer.isAdminOffer())
                addOrUpdateInstrumentByAdmin(out,symbol,offer);
            else{
                Instrument instrument = loadVerifiedParameters(offer,symbol);
                instrument.executeSellingByType(out,offer);
            }

        } catch (DataIllegalException e) {
            out.println(e.getMessage());
            return;
        }
    }

    public void executeBuyingOffer(PrintWriter out, BuyingOffer offer, String symbol){
        try {
            if(offer.isAdminOffer())
                deleteOrUpdateInstrumentByAdmin(out,symbol,offer);
            else {
                Instrument instrument = loadVerifiedParameters(offer, symbol);
                instrument.executeBuyingByType(out, offer);
            }
        } catch (DataIllegalException e) {
            out.println(e.getMessage());
            return;
        }
    }

    private void addOrUpdateInstrumentByAdmin(PrintWriter out,String symbol,Offering offer) {
        throw new RuntimeException("No Such Method");
    }
    private void deleteOrUpdateInstrumentByAdmin(PrintWriter out,String symbol,Offering offer) {
        throw new RuntimeException("No Such Method");
    }

    private Instrument loadVerifiedParameters(Offering offer, String symbol) throws DataIllegalException {
        Instrument instrument=null;
        if(!customerIsRegistered(offer))
            throw new DataIllegalException("Unknown id");
        if((instrument= getSymbol(symbol))==null)
            throw new DataIllegalException("Unknown instrument");
        return instrument;
    }


    private Boolean customerIsRegistered(Offering offer){
        if( customers.get(offer.getID())==null)
            return false;
        else
            return true;
    }

    private Instrument getSymbol(String inst){
        for(Instrument instrument : instruments)
            if( instrument.symbolIsMatched(inst))
                return instrument;
        return null;
    }
}