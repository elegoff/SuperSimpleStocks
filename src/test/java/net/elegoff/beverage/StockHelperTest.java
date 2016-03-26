package net.elegoff.beverage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.elegoff.beverage.model.Stock;

import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class StockHelperTest {

	
	@Test
	public void loadJson() throws FileNotFoundException, IOException, ParseException{
		List<Stock > stocks = StockHelper.loadJson();
		Assert.assertNotNull(stocks);
		Assert.assertEquals(5, stocks.size());
	}
}
