import flashcards.MainKt;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.List;

public class FlashcardsTest extends StageTest<String> {
    
    private static List<String> listOfTerms;
    private static List<String> listOfDefinitions;
    private static List<String> listOfAddedDefinitions;
    private static List<String> listOfRemovedDefinitions;
    
    @BeforeClass
    public static void generateLists(){
        listOfTerms = getListOfTerms();
        listOfDefinitions = getListOfDefinitions();
        listOfAddedDefinitions = getListOfWrongDefinitions1();
        listOfRemovedDefinitions = getListOfWrongDefinitions2();
    }
    
    @DynamicTestingMethod
    CheckResult test1() {
        TestedProgram main = new TestedProgram(MainKt.class);
        
        String output = main.start().toLowerCase().trim();
        if (!output.startsWith("input")) {
            return CheckResult.wrong("Your program should prompt the user with the message \"Input the number of " +
                    "cards:\"");
        }
        
        return CheckResult.correct();
    }
    
    //Test where user gets the correct definition
    @DynamicTestingMethod
    CheckResult test2() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
    
        String output;
        String term;
        String definition;
        String lineOne;
        String[] lines;
        
        int numberOfTimes = listOfTerms.size();
        output = main.execute(numberOfTimes + "").toLowerCase().trim();
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            
            int cardNumber = i + 1;
            if (!output.startsWith("card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"Card #n\", where n is the index number of the " +
                        "card to be created");
            }
            
            output = main.execute(term).toLowerCase().trim();
            if (!output.startsWith("the definition for card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"The definition for card #n\" after saving the " +
                        "term entered by the user.");
            }
            
