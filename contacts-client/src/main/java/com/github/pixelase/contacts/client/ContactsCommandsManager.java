package com.github.pixelase.contacts.client;

import com.github.pixelase.commands.core.Command;
import com.github.pixelase.commands.core.CommandsManager;
import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.services.PersonService;
import com.github.pixelase.contacts.services.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

public class ContactsCommandsManager extends CommandsManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactsCommandsManager.class);

    private final ApplicationContext aContext;

    public ContactsCommandsManager(String contexLocation) {
        aContext = new ClassPathXmlApplicationContext(contexLocation);
        final PersonService personService = aContext.getBean(PersonService.class);
        final PhoneService phoneService = aContext.getBean(PhoneService.class);

        commands.add(new Command("exit", "Shutdown the program.") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);
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
        });
        commands.add(new Command("help", "Display the help information about commands.") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);
                System.out.println();
                for (Command command : commands) {
                    System.out.println(command);
                }
                System.out.println();
            }
        });
        commands.add(new Command("find",
                "Find persons whose full name partially matched with the input string (-n for numbers).") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);

                if (args.length >= 2 && args[0].equals("-n")) {
                    final List<Phone> result = phoneService.findAllByPartialMatching(args[1]);

                    System.out.println();
                    if (result != null && !result.isEmpty()) {
                        for (Phone phone : result) {
                            System.out.println(phone.getNumber());
                        }
                    } else {
                        System.out.println("Numbers not found");
                    }
                    System.out.println();
                } else {
                    String firstName = "";
                    String lastName = "";
                    List<Person> result = null;

                    if (args.length >= 2) {
                        firstName = args[0];
                        lastName = args[1];

                        result = personService.findAllByPartialMatching(firstName, lastName);
                    } else if (args.length == 1) {
                        List<Person> firstNameSearchResult = personService.findAllByPartialMatching(args[0], lastName);

                        if (firstNameSearchResult.isEmpty()) {
                            result = personService.findAllByPartialMatching(firstName, args[0]);
                        } else {
                            result = firstNameSearchResult;
                        }
                    }

                    System.out.println();
                    if (result != null && !result.isEmpty()) {
                        for (Person person : result) {
                            System.out.println(person.toSimpleFormat());
                            System.out.println();
                        }
                    } else {
                        System.out.println("Persons not found\n");
                    }
                }
            }
        });
        commands.add(new Command("info", "Displays detailed information about person (-n for number).") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);

                System.out.println();
                if (args.length >= 2 && args[0].equals("-n")) {
                    Phone phone = phoneService.findOne(args[1]);

                    if (phone != null) {
                        System.out.println(phone.toDetailedFormat());
                        System.out.println();

                    } else {
                        System.out.println("Phone number not found.\n");
                    }
                } else if (args.length >= 2) {
                    Person person = personService.findOne(args[0], args[1]);

                    if (person != null) {
                        System.out.println(person.toDetailedFormat());
                    } else {
                        System.out.println("Person not found.\n");
                    }
                } else {
                    System.out.println("Incorrect input!\n");
                }
            }
        });
        commands.add(new Command("save", "Save person (+ number).") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);

                System.out.println();
                if (args.length >= 2) {
                    final String firstName = args[0];
                    final String lastName = args[1];

                    Person person = personService.findOne(firstName, lastName);
                    if (args.length == 2) {
                        if (person == null) {
                            personService.save(new Person(firstName, lastName));
                            System.out.println("Successfully saved.\n");
                            return;
                        }

                        System.out.println("This person already exists.\n");
                        return;
                    } else {
                        final String number = args[2];
                        final Phone phone = phoneService.findOne(number);

                        if (phone == null || phone.getPerson() == null) {

                            if (person == null) {
                                person = personService.save(new Person(firstName, lastName));
                            }

                            Phone savedPhone = phoneService.save(new Phone(number, person));
                            person.getPhones().add(savedPhone);

                            System.out
                                    .println(String.format("Person:\n%s\n" + "Person with number successfully saved.\n",
                                            person.toDetailedFormat()));
                        } else {
                            System.out.println("This number is already exists.\n");
                        }
                    }
                } else {
                    System.out.println(
                            "Your input should be in such format:\n" + "<Firstname> <Lastname> [Phone number}.\n");
                }

            }
        });
        commands.add(new Command("show", "Display all persons (-n for numbers).") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);

                System.out.println();
                StringBuilder result = new StringBuilder();

                if (args.length >= 1) {
                    if (args[0].equals("-n")) {
                        final List<Phone> found = phoneService.findAll();
                        if (found.isEmpty()) {
                            System.out.println("Phone table is empty.\n");
                            return;
                        }
                        result.append("Phone numbers:\n");

                        for (Phone phone : found) {
                            result.append(String.format("* %s\n", phone.getNumber()));
                        }
                    } else {
                        result.append(String.format("Wrong option %s\n", args[0]));
                    }
                } else {
                    List<Person> found = personService.findAll();
                    if (found.isEmpty()) {
                        System.out.println("Persons table is empty.\n");
                        return;
                    }
                    result.append("Persons:\n");

                    for (Person person : found) {
                        result.append(String.format("* %s\n", person.toSimpleFormat()));
                    }
                }

                System.out.println(result);
            }
        });

        commands.add(new Command("delete", "Delete one (-a all) person('s) (-n for numbers).") {
            @Override
            public void execute() {
                LOGGER.info("{} command execution", name);

                System.out.println();

                List<String> listArgs = Arrays.asList(args);
                if (args.length >= 2) {
                    if (listArgs.contains("-n") && args.length == 2) {
                        if (listArgs.contains("-a")) {
                            phoneService.deleteAll();
                            System.out.println("All phone numbers successfully deleted.\n");
                            return;
                        }

                        Phone foundPhone = phoneService.findOne(args[1]);
                        if (foundPhone != null) {
                            phoneService.delete(foundPhone.getId());
                            System.out.println(String.format("Phone number %s successfully deleted.\n", args[1]));
                        } else {
                            System.out.println(String.format("Phone number %s not found.\n", args[1]));
                        }
                        return;
                    }
                }

                if (listArgs.contains("-a")) {
                    for (Person person : personService.findAll()) {
                        phoneService.delete(person.getPhones());
                    }
                    personService.deleteAll();
                    System.out.println("All persons successfully deleted.\n");
                }
            }
        });

        commands.sort(null);
    }

}
