package com.github.pixelase.contacts.client;

import com.github.pixelase.commands.core.CommandsPerformer;
import com.github.pixelase.commands.utils.input.CommandInputParser;
import com.github.pixelase.commands.utils.input.CommandInputReader;
import com.github.pixelase.commands.utils.input.CommandParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class ConsoleLauncher {
    public static final String SPRING_CONTEXT_LOCATION = "spring-context.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleLauncher.class);

    public static void main(String[] args) {
        try {
            final CommandsPerformer performer = new ContactsCommandsManager(SPRING_CONTEXT_LOCATION);

            while (true) {
                System.out.print(Paths.get("").toAbsolutePath().toString() + ">");

                try {
                    CommandParseResult parseResult = CommandInputParser.parse(CommandInputReader.readLine(System.in));
                    LOGGER.debug(parseResult.toString());

                    if (!performer.tryPerform(parseResult.getCommandName(), parseResult.getArgs())) {
                        String logMessage = "Command not found.";
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
