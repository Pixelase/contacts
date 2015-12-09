package com.github.pixelase.contacts.client;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pixelase.commands.core.CommandsPerformer;
import com.github.pixelase.commands.utils.input.CommandInputParser;
import com.github.pixelase.commands.utils.input.CommandInputReader;
import com.github.pixelase.commands.utils.input.CommandParseResult;

public class ConsoleLauncher {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleLauncher.class);

	public static void main(String[] args) {
		try {
			CommandsPerformer performer = new ContactsCommandsManager("spring-context.xml");
			String logMessage;

			while (true) {
				System.out.print(Paths.get("").toAbsolutePath().toString() + ">");

				try {
					CommandParseResult parseResult = CommandInputParser.parse(CommandInputReader.readLine(System.in));
					LOGGER.debug(parseResult.toString());

					if (!performer.tryPerform(parseResult.getCommandName(), parseResult.getArgs())) {
						logMessage = "Command not found.";
						System.out.println("\n" + logMessage + "\n");
						LOGGER.info(logMessage);
					}
				} catch (Exception e) {
					System.out.println("Something went wrong: " + e.getMessage());
					LOGGER.error(e.toString());
				}
			}

		} catch (Exception e) {
			System.out.println("Critical error: " + e.getMessage());
			LOGGER.error(e.toString());
		}
	}
}
