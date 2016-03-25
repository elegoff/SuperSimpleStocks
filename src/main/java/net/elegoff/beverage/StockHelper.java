package net.elegoff.beverage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import net.elegoff.beverage.model.Stock;

public class StockHelper {

	

	
	/**
	 * @param symbol : Stock symbol
	 * @param stocks : a List of Stock objects
	 * @return the Stock instance (if any) for the given symbol
	 * @throws NoSuchElementException
	 */
	public static  Stock getStockBySymbol(String symbol, List<Stock> stocks) throws NoSuchElementException{
		if (symbol == null || symbol.isEmpty()){
			return null;
		}
		
		Optional<Stock> li = stocks.stream().filter(s ->s.getSymbol().equalsIgnoreCase(symbol)).findFirst();
		return li.get();
	}
	
}
