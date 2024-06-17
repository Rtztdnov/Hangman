import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HangmanMain {

    static Scanner scanner = new Scanner(System.in);
    static final String wrongLettersSign = "Неверно введенные символы: ";
    static final String hiddenMainWordSign = "Загадано слово: ";
    static final String playerLoseSing = "Вы проиграли.";
    static final String playerWinSing = "Вы выиграли!";
    static final String doYouWantStartTheGameSing = "Хотите начать новую игру? (Y/N)";
    static final String sayGoodbye = "Прощайте!";
    static final String errorCountSign = "Количество ошибок: ";
    static Scanner scannerFromFile;

    static {
        try {
            scannerFromFile = new Scanner(new File("ListOfWords.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
    }

    static List<String> listOfWords = makeListOfWordsFromFile();
    static Set<String> wrongLetters = new LinkedHashSet<>();
    static Set<String> correctLetters = new HashSet<>();
    static List<String> theMainWord = new ArrayList<>();
    public static final int NO_ERROR = 0;
    public static final int ONE_ERRORS = 1;
    public static final int TWO_ERRORS = 2;
    public static final int THREE_ERRORS = 3;
    public static final int FOUR_ERRORS = 4;
    public static final int FIVE_ERRORS = 5;
    public static final int SIX_ERRORS = 6;


    public static void main(String[] args) {

        startNewGame();

    }

    static void startNewGame() {
        System.out.println(doYouWantStartTheGameSing);
        String playerInput = "";
        while (scanner.hasNext()) {
            playerInput = scanner.next();
            if (playerInput.matches("[yY]")) {
                startNewGameLoop();
                return;
            } else if (playerInput.matches("[nN]")) {
                System.out.println(sayGoodbye);
                return;
            }
            else {
                System.out.println("Введите Y или N");
            }
        }
    }

    static void startNewGameLoop() {

        theMainWord = getMainPlayingWord(listOfWords);
        int errorCount = 0;
        correctLetters.clear();
        wrongLetters.clear();
        while (true) {
            buildHangman(errorCount);
            System.out.println(hiddenMainWordSign + hideTheMainWord(theMainWord));
            printListInGameWithSign(wrongLettersSign, wrongLetters);
            System.out.println(errorCountSign + errorCount);
            String currentPlayerInput = playerInputScan();

            if (theMainWord.contains(currentPlayerInput)) {
                correctLetters.add(currentPlayerInput);
            } else {
                wrongLetters.add(currentPlayerInput);
                errorCount++;
            }
            if (errorCount == SIX_ERRORS) {
                System.out.println(playerLoseSing);
                buildHangman(errorCount);
                printListInGameWithSign(hiddenMainWordSign, theMainWord);
                break;
            } else if (correctLetters.containsAll(theMainWord)) {
                System.out.println(playerWinSing);
                printListInGameWithSign(hiddenMainWordSign, theMainWord);
                break;
            }
        }
        startNewGame();
    }


    static boolean isPlayerInputCorrect(String playerInput) {
        if (playerInput.matches("\\d")) {
            System.out.println("Введите букву, а не циффру");
            return false;
        } else if (playerInput.length() > 1) {
            System.out.println("Введите один символ");
            return false;
        } else if (!playerInput.matches("[а-яА-я]")) {
            System.out.println("Введите символ на кириллице");
            return false;
        } else if (wrongLetters.contains(playerInput.toLowerCase()) ||
                correctLetters.contains(playerInput.toLowerCase())) {
            System.out.println("Такая буква уже была!");
            return false;
        } else {
            return true;
        }
    }

    static String playerInputScan() {
        String playerInput = "";
        boolean checkPassed = false;

        while (!checkPassed) {
            if (scanner.hasNextLine()) {
                playerInput = scanner.next();
                checkPassed = isPlayerInputCorrect(playerInput);
            }
        }
        return playerInput.toLowerCase();
    }

    static List<String> getMainPlayingWord(List<String> arrayOfWords) {

        List<String> mainWordInList = new ArrayList<>();
        Random random = new Random();
        int randomIndex = random.nextInt(arrayOfWords.size());
        String mainWord = arrayOfWords.get(randomIndex);

        for (char ch : mainWord.toCharArray()) {
            mainWordInList.add(String.valueOf(ch));
        }
        return mainWordInList;
    }

    static List<String> makeListOfWordsFromFile() {
        List<String> arrayOfWords = new ArrayList<>();
        while (scannerFromFile.hasNext()) {
            arrayOfWords.add(scannerFromFile.nextLine());
        }
        return arrayOfWords;
    }

    static String hideTheMainWord(List<String> notHiddenWord) {
        String hiddenWord = "";

        for (int i = 0; i < notHiddenWord.size(); i++) {
            if (correctLetters.contains(notHiddenWord.get(i)))
                hiddenWord = hiddenWord + notHiddenWord.get(i);
            else {
                hiddenWord = hiddenWord + "*";
            }
        }
        return hiddenWord;
    }

    /*
    "______\n|    |\n|    O\n|   /|\\\n|   / \\\n|"
    is
    ______
    |    |
    |    O
    |   /|\
    |   / \
    |
     */
    static void buildHangman(int errorCount) {
        switch (errorCount) {
            case NO_ERROR:
                System.out.println("______\n|    |\n|    \n|   \n|   \n|");
                break;
            case ONE_ERRORS:
                System.out.println("______\n|    |\n|    O\n|   \n|   \n|");
                break;
            case TWO_ERRORS:
                System.out.println("______\n|    |\n|    O\n|    |\n|  \n|");
                break;
            case THREE_ERRORS:
                System.out.println("______\n|    |\n|    O\n|   /|\n|   \n|");
                break;
            case FOUR_ERRORS:
                System.out.println("______\n|    |\n|    O\n|   /|\\\n|   \n|");
                break;
            case FIVE_ERRORS:
                System.out.println("______\n|    |\n|    O\n|   /|\\\n|   /\n|");
                break;
            case SIX_ERRORS:
                System.out.println("______\n|    |\n|    O\n|   /|\\\n|   / \\\n|");
                break;
        }
    }

    static void printListInGameWithSign(String sing, List<String> list) {

        System.out.print(sing);
        if (list.isEmpty()) {
            System.out.print("");
        }
        for (String l : list) {
            System.out.print(l);
        }
        System.out.println();
    }

    static void printListInGameWithSign(String sing, Set<String> list) {

        System.out.print(sing);
        if (list.isEmpty()) {
            System.out.print("");
        }
        for (String l : list) {
            System.out.print(l);
        }
        System.out.println();
    }



}
