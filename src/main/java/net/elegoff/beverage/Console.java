package net.elegoff.beverage;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.budhash.cliche.ShellFactory;

/**
 * @author elegoff
 *  Main class of the console application
 */
public class Console {

	private static Logger logger = LoggerFactory.getLogger(Console.class);
	
	public static void main(String[] args) {
				try {
			ShellFactory.createConsoleShell("Beverage-trader", "Type ?list (or ?l) to get a list of commands", new Commands())
			.commandLoop();
		} catch (IOException e) {
			logger.error("An exception occured" + e.getMessage());
		}
		
	}
	

	
	

}
