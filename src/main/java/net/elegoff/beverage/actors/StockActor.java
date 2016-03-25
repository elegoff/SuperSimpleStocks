package net.elegoff.beverage.actors;


import akka.actor.*;
import akka.japi.Creator;
import net.elegoff.beverage.BeverageTradingException;
import net.elegoff.beverage.Calculator;
import net.elegoff.beverage.StockHelper;
import net.elegoff.beverage.model.Stock;
import net.elegoff.beverage.model.StockType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;



/**
 * @author elegoff
 * Actor classes handling Stock related actions and keeping track of list of Stock objects 
 */
public class StockActor extends UntypedActor{
	
	
	/**
	 * @author elegoff
	 * Stock actor creator
	 */
	public static class StockActorC implements Creator<StockActor> {		
		private static final long serialVersionUID = 1L;

		@Override public StockActor create() {
		    return new StockActor(initStocks());
		  }
		}
	
	
	//Keep track in memory of current known stocks
	private List<Stock> stocks;

	
	
	
	/**
	 * Public contructor
	 * @param stocks : a list of initial Stock objects
	 */
	public StockActor(List<Stock> stocks) {
		super();
		this.stocks = stocks;
	}




	/* (non-Javadoc)
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
	
		//Requesting a list of Stocks
		if (msg instanceof StockMsg.ListStocks){
			getSender().tell(stocks, getSelf());
		}
		//Requesting Dividend Yield calculation
		else if(msg instanceof StockMsg.DividendYield){
			StockMsg.DividendYield m = (StockMsg.DividendYield)msg;
			try{
				Stock stock = StockHelper.getStockBySymbol(m.getSymbol(), stocks);
				double dy = Calculator.calculateDividendYield(stock, m.getPrice());
				getSender().tell(dy, getSelf());	
			}catch(BeverageTradingException be){
				getSender().tell(new Status.Failure(be), getSelf());	
			}catch(NoSuchElementException nse){
				getSender().tell(new Status.Failure(new BeverageTradingException("Symbol : " + m.getSymbol() + " is not found"  )), getSelf());				
			}
			
			
			
		}
		//Requesting P/E Ratio
			else if(msg instanceof StockMsg.PERatio){
				StockMsg.PERatio m = (StockMsg.PERatio)msg;
				try{
					Stock stock = StockHelper.getStockBySymbol(m.getSymbol(), stocks);
					double per = Calculator.calculatePERatio(stock, m.getPrice());
					getSender().tell(per, getSelf());
				}catch(BeverageTradingException be){
					getSender().tell(new Status.Failure(be), getSelf());	
				}catch(NoSuchElementException nse){
					getSender().tell(new Status.Failure(new BeverageTradingException("Symbol : " + m.getSymbol() + " is not found"  )), getSelf());				
			}
				
				
			
		}else{
			unhandled(msg);
		}
	}
	
	
	//provide an initial list of Stocks
	private static List<Stock> initStocks(){
		List<Stock> result= new ArrayList<Stock>();
		
		result.add(new Stock("TEA", StockType.COMMON, 0d,0,100d));
		result.add(new Stock("POP", StockType.COMMON, 8d,0,100d));
		result.add(new Stock("ALE", StockType.COMMON, 23d,0,60d));
		result.add(new Stock("GIN", StockType.PREFERRED, 8d,2,100d));
		result.add(new Stock("JOE", StockType.COMMON, 13d,0,250d));
		
		return result;
	}
	
	

}
