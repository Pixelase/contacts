package com.github.pixelase.contacts.client;

import java.nio.file.Paths;

import com.github.pixelase.commands.core.CommandsPerformer;
import com.github.pixelase.commands.utils.input.CommandInputParser;
import com.github.pixelase.commands.utils.input.CommandInputReader;
import com.github.pixelase.commands.utils.input.CommandParseResult;

public class ConsoleLauncher {

	public static void main(String[] args) {
		try {
			CommandsPerformer performer = new ContactsCommandsManager("spring-context.xml");

			while (true) {
				System.out.print(Paths.get("").toAbsolutePath().toString() + ">");

				try {
					CommandParseResult parseResult = CommandInputParser.parse(CommandInputReader.readLine(System.in));

					if (!performer.tryPerform(parseResult.getCommandName(), parseResult.getArgs())) {
						System.out.println("\nCommand not found.\n");
					}
				} catch (Exception e) {
					System.out.println("Something went wrong: " + e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("Critical error: " + e.getMessage());
		}
	}
}
