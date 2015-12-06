package com.github.pixelase.contacts.client;

import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pixelase.commands.core.Command;
import com.github.pixelase.commands.core.CommandsManager;
import com.github.pixelase.commands.core.CommandsPerformer;
import com.github.pixelase.commands.utils.input.CommandInputParser;
import com.github.pixelase.commands.utils.input.CommandInputReader;
import com.github.pixelase.commands.utils.input.CommandParseResult;
import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.services.PersonService;
import com.github.pixelase.contacts.services.PhoneService;

public class ConsoleLauncher {
	private static ApplicationContext aContext;

	public static void main(String[] args) {
		try {
			aContext = new ClassPathXmlApplicationContext("spring-context.xml");
			final PersonService personService = aContext.getBean(PersonService.class);
			final PhoneService phoneService = aContext.getBean(PhoneService.class);

			final CommandsManager.Builder builder = new CommandsManager.Builder();
			CommandsPerformer performer = builder.add(new Command("exit", "Shutdown the program.") {
				@Override
				public void execute() {
					System.out.println();
					System.out.print("Program is closing");

					for (int i = 0; i < 3; i++) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						System.out.print(".");
					}

					System.out.println();
					System.exit(0);
				}
			}).add(new Command("help", "Display the help information about commands.") {
				@Override
				public void execute() {
					System.out.println();
					for (Command command : builder.getCommands()) {
						System.out.println(command);
					}
					System.out.println();
				}
			}).add(new Command("findp", "Find persons whose full name partially matched with the input string.") {
				@Override
				public void execute() {
					String firstName = "";
					String lastName = "";
					List<Person> result = null;

					if (args.length >= 2) {
						firstName = args[0];
						lastName = args[1];
						result = personService.findAll(firstName, lastName);
					} else if (args.length == 1) {
						List<Person> firstNameSearch = personService.findAll(args[0], lastName);

						if (firstNameSearch.isEmpty()) {
							result = personService.findAll(firstName, args[0]);
						} else {
							result = firstNameSearch;
						}
					}

					System.out.println();
					if (result != null && !result.isEmpty()) {
						for (Person person : result) {
							System.out.println(person.toDetailedFormat());
						}
					} else {
						System.out.println("Persons not found");
					}
					System.out.println();
				}
			}).add(new Command("findn", "Find numbers that partially matched with the input string.") {
				@Override
				public void execute() {
					List<Phone> result = null;

					if (args.length >= 1) {
						result = phoneService.findAll(args[0]);
					}

					System.out.println();
					if (result != null && !result.isEmpty()) {
						for (Phone phone : result) {
							System.out.println(phone.getNumber());
						}
					} else {
						System.out.println("Numbers not found");
					}
					System.out.println();

				}
			}).add(new Command("pinfo", "Displays detailed information about person.") {
				@Override
				public void execute() {
					System.out.println();
					if (args.length >= 2) {
						List<Person> result = personService.findAll(args[0], args[1]);

						if (!result.isEmpty()) {
							for (Person person : result) {
								System.out.println(person.toDetailedFormat());
							}
						} else {
							System.out.println("Persons not found");
						}
					} else {
						System.out.println("You should enter firstname and lastname");
					}
					System.out.println();

				}
			}).add(new Command("ninfo", "Displays detailed information about number.") {
				@Override
				public void execute() {
					System.out.println();
					if (args.length >= 1) {
						List<Phone> result = phoneService.findAll(args[0]);

						if (!result.isEmpty()) {
							for (Phone phone : result) {
								System.out.println(phone.toDetailedFormat());
							}
						} else {
							System.out.println("Phone numbers not found");
						}
					} else {
						System.out.println("You should enter number");
					}
					System.out.println();

				}
			}).add(new Command("create", "Create person.") {
				@Override
				public void execute() {
					System.out.println();
					if (args.length >= 2) {
						Person person = personService.save(new Person(args[0], args[1]));

						if (args.length == 3) {
							phoneService.save(new Phone(args[2], person));
						}
						System.out.println(
								String.format("Person:\n%s\n" + "Successfully saved.", person.toDetailedFormat()));

					} else {
						System.out.println("You should enter person params in such format:\n"
								+ "<Firstname> <Lastname> [Phone number}");
					}
					System.out.println();

				}
			}).add(new Command("save", "Add number to person.") {
				@Override
				public void execute() {
					System.out.println();
					if (args.length >= 3) {
						String firstName = args[0];
						String lastName = args[1];
						String number = args[2];

						List<Person> persons = personService.findAll(firstName, lastName);
						List<Phone> foundNumbers = phoneService.findAll(number);

						if (foundNumbers.isEmpty()) {
							Person person = null;
							if (persons.isEmpty()) {
								person = personService.save(new Person(firstName, lastName));
								persons.add(person);
							}

							person = persons.get(0);
							phoneService.save(new Phone(number, person));

							System.out.println(String.format("Person:\n%s\n" + "Person with number successfully saved.",
									person.toDetailedFormat()));
						} else {
							System.out.println("This number is already exists");
						}

					} else {
						System.out.println(
								"Your input should be in such format:\n" + "<Firstname> <Lastname> [Phone number}");
					}
					System.out.println();

				}
			}).add(new Command("show", "Display differend kind of information.") {
				@Override
				public void execute() {
					System.out.println();
					StringBuilder result = new StringBuilder();

					if (args.length >= 1) {
						if (args[0].contains("-n")) {
							List<Phone> found = phoneService.findAll();
							result.append("Phone numbers:\n");
							for (Phone phone : found) {
								result.append(String.format("* %s\n", phone.getNumber()));
							}
						} else {
							result.append(String.format("Wrong option %s\n", args[0]));
						}
					} else {
						List<Person> found = personService.findAll();
						result.append("Persons:\n");
						for (Person person : found) {
							result.append(String.format("* %s\n", person.toSimpleFormat()));
						}
					}
					System.out.println(result);
					System.out.println();

				}
			}).build();

			while (true) {
				System.out.print(Paths.get("").toAbsolutePath().toString() + ">");

				try {
					CommandParseResult parseResult = CommandInputParser.parse(CommandInputReader.readLine(System.in));

					if (!performer.tryPerform(parseResult.getCommandName(), parseResult.getArgs())) {
						System.out.println("Command not found.");
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
