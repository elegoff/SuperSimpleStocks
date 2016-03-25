package net.elegoff.beverage;



import java.util.List;

import org.junit.Assert;
import org.junit.Test;


import net.elegoff.beverage.model.Stock;
import net.elegoff.beverage.model.Trade;
import net.elegoff.beverage.model.TradeType;

public class ControllerTest {

	@Test
	public void stockListTest() {
		List<Stock> stocks = Controller.getStockList();
		
		Assert.assertNotNull(stocks);
		
		
		//Initial stock list is created with 5 stocks
		Assert.assertEquals(5, stocks.size());
		
	}
	
	@Test
	public void tradeListTest() {
		List<Trade> trades = Controller.getTradeList();
		
		Assert.assertNotNull(trades);
		
	}
	
	
	@Test(expected= BeverageTradingException.class)
	public void dividendYieldUnknownTest() throws BeverageTradingException{
			Controller.getDividendYield("UNKNOWN", 10);
		
	}
	
	@Test(expected= BeverageTradingException.class)
	public void PERatioUnknownTest() throws BeverageTradingException{
			Controller.getPERatio("UNKNOWN", 10);
		
	}
	
	@Test(expected= BeverageTradingException.class)
	public void dividendYieldPriceZero() throws BeverageTradingException{
			Controller.getDividendYield("TEA", 0);
		
	}
	
	@Test(expected= BeverageTradingException.class)
	public void PERatioDividendZero() throws BeverageTradingException{
			Controller.getPERatio("TEA", 42);
		
	}
	
	
	@Test
	public void dividendYieldCommonOK() throws BeverageTradingException{
			double res =Controller.getDividendYield("POP", 2);
		Assert.assertEquals(4, res,0);
	}
	
	@Test
	public void PERatioCommonOK() throws BeverageTradingException{
			double res =Controller.getPERatio("POP", 2);
		Assert.assertEquals(0.25, res,0);
	}

	
	
	@Test
	public void dividendYieldPrefOK() throws BeverageTradingException{
			double res =Controller.getDividendYield("GIN", 2);
		Assert.assertEquals(1, res,0);
	}
	
	@Test
	public void PERatioPrefOK() throws BeverageTradingException{
			double res =Controller.getPERatio("GIN", 2);
		Assert.assertEquals(0.25, res,0);
	}
	
	
	@Test 
	public void recordBuyTradeTest() throws BeverageTradingException{
		Trade trade = Controller.recordTrade("GIN", 12, TradeType.BUY, 42);
		Assert.assertNotNull(trade);
		List<Trade> trades = Controller.getTradeList();
		Assert.assertFalse(trades.isEmpty());
	}

	@Test 
	public void recordSellTradeTest() throws BeverageTradingException{
		List<Trade> trades = Controller.getTradeList();
		int size = 0;
		if (trades != null){
			size = trades.size();
		}
		
		Trade trade = Controller.recordTrade("TEA", 5, TradeType.SELL, 24);
		Assert.assertNotNull(trade);
		trades = Controller.getTradeList();
		Assert.assertFalse(trades.isEmpty());
		Assert.assertEquals(size +1 , trades.size());
	}

	@Test(expected= BeverageTradingException.class)
	public void recordTradeForUnknownStock() throws BeverageTradingException{
		Controller.recordTrade("UNKNOWN", 12, TradeType.BUY, 42);
		
	}
	
	@Test
	public void calculateVolumeWeightedTest() throws BeverageTradingException{
		
		String symbol = "TEA";
		double result = Controller.calculateVolumeWeighted(symbol, 5);
		
			//add at least one trade for symbol
			Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
			Controller.recordTrade(symbol, 30, TradeType.BUY, 20);
			result = Controller.calculateVolumeWeighted(symbol, 5);
			Assert.assertEquals(17.5, result ,0);
		
		
	}
	
	@Test(expected= BeverageTradingException.class)
	public void calculateVolumeWeightedUnkownTest() throws BeverageTradingException{
		
		String symbol = "UNKNOWN";
		double result = Controller.calculateVolumeWeighted(symbol, 5);
		
			//add at least one trade for symbol
			Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
			Controller.recordTrade(symbol, 30, TradeType.BUY, 20);
			result = Controller.calculateVolumeWeighted(symbol, 5);
			Assert.assertEquals(17.5, result ,0);
	}
	
	@Test
	public void calcGBCE() throws BeverageTradingException{
		String symbol = "TEA";
		Controller.recordTrade(symbol, 10, TradeType.BUY, 10);
		Controller.recordTrade(symbol, 30, TradeType.BUY, 20);//TEA weighted vol = 17.5
		Controller.recordTrade("GIN", 5, TradeType.SELL, 30);//GIN weighted vol = 30
		
		double gbce = Controller.GBCEAllShareIndex(5); //during the last 5 minutes
		
		Assert.assertEquals(Math.pow((17.5 * 30), 0.5), gbce, 0.001);
		
	}
	
	
}