            output = main.execute(definition).toLowerCase().trim();
        }
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            
            if (!output.contains("\"" + term + "\"")) {
                return CheckResult.wrong("Your program should print the definition requested in quotes");
            }
            
            if (!output.contains("definition")) {
                return CheckResult.wrong("Your program should prompt the user for the definition of a card with the message " +
                        "\" Print the definition of \"term\": where \"term\" is the term of the flashcard to be checked");
            }
            
            output = main.execute(definition).toLowerCase().trim();
            
            lines = output.split("\n");
            lineOne = lines[0];
            
            if (!lineOne.equals("correct!")) {
                return CheckResult.wrong("Your program should print \"Correct!\" if the user inputs the correct " +
                        "definition");
            }
            
            if (i != (numberOfTimes -1)) {
                
                if (lines.length != 2) {
                    return CheckResult.wrong("Your program should give feedback to the user on one line and the " +
                            "next question on the next line");
                }
                
                output = lines[1].trim();
            }
            
        }
        
        
        return CheckResult.correct();
        
    }
    
    //Test to check if the user's program saves an existing card
    @DynamicTestingMethod
    CheckResult test3() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
        String output;
        
        main.execute("4");
        main.execute("print()");
        main.execute("outputs text");
        
        output = main.execute("print()").toLowerCase();
        if (!output.contains("\"print()\" already exists")) {
            return CheckResult.wrong("Your program should not add a term that already exists and should notify the " +
                    "user with the message \"The term 'term' already exists. Try again:\"");
        }
    
    
        main.execute("son");
        main.execute("a male child");
        
        main.execute("str()");
        main.execute("converts to a string");
        
        main.execute("daughter");
        output = main.execute("a male child");
        if (!output.contains("\"a male child\" already exists")) {
            return CheckResult.wrong("Your program should not add a definition that already exists and should notify " +
                    "the user with the message \"The definition 'definition' already exists. Try again:\"");
        }
        
        return CheckResult.correct();
    }
    
    //Test to check if the program's output are in separate lines
    @DynamicTestingMethod
    CheckResult test4() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
        
        main.execute("2");
        main.execute("print()");
        main.execute("outputs text");
        main.execute("print()");
        main.execute("str()");
        main.execute("outputs text");
        main.execute("converts to a string");
        main.execute("outputs text");
        main.execute("converts to a string");
        
        return null;
    }
    
    //Test where user enters a definition for another term.
    @DynamicTestingMethod
    CheckResult test5() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
    
        String output;
        String lineOne;
        String[] lines;
        String term;
        
        int numberOfTimes = listOfTerms.size();
        output = main.execute(numberOfTimes + "").toLowerCase().trim();
        
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            
            int cardNumber = i + 1;
            if (!output.startsWith("card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"Card #n\", where n is the index number of the " +
                        "card to be created");
            }
            
            output = main.execute(term).toLowerCase().trim();
            if (!output.startsWith("the definition for card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"The definition for card #n\" after saving the " +
                        "term entered by the user.");
            }
            
            output = main.execute(listOfDefinitions.get(i)).toLowerCase().trim();
        }
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            
            if (!output.contains("\"" + term + "\"")) {
                return CheckResult.wrong("Your program should print the definition requested in quotes");
            }
            
            if (!output.contains("definition")) {
                return CheckResult.wrong("Your program should prompt the user for the definition of a card with the " +
                        "message \" Print the definition of \"term\": where \"term\" is the term of the flashcard to be checked");
            }
            
            //Swap the definitions of similar terms
            int index;
            if (i % 2 == 0) {
                output = main.execute(listOfDefinitions.get(i + 1)).toLowerCase().trim();
                index = i + 1;
            } else {
                output = main.execute(listOfDefinitions.get(i - 1)).toLowerCase().trim();
                index = i - 1; 
            }
            
            
            lines = output.split("\n");
            lineOne = lines[0];
            
            if (!lineOne.contains("wrong. the right answer is \"" + listOfDefinitions.get(i) + "\"")) {
                return CheckResult.wrong("Your program should print \"Wrong.\" followed by the correct definition in " +
                        "quotes if the user inputs the wrong definition");
            }
            
            if (!lineOne.contains("correct for \"" + listOfTerms.get(index) + "\"")) {
                return CheckResult.wrong("Your output should also contain \"but your definition is correct for 'term'" +
                        " \"");
            }
            
            if (i != (numberOfTimes -1)) {
                
                if (lines.length != 2) {
                    return CheckResult.wrong("Your program should give feedback to the user on one line and the " +
                            "next question on the next line");
                }
                
                output = lines[1].trim();
            }
            
        }
        
        return CheckResult.correct();
    }
    
    //Test where user fails the definition by adding extra words that might invalidate the definition
    @DynamicTestingMethod
    CheckResult test6() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
    
        String output;
        String term;
        String definition;
        String lineOne;
        String wrongDefinition;
        String[] lines;
        
        
        int numberOfTimes = listOfTerms.size();
        output = main.execute(numberOfTimes  + "").toLowerCase().trim();
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            
            int cardNumber = i + 1;
            if (!output.startsWith("card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"Card #n\", where n is the index number of the " +
                        "card to be created");
            }
            
            output = main.execute(term).toLowerCase().trim();
            if (!output.startsWith("the definition for card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"The definition for card #n\" after saving the " +
                        "term entered by the user.");
            }
            
            output = main.execute(definition).toLowerCase().trim();
        }
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            wrongDefinition = listOfAddedDefinitions.get(i);
            
            if (!output.contains("\"" + term + "\"")) {
                return CheckResult.wrong("Your program should print the definition requested in quotes");
            }
            
            if (!output.contains("definition")) {
                return CheckResult.wrong("Your program should prompt the user for the definition of a card with the message " +
                        "\" Print the definition of \"term\": where \"term\" is the term of the flashcard to be checked");
            }
            
            output = main.execute(wrongDefinition).toLowerCase().trim();
            
            lines = output.split("\n");
            lineOne = lines[0];
            
            if (!lineOne.contains("wrong. the right answer is \"" + definition + "\"")) {
                return CheckResult.wrong("Your program should print \"Wrong\" followed by the correct definition in " +
                        "quotes if the user inputs the wrong definition");
            }
            
            if (i != (numberOfTimes -1)) {
                
                if (lines.length != 2) {
                    return CheckResult.wrong("Your program should give feedback to the user on one line and the " +
                            "next question on the next line");
                }
                
                output = lines[1].trim();
            }
            
        }
        
        return CheckResult.correct();
    }
    
    //Test where user fails the definition by removing essential word that invalidates the definition
    @DynamicTestingMethod
    CheckResult test7() {
        TestedProgram main = new TestedProgram(MainKt.class);
        main.start();
        
        String output;
        String lineOne;
        String term;
        String definition;
        String wrongDefinition;
        String[] lines;
        
        int numberOfTimes = listOfTerms.size();
        output = main.execute(numberOfTimes + "").toLowerCase().trim();
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            
            int cardNumber = i + 1;
            if (!output.startsWith("card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"Card #n\", where n is the index number of the " +
                        "card to be created");
            }
            
            output = main.execute(term).toLowerCase().trim();
            if (!output.startsWith("the definition for card #" + cardNumber)) {
                return CheckResult.wrong("Your program should print \"The definition for card #n\" after saving the " +
                        "term entered by the user.");
            }
            
            output = main.execute(definition).toLowerCase().trim();
        }
        
        for (int i = 0; i < numberOfTimes; i++) {
            term = listOfTerms.get(i);
            definition = listOfDefinitions.get(i);
            wrongDefinition = listOfRemovedDefinitions.get(i);
            
            if (!output.contains("\"" + term + "\"")) {
                return CheckResult.wrong("Your program should print the definition requested in quotes");
            }
            
            if (!output.contains("definition")) {
                return CheckResult.wrong("Your program should prompt the user for the definition of a card with the message " +
                        "\" Print the definition of \"term\": where \"term\" is the term of the flashcard to be checked");
            }
            
            output = main.execute(wrongDefinition).toLowerCase().trim();
            
            lines = output.split("\n");
            lineOne = lines[0];
            
            if (!lineOne.contains("wrong. the right answer is \"" + definition + "\"")) {
                return CheckResult.wrong("Your program should print \"Wrong\" followed by the correct definition in " +
                        "quotes if the user inputs the wrong definition");
            }
            
            if (i != (numberOfTimes -1)) {
                
                if (lines.length != 2) {
                    return CheckResult.wrong("Your program should give feedback to the user on one line and the " +
                            "next question on the next line");
                }
                
                output = lines[1].trim();
            }
            
        }
        
        return CheckResult.correct();
    }
    
    //Used to check if the program's output are in separate lines. Uses in test 4
    @Override
    public CheckResult check(String reply, String attach) {
        String[] lines = reply.split("\n");
        
        if (lines.length != 11)  {
            return CheckResult.wrong("Your program printed wrong number of lines. " +
                    "Make sure all your outputs are on separate lines");
        }
        
        return CheckResult.correct();
    }
    
    private static List<String> getListOfTerms() {
        return Arrays.asList(
                "print()",
                "str()",
                "son",
                "daughter",
                "uncle",
                "ankle");
    }
    
    private static List<String> getListOfDefinitions() {
        return Arrays.asList(
                "outputs text",
                "converts to a string",
                "a male child",
                "a female child",
                "a brother of one's parent",
                "a part of the body where the foot and the leg meet"
        );
    }
    
    //Extra wrongs invalidate the definition
    private static List<String> getListOfWrongDefinitions1() {
        return Arrays.asList(
                "outputs text line by line",
                "converts to a string and an integer",
                "a male child or a female child",
                "a female child or a male child",
                "a brother of one's parent or grandparents",
                "a part of the body where the foot and the leg meet or the arm"
        );
    }
    
    //Essential words removed invalidates the definition
    private static List<String> getListOfWrongDefinitions2() {
        return Arrays.asList(
                "outputs",
                "converts a string",
                "a child",
                "a child",
                "a brother of one",
                "a part of the body where the foot meet"
        );
    }
    
}
